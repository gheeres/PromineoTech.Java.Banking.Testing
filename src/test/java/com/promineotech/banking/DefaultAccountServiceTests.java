package com.promineotech.banking;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
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
    repository = Mockito.spy(getRepository());
    service = getService(repository);
  }
  
  @AfterEach
  void tearDown() {
  }

  private AccountRepository getRepository() {
    return new AccountListRepository();
  }

  private AccountService getService() {
    return getService(null);
  }
  
  private AccountService getService(AccountRepository repository) {
    if (repository == null) {
      repository = getRepository();
    }
    AccountNumberGenerator accountNumberGenerator = new SequentialAccountNumberGenerator(DEFAULT_STARTING_ACCOUNT_NUMBER);
    return new DefaultAccountService(repository, accountNumberGenerator);
  }

  @Test
  void assertThatOpenCreatesNewAccount() {
    String owner = "test@bank.com";
    float balance = 101F;
    
    AccountModel expected = new AccountModel("1000000", owner)
                                .setBalance(balance);
    doReturn(expected).when(repository)
                      .save(anyString(), anyString(), anyFloat());

    Response<AccountModel> response = service.open(owner, balance);
    assertThat(response.getCode()).isEqualTo(Response.OK);
    
    AccountModel account = response.get();
    assertThat(account).isNotNull();
    assertThat(account.getOwner()).isEqualTo(owner);
    assertThat(account.getNumber()).isNotEmpty();
    assertThat(account.getBalance()).isCloseTo(balance, within(0.0001F));
  }
  
  @Test
  void assertThatOpenWithNullOwnerReturnBadRequest() {
    float balance = 101F;
    String owner = null;
    doReturn(null).when(repository)
                  .save(anyString(), anyString(), anyFloat());

    Response<AccountModel> response = service.open(owner, balance);
    assertThat(response.getCode()).isEqualTo(Response.BAD_REQUEST);

    AccountModel account = response.get();
    assertThat(account).isNull();
  }

  @ParameterizedTest
  @ValueSource(floats = { 0F, -0.01F, 4.99F, 4.999999999F })
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
    String accountNumber = "00000000";
    
    Response<AccountModel> response = service.close(accountNumber);
    assertThat(response.getCode()).isEqualTo(Response.NOT_FOUND);
    
    AccountModel account = response.get();
    assertThat(account).isNull();
  }

  @Test
  void assertThatCloseWithNoBalanceInAccountSucceeds() {
    String owner = "test@bank.com";
    float balance = 0F;
    String accountNumber = "10000000";
    
    AccountModel expected = new AccountModel(accountNumber, owner)
                                            .setBalance(balance); 
    doReturn(Optional.ofNullable(expected)).when(repository)
                                           .get(accountNumber);
    doReturn(expected).when(repository)
                      .save(accountNumber, owner, balance);

    Response<AccountModel> response = service.close(accountNumber);
    assertThat(response.getCode()).isEqualTo(Response.OK);
    
    AccountModel account = response.get();
    assertThat(account).isNotNull();
    assertThat(account.getOwner()).isEqualTo(owner);
    assertThat(account.getNumber()).isNotEmpty();
    assertThat(account.getBalance()).isCloseTo(balance, within(0.0001F));
  }

  @Test
  void assertThatCloseWithBalanceRemainingInAccountReturnsBadRequest() {
    String owner = "test@bank.com";
    float balance = 100F;
    String accountNumber = "10000000";
    
    AccountModel expected = new AccountModel(accountNumber, owner)
                                            .setBalance(balance);
    doReturn(Optional.ofNullable(expected)).when(repository)
                                           .get(accountNumber);
    doReturn(expected).when(repository)
                      .remove(accountNumber);

    Response<AccountModel> response = service.close(accountNumber);
    assertThat(response.getCode()).isEqualTo(Response.BAD_REQUEST);
    
    AccountModel account = response.get();
    assertThat(account).isNotNull();
    assertThat(account.getOwner()).isEqualTo(owner);
    assertThat(account.getNumber()).isNotEmpty();
    assertThat(account.getBalance()).isCloseTo(balance, within(0.0001F));
  }
  
  @ParameterizedTest
  @ValueSource(floats = { 0F, -0.01F, -0.000001F, -1F })
  void assertThatDepositWithLessOrEqualToZeroAmountReturnsBadRequest(float depositAmount) {
    String owner = "test@bank.com";
    float balance = 100F;
    String accountNumber = "10000000";
    
    AccountModel expected = new AccountModel(accountNumber, owner)
                                            .setBalance(balance);
    doReturn(Optional.ofNullable(expected)).when(repository)
                                           .get(accountNumber);
    doReturn(expected).when(repository)
                      .save(accountNumber, owner, balance);

    Response<AccountModel> response = service.deposit(accountNumber, depositAmount);
    assertThat(response.getCode()).isEqualTo(Response.BAD_REQUEST);
    
    AccountModel account = response.get();
    assertThat(account).isNotNull();
    assertThat(account.getOwner()).isEqualTo(owner);
    assertThat(account.getNumber()).isNotEmpty();
    assertThat(account.getBalance()).isCloseTo(balance, within(0.0001F));
    
    verify(repository, never()).save(accountNumber, owner, balance);
  }  

  @ParameterizedTest
  @ValueSource(floats = { 10F, 10.01F, 100.0F, 9.99F })
  void assertThatDepositWithAmountGreaterThanZeroIncrementsTheAccountBalance(float depositAmount) {
    String owner = "test@bank.com";
    float balance = 100F;
    String accountNumber = "10000000";
    
    AccountModel existing = new AccountModel(accountNumber, owner)
                                            .setBalance(balance);
    doReturn(Optional.ofNullable(existing)).when(repository)
                                           .get(accountNumber);
    float expectedBalance = existing.getBalance() + depositAmount;
    AccountModel expected = new AccountModel(existing)
                                .setBalance(expectedBalance);
    doReturn(expected).when(repository)
                      .save(accountNumber, owner, expectedBalance);

    Response<AccountModel> response = service.deposit(accountNumber, depositAmount);
    assertThat(response.getCode()).isEqualTo(Response.OK);
    
    AccountModel account = response.get();
    assertThat(account).isNotNull();
    assertThat(account.getOwner()).isEqualTo(owner);
    assertThat(account.getNumber()).isNotEmpty();
    assertThat(account.getBalance()).isCloseTo(balance, within(0.0001F));
    
    verify(repository, never()).save(accountNumber, owner, balance);
  }  
}
