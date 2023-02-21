package Controller;

import common.AlertStatus;
import common.ItemInCatalog;
import common.ItemInOrder;
import common.ItemsAlert;
import common.MsgHandler;
import common.RemoteOrder;
import common.Subscribe;
import common.TypeMsg;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import client.Client;
import client.ClientUI;

// ConfirmOrderController is responsible for the Confirm Order Screen in the end of the order process.

public class ConfirmOrderController implements Initializable {
    public static ConfirmOrderController confirmOrderController;

    @FXML
    private Button Backbt;

    @FXML
    private Button Xbt;

    @FXML
    private Text address_input;

    @FXML
    private TextField cardCVV;

    @FXML
    private TextField cardExpDate;

    @FXML
    private Button confirmBtn;

    @FXML
    private TextField customerCreditCardNumber;

    @FXML
    private TextField customerID;

    @FXML
    private TextFlow deliveyAddrLabel;

    @FXML
    private Label lbrole;

    @FXML
    private Button logout;

    @FXML
    private TextFlow machineLabel;

    @FXML
    private Text machine_input;
    
    @FXML
    private TableColumn<ItemInOrder,String> items_Col;

    @FXML
    private TableColumn<ItemInOrder,String> pricePerUnit_Col;
    
    @FXML
    private TableColumn<ItemInOrder,String> amount_Col;

    @FXML
    private HBox role;

    @FXML
    private TableView<ItemInOrder> tableView;

    @FXML
    private Text total_price_input;

    @FXML
    private Text txtrole;

    @FXML
    private Text welcometxt;
    
    @FXML
    private Label deliv_addr_title;
    
    @FXML
    private Label machine_id_title;
    
    @FXML
    private TextFlow pathEK;

    @FXML
    private TextFlow pathOL;
    
    @FXML
    private Button cancelOrderBtn;
    
    @FXML
    private Text EK_cust_sub;

    @FXML
    private Text OL_cust_sub;
    
    ArrayList<ItemInCatalog> orderCart;
    public static String ClientID;
    public static String clientCreditNumber; 
    public static String clientCreditCVV; 
    public static String clientCreditExp;
    public static String items_under_alert;
    private String machine_id_for_DB;
    private String machine_area;
    private String role_screen; // Customer --> "ClientScreen"  ,  Subscriber --> SubscriberScreen

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	role_screen = LoginController.userLogin.getRole().toString();
    	// switch case for "EK" or "OL"
    	switch (Client.configuration.get(0).toString()) {
    	case "EK":
    		pathEK.setVisible(true);
			pathOL.setVisible(false);
			if (role_screen == "SubscriberScreen")
				EK_cust_sub.setText("Subscriber Menu>");
    		machine_id_for_DB = Client.configuration.get(2).toString();	// from config file
    		machine_area = Client.configuration.get(1).toString();		// from config file
    		machine_input.setText(machine_id_for_DB);
    		deliv_addr_title.setVisible(false);
    		address_input.setVisible(false);
    		break;
    	case "OL": // Client.order is instance of RemoteOrder
    		pathEK.setVisible(false);
			pathOL.setVisible(true);
			if (role_screen == "SubscriberScreen")
				OL_cust_sub.setText("Subscriber Menu>");
    		switch (OLMainController.orderType) {
    		case "REMOTE_DELIVERY":
    			machine_area = OLMainController.machine_info; 			// machine_info is only: "Area"
    			machine_id_for_DB = ""; 								// this is delivery so there is no machine_id and no location
    			address_input.setText(OLMainController.delivery_addr);
    			machine_id_title.setVisible(false);
    			machine_input.setVisible(false);
    			break;
    		case "REMOTE_PICKUP":
    			String strToParse = OLMainController.machine_info; 		// machine_info is: "Area Location machine_id"
    			String[] areaLocationID = strToParse.split(" ");
    			machine_area = areaLocationID[0];						// need String #1: "Area"
    			machine_id_for_DB = areaLocationID[2]; 					// need String #3: "machine_id" 
    			machine_input.setText(OLMainController.machine_info);
    			deliv_addr_title.setVisible(false);
        		address_input.setVisible(false);
    			break;
    		default:
    			break;	
    		}
    		break;
    	default:
			break;
    	}
    	
        // set up the columns in the table
    	items_Col.setCellValueFactory(new PropertyValueFactory<ItemInOrder,String>("item_name"));
        amount_Col.setCellValueFactory(new PropertyValueFactory<ItemInOrder,String>("item_amount"));
        pricePerUnit_Col.setCellValueFactory(new PropertyValueFactory<ItemInOrder,String>("item_price"));
        
