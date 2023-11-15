package ui;

import core.BankCard;
import core.Logics;
import core.Profile;
import core.Transaction;
import core.accounts.AbstractAccount;
import core.accounts.BsuAccount;
import core.accounts.SavingsAccount;
import core.accounts.SpendingsAccount;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controller for BankApp.
 */
public class BankAppController {
  @FXML
  private Label profileName;
  @FXML
  private AnchorPane spendingTab;
  @FXML
  private AnchorPane paymentsTab;
  @FXML
  private AnchorPane homeTab;
  @FXML
  private AnchorPane savingsTab;
  @FXML
  private AnchorPane profileTab;

  // register fxml
  @FXML
  private ImageView backArrow;
  @FXML
  private TextField fullName;
  @FXML
  private TextField email;
  @FXML
  private TextField phoneNr;
  @FXML
  private PasswordField password;
  @FXML
  private PasswordField passwordConfirm;
  @FXML
  private Button registerButton;
  @FXML
  private Label registerError;

  // login FXML
  @FXML
  private Button loginButton;
  @FXML
  private TextField emailInput;
  @FXML
  private PasswordField passwordInput;
  @FXML
  private Label loginError;
  @FXML
  private Label signUpButton;

  // pay FXML
  @FXML
  private ChoiceBox<String> payFromChoiceBox;
  @FXML
  private TextField payTo;
  @FXML
  private TextField payAmount;
  @FXML
  private AnchorPane payButton;
  @FXML
  private Text feedbackInPay;

  // payments FXML
  @FXML
  private AnchorPane goToTransferButton;
  @FXML
  private AnchorPane goToPayButton;

  // transfer FXML
  @FXML
  private TextField transferAmount;
  @FXML
  private AnchorPane transferButton;
  @FXML
  private Text feedbackInTransfer;
  @FXML
  private ChoiceBox<String> transferToChoiceBox;
  @FXML
  private ChoiceBox<String> transferFromChoiceBox;

  // savings fxml
  @FXML
  private Label totalBalance;
  @FXML
  private Label transferSavingsButton;
  @FXML
  private Label newSavingAccountButton;

  // overview fxml
  @FXML
  private Label spendingAccountBalance;
  @FXML
  private Label newAccountButton;
  @FXML
  private Label deleteAccountButton;
  @FXML
  private GridPane accountsTable;
  @FXML
  private AnchorPane overview;

  // spending fxml
  @FXML
  private GridPane transactionTable;

  // newAccount fxml
  @FXML
  private ChoiceBox<String> selectAccountType;
  @FXML
  private TextField giveAccountName;
  @FXML
  private AnchorPane createAccountButton;
  @FXML
  private Text feedbackInNewAccount;

  // settings fxml
  @FXML
  private TextField confirmChangePassword;
  @FXML
  private TextField changeNumberTo;
  @FXML
  private TextField changePasswordTo;
  @FXML
  private AnchorPane updateSettings;
  @FXML
  private Text feedbackInSettings;
  @FXML
  private Label deleteProfileButton;

  // profile fxml
  @FXML
  private AnchorPane settingsButton;
  @FXML
  private AnchorPane cardsButton;
  @FXML
  private AnchorPane logOutButton;

  // deleteAccount fxml
  @FXML
  private TextField deleteAccountName;
  @FXML
  private Button deleteAccountNow;
  @FXML
  private AnchorPane deleteAccount;
  @FXML
  private Text feedbackInDeleteAccount;

  // cards fxml
  @FXML
  private Label accountsWithBankcardLabel;
  @FXML
  private Label unBlockCardButton;
  @FXML
  private Label blockCardButton;
  @FXML
  private Label orderCardButton;
  @FXML
  private Label noCardsLabel;
  @FXML
  private GridPane cardsTable;

  // orderOrBlock fxml
  @FXML
  private Text orderOrBlockTitle;
  @FXML
  private Text feedbackInOrderOrBlock;
  @FXML
  private ChoiceBox<String> orderOrBlockChoiceBox;
  @FXML
  private Button orderOrBlockButton;

  private static Profile profile;

  private static final String endpointBaseUri = "http://localhost:8080/profiles/";
  private RemoteProfilesAccess profilesAccess;

