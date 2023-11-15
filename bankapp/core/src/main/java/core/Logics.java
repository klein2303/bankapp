package core;

import core.accounts.SpendingsAccount;
import java.util.List;

/**
 * Class to perform operations for BankAppController.
 */
public class Logics {
  /**
   * Takes in a list of transaction and makes a list of the last 10 elements from
   * the list.
   *
   * @param transactions - The list of transactions
   * @return List of at most 10 most recently added transactions
   */
  public static Transaction[] getReveredTransactionsArray(List<Transaction> transactions) {
    Transaction[] original = transactions.toArray(new Transaction[transactions.size()]);
    Transaction[] reversed = new Transaction[10];
    int count0 = 0;
    while (count0 < reversed.length && count0 < original.length) {
      reversed[count0] = original[original.length - (count0 + 1)];
      count0++;
    }
    return reversed;
  }

  /**
   * Finds spendingsaccount with given accountNr from a list of profiles.
   *
   * @param accountNr - Accountnumber of the Account object to search for
   * @param profiles  - list of profiles
   * @return Spendingsaccount with given accountNr
   */
  public static SpendingsAccount findOverallSpendingsAccount(String accountNr,
      List<Profile> profiles) {
    SpendingsAccount spendingsAccount;
    spendingsAccount = (SpendingsAccount) profiles.stream()
        .flatMap(profile -> profile.getAccounts().stream())
        .filter(account -> (account instanceof SpendingsAccount))
        .filter(account -> account.getAccNr().equals(accountNr))
        .findFirst().orElse(null);
    return spendingsAccount;
  }

  /**
   * Checks if profile's password matches with given password.
   *
   * @param profile  - profile to check
   * @param password - password to match with
   * @return Profile if passwords match
   *
   * @throws IllegalArgumentException - if passwords don't match
   */
  public static Profile checkProfile(Profile profile, String password) {
    if (profile.getPassword().equals(password)) {
      return profile;
    }
    throw new IllegalArgumentException("Password does not match profile");
  }

  /**
   * Checks if a email or phonenumber have been registered before.
   *
   * @param profiles - List of all profiles
   * @param email    - email to check for
   * @param phoneNr  - phonenumber to check for
   */
  public static void checkAlreadyRegistered(List<Profile> profiles, String email, String phoneNr) {
    for (Profile profile : profiles) {
      if (profile.getEmail().equals(email) || profile.getTlf().equals(phoneNr)) {
        throw new IllegalArgumentException("Account already registered");
      }
    }
  }

}
