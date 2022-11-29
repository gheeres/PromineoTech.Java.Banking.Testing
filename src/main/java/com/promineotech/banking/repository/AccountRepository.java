package com.promineotech.banking.repository;

import java.io.Closeable;
import java.util.Optional;
import java.util.stream.Stream;
import com.promineotech.banking.model.AccountModel;

public interface AccountRepository extends Closeable {
  /**
   * Saves the updated account information.
   * @param number The unique account number.
   * @param owner The unique id of the account holder.
   * @param balance The current balance of the account.
   * @return The updated or saved account information.
   */
  AccountModel save(String number, String owner, Float balance);
  
  /**
   * Removes or deletes the account information.
   * @param number The unique account number.
   * @return The deleted or removed account information.
   */
  AccountModel remove(String number);

  /**
   * Retrieves an account based on the unique account number.
   * @param number The unique account number.
   * @return The account if found, otherwise an empty Optional.
   */
  Optional<AccountModel> get(String number);

  /**
   * Retrieves all accounts owned by the same account holder.
   * @param owner The unique id of the account holder.
   * @return The collection of accounts if any otherwise an empty list is returned.
   */
  Stream<AccountModel> ownedBy(String owner);

  /**
   * Retrieves all accounts.
   * @return The collection of accounts if any otherwise an empty list is returned.
   */
  Stream<AccountModel> all();
}
