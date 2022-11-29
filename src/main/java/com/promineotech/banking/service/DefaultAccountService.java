package com.promineotech.banking.service;

import java.util.Optional;
import com.promineotech.banking.Response;
import com.promineotech.banking.model.AccountModel;
import com.promineotech.banking.repository.AccountRepository;

public class DefaultAccountService implements AccountService {
  public static final float MINIMUM_BALANCE_TO_OPEN_ACCOUNT = 5F;
  private AccountRepository repository;
  private AccountNumberGenerator accountNumberGenerator;
  
  public DefaultAccountService(AccountRepository repository, 
      AccountNumberGenerator accountNumberGenerator) {
    this.repository = repository;
    this.accountNumberGenerator = accountNumberGenerator;
  }
  
  @Override
  public Response<AccountModel> open(String owner, Float initialDeposit) {
    //TODO
    throw new UnsupportedOperationException();
  }

  @Override
  public Response<AccountModel> close(String number) {
    //TODO
    throw new UnsupportedOperationException();
  }

  @Override
  public Response<AccountModel> deposit(String number, Float amount) {
    //TODO
    throw new UnsupportedOperationException();
  }

  @Override
  public Response<AccountModel> withdraw(String number, Float amount) {
    //TODO
    throw new UnsupportedOperationException();
  }
}
