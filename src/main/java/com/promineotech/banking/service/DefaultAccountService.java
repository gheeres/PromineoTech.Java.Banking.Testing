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
	if ((owner == null) || (owner.isEmpty())) {
      return Response.BadRequest("An invalid owner was specified.");
	}
	if (initialDeposit < MINIMUM_BALANCE_TO_OPEN_ACCOUNT) {
      return Response.BadRequest(String.format("A minimum depost of $%f is required to open an account", MINIMUM_BALANCE_TO_OPEN_ACCOUNT));
	}
	
	String accountNumber = accountNumberGenerator.generate();
	AccountModel account = repository.save(accountNumber, owner, initialDeposit);
	if (account != null) {
	  return Response.OK(account, "Account created");	
	}
	return Response.Error("Account creation failed.");
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