        // load the data from the cart from the Order screen
        tableView.setItems(getOrderItmes());
        float result = Client.order.getFinal_price()*OrderCatalogScreenController.discount;
        DecimalFormat df = new DecimalFormat("#.##");
        Client.order.setFinal_price(Float.parseFloat(df.format(result)));
        total_price_input.setText(String.format("%.2f", Client.order.getFinal_price())+" NIS");
        
        setUpScreenInfo();
		while (ClientID.equals(null)  || clientCreditNumber.equals(null) || clientCreditExp.equals(null) || clientCreditCVV.equals(null)) {};
		customerID.setText(ClientID);
		customerCreditCardNumber.setText(clientCreditNumber);
		cardExpDate.setText(clientCreditExp);
		cardCVV.setText(clientCreditCVV);
    }
    
    /** 
     * The method first retrieves a `cart` object from an instance of the `Client` class.
     * It then creates an empty `ObservableList` object called `itemsToShowInTable` that will be used to store the `ItemInOrder` objects.
     * The method then iterates through the items in the `cart` object, creates an `ItemInOrder` object for each item,
     * and adds it to the `itemsToShowInTable` list. The `ItemInOrder` object is constructed using the name, amount, and price
     * of the item in the `cart`. The price is formatted as a string with two decimal places before being passed as an argument
     * to the `ItemInOrder` constructor.
     * Finally, the method returns the `itemsToShowInTable` list.
     * @return An `ObservableList` object containing `ItemInOrder` objects.
     */
    public ObservableList<ItemInOrder> getOrderItmes() {
    	orderCart = Client.cart;
        ObservableList<ItemInOrder> itemsToShowInTable = FXCollections.observableArrayList();
        for (int i=0; i<orderCart.size(); i++ ) {
        	String itemPrice = String.format("%.2f",orderCart.get(i).getItem_price());
        	itemsToShowInTable.add(new ItemInOrder(orderCart.get(i).getItem_name(),orderCart.get(i).getAmount_in_cart(),Float.parseFloat(itemPrice)));
        }
        return itemsToShowInTable;
    }
    
    /**
	 * This method takes the customer's id (that logged in) and 
	 * performs a query to get his credit card info.
	 * then, we can present this info in the confirm order screen
	 * @return 	 */
    public void setUpScreenInfo() {
		ArrayList<Object> data_for_server = new ArrayList<Object>();
		data_for_server.add(LoginController.userLogin.getUser_id());
		MsgHandler getClientCardInfoMessage = new MsgHandler(TypeMsg.Import_User_Credit_Data,data_for_server);
		ClientUI.chat.accept((Object) getClientCardInfoMessage);
	}
    
    /**
	 * This method is called in the client class and
	 * receives the credit card info that comes back from the DB
	 * @return 	 */
    public static void initiate_customer_credit_card(ArrayList<Object> customer_card_credentials) { 
		ClientID = (String) customer_card_credentials.get(0);
		clientCreditNumber = (String)customer_card_credentials.get(1);
		clientCreditExp = ((String)customer_card_credentials.get(2));
		clientCreditCVV = ((String)customer_card_credentials.get(3));
		
		//customerCard.set expiration date --> parse a new string and add it to the text fiel	
	}
    
    
    /**
     * The method receives an `ArrayList` object called `orderCode` as a parameter. This `ArrayList` is expected to contain
     * a single string value, which represents the new order code that was generated by a method that inserted a new order
     * into the database.
     * @param orderCode An `ArrayList` object containing a single string value representing the new order code.
     */
	public static void takeNewOrderCode(ArrayList<Object> orderCode) {
		Client.order.setOrder_code((String)orderCode.get(0));
	}
    
	
	/**
	 * The method receives an `ArrayList` object called `itemsAlertFromDB` as a parameter. This `ArrayList` is expected to
	 * contain a list of strings, each representing the name of an item that has a quantity lower than the threshold level
	 * in the database.
	 * The method creates a new `ArrayList` object called `items_under_alert` and populates it with the names of the items
	 * from the `itemsAlertFromDB` list.
	 * @param itemsAlertFromDB An `ArrayList` object containing a list of strings representing the names of items under alert.
	 */
    public static void initiate_items_under_alert (ArrayList<Object> itemsAlertFromDB) {
    	items_under_alert = "";
    	if (!itemsAlertFromDB.isEmpty()) {
    		for (int i=0; i<itemsAlertFromDB.size(); i+=2) {
    			items_under_alert += (String)itemsAlertFromDB.get(i) +","+(String)itemsAlertFromDB.get(i+1);
    			if (i+1 != itemsAlertFromDB.size()-1)
    				items_under_alert += ",";
    		}
    	}
    }
    
    /**
     * This is the `CancelOrder` method.
     * When the button is clicked, the method displays a confirmation alert to the user, asking if they are sure they want
     * to cancel the order. If the user clicks the "Yes" button, the method clears the `cart` and `order` objects from the
     * `Client` class and navigates back to the "Home" screen. If the user clicks the "No" button, the method does nothing.
     * @param actionEvent The `ActionEvent` object generated when the "Cancel Order" button is clicked.
     */
    @FXML
    void CancelOrder (ActionEvent actionEvent) {
    	// turn off the timer
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Order");
        alert.setHeaderText("Are you sure you want to cancel the order?");
        alert.setContentText("This action cannot be undone.");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == yesButton) {
          	// go back to home screen
        	Client.cart = null;
    		Client.order = null;
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
        } else if (result.get() == noButton) {
        	// do nothing
        }
    }
    
    /**
     * This is the 'goBackToCatalog' method.
     * When the button is clicked, the user returns to the making of the order screen.
     * @param actionEvent The `ActionEvent` object generated when the "Back" button is clicked.
     */
    @FXML
    void goBackToCatalog(ActionEvent event) {
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
     * This is the 'clickOnConfirm' method.
     * When the button is clicked, the user moves to the next screen that summarize his order.
     * It also takes care of the data base: update of the machine inventory, saving of the order,
     * initiate new alert on items below the threshold (if needed) and update subscriber first order discount.
     * @param actionEvent The `ActionEvent` object generated when the "Confirm" button is clicked.
     */
	@FXML
	public void clickOnConfirm(ActionEvent event) {
		Client.order.setCustomer_id(ClientID); // set the customer id
		Client.order.setOrder_confirmed_date(giveMeCurrentTimeFormat(true)); // set the date of the order
		Client.order.setItems_in_order(orderCart);	// set the items in the order
		Client.order.setMachine_id(machine_id_for_DB);
		Client.order.setMachine_area(machine_area);
		ArrayList<Object> msg = new ArrayList<>();
		switch (Client.configuration.get(0).toString()) {
    	case "EK": // the local order is ready
    		msg.add(Client.order);
    		break;
    	case "OL":
    		// estimated_time and received_time will be null;
    		((RemoteOrder)Client.order).setOrder_type(OLMainController.orderType);
    		((RemoteOrder)Client.order).setStatus("ACTIVE");
    		((RemoteOrder)Client.order).setDelivery_address(OLMainController.delivery_addr);
    		msg.add((RemoteOrder)Client.order);
    		break;
    	default:
			break;
		}
		
		// update local machine if its local order or pickup order and return items under alert
		if (machine_id_for_DB != "")
			ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.Update_Machine_Data,msg));
		
		//add order to data base - to local orders also it creates new order code
		ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.Save_Order_To_DB,msg));
		
		if((machine_id_for_DB != "") && (items_under_alert != "")) { 
			// if it is not a delivery order and also there were items in alert
			// we build a new ItemsAlert object and send it to server
			ItemsAlert alert = new ItemsAlert(null, machine_id_for_DB, 
					giveMeCurrentTimeFormat(false),AlertStatus.ACTIVE,items_under_alert,null,null);
			msg.clear();
			msg.add(alert);
			ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.Add_alert_to_DB,msg));
		}
		
		// if user is subscriber and didn't make first order --> update DB
		if (role_screen == "SubscriberScreen" && !SubscriberScreenController.made_first_order) {
			msg.clear();
			msg.add(LoginController.userLogin.getUser_id());
			ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.Update_sub_first_order,msg));
		}
		
		AnchorPane pane;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/OrderConfirmPopUp.fxml"));
			pane = loader.load();
			OrderConfirmPopUpController.OrderConfirmController=loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Scene scene = new Scene(pane);
		ClientUI.getStage().setScene(scene);
		ClientUI.getStage().show();
	}
	
	/**
	 * Returns the current date and time as a string in the format "yyyy-MM-dd HH:mm".
	 *
	 * @param full_time Indicates whether to include the full time or just the hour and minute.
	 * @return The current date and time as a string.
	 */	
	public String giveMeCurrentTimeFormat(boolean full_time) {
	    ZoneId zone = ZoneId.of("Asia/Jerusalem");
	    ZonedDateTime now = ZonedDateTime.now(zone);
	    String fullTime = now.toString();
	    // Split the string by the 'T' character
	    String[] parts = fullTime.split("T");

	    // Get the first part (the date)
	    String firstPart = parts[0];
	    if (!full_time)
	        return firstPart;

	    // Get the second part (the time) and leave only the HH:mm part
	    String secondPart = parts[1].substring(0, 5);

	    // Concatenate the first and second parts with a space in between
	    String result = firstPart + " " + secondPart;
	    return result;
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
