package com.promineotech.banking.service;

import java.util.UUID;

/**
 * A random unique account number generator.
 */
public class RandomAccountNumberGenerator implements AccountNumberGenerator {
  @Override
  public String generate() {
    return UUID.randomUUID().toString();
  }
}