  /**
   * Updates bankcard overview.
   */
  @FXML
  public void updateCards() {
    if (!profile.getBankCards().isEmpty()) {
      noCardsLabel.setVisible(false);
      accountsWithBankcardLabel.setVisible(true);
      int count = 0;
      String cardBlocked = "   (Card is blocked)";
      for (BankCard bankCard : profile.getBankCards()) {
        String message;
        if (bankCard.isCardBlocked()) {
          message = cardBlocked;
        } else {
          message = "";
        }
        Label label = new Label(
            "AccNr: " + bankCard.getAccount().getAccNr()
                + "  CardNr: " + bankCard.getCardNr() + message);
        label.setLayoutX(-20);
        label.setStyle("-fx-font-size: 12px; -fx-min-width: 100px; -fx-min-height: 30px;");
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefWidth(400.0);
        anchorPane.setPrefHeight(300.0);
        anchorPane.getChildren().add(label);
        AnchorPane.setTopAnchor(anchorPane, count * (30.0));
        cardsTable.add(anchorPane, 0, count);
        AnchorPane.setTopAnchor(cardsTable, 100.0);
        count++;
      }
    } else {
      noCardsLabel.setVisible(true);
      accountsWithBankcardLabel.setVisible(false);
    }
  }

  /**
   * Sets the stage for ordering a card.
   *
   * @param event - Mouseclick event
   * @throws IOException - Exception if load fails
   */
  @FXML
  public void handleOrderNewCardStage1(MouseEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("OrderOrBlockCard.fxml"));
    AnchorPane orderOrBlockCard = loader.load();
    Stage stage = new Stage();
    stage.setScene(new Scene(orderOrBlockCard));
    stage.setTitle("Order A new Card");
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.show();
    Parent root = stage.getScene().getRoot();
    Text orderOrBlockTitle = (Text) root.lookup("#orderOrBlockTitle");
    Button orderOrBlockButton = (Button) root.lookup("#orderOrBlockButton");
    ChoiceBox<String> orderOrBlockChoiceBox = (ChoiceBox<String>) root
        .lookup("#orderOrBlockChoiceBox");
    Text feedbackInOrderOrBlock = (Text) root.lookup("#feedbackInOrderOrBlock");

