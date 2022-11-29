package com.promineotech.banking.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.promineotech.banking.exception.BankingException;

class AccountModelTest {

  @BeforeEach
  void setUp() throws Exception {}

  @AfterEach
  void tearDown() throws Exception {}

  @Test
  void testToStringWithAValidAccountModelReturnsString() throws Exception {
    // Arrange
    String expected = "AccountModel [owner=test, "
        + "number=1111, balance=0.0, lastTransaction=null]";
    AccountModel account = new AccountModel("1111", "test");
    
    // Act
    String actual = account.toString();
    
    // Assert
    assertThat(actual).isEqualTo(expected);
  }
  
  @Test
  void testConstructorWithNullNumberThrowsException() {
    // Arrange
    try {
      AccountModel account = new AccountModel(null, "test");
      fail("Account should not allow empty number");
    } catch(BankingException exception) {
      // This is the expected or desired path    
    }
  }

  @Test
  void testConstructorWithNullOwnerThrowsException() {
    assertThatExceptionOfType(BankingException.class)
    .isThrownBy(() -> {
      AccountModel account = new AccountModel("1000000", null); 
    })
    .withMessage("Owner cannot be null");  
  }
  
  @Test
  void testSetOwnerWithNullThrowsException() {
    // Arrange
    try {
      AccountModel account = new AccountModel("1111", "test");
      account.setOwner(null);
      fail("Account should not allow empty owner.");
    } catch(BankingException exception) {
      // This is the expected or desired path    
    }
  }
  
  @Test
  void testConstructorWithNullParentThrowsException() {
    assertThatExceptionOfType(BankingException.class)
    .isThrownBy(() -> {
      AccountModel account = new AccountModel((AccountModel) null);
    })
    .withMessage("Cannot clone / copy an empty or missing account");  
  }

  @Test
  void testConstructorWithNonNullParentCopiesValues() {
    String owner = "bob@bank.com";
    String accountNumber = "999999999";
    float balance = 100F;
    AccountModel expected = new AccountModel(accountNumber, owner)
        .setBalance(balance);
    
    AccountModel actual = new AccountModel(expected);
    
    assertThat(expected.getNumber())
              .isEqualToIgnoringCase(actual.getNumber());
    assertThat(expected.getOwner())
              .isEqualToIgnoringCase(actual.getOwner());
    assertThat(expected.getBalance())
              .isCloseTo(actual.getBalance(), within(0.0001F));
    assertThat(expected.getLastTransaction())
              .isEqualTo(actual.getLastTransaction());
  }
}
