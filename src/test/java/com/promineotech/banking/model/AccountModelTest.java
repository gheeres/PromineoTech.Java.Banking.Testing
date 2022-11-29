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
    //TODO
    fail("Test not implemented.");
  }
  
  @Test
  void testConstructorWithNullNumberThrowsException() {
    //TODO
    fail("Test not implemented.");
  }

  @Test
  void testConstructorWithNullOwnerThrowsException() {
    //TODO
    fail("Test not implemented.");
  }
  
  @Test
  void testSetOwnerWithNullThrowsException() {
    //TODO
    fail("Test not implemented.");
  }
  
  @Test
  void testConstructorWithNullParentThrowsException() {
    //TODO
    fail("Test not implemented.");
  }

  @Test
  void testConstructorWithNonNullParentCopiesValues() {
    //TODO
    fail("Test not implemented.");
  }
}
