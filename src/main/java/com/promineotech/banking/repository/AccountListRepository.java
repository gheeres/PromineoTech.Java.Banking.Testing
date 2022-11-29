package com.promineotech.banking.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.promineotech.banking.exception.AccountNotFoundException;
import com.promineotech.banking.exception.BankingException;
import com.promineotech.banking.model.AccountModel;

public class AccountListRepository implements AccountRepository {
  private static final List<AccountModel> defaultAccounts = List.of(
    new AccountModel("123456", "bob@bank.com").setBalance(1000F),
    new AccountModel("123456-002", "bob@bank.com").setBalance(301F),
    new AccountModel("123456-007", "bob@bank.com").setBalance(235F),
    new AccountModel("100001", "tom@bank.com").setBalance(100F),
    new AccountModel("100002", "april@bank.com").setBalance(500F),
    new AccountModel("100002-001", "april@bank.com").setBalance(9387F)
  );
  protected List<AccountModel> accounts;
  
  public AccountListRepository() {
    this(defaultAccounts);
  }
  public AccountListRepository(List<AccountModel> accounts) {
    if (accounts != null) {
      this.accounts = accounts.stream()
          .collect(Collectors.toList());
    }
    else { 
      this.accounts = new ArrayList<>();
    }
  }
  
  @Override
  public AccountModel save(String number, String owner, Float balance) {
    if ((number != null) && (! number.isEmpty())) {
      Optional<AccountModel> existingAccount = get(number);
      if (! existingAccount.isPresent()) {
        AccountModel newAccount = new AccountModel(number, owner);
        newAccount.setBalance(balance);
        accounts.add(newAccount);
        return(newAccount);
      }

      existingAccount.get().setBalance(balance);
      return(existingAccount.get());
    }
    
    throw new AccountNotFoundException(number);
  }

  @Override
  public AccountModel remove(String number) {
    if ((number != null) && (! number.isEmpty())) {
      Optional<AccountModel> account = get(number);
      if (account.isPresent()) {
        accounts.remove(account.get());
        return(account.get());
      }
    }
    throw new AccountNotFoundException(number);
  }

  @Override
  public Optional<AccountModel> get(String number) {
    if ((number != null) && (! number.isEmpty())) {
      return accounts.stream()
                     .filter((a) -> a.getNumber().equalsIgnoreCase(number))
                     .findFirst();
    }
    return Optional.empty();
  }

  @Override
  public Stream<AccountModel> ownedBy(String owner) {
    if ((owner != null) && (! owner.isEmpty())) {
      return accounts.stream()
                     .filter((a) -> a.getOwner().equalsIgnoreCase(owner));
    }
    return Stream.empty();
  }
  @Override
  public Stream<AccountModel> all() {
    return(accounts.stream());
  }

  @Override
  public void close() {
  }
}
