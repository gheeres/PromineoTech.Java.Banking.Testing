package com.promineotech.banking.repository;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;
import com.promineotech.banking.model.AccountModel;

/**
 * An empty or null implementation of an AccountRepository
 */
public class EmptyAccountRepository implements AccountRepository {
  @Override
  public void close() throws IOException {
  }

  @Override
  public AccountModel save(String number, String owner, Float balance) {
    return null;
  }

  @Override
  public AccountModel remove(String number) {
    return null;
  }

  @Override
  public Optional<AccountModel> get(String number) {
    return Optional.empty();
  }

  @Override
  public Stream<AccountModel> ownedBy(String owner) {
    return Stream.empty();
  }

  @Override
  public Stream<AccountModel> all() {
    return Stream.empty();
  }
}
