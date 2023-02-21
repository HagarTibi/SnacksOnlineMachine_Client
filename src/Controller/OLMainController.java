package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import client.Client;
import client.ClientUI;
import common.MsgHandler;
import common.RemoteOrder;
import common.TypeMsg;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

// Screen in OL configuration - presented to the customer/subscriber 
// after he press the "new order" button. It handles the information about 
// which kind of of remote order does he wants: Delivery or Self Pickup

public class OLMainController implements Initializable{
	public static OLMainController oLMainController;

	@FXML
    private Button Backbt;

    @FXML
    private Button Xbt;
    
    @FXML
    private ComboBox<String> chooseAreaForDelivery;

    @FXML
    private ComboBox<String> chooseArea;

    @FXML
    private ComboBox<String> chooseMachine;

    @FXML
    private Button continueToOrder;

    @FXML
    private CheckBox deliveryOp;

    @FXML
    private Label lbrole;

    @FXML
    private Button logout;

    @FXML
    private CheckBox selfPickupOp;

    @FXML
    private Text txtrole;

    @FXML
    private Text welcometxt;

    @FXML
    private BorderPane workhere;
    
    @FXML
    private TextField userAddrInput;
    
    @FXML
    private Text enterYourAddLine;
    
    @FXML
    private Text errorMsg;
    
