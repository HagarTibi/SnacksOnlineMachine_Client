package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import client.ClientUI;
import common.MsgHandler;
import common.Sale;
import common.TypeMsg;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class GenerateNewSaleController implements Initializable {
	public static GenerateNewSaleController newSale;

	@FXML
	private Button Backbt;

	@FXML
	private Button Xbt;

	@FXML
	private Button cancelBt;

	@FXML
	private ComboBox<String> discountCombo;

	@FXML
	private ComboBox<String> hourFromCombo;

	@FXML
	private ComboBox<String> hourToCombo;

	@FXML
	private Button logout;

	@FXML
	private Button newSaleBt;

	@FXML
	private TextField saleNameField;
	@FXML
	private TextField saleDescription;

	@FXML
	private Text welcometxt;

	private Sale newsale;

	private ObservableList<String> hours;
	private ObservableList<String> percentage;
	private String name, description, discount, imgUrl, hoursActive;

	/**
	 * This Method disconnect client by X button
	 * 
	 * @author G-10
	 */
	@FXML
	void disconnectClient(ActionEvent event) throws Exception {
		ArrayList<Object> details = new ArrayList<>();
		details.add(LoginController.userLogin.getUser_id());
		MsgHandler disconnectToServer = new MsgHandler(TypeMsg.Request_disconnected, details);
		ClientUI.chat.accept((Object) disconnectToServer);
	}

	/**
	 * This Method logout the client and back to login screen
	 * 
	 * @author G-10
	 */
	@FXML
	public void logoutBtn(ActionEvent actionEvent) {
		ArrayList<Object> details = new ArrayList<>();
		details.add(LoginController.userLogin.getUser_id());
		MsgHandler logoutCustomer = new MsgHandler(TypeMsg.Request_logout, details);
		ClientUI.chat.accept((Object) logoutCustomer);
		AnchorPane pane;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/Login.fxml"));
			pane = loader.load();
			LoginController.controller = loader.getController();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Scene scene = new Scene(pane);
		Platform.runLater(() -> {
			ClientUI.getStage().setScene(scene);
			ClientUI.getStage().show();
		});
	}

	/**
	 * 
	 * This method is called when the user clicks the "Back" button. It navigates
	 * the user back to the Marketing Manager Home screen.
	 * 
	 * @param event the action event that triggers this method call
	 */
	@FXML
	void onClickBack(ActionEvent event) {
		AnchorPane pane;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/MarketingHomeScreen.fxml"));
			pane = loader.load();
			MarketingManagerHomeController.marketingScreenController = loader.getController();
		}

		catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Scene scene = new Scene(pane);
		ClientUI.getStage().setScene(scene);
		// ClientUI.getStage().set
		ClientUI.getStage().show();
	}

	/**
	 * Initializes the items for the hourFromCombo, hourToCombo, and discountCombo
	 * combo boxes. The hourFromCombo and hourToCombo combo boxes are initialized
	 * with a list of strings in the format 'HH:00', where HH is a number from 00 to
	 * 23. The discountCombo combo box is initialized with a list of strings in the
	 * format 'NN0%', where NN is a number from 10 to 100.
	 */
	@FXML
	public void initiateComboBox() {
		ArrayList<String> hoursto = new ArrayList<>();
		for (int i = 0; i < 24; i++) {
			if (i < 10)
				hoursto.add(0 + String.valueOf(i) + ":00");
			else
				hoursto.add(String.valueOf(i) + ":00");
		}
		hours = FXCollections.observableArrayList(hoursto);

		ArrayList<String> percentageto = new ArrayList();
		for (int i = 1; i < 11; i++) {
			percentageto.add(String.valueOf(i) + "0%");
		}
		percentage = FXCollections.observableArrayList(percentageto);

		// initiate comboBoxes
		hourFromCombo.setItems(hours);
		hourToCombo.setItems(hours);
		discountCombo.setItems(percentage);

	}

	/**
	 * 
	 * This method is called when the "Create Sale" button is clicked. It creates a
	 * new sale with the specified name, discount, description, hours of activity,
	 * and image URL. If any of the fields are empty or improperly formatted, an
	 * error alert is displayed and the sale is not created. If the hours of
	 * activity are invalid (end time is before start time), an error alert is
	 * displayed and the sale is not created. If the sale is successfully created,
	 * the user is redirected to a Successful Sales Adding Alert screen.
	 * 
	 * @param event the event triggered by clicking the "Create Sale" button
	 */
	@FXML
	public void onClickCreateSale(ActionEvent event) {

		if (LegalString(saleNameField.getText()) && LegalString(saleDescription.getText())
				&& discountCombo.getValue() != null && hourFromCombo.getValue() != null
				&& hourToCombo.getValue() != null && (hoursCompare())) {
			name = saleNameField.getText();
			discount = discountCombo.getValue();
			String discountafterremovepresentchar = discount.substring(0, discount.length() - 1);
			description = saleDescription.getText();
			hoursActive = hourFromCombo.getValue() + "-" + hourToCombo.getValue();
			HashMap<String, String> areas = new HashMap<String, String>();
			areas.put("North", "0");
			areas.put("South", "0");
			areas.put("UAE", "0");
			newsale = new Sale(null, name, discountafterremovepresentchar, description, hoursActive, areas);
			ArrayList<Sale> arrsale = new ArrayList<>();
			arrsale.add(newsale);
			ClientUI.chat.accept((Object) new MsgHandler<Sale>(TypeMsg.Create_New_Sale, arrsale));// create in DB NEW
																									// SALE and return
																									// alert to
																									// marketing manager
																									// of successful
			AnchorPane pane;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/gui/SuccessfulSalesAddingAlert.fxml"));
				pane = loader.load();
				SuccessfulSalesAddingAlertController.successAlert = loader.getController();
			}

			catch (IOException e) {
				e.printStackTrace();
				return;
			}
			Scene scene = new Scene(pane);
			ClientUI.getStage().setScene(scene);
			// ClientUI.getStage().set
			ClientUI.getStage().show();
		} else {
			if (!LegalString(saleNameField.getText())) {
				Alert alertName = new Alert(AlertType.ERROR);
				alertName.setTitle("Sale Name field empty");
				alertName.setContentText("Please fill Sale name field currectly(Only letter or (%,-, )");

				alertName.showAndWait();
				return;
			}
			if (!LegalString(saleDescription.getText())) {
				Alert alertDes = new Alert(AlertType.ERROR);
				alertDes.setTitle("Description field empty");
				alertDes.setContentText("Please fill Description field currectly(Only letter or (%,-, )");

				alertDes.showAndWait();
				return;
			}
			if (discountCombo.getValue() == null) {
				Alert alertDiscount = new Alert(AlertType.ERROR);
				alertDiscount.setTitle("Discount field empty");
				alertDiscount.setContentText("Please fill Discount field");
				alertDiscount.showAndWait();
				return;

			}

			if (hourFromCombo.getValue() == null) {
				Alert alertHourFrom = new Alert(AlertType.ERROR);
				alertHourFrom.setTitle("Hours Active (From) field is not filled correctly");
				alertHourFrom.setContentText("Please fill up the hours active fields correctly");

				alertHourFrom.showAndWait();
				return;
			}
			if (hourToCombo.getValue() == null) {
				Alert alertHourTo = new Alert(AlertType.ERROR);
				alertHourTo.setTitle("Hours Active (To) field is not filled correctly");
				alertHourTo.setContentText("Please fill up the hours active fields correctly");

				alertHourTo.showAndWait();
				return;
			}

			// if the hour from bigger or equal to hour to
			if (!hoursCompare()) {
				Alert alertHourTo = new Alert(AlertType.ERROR);
				alertHourTo.setTitle("Hours from equal or bigger from hour from");
				alertHourTo.setContentText("Please fill up the hours active fields correctly");

				alertHourTo.showAndWait();
				return;
			}
		}
	}

	/**
	 * Compares the hours selected in the hourFromCombo and hourToCombo combo boxes.
	 * 
	 * @return true if the hour selected in the hourToCombo combo box is greater
	 *         than the hour selected in the hourFromCombo combo box, false
	 *         otherwise.
	 */

	public boolean hoursCompare() {
		String from = hourFromCombo.getValue();
		String to = hourToCombo.getValue();
		StringBuilder fromSb = new StringBuilder();
		StringBuilder toSb = new StringBuilder();
		for (int i = 0; i < 2; i++) {
			fromSb.insert(i, from.charAt(i));
			toSb.insert(i, to.charAt(i));
		}
		if (Double.parseDouble(toSb.toString()) > Double.parseDouble(fromSb.toString())) {
			return true;
		}
		return false;

	}

	/**
	 * Clears all input fields and sets the values in the discountCombo,
	 * hourFromCombo, and hourToCombo combo boxes to null. Displays a confirmation
	 * alert.
	 * 
	 * @param event the event that triggers this method, a button click
	 */

	@FXML
	public void onClickDiscard(ActionEvent event) {

		discountCombo.setValue(null);
		hourFromCombo.setValue(null);
		hourToCombo.setValue(null);
		saleNameField.clear();
		saleDescription.clear();
		Alert alertDiscard = new Alert(AlertType.CONFIRMATION);
		alertDiscard.setTitle("Sale discard");
		alertDiscard.setContentText("Sale discard");
		

		ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.OK_DONE);
		alertDiscard.getButtonTypes().setAll(buttonTypeOk);

		Optional<ButtonType> result = alertDiscard.showAndWait();
		if (result.get() == buttonTypeOk) {
			// close the dialog
			alertDiscard.close();
		}
		return;

	}

	/**
	 * Checks if a string contains only valid characters.
	 * 
	 * @param str the string to check
	 * @return true if the string contains only letters, numbers, and the following
	 *         characters: %:!,. -, false otherwise.
	 */

	public static boolean LegalString(String str) {
		// Return false if the string
		// has empty or null
		if (str.equals("") || str.equals(null)) {
			return false;
		}
		ArrayList<Character> alphabets = new ArrayList<Character>();
		String alpha = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ%:!,. -0123456789";
		for (int i = 0; i < alpha.length(); i++) {
			alphabets.add(alpha.charAt(i));
		}
		// Traverse the string from
		// start to end
		for (int i = 0; i < str.length(); i++) {
			// Check if the specified
			// character is not a letter then
			// return false,
			// else return true
			if (!alphabets.contains(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initiateComboBox();

	}

}