    if (orderOrBlockTitle != null) {
      orderOrBlockTitle.setText("Order Card");
      orderOrBlockTitle.setText("Order Card");
    }
    if (orderOrBlockButton != null) {
      orderOrBlockButton.setText("Order");
      orderOrBlockButton.setText("Order");
    }
    if (profile.accountsWithoutBankcards().size() == 0) {
      feedbackInOrderOrBlock.setText("No accounts that \n can be bankcards");
      feedbackInOrderOrBlock.setFill(Color.RED);
    } else if (profile.getAccounts().size() != 0 && orderOrBlockChoiceBox != null) {
      orderOrBlockChoiceBox.getItems().addAll(profile.accountsWithoutBankcards());
    }

  }

  /**
   * Sets the stage for blocking a card.
   *
   * @param event - Mouseclick event
   * @throws IOException - Exception if load fails
   */
  @FXML
  public void handleBlockCardStage1(MouseEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("OrderOrBlockCard.fxml"));
    AnchorPane orderOrBlockCard = loader.load();
    Stage stage = new Stage();
    stage.setScene(new Scene(orderOrBlockCard));
    stage.setTitle("Block Card");
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.show();

    Parent root = stage.getScene().getRoot();
    Text orderOrBlockTitle = (Text) root.lookup("#orderOrBlockTitle");
    Button orderOrBlockButton = (Button) root.lookup("#orderOrBlockButton");
    ChoiceBox<String> orderOrBlockChoiceBox = (ChoiceBox<String>) root
        .lookup("#orderOrBlockChoiceBox");
    Text feedbackInOrderOrBlock = (Text) root.lookup("#feedbackInOrderOrBlock");

    if (orderOrBlockTitle != null) {
      orderOrBlockTitle.setText("Block Card");
    }
    if (orderOrBlockButton != null) {
      orderOrBlockButton.setText("Block");
    }

    if (profile.getBankCards().isEmpty()) {
      feedbackInOrderOrBlock.setText("You have no bankcards \n to block");
      feedbackInOrderOrBlock.setFill(Color.RED);
    }
    try {
      orderOrBlockChoiceBox.getItems().addAll(profile.getListOfNotBlockedAccNrBankCards());

    } catch (Exception e) {
      feedbackInOrderOrBlock.setText("Something went wrong");
      feedbackInOrderOrBlock.setFill(Color.RED);
    }
  }

  /**
   * Sets the stage for unblocking a card.
   *
   * @param event - Mouseclick event
   * @throws IOException - Exception if load fails
   */
  @FXML
  public void handleUnblockCardStage1(MouseEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("OrderOrBlockCard.fxml"));
    AnchorPane unblockCardAnchorPane = loader.load();
    Stage stage = new Stage();
    stage.setScene(new Scene(unblockCardAnchorPane));
    stage.setTitle("Unblock Card");
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.show();

    Parent root = stage.getScene().getRoot();
    Text orderOrBlockTitle = (Text) root.lookup("#orderOrBlockTitle");
    Button orderOrBlockButton = (Button) root.lookup("#orderOrBlockButton");
    ChoiceBox<String> orderOrBlockChoiceBox = (ChoiceBox<String>) root
        .lookup("#orderOrBlockChoiceBox");
    Text feedbackInOrderOrBlock = (Text) root.lookup("#feedbackInOrderOrBlock");

    if (orderOrBlockTitle != null) {
      orderOrBlockTitle.setText("Unblock Card");
    }
    if (orderOrBlockButton != null) {
      orderOrBlockButton.setText("Unblock");
    }

    if (profile.getListOfBlockedAccNrBankCards().size() == 0) {
      feedbackInOrderOrBlock.setText("You have no bankcards \n to unblock");
      feedbackInOrderOrBlock.setFill(Color.RED);
    }

    try {
      orderOrBlockChoiceBox.getItems().addAll(profile.getListOfBlockedAccNrBankCards());
    } catch (Exception e) {
      feedbackInOrderOrBlock.setText("Something went wrong");
      feedbackInOrderOrBlock.setFill(Color.RED);
    }
  }

  /**
   * Handling the actual process of ordering or blocking or unblocking.
   *
   * @param event - Mouseclick event
   */
  @FXML
  public void handleOrderOrBlockStage2(MouseEvent event) {
    String accNr = orderOrBlockChoiceBox.getValue();
    Node sourceNode = (Node) event.getSource();
    Stage stage = (Stage) sourceNode.getScene().getWindow();
    if (orderOrBlockTitle.getText().equals("Order Card")) {
      SpendingsAccount spendingsAccount = null;
      spendingsAccount = profile.findSpendingsAccount(accNr);
      try {
        spendingsAccount.createBankCard();
        orderOrBlockChoiceBox.setValue("");
        feedbackInOrderOrBlock.setText("Order completed!");
        stage.close();
        writeInfo();
      } catch (Exception e) {
        feedbackInOrderOrBlock.setText("Something went wrong");
        feedbackInOrderOrBlock.setFill(Color.RED);
      }
    } else if (orderOrBlockTitle.getText().equals("Block Card")) {
      try {
        profile.getBankCard(accNr).blockCard();
        orderOrBlockChoiceBox.setValue("");
        feedbackInOrderOrBlock.setText("Block completed!");
        stage.close();
        writeInfo();
      } catch (Exception e) {
        feedbackInOrderOrBlock.setText(e.getMessage());
        feedbackInOrderOrBlock.setFill(Color.RED);
      }

    } else if (orderOrBlockTitle.getText().equals("Unblock Card")) {
      try {
        BankCard bankCard = profile.getBankCard(accNr);
        bankCard.unblockCard();
        orderOrBlockChoiceBox.setValue("");
        feedbackInOrderOrBlock.setText("Unblock completed!");
        stage.close();
        writeInfo();
      } catch (Exception e) {
        feedbackInOrderOrBlock.setText(e.getMessage());
        feedbackInOrderOrBlock.setFill(Color.RED);
      }

    }
  }

  /**
   * Initializes fields based on current page.
   */
  public void initialize() {
    try {
      profilesAccess = new RemoteProfilesAccess(new URI(endpointBaseUri));
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    if (accountsTable != null) {
      updateAccounts();
    }
    if (transactionTable != null) {
      updateTransaction();
    }
    if (profileName != null) {
      profileName.setText(profile.getName() + "'s Profile");
    }
    if (totalBalance != null) {
      totalBalance.setText(String.valueOf(profile.getTotalBalance()));
    }
    if (spendingAccountBalance != null && !profile.getAccounts().isEmpty()) {
      spendingAccountBalance.setText(String.valueOf(profile.getAccounts().get(0).getBalance()));
    }
    if (selectAccountType != null) {
      selectAccountType.getItems().addAll("Checking account", "Savings account",
          "BSU");
    }

    if (transferFromChoiceBox != null) {
      getInputsChoiceBox(transferFromChoiceBox);
    }

    if (transferToChoiceBox != null) {
      getInputsChoiceBox(transferToChoiceBox);
    }

    if (payFromChoiceBox != null) {
      for (SpendingsAccount spend : profile.getSpendingsAccounts()) {
        payFromChoiceBox.getItems().add(spend.getAccNr());
      }
    }

    if (noCardsLabel != null) {
      updateCards();
    }
  }

  public Profile getProfile() {
    return profile;
  }

  private void getInputsChoiceBox(ChoiceBox<String> choiceBox) {
    for (AbstractAccount absAcc : profile.getAccounts()) {
      choiceBox.getItems().add(absAcc.getAccNr());
    }
  }

  /**
   * Initializes the switch process between tabs.
   *
   * @param event - Mouseclick event
   * @throws IOException - Exception if load fails
   */

  @FXML
  public void initializeTab(MouseEvent event) throws IOException {
    String tab = ((Label) ((AnchorPane) event.getSource()).getChildren().get(0)).getText();
    if (tab.equals("Home")) {
      tab = "Overview";
    }
    switchTab(tab);

  }

  /**
   * Switches the tab to according to the value given by initializeTab.
   *
   * @param tab - name of tab to switch to
   * @throws IOException - Exception if load fails
   */

  @FXML
  private void switchTab(String tab) throws IOException {
    Stage primaryStage = (Stage) profileTab.getScene().getWindow();

    primaryStage.setTitle("Bankapp - " + tab);

    FXMLLoader tabLoader = new FXMLLoader(getClass().getResource(tab + ".fxml"));
    Parent tabPane = tabLoader.load();
    Scene tabScene = new Scene(tabPane);

    primaryStage.setScene(tabScene);
    primaryStage.show();
  }

  /**
   * Updates the account-table with the values of the accounts belonging to the
   * profile.
   * 
   */
  @FXML
  public void updateAccounts() {
    AnchorPane accountLabelAnchorPane = new AnchorPane();
    AnchorPane balanceLabelAnchorPane = new AnchorPane();
    accountLabelAnchorPane.setStyle("-fx-background-color: #214C69;");
    balanceLabelAnchorPane.setStyle("-fx-background-color: #214C69;");
    accountsTable.add(accountLabelAnchorPane, 0, 0);
    accountsTable.add(balanceLabelAnchorPane, 1, 0);

    int count = 1;
    if (profile.getAccounts().isEmpty()) {
      return;
    }
    for (AbstractAccount account : profile.getAccounts()) {
      AnchorPane accountAnchorPane = new AnchorPane();
      Label accountName = new Label(account.getName() + "\n"
          + account.getAccNr() + "\n" + account.toString());
      accountName.setStyle("-fx-font-size: 10px; -fx-min-width: 100px; -fx-min-height: 20px;");
      accountName.setLayoutX(10);
      accountAnchorPane.getChildren().add(accountName);
      accountAnchorPane.setPrefSize(100, 50);
      accountsTable.add(accountAnchorPane, 0, count);

      AnchorPane balanceAnchorPane = new AnchorPane();
      Label accountBalance = new Label(String.valueOf(account.getBalance()));
      accountBalance.setStyle("-fx-font-size: 10px; -fx-min-width: 100px; -fx-min-height: 20px;");
      accountBalance.setLayoutX(10);
      balanceAnchorPane.getChildren().add(accountBalance);
      balanceAnchorPane.setPrefSize(100, 50);
      accountsTable.add(balanceAnchorPane, 1, count);
      count += 1;
    }

  }

  /**
   * Updates the transaction-table in spending page when the profile transfers or
   * pays.
   *
   * @throws IOException - Exception if load fails
   */
  @FXML
  public void updateTransaction() {
    int size = profilesAccess.getTransactions(profile.getEmail()).size();
    if (size == 0) {
      return;
    }

    Transaction[] reversedTransaction = Logics
        .getReveredTransactionsArray(profilesAccess.getTransactions(profile.getEmail()));

    int count = 1;
    for (Transaction transaction : reversedTransaction) {
      if (transaction == null) {
        break;
      }
      AbstractAccount acc1 = profile.findAbstractAccountByAccNr(transaction.getTransactionTo());
      AbstractAccount acc2 = profile.findAbstractAccountByAccNr(transaction.getTransactionFrom());

      Label amountLabel;
      Label accountLabel = new Label(transaction.getTransactionFrom());

      amountLabel = new Label(String.valueOf((transaction.getAmount()))
          + "  " + transaction.getMessage());
      accountLabel.setLayoutX(10);
      accountLabel.setLayoutY(8);
      amountLabel.setLayoutX(20);
      amountLabel.setLayoutY(8);

      AnchorPane accountAnchorPane = new AnchorPane();
      accountAnchorPane.getChildren().add(accountLabel);

      AnchorPane amountAnchorPane = new AnchorPane();
      amountAnchorPane.getChildren().add(amountLabel);
      transactionTable.add(accountAnchorPane, 0, count);
      transactionTable.add(amountAnchorPane, 1, count);
      BorderStroke borderStroke = new BorderStroke(
          Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1));
      Border tableBorder = new Border(borderStroke);
      transactionTable.setBorder(tableBorder);

      count++;
    }
  }

  /**
   * Handles the switch from login page to signup page.
   *
   * @param event - Mouseclick event
   */

  @FXML
  public void handleSignUpClick(MouseEvent event) {
    labelGoTo(event, "Register", signUpButton);
  }

  /**
   * Handles mouseclick to log out.
   *
   * @param event - Mouseclick event
   */

  @FXML
  public void switchTabToLoginPage(MouseEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
      Parent root = loader.load();
      Scene scene = new Scene(root);
      Stage stage = (Stage) backArrow.getScene().getWindow();
      stage.setScene(scene);
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Handles mouseclick to log out.
   *
   * @param event  - Mouseclick event
   * @param source the name of the fxml (without ".fxml")
   * @param label  fx-id of the label that leads to source
   */
  private void labelGoTo(MouseEvent event, String source, Label label) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(source + ".fxml"));
      Parent root = loader.load();
      Scene scene = new Scene(root);
      Stage stage = (Stage) label.getScene().getWindow();
      stage.setScene(scene);
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * Handles mouse click of an AnchorPane to a specific fxml.
   *
   * @param event      - Mouseclick event
   * @param source     the name of the fxml (without ".fxml")
   * @param anchorPane the fx-id of the AnchorPane that leads to source
   */
  private void anchorPaneGoTo(MouseEvent event, String source, AnchorPane anchorPane) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(source + ".fxml"));
      Parent root = loader.load();
      Scene scene = new Scene(root);
      Stage stage = (Stage) anchorPane.getScene().getWindow();
      stage.setScene(scene);
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Handles mouse click of a button to a specific fxml.
   *
   * @param event  - Mouseclick event
   * @param source the name of the fxml (without ".fxml")
   * @param button the fx-id of the button that leads to source
   */
  private void buttonGoTo(MouseEvent event, String source, Button button) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(source + ".fxml"));
      Parent root = loader.load();
      Scene scene = new Scene(root);
      Stage stage = (Stage) button.getScene().getWindow();
      stage.setScene(scene);
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
      loginError.setText(e.getMessage());
    }
  }

  /**
   * Handles mouse click of a label that goes to a fxml that handles transactions.
   *
   * @param event - Mouseclick event
   */
  @FXML
  public void goToTransfer2(MouseEvent event) {
    labelGoTo(event, "Transfer", transferSavingsButton);
  }

  /**
   * Handles mouse click of a label that goes to a fxml that handles creation of
   * new account.
   *
   * @param event - Mouseclick event
   */
  @FXML
  public void goToAccount(MouseEvent event) {
    labelGoTo(event, "NewAccount", newSavingAccountButton);
  }

  /**
   * Handles mouse click that goes to a fxml that handles creation of new account.
   *
   * @param event - Mouseclick event
   */
  @FXML
  public void goToAccount2(MouseEvent event) {
    labelGoTo(event, "NewAccount", newAccountButton);
  }

  /**
   * Handles Pay button in Payments.
   *
   * @param event - Mouseclick event
   */
  @FXML
  public void goToPay(MouseEvent event) {
    anchorPaneGoTo(event, "Pay", goToPayButton);
  }

  /**
   * Handles transfer button in Payments.
   *
   * @param event - Mouseclick event
   */
  @FXML
  public void goToTransfer(MouseEvent event) {
    anchorPaneGoTo(event, "Transfer", goToTransferButton);
  }

  /**
   * Handles settings button in Profile.
   *
   * @param event - Mouseclick event
   */
  @FXML
  public void goToSettings(MouseEvent event) {
    anchorPaneGoTo(event, "Settings", settingsButton);
  }

  @FXML
  public void goToCards(MouseEvent event) {
    anchorPaneGoTo(event, "Cards", cardsButton);
  }

  /**
   * Handles the payment process when pay button is clicked, and saves the
   * information.
   *
   * @param event - Mouseclick event
   */
  @FXML
  public void handlePayment(MouseEvent event) {
    String payFrom = payFromChoiceBox.getValue();
    String accPersonToPay = payTo.getText();
    int amount = Integer.parseInt(payAmount.getText());
    SpendingsAccount acc1 = null; // account paying (this.profile)
    SpendingsAccount acc2 = null; // person payed to

    if (payFrom.isEmpty() || accPersonToPay.isEmpty() || payAmount.getText().isEmpty()) {
      feedbackInPay.setText("Please fill in the fields");
    } else {
      try {
        acc1 = profile.findSpendingsAccount(payFrom);
        acc2 = Logics.findOverallSpendingsAccount(accPersonToPay, profilesAccess.getProfiles());

        acc1.pay(acc2, amount);
        feedbackInPay.setText("Payment successful!");
        payFromChoiceBox.setValue("");
        payTo.clear();
        payAmount.clear();
        writeInfo();
        profilesAccess.updateProfilesInfo(acc2.getProfile());

        profilesAccess.writeTransactions(acc1, acc2);

      } catch (Exception e) {
        e.printStackTrace();
        feedbackInPay.setText("Something went wrong");
        feedbackInPay.setFill(Color.RED);
      }
    }
  }

  /**
   * Handles the tranfering process when transfer button is clicked, and saves the
   * new information.
   *
   * @param event - Mouseclick event
   */

  @FXML
  public void handleTransfer(MouseEvent event) {
    String fromAccountChoiceBox = transferFromChoiceBox.getValue();
    String toAccountChoiceBox = transferToChoiceBox.getValue();
    if (transferAmount.getText().isEmpty()) {
      feedbackInTransfer.setText("Fill in amount");
    }
    if (transferAmount.getText().isEmpty()) {
      feedbackInTransfer.setText("Fill in amount");
    }
    int amount = Integer.parseInt(transferAmount.getText());

    AbstractAccount acc1 = null;
    AbstractAccount acc2 = null;

    try {
      acc1 = profile.findAbstractAccountByAccNr(fromAccountChoiceBox);
      acc2 = profile.findAbstractAccountByAccNr(toAccountChoiceBox);

      acc2.transferFrom(acc1, amount);
      profilesAccess.writeTransactions(acc1, acc2);
      writeInfo();

      transferAmount.setText("");
      transferFromChoiceBox.setValue("");
      transferToChoiceBox.setValue("");
      feedbackInTransfer.setFill(Color.BLACK);
      feedbackInTransfer.setText("Transfer completed!");
    } catch (IllegalArgumentException e) {
      feedbackInTransfer.setText("Something went wrong");
      feedbackInTransfer.setFill(Color.RED);
    }
  }

  /**
   * Creates new account when a given mouse event happens.
   *
   * @param event - Mouseclick event
   */
  @FXML
  public void createNewAccount(MouseEvent event) {
    String type = selectAccountType.getValue();
    String name = giveAccountName.getText();
    int numAccounts = profile.getAccounts().size();
    AbstractAccount account = null;

    if (name.isEmpty()) {
      feedbackInNewAccount.setText("Fill the field");
      feedbackInNewAccount.setFill(Color.RED);
    } else {
      String[] validTypes = { "BSU", "Checking account", "Savings account" };

      if (type.equals(validTypes[0])) {
        account = new BsuAccount(name, profile);
      } else if (type.equals(validTypes[1])) {
        account = new SpendingsAccount(name, profile);
      } else if (type.equals(validTypes[2])) {
        account = new SavingsAccount(name, profile);
      }
      try {
        profile.addAccount(account);
      } catch (IllegalArgumentException e) {
        feedbackInNewAccount.setText(e.getMessage());
        feedbackInNewAccount.setFill(Color.RED);
      }

      if (numAccounts + 1 == profile.getAccounts().size()) {
        feedbackInNewAccount.setText("New account created!");
      }

      giveAccountName.setText("");
      writeInfo();
    }
  }

  /**
   * Opens a fxml where you can delete an account.
   *
   * @param event - Mouseclick event
   * @throws IOException - Exception if loading fails
   */
  @FXML
  public void handleDeleteAccountStage1(MouseEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("DeleteAccount.fxml"));
    AnchorPane deleteAccount = loader.load();
    Stage stage = new Stage();
    stage.setScene(new Scene(deleteAccount));
    stage.setTitle("Delete Account");
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.show();

  }

  /**
   * deletes the account with the written name in the text field.
   *
   * @param event - Mouseclick event
   */
  @FXML
  public void handleDeleteAccountStage2(MouseEvent event) {
    String accountToBeDeleted = deleteAccountName.getText();
    AbstractAccount acc = null;
    try {
      acc = profile.findAbstractAccountByName(accountToBeDeleted);
      profile.removeAccount(acc);
      writeInfo();
      Stage stage = (Stage) deleteAccount.getScene().getWindow();
      stage.close();
    } catch (Exception e) {
      feedbackInDeleteAccount.setText("Cannot find account");
      feedbackInDeleteAccount.setFill(Color.RED);
    }
  }

  /**
   * Saves new information.
   * 
   */
  private void writeInfo() {
    profilesAccess.updateProfilesInfo(profile);
  }

  /**
   * Handles mouseclick on the login-button.
   *
   * @param event - Mouseclick event
   */

  @FXML
  public void handleLoginButton(MouseEvent event) {
    try {
      String email = emailInput.getText();
      String password = passwordInput.getText();
      profile = Logics.checkProfile(profilesAccess.getProfile(email), password);

      buttonGoTo(event, "Overview", loginButton);
    } catch (Exception e) {
      e.printStackTrace();
      loginError.setText(e.getMessage());
    }
  }

  /**
   * Deletes this profile.
   *
   * @param event - Mouseclick event
   */
  @FXML
  public void handleDeleteProfile(MouseEvent event) {
    profilesAccess.deleteProfile(profile);
    labelGoTo(event, "Login", deleteProfileButton);
  }

  /**
   * handles the changes in profile and saves it.
   *
   * @param event - Mouseclick event
   */
  @FXML
  public void handleUpdateSettings(MouseEvent event) {
    String newNum = changeNumberTo.getText();
    String newPassword = changePasswordTo.getText();
    String newPassword2 = confirmChangePassword.getText();

    try {
      if (!newNum.isEmpty()) {
        profile.changeTlf(newNum);
      }
      if (!(newPassword.isEmpty()) && !(newPassword2.isEmpty())
          && newPassword.equals(newPassword2)) {
        profile.changePassword(newPassword2);
      }
    } catch (IllegalArgumentException e) {
      feedbackInSettings.setText(e.getMessage());
    }

    feedbackInSettings.setText("Update successfull!");
    writeInfo();
  }

  /**
   * Handles logic for registering new profile.
   * 
   */
  @FXML
  public void register() {
    if (password.getText().equals(passwordConfirm.getText())) {
      try {
        Logics.checkAlreadyRegistered(profilesAccess.getProfiles(),
            email.getText(), phoneNr.getText());
        profile = new Profile(fullName.getText(), email.getText(),
            phoneNr.getText(), password.getText());
        SpendingsAccount account = new SpendingsAccount("Spendings account", profile);
        account.add(100);
        profile.addAccount(account);
        writeInfo();

        Stage primaryStage = (Stage) registerButton.getScene().getWindow();

        primaryStage.setTitle("Bankapp - Overview");

        FXMLLoader tabLoader = new FXMLLoader(getClass().getResource("Overview.fxml"));
        Parent tabPane = tabLoader.load();
        Scene tabScene = new Scene(tabPane);

        primaryStage.setScene(tabScene);
        primaryStage.show();
      } catch (Exception e) {
        registerError.setText(e.getMessage());
      }
    } else {
      registerError.setText("Passwords do not match");
    }
  }
}
