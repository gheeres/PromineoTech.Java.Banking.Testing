package com.promineotech.banking;

import java.io.IOException;
import java.util.Scanner;
import java.util.stream.Collectors;
import com.promineotech.banking.model.AccountModel;
import com.promineotech.banking.repository.AccountCSVRepository;
import com.promineotech.banking.repository.AccountJDBCRepository;
import com.promineotech.banking.repository.AccountListRepository;
import com.promineotech.banking.repository.AccountRepository;
import com.promineotech.banking.service.AccountNumberGenerator;
import com.promineotech.banking.service.AccountService;
import com.promineotech.banking.service.DefaultAccountService;
import com.promineotech.banking.service.RandomAccountNumberGenerator;
import com.promineotech.banking.service.SequentialAccountNumberGenerator;

public class Application {
  Scanner input = new Scanner(System.in);

  public static void main(String[] args) {
    System.out.println("---------------- [Start] ----------------");
    new Application().run(args);
    System.out.println("---------------- [ End ] ----------------");
  }
  
  /**
   * Retrieves the default or preferred back-end system.
   * @return The configured AccountRepository
   */
  public AccountRepository getRepository() {
    System.out.println("Which backend do you want to connect to:");
    System.out.printf(" a.) MySQL (Database / JDBC)%n");
    System.out.printf(" b.) CSV File (%s)%n", Configuration.getProperty("alternate.path"));
    System.out.printf(" c.) In Memory (default)%n");
    String backend = input.nextLine();
    
    if ("a".equalsIgnoreCase(backend)) {
      String url = Configuration.getProperty("datasource.url");
      return new AccountJDBCRepository(url);
    }
    
    if ("b".equalsIgnoreCase(backend)) {
      String filename = Configuration.getProperty("alternate.path");
      try {
        return new AccountCSVRepository(filename);
      }
      catch (IOException e) {
        System.out.printf("Requested file was not found. File: %s%n", filename);
      }
    }

    System.out.println("Using default / fallback 'In Memory' repository. No changes will be saved.");
    return new AccountListRepository();
  }

  /**
   * Retrieves the default or preferred account number generator sequence.
   * @return The configured AccountNumberGenerator
   */
  public AccountNumberGenerator getAccountNumberGenerator() {
    System.out.println("How should account numbers be generated?");
    System.out.printf(" a.) Random (default)%n");
    System.out.printf(" b.) Sequential%n");
    String generator = input.nextLine();
    
    if ("b".equalsIgnoreCase(generator)) {
      int defaultAccountNumberStart = 10000000;
      System.out.printf("What number should accounts start at? i.e. %d%n", defaultAccountNumberStart);
      String accountNumberStartAsString = input.nextLine();
      int accountNumberStart = defaultAccountNumberStart;
      try {
        accountNumberStart = Integer.parseInt(accountNumberStartAsString);
      }
      catch(NumberFormatException e) {
        accountNumberStart = defaultAccountNumberStart;
      }
      return new SequentialAccountNumberGenerator(accountNumberStart);
    }
    return new RandomAccountNumberGenerator();
  }

  public void run(String[] args) {
    AccountRepository repository = getRepository();
    AccountNumberGenerator accountNumberGenerator = getAccountNumberGenerator();
    AccountService service = new DefaultAccountService(repository, 
                                                       accountNumberGenerator);
    // service.open(...);
    // service.deposit(...);
    // service.withdraw(...);
    // service.close(...);
    
    for (AccountModel account : repository.all()
                                          .collect(Collectors.toList())) {
      System.out.println(account);
    }
  }
}
