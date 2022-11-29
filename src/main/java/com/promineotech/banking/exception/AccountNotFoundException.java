package com.promineotech.banking.exception;

/**
 * An exception that occurs when an account cannot be
 * found.
 */
public class AccountNotFoundException extends BankingException {
  private static final long serialVersionUID = 1L;
  private String number;
  
  public AccountNotFoundException(String number) {
    this(number, String.format("The requested account not found. Account: %s", number));
  }

  public AccountNotFoundException(String number, String message) {
    super(message);
    this.number = number;
  }

  public String getNumber() {
    return number;
  }
}
