package com.promineotech.banking;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import com.promineotech.banking.model.AccountModel;
import com.promineotech.banking.repository.AccountListRepository;
import com.promineotech.banking.repository.AccountRepository;
import com.promineotech.banking.service.AccountNumberGenerator;
import com.promineotech.banking.service.AccountService;
import com.promineotech.banking.service.DefaultAccountService;
import com.promineotech.banking.service.SequentialAccountNumberGenerator;

class DefaultAccountServiceTests {
  private final int DEFAULT_STARTING_ACCOUNT_NUMBER = 10000000;
  private AccountRepository repository;
  private AccountService service;

  @BeforeEach
  void setUp() {
    repository = Mockito.spy(new AccountListRepository());
	AccountNumberGenerator accountNumberGenerator = new SequentialAccountNumberGenerator(DEFAULT_STARTING_ACCOUNT_NUMBER);
	service = new DefaultAccountService(repository, accountNumberGenerator);
  }
  
  @AfterEach
  void tearDown() {
  }

  @Test
  void assertThatOpenCreatesNewAccount() {
	String owner = "test@bank.com";
	float balance = 101.1F;
	String number = "1000000";
	
	AccountModel expected = new AccountModel(number, owner).setBalance(balance);
	doReturn(expected).when(repository)
	                  .save(anyString(), anyString(), anyFloat());
	
	Response<AccountModel> response = service.open(owner, balance);
	assertThat(response.getCode()).isEqualTo(Response.OK);
	
	AccountModel actual = response.get();
	assertThat(actual).isNotNull();
	assertThat(actual.getOwner()).isEqualTo(owner);
	assertThat(actual.getNumber()).isNotEmpty();
	assertThat(actual.getBalance()).isCloseTo(balance, within(0.00001F));
  }
  
  @Test
  void assertThatOpenWithNullOwnerReturnBadRequest() {
    //TODO
    fail("Test not implemented.");
  }

  @ParameterizedTest
  @ValueSource(floats = { 0F, -0.01F, 4.99F, 4.99999F })
  void assertThatOpenWithLessThanRequiredInitialDepositReturnsBadRequest(float balance) {
    String owner = "test@bank.com";
	    
    doReturn(null).when(repository)
                  .save(anyString(), anyString(), anyFloat());

    Response<AccountModel> response = service.open(owner, balance);
    assertThat(response.getCode()).isEqualTo(Response.BAD_REQUEST);
	    
    AccountModel account = response.get();
    assertThat(account).isNull();  
  }

  @Test
  void assertThatCloseWithInvalidAccountNumberReturnsNotFound() {
    //TODO
    fail("Test not implemented.");
  }

  @Test
  void assertThatCloseWithNoBalanceInAccountSucceeds() {
    //TODO
    fail("Test not implemented.");
  }

  @Test
  void assertThatCloseWithBalanceRemainingInAccountReturnsBadRequest() {
    //TODO
    fail("Test not implemented.");
  }
  
  @ParameterizedTest
  @ValueSource(floats = { 0F, -0.01F, -0.000001F, -1F })
  void assertThatDepositWithLessOrEqualToZeroAmountReturnsBadRequest(float depositAmount) {
    //TODO
    fail("Test not implemented.");
  }  

  @ParameterizedTest
  @ValueSource(floats = { 10F, 10.01F, 100.0F, 9.99F })
  void assertThatDepositWithAmountGreaterThanZeroIncrementsTheAccountBalance(float depositAmount) {
    //TODO
    fail("Test not implemented.");
  }  
}
