package com.promineotech.banking.repository;

import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.promineotech.banking.model.AccountModel;

public class AccountCSVRepository extends AccountListRepository 
                                  implements AccountRepository, Closeable {
  private static final String[] HEADER = new String[] {
    "Account",
    "Owner",
    "Balance",
    "TransactionDate"
  };
  private static final DateFormat ISO8601UTCDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");

  private String filename;

  /**
   * Creates an instance of AccountModel from a CSV line
   * @param line The comma separated line values
   * @return The instance if valid, otherwise null.
   */
  private AccountModel fromCSV(String[] line) {
    try {
      return new AccountModel(
        line[0], line[1],
        Float.parseFloat(line[2]),
        ISO8601UTCDateFormat.parse(line[3])
      );
    } catch(NumberFormatException e) {
    } catch (ParseException e) {
    }
    return(null);
  }
  
  /**
   * Converts an instance of AccountModel to a CSV formatted line.
   * @param account The instance to convert.
   * @return The converted line.
   */
  private String[] toCSV(AccountModel account) {
    if (account != null) {
      return(new String[] {
         account.getNumber(),
         account.getOwner(),
         account.getBalance().toString(),
         ISO8601UTCDateFormat.format(account.getLastTransaction())
      });
    }
    return(new String[0]);
  }
  
  /**
   * Creates an instance of CSVAccountRepository
   * @param filename The path/name of the CSV file.
   */
  public AccountCSVRepository(String filename) throws IOException {
    ISO8601UTCDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    this.filename = filename;
    refresh();
  }

  public void refresh() {
    List<AccountModel> accounts = new ArrayList<>();
    File file = new File(filename);
    if (file.exists()) {
      try (FileReader fileReader = new FileReader(file);
           CSVReader csvReader = new CSVReader(fileReader)) {
        int index = 0;
        String[] line;
        while ((line = csvReader.readNext()) != null) {
          if (index > 0) {
            AccountModel account = fromCSV(line);
            if (account != null) {
              accounts.add(account);
            }
          }
          index++;
        }
      } catch (IOException e) {
      }
    }
    this.accounts = accounts;
  }

  public void save() {
    File file = new File(filename);
    try (FileWriter fileWriter = new FileWriter(file);
         CSVWriter csvWriter = new CSVWriter(fileWriter)) {
      csvWriter.writeNext(HEADER);
      
      for(AccountModel account: accounts) {
        String[] line = toCSV(account);
        if (line.length > 0) {
          csvWriter.writeNext(line);
        }
      }
    }
    catch(IOException e) {
    }
  }
  
  @Override
  public void close() {
    save();
  }
}
