package com.promineotech.banking.service;

/**
 * A sequential account number generator.
 */
public class SequentialAccountNumberGenerator implements AccountNumberGenerator {
  private int lastAccountNumber;
  
  public SequentialAccountNumberGenerator(int startingAccountNumber) {
    lastAccountNumber = startingAccountNumber;
  }
  
  @Override
  public String generate() {
    return String.format("%010d", ++lastAccountNumber);
  }
}
