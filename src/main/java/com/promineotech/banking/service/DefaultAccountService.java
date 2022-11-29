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
      return(Response.BadRequest("An invalid owner supplied."));
    }
    if (initialDeposit <= MINIMUM_BALANCE_TO_OPEN_ACCOUNT) {
      return(Response.BadRequest(String.format("A minimum deposit of $%f is required to open a new account.",
                 MINIMUM_BALANCE_TO_OPEN_ACCOUNT)));
    }

    String accountNumber = accountNumberGenerator.generate();
    AccountModel account = repository.save(accountNumber, owner, 
                                           initialDeposit);
    if (account != null) {
      return(Response.OK(account, "Account created."));
    }
    return Response.Error("Account creation failed.");
  }

  @Override
  public Response<AccountModel> close(String number) {
    Optional<AccountModel> account = repository.get(number);
    if (account.isPresent()) {
      if (account.get().getBalance() == 0) {
        AccountModel removedAccount = repository.remove(account.get().getNumber());
        if (removedAccount != null) {
          return Response.OK(removedAccount, "Account closed.");
        }
        return(Response.Error("Failed to close requested account."));
      }
      return(Response.BadRequest(account.get(),
                                 "Account cannot be closed with a non-zero balance."));
    }
    return Response.NotFound("Account doesn't exist. Close request denied");
  }

  @Override
  public Response<AccountModel> deposit(String number, Float amount) {
    Optional<AccountModel> existingAccount = repository.get(number);
    if (existingAccount.isPresent()) {
      if (amount > 0) {
        AccountModel updatedAccount = repository.save(number, 
            existingAccount.get().getOwner(),
            existingAccount.get().getBalance() + amount);
        if (updatedAccount != null) {
          return Response.OK(updatedAccount,
              String.format("$%3.2f deposited into account. Account Balance: $%3.2f",
                            amount, updatedAccount.getBalance()));
        }
        return Response.Error("Failed to deposit required funds into account.");
      }
      return Response.BadRequest(existingAccount.get(), 
                             "Can't deposit a negative amount");
    }
    return Response.NotFound("Account doesn't exist. Can't deposit into invalid account");
  }

  @Override
  public Response<AccountModel> withdraw(String number, Float amount) {
    Optional<AccountModel> existingAccount = repository.get(number);
    if (existingAccount.isPresent()) {
      if (amount > 0) {
        AccountModel updatedAccount = repository.save(number, 
            existingAccount.get().getOwner(), 
            existingAccount.get().getBalance() - amount);
        if (updatedAccount != null) {
          return Response.OK(updatedAccount,
              String.format("$%3.2f withdrawn from account. Account Balance: $%3.2f",
                            amount, updatedAccount.getBalance()));
        }
        return Response.Error("Failed to withdraw requested funds from account.");
      }
      return Response.BadRequest(existingAccount.get(), 
                                 "Can't withdraw a negative amount");
    }
    return Response.NotFound("Account doesn't exist. Can't deposit into invalid account");
  }
}