    public static ArrayList<String> areas_locations;
    public static String orderType = null;
    public static String machine_info;
    public static String delivery_addr;
    private String p_area;
    private String p_location;
    private String d_area;
	private String d_address;
	private String role_screen; // Customer --> "ClientScreen"  ,  Subscriber --> SubscriberScreen
    
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		role_screen = LoginController.userLogin.getRole().toString();
		Client.cart = new ArrayList<>();
		Client.order = new RemoteOrder();
		ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.Import_Machines_Locations, null));
		errorMsg.setVisible(false);
		userAddrInput.setVisible(false);
		enterYourAddLine.setVisible(false);
		chooseAreaForDelivery.setVisible(false);
		chooseArea.setVisible(false);
		chooseMachine.setVisible(false);
		ObservableList<String> list = FXCollections.observableArrayList("North","South","UAE");
		chooseArea.setItems(list);
		chooseAreaForDelivery.setItems(list);
		p_area = null;
	    p_location = null;
	    d_area = null;
		d_address = null;
	}
	
	/**
	 * Action event Self Pickup checkbox. 
	 * When Pressed, the user will see that he needs to fill out
	 * which area and machine (in that area) he wants his pickup order to be.
	 * Also it hides the delivery option details
	 *
	 * @param event action event of "Self Pickup" checkbox
	 */
	@FXML
	private void handleSelfPickUpBox(ActionEvent event) {
		errorMsg.setVisible(false);
		userAddrInput.setVisible(false);
		enterYourAddLine.setVisible(false);
		chooseAreaForDelivery.getSelectionModel().select("Select Area");
		chooseAreaForDelivery.setVisible(false);
		chooseArea.setVisible(true);
		chooseMachine.setVisible(true);
		if (selfPickupOp.isSelected()) {
			deliveryOp.setSelected(false);
		}
	}
	
	/**
	 * Action event for combo box of areas and machines. 
	 * When area is selected, the combobox of machines is loaded
	 * according to the selected area.
	 *
	 * @param event action event of "Self Pickup" checkbox
	 */
	@FXML
	private void handleAreaSelection(ActionEvent event) {
		ObservableList<String> locations = FXCollections.observableArrayList();
		locations.clear();
		p_area = chooseArea.getValue();
		errorMsg.setVisible(false);
		for (int i=0; i<areas_locations.size(); i+=3) {
			if ((areas_locations.get(i)).equals(p_area)) {
				String machine = areas_locations.get(i+1)+" "+areas_locations.get(i+2);
				locations.add(machine);
			}
		}
		chooseMachine.setItems(locations);
	}
	
	/**
	 * Initiate machines info that arrived from the server
	 *
	 * @param areasAndMachines strings of the machines info: area, location and machine id
	 */
	public static void initiate_machines_in_area (ArrayList<Object> areasAndMachines) {
		areas_locations = new ArrayList<>();
		for (int i=0; i<areasAndMachines.size(); i++) {
			areas_locations.add((String)areasAndMachines.get(i));
		}
	}
	
	/**
	 * Action event for when choosing a machine from combobox
	 *
	 * @param event ActionEvent for choosing machine
	 */
	@FXML
	private void handleLocationSelection(ActionEvent event) {
		p_location = chooseMachine.getValue();
		errorMsg.setVisible(false);
	}
	
	// -------------- pickup ends, delivery begins --------------
	
	/**
	 * Action event Delivery checkbox. 
	 * When Pressed, the user will see that he needs to fill out
	 * area and delivery address for his order.
	 * Also it hides the self pickup option details.
	 *
	 * @param event action event of "Delivery" checkbox
	 */
	@FXML
	private void handleDeliveryBox(ActionEvent event) {
		errorMsg.setVisible(false);
		userAddrInput.setVisible(true);
		enterYourAddLine.setVisible(true);
		chooseAreaForDelivery.setVisible(true);
		chooseArea.getSelectionModel().select("Select Area");
		chooseMachine.getSelectionModel().select("Select Machine");
		chooseArea.setVisible(false);
		chooseMachine.setVisible(false);
		if (deliveryOp.isSelected()) {
			selfPickupOp.setSelected(false);
		}
	}
	
	/**
	 * Action event for combo box of areas and machines. 
	 * When area is selected, the combobox of machines is loaded
	 * according to the selected area.
	 *
	 * @param event action event of "Delivery" checkbox
	 */
	@FXML
	private void handleAreaSelectionDelivery(ActionEvent event) {
		d_area = chooseAreaForDelivery.getValue();
		errorMsg.setVisible(false);
	}
	
	/**
     * This method is called when the user clicks the "Continue to Order" button.
     * It checks if the user has entered a delivery address and if the delivery or self-pickup button is selected, 
     * along with the relevant area and location/address fields being filled out.
     * If all the conditions are met, it sets the order type, 
     * machine information, and delivery address, then proceeds to the catalog screen.
     * If any of the conditions are not met, it shows an error message to the user.
     *
     * @param event the action event of clicking the "Continue to Order" button
     */
	@FXML
	private void continueToOrderBtn(ActionEvent event) {
		if (!userAddrInput.getText().isEmpty()) {
		    d_address = userAddrInput.getText();
		}
		if (deliveryOp.isSelected() && d_area != null && d_area != "Select Area" && d_address != null) {
			orderType = "REMOTE_DELIVERY";
		    machine_info = d_area;
		    delivery_addr = d_address;
		    goCatalogScreen();
		}
		else if (selfPickupOp.isSelected() && p_area != null && p_area != "Select Area" && p_location != null && p_location != "Select Machine") { 
			orderType = "REMOTE_PICKUP";
		    machine_info = p_area+" "+p_location; // "Area Location machine_id" 
		    delivery_addr = null;
		    goCatalogScreen();
		}
		else { 
			errorMsg.setVisible(true);
		}
	}
	
	/**
	 * method to load the Order Catalog screen for
	 * making the order
	 */
	private void goCatalogScreen() {
		AnchorPane pane;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/OrderCatalogScreen.fxml"));
			pane = loader.load();
			OrderCatalogScreenController.orderCatalogScreenController = loader.getController();
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Scene scene = new Scene(pane);
		ClientUI.getStage().setScene(scene);
		ClientUI.getStage().show();
	}
	
	/**
     * This is the 'GoBackToHomeScreen' method.
     * When the button is clicked, the user returns customer/subscriber home screen.
     * @param actionEvent The `ActionEvent` object generated when the "Back" button is clicked.
     */
	@FXML
	public void GoBackToHomeScreen(ActionEvent event) {
		AnchorPane pane1;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/"+role_screen+".fxml"));
			pane1 = loader.load();
			if (role_screen == "ClientScreen")
				ClientScreenController.controller = loader.getController();
			else
				SubscriberScreenController.controller = loader.getController();
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Scene scene1 = new Scene(pane1);
		ClientUI.getStage().setScene(scene1);
		ClientUI.getStage().show();
	}
	
	/**
     * Event handler for the disconnect button.It sends a disconnect request to the server.
     *
     * @param event the action event that triggers this handler
     */
	@FXML
    void disconnectClient(ActionEvent event) throws Exception {
		ArrayList<Object> details = new ArrayList<>();
        details.add(LoginController.userLogin.getUser_id());
        MsgHandler disconnectToServer = new MsgHandler(TypeMsg.Request_disconnected, details);
        ClientUI.chat.accept((Object) disconnectToServer);
    }
	
	/**
	 * Action event to return to Login screen
	 * @param event action event of "Logout" button
	 */
	@FXML
    public void LogOutClicked(ActionEvent actionEvent) {
		if (role_screen == "SubscriberScreen")
			if (SubscriberScreenController.scheduledFuture != null)
				SubscriberScreenController.scheduledFuture.cancel(true);
		else 
			if (ClientScreenController.scheduledFuture!=null)
				ClientScreenController.scheduledFuture.cancel(true);
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
	
}
