package com.promineotech.banking.service;

/**
 * An interface for generating unique account identifiers.
 */
public interface AccountNumberGenerator {
  /**
   * Generates a unique account number.
   * @return The account number.
   */
  String generate();
}
