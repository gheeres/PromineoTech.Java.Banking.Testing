package com.promineotech.banking.repository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import com.promineotech.banking.exception.AccountNotFoundException;
import com.promineotech.banking.exception.BankingException;
import com.promineotech.banking.model.AccountModel;

public class AccountJDBCRepository implements AccountRepository {
  private String url;
  
  public AccountJDBCRepository(String url) {
    this.url = url;
  }
  
  /**
   * Retrieves / opens JDBC connection.
   * @return The open connection.
   */
  public Connection getConnection() {
    try {
      return DriverManager.getConnection(url);
    } catch (SQLException e) {
      throw new BankingException("Failed to get database connection.", e);
    }
  }
  
  @Override
  public void close() throws IOException {
  }

  @Override
  public AccountModel save(String number, String owner, Float balance) {
    try (Connection connection = getConnection()) {
      //Prepare SQL Statement
      final String sql = "INSERT INTO Bank (Account,Owner,Balance) "
                       + "VALUES (?,?,?);";
      try (PreparedStatement statement = connection.prepareStatement(sql)){
        //Add / bind parameters
        statement.setString(1,number);
        statement.setString(2,owner);
        statement.setDouble(3,balance);
        
        //Execute / Open Reader
        int count = statement.executeUpdate();
        if (count > 0) {
          Optional<AccountModel> account = get(number);
          if (account.isPresent()) {
            return(account.get());
          }
        }
      } catch (Exception e) {
        throw new BankingException("Failed to save requested account information.", e);
      }
    }
    catch (SQLException e) {
      throw new BankingException("Failed to save requested account information.", e);
    }
    return null;
  }

  @Override
  public AccountModel remove(String number) {
    Optional<AccountModel> account = get(number);
    if (account.isPresent()) {
      try (Connection connection = getConnection()) {
        //Prepare SQL Statement
        final String sql = "DELETE FROM Bank "
                         + "WHERE Account = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
          //Add / bind parameters
          statement.setString(1,number);
        
          //Execute / Open Reader
          int count = statement.executeUpdate();
          if (count > 0) {
            return(account.get());
          }
          throw new AccountNotFoundException(number);
        } catch (Exception e) {
          throw new BankingException("Failed to remove requested account information.", e);
        }
      } catch (SQLException e) {
        throw new BankingException("Failed to remove requested account information.", e);
      }
    }
    
    throw new AccountNotFoundException(number);
  }

  /**
   * Serializes a ResultSet into an AccountModel instance.
   * @param rs The ResultSet to process.
   * @return The serialized instance if successful, otherwise returns null.
   */
  private AccountModel toAccountModel(ResultSet rs) {
    try {
      return new AccountModel(rs.getString("Account"), 
                              rs.getString("Owner"),
                              rs.getFloat("Balance"),
                              rs.getDate("TransactionDate"));
    } catch (SQLException e) {
      return null;
    }
  }
  
  @Override
  public Optional<AccountModel> get(String number) {
    try (Connection connection = getConnection()) {
      //Prepare SQL Statement
      final String sql = "SELECT Account,Owner,Balance,TransactionDate "
                       + "FROM account "
                       + "WHERE Account = ?;";
      try (PreparedStatement statement = connection.prepareStatement(sql)){
        // Add / bind parameters
        statement.setString(1, number);
        
        // Execute / Open Reader
        try (ResultSet rs = statement.executeQuery()) {
          //Iterate (row by row)
          if(rs.next()) {
            AccountModel account = toAccountModel(rs);
            return(Optional.ofNullable(account));
          }
        }
      } catch (Exception e) {
        String message = String.format("Failed to serialize/retrieve requested account. Account: %s",
                                       number);
        throw new BankingException(message, e);
      }  
    } catch (SQLException e) {
      throw new BankingException("Failed to read from underlying data provider.", e);
    }  
    return(Optional.empty());  
  }

  @Override
  public Stream<AccountModel> ownedBy(String owner) {
    try (Connection connection = getConnection()) {
      //Prepare SQL Statement
      final String sql = "SELECT Account,Owner,Balance,TransactionDate "
                       + "FROM account "
                       + "WHERE Owner = ?;";
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        //Add / bind parameters
        statement.setString(1, owner);
      
        //Execute / Open Reader
        try(ResultSet rs = statement.executeQuery()) {
          //Iterate (row by row)
          List<AccountModel> accounts = new ArrayList<AccountModel>();
          while(rs.next()) {
            AccountModel account = toAccountModel(rs);
            if (account != null) {
              accounts.add(account);
            }
          }
          return(accounts.stream());
        }
      } catch (Exception e) {
        String message = String.format("Failed to serialize/retrieve requested accounts. Owner: %s",
                                       owner);
        throw new BankingException(message, e);
      }  
    } catch (SQLException e) {
      throw new BankingException("Failed to read from underlying data provider.", e);
    }  
  }

  @Override
  public Stream<AccountModel> all() {
    try (Connection connection = getConnection()) {
      // Prepare SQL Statement
      final String sql = "SELECT Account,Owner,Balance,TransactionDate "
                       + "FROM account "
                       + "ORDER BY Account;";
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        // Add / bind parameters
      
        //Execute / Open Reader
        try (ResultSet rs = statement.executeQuery()) {
          //Iterate (row by row)
          List<AccountModel> accounts = new ArrayList<AccountModel>();
          while(rs.next()) {
            AccountModel account = toAccountModel(rs);
            if (account != null) {
              accounts.add(account);
            }
          }
          return(accounts.stream());
        }
      }
      catch (Exception e) {
        throw new BankingException("Failed to serialize/retrieve requested accounts.", e);
      }  
    } catch (SQLException e) {
      throw new BankingException("Failed to read from underlying data provider.", e);
    }  
  }
}
