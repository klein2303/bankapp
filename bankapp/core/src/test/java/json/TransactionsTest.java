package json;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import core.Profile;
import core.Transaction;
import core.Accounts.SpendingsAccount;

public class TransactionsTest {

    private static final String currentDir = System.getProperty("user.dir");
    private final static String filename = currentDir + "/src/test/java/json/TransactionsOverviewTest.json";
    private final static String fakefile = "fakefile.json";

    private Profile profile1;
    private Profile profile2;
    private SpendingsAccount account1;
    private SpendingsAccount account2;
    private Transaction transaction1;

    @BeforeEach
    @DisplayName("Setting up profiles and accounts")
    public void setup() throws StreamWriteException, DatabindException, IOException {
        TransactionsPersistence.clearFile(filename);

        profile1 = new Profile("Ola Nordmann", "Ola@ntnu.no", "40123456", "Passord1");
        profile2 = new Profile("Kari Nordmann", "Kari@ntnu.no", "40654321", "Passord2");
        account1 = new SpendingsAccount("Useraccount", profile1);
        profile1.addAccount(account1);
        account2 = new SpendingsAccount("Useraccount", profile2);
        profile2.addAccount(account2);
        account1.add(500);
        account2.add(1000);
        transaction1 = new Transaction(profile1.getEmail(), account2.getAccNr(), account2.getProfile().getName(),
                account1.getAccNr(), 100);

    }

    @Test
    @DisplayName("Tests if constructor constructs object correct")
    public void testConstructor() {
        Transaction transaction2 = new Transaction(profile2.getEmail(), account1.getAccNr(),
                account1.getProfile().getName(), account2.getAccNr(), 300);
        assertEquals(transaction2.getEmail(), profile2.getEmail());
        assertEquals(transaction2.getTransactionTo(), account1.getAccNr());
        assertTrue(transaction2.getAmount() == 300);
    }

    @Test
    @DisplayName("Testing if application throws IOException if path does not exist")
    public void testFakeFile() {
        assertThrows(IOException.class, () -> TransactionsPersistence.writeTransactions(transaction1, fakefile));
        assertThrows(IOException.class, () -> TransactionsPersistence.readTransactions(fakefile));
    }

    @Test
    @DisplayName("Tests if written to file")
    public void testWriteTransactions() throws IOException {
        System.out.println(transaction1);
        TransactionsPersistence.writeTransactions(transaction1, filename);
        List<Transaction> transactionsList = TransactionsPersistence.readTransactions(filename);
        System.out.println(transactionsList);
        assertEquals(profile1.getEmail(), transactionsList.get(0).getEmail());
    }

    @Test
    @DisplayName("Tests if read correctly")
    public void testReadTransactions() throws IOException {
    }

    @Test
    @DisplayName("Test getter for the account paid to")
    public void testGetTransferTo() {
        assertEquals(transaction1.getTransactionTo(), account2.getAccNr());
        assertNotEquals(transaction1.getTransactionTo(), account1.getAccNr());
    }

    @Test
    @DisplayName("Test getter for the payer")
    public void testGetProfile() {
        assertEquals(transaction1.getEmail(), profile1.getEmail());
        assertNotEquals(transaction1.getEmail(), profile2.getEmail());
    }

    @Test
    @DisplayName("Test getter for the amount")
    public void testGetAmount() {
        assertTrue(transaction1.getAmount() == 100);
        assertFalse(transaction1.getAmount() == 300);
    }

}
