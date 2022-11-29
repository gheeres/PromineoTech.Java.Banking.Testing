package com.promineotech.banking.service;

import com.promineotech.banking.Response;
import com.promineotech.banking.model.AccountModel;

/**
 * An interface for interacting with a financial bank account.
 */
public interface AccountService {
  /**
   * Opens or creates a new account.
   * @param owner The unique id of the owner of the account.
   * @param initial Deposit initial deposit / amount for the account.
   * @return The created account information.
   */
  public Response<AccountModel> open(String owner, Float initialDeposit);

  /**
   * Closes or deletes a existing account.
   * @param number The unique account identifier.
   * @return The closed account information.
   */
  public Response<AccountModel> close(String number);

  /**
   * Deposits the specified amount into the account.
   * @param number The unique account identifier.
   * @param amount The amount to deposit.
   * @return The updated account information.
   */
  public Response<AccountModel> deposit(String number, Float amount);
  
  /**
   * Withdraws the specified amount from the account.
   * @param number The unique account identifier.
   * @param amount The amount to withdraw.
   * @return The updated account information.
   */
  public Response<AccountModel> withdraw(String number, Float amount);
}
