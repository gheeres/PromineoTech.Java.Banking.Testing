package com.promineotech.banking.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.within;
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
  void testToStringWithAValidAccountModelReturnsString() {
	// Arrange
	String expected = "AccountModel [owner=test, number=1111, balance=0.0, lastTransaction=null]";
	AccountModel account = new AccountModel("1111", "test");			
	  
	// Act
	String actual = account.toString();
	  
	// Assert
	assertThat(actual).isEqualTo(expected);
  }
  
  @Test
  void testConstructorWithNullNumberThrowsException() {
	try {
      // Create invalid account
      AccountModel account = new AccountModel(null, "test");
      fail("Account should not allow empty number");
	}
	catch(BankingException e) {
	  // this is the expected or desired path
	}
  }

  @Test
  void testConstructorWithNullOwnerThrowsException() {
    try {
	  // Create invalid account
	  AccountModel account = new AccountModel("1111", null);
	  fail("Account should not allow empty owner");
    }
	catch(BankingException e) {
	  // this is the expected or desired path
    }
  }
  
  @Test
  void testSetOwnerWithNullThrowsException() {
    try {
  	  AccountModel account = new AccountModel("1111", "test");
  	  account.setOwner(null);

  	  fail("Account should not allow empty owner");
    }
 	catch(BankingException e) {
 	  // this is the expected or desired path
    }
  }
  
  @Test
  void testConstructorWithNullParentThrowsException() {
	assertThatExceptionOfType(BankingException.class)
	.isThrownBy(() -> {
	  AccountModel account = new AccountModel(null);	
	});
  }

  @Test
  void testConstructorWithNonNullParentCopiesValues() {
	// Arrange
	String owner = "bob@bank.com";
	String accountNumber = "9999999";
	float balance = 101.1F;
	AccountModel expected = new AccountModel(accountNumber, owner)
			                    .setBalance(balance);
	
	// Act
	AccountModel actual = new AccountModel(expected);
	
	// Assert
	assertThat(actual.getOwner()).isEqualTo(expected.getOwner());
	assertThat(actual.getNumber()).isEqualTo(expected.getNumber());
	assertThat(actual.getBalance()).isEqualTo(expected.getBalance(), within(0.00001F));
	assertThat(actual.getLastTransaction()).isEqualTo(expected.getLastTransaction());
  }
}
