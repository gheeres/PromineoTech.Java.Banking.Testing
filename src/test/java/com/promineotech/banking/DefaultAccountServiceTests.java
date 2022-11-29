package com.promineotech.banking;

import static org.assertj.core.api.Assertions.fail;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import com.promineotech.banking.repository.AccountRepository;
import com.promineotech.banking.service.AccountService;

class DefaultAccountServiceTests {
  private final int DEFAULT_STARTING_ACCOUNT_NUMBER = 10000000;
  private AccountRepository repository;
  private AccountService service;

  @BeforeEach
  void setUp() {
  }
  
  @AfterEach
  void tearDown() {
  }

  @Test
  void assertThatOpenCreatesNewAccount() {
    //TODO
    fail("Test not implemented.");
  }
  
  @Test
  void assertThatOpenWithNullOwnerReturnBadRequest() {
    //TODO
    fail("Test not implemented.");
  }

  @ParameterizedTest
  @ValueSource(floats = { 0F, -0.01F, 4.99F, 4.999999999F })
  void assertThatOpenWithLessThanRequiredInitialDepositReturnsBadRequest(float balance) {
    //TODO
    fail("Test not implemented.");
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
