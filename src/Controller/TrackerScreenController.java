package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
import client.ClientUI;
import common.PresentDeliveryOrder;

import common.MsgHandler;
import common.PickUpOrder;
import common.TypeMsg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


public class TrackerScreenController implements Initializable{
	public static TrackerScreenController trackerController;
    
	@FXML
    private Text cusORsub;
	
	@FXML
    private Button Backbt;

    @FXML
    private Button Xbt;

    @FXML
    private Button logout;

    @FXML
    private Text welcometxt;
    
    @FXML
    private TableView<PickUpOrder> pickUpTable;
    
    @FXML
    private TableColumn<PickUpOrder,String> orderNumPick;

    @FXML
    private TableColumn<PickUpOrder,String> confirmDatePick;
    
    @FXML
    private TableColumn<PickUpOrder,String> machineIdPick;
    
    @FXML
    private TableView<PresentDeliveryOrder> deliveryTable;
    
    @FXML
    private TableColumn<PresentDeliveryOrder,String> orderNumDelivery;

    @FXML
    private TableColumn<PresentDeliveryOrder,String> confirmDateDelivery;
    
    @FXML
    private TableColumn<PresentDeliveryOrder,String> estimatedDelivery;

    @FXML
    private TableColumn<PresentDeliveryOrder,String> AddressDelivery;
    
   
    @FXML
    private TableColumn<PresentDeliveryOrder,String> statusDelivery;
    
    @FXML
    private ComboBox<String> deliveryNums;
    
    @FXML
    private Button GotItBt;
    
    @FXML
    private Text errorMsg;
    
    private String role_screen; // Customer --> "ClientScreen"  ,  Subscriber --> SubscriberScreen
    private ObservableList<String> deliveryNum=FXCollections.observableArrayList();
    ArrayList<PickUpOrder> pickUpsTable;
    ArrayList<PresentDeliveryOrder> deliveriesTable;
    ArrayList<Object> toServer=new ArrayList<>();

    
    /**
    Returns an {  ObservableList} of {  PickUpOrder} objects that will be displayed in a table.
    The list is obtained by iterating through the {@code pickUpsTable} list, which is a static variable from the {  Client} class, and adding each element to the {@code ordersShowInTable} list.
    @return An {  ObservableList} of {  PickUpOrder} objects that will be displayed in a table
    */
    public ObservableList<PickUpOrder> getPickUpsOrder() {
    	pickUpsTable = Client.pickUps;
        ObservableList<PickUpOrder> ordersShowInTable = FXCollections.observableArrayList();
        for (int i=0; i<pickUpsTable.size(); i++ ) {
        	ordersShowInTable.add(pickUpsTable.get(i));
        }
        return ordersShowInTable;
    }
    
    /**
    Returns an {  ObservableList} of {  PresentDeliveryOrder} objects that will be displayed in a table.
    The list is obtained by iterating through the {@code deliveriesTable} list, which is a static variable from the {  Client} class, and adding each element to the {@code ordersShowInTable} list.
    @return An {  ObservableList} of {  PresentDeliveryOrder} objects that will be displayed in a table
    */
    public ObservableList<PresentDeliveryOrder> getPresentDeliveryOrder() {
    	deliveriesTable = Client.delivery;
        ObservableList<PresentDeliveryOrder> ordersShowInTable = FXCollections.observableArrayList();
        for (int i=0; i<deliveriesTable.size(); i++ ) {
        	ordersShowInTable.add(deliveriesTable.get(i));
        }
        return ordersShowInTable;
    }

    /**
    Handles the event of the "Back" button being clicked.
    Clears the static lists {  Client#pickUps} and {  Client#delivery} and loads the {@code ClientScreen} scene.
    The {@code ClientScreenController} object is also retrieved so that it can be used to communicate between the controllers.
    @param event The event object associated with the button click
    */
	@FXML
	public void onClickBack(ActionEvent event) {
		Client.pickUps.clear();
		Client.delivery.clear();
		AnchorPane pane;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/"+role_screen+".fxml"));
			pane = loader.load();
			if (role_screen == "ClientScreen") 
				ClientScreenController.controller = loader.getController();
			else
				SubscriberScreenController.controller = loader.getController();
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
	Handles the event of the "Disconnect" button being clicked.
	Create a {  MsgHandler} object with {  TypeMsg#Request_disconnected} as the message type,
	and sends it to the server by calling {  ClientUI#chat} accept method.
	Then, it close the application by calling System.exit(0).
	@param event The event object associated with the button click
	@throws Exception in case of any failure in the process
	*/
	@FXML
    void disconnectClient(ActionEvent event) throws Exception {
		ArrayList<Object> details = new ArrayList<>();
        details.add(LoginController.userLogin.getUser_id());
        MsgHandler disconnectToServer = new MsgHandler(TypeMsg.Request_disconnected, details);
        ClientUI.chat.accept((Object) disconnectToServer);
    }
	
	/**
	Handles the event of the "Logout" button being clicked.
	It send a {  MsgHandler} object with {  TypeMsg#Request_disconnected} as the message type to the server by calling
	{  ClientUI#chat} accept method, in order to disconnect client from server.
	Then, it loads the {@code Login} scene and set the new scene to the stage, Also it create a new {  LoginController} object
	and assign it to the {  LoginController#controller}
	@param event The event object associated with the button click
	*/
	@FXML
	void logoutButton (ActionEvent event){
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
	Handles the event of the "Got It" button being clicked.
	It gets the selected order code from {@code deliveryNums} combobox and send a {  MsgHandler} object with
	{TypeMsg#Update_order_Status} as the message type and the order code as the data, to the server by calling
	{ClientUI#chat} accept method.
	Then it shows an alert message to confirm that the order status is changed.
	After that, it clears the static lists {Client#pickUps} and { Client#delivery} and sends request to import the new orders
	and loads the {@code TrackerScreen} scene.
	@param event The event object associated with the button click
	*/
	@FXML
	public void onClickGotIt(ActionEvent event) {
		if (deliveryNums.getValue() == null) {
			errorMsg.setVisible(true);
			return;
		}
		String order_code=deliveryNums.getValue();
		toServer.add(order_code);
		String timeNow=giveMeCurrentTimeFormat(true);
	
		toServer.add(timeNow);
		try {
			ClientUI.chat.accept(new MsgHandler<>(TypeMsg.Update_order_Status ,toServer));
		} catch (Exception e) {
			System.out.println("exception can not update sale");
			e.printStackTrace();
		}
		
		Alert alertCon = new Alert(AlertType.CONFIRMATION);
		alertCon.setTitle("Order status is changed");
		alertCon.setContentText("Thank You for your purchase!");
		alertCon.showAndWait();
		Client.pickUps.clear();
		Client.delivery.clear();
		ArrayList<Object>idArr=new ArrayList<Object>();
		idArr.add(LoginController.userLogin.getUser_id());
		try {
        	ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.Import_PickUps_Orders,idArr));

    	}catch(Exception e) {
    		System.out.println("fail in accept method");
    		return;
    	}
		try {
        	ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.Impotrt_Delivery_Orders,idArr));

    	}catch(Exception e) {
    		System.out.println("fail in accept method");
    		return;
    	}
		AnchorPane pane;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/TrackerScreen.fxml"));
			pane = loader.load();
			TrackerScreenController.trackerController=loader.getController();
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Scene scene = new Scene(pane);
		ClientUI.getStage().setScene(scene);
		ClientUI.getStage().show();
	}
	
	@FXML
	public void handleComboBox(ActionEvent event) {
		errorMsg.setVisible(false);
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
	Initializes the values of the Pickup and Delivery tables and the Combobox.
	It sets the cells of the "pickUpTable" table to display the values of the "order_code", "confirm_date", and "machine_id" fields of the {  PickUpOrder} objects.
	Similarly, it sets the cells of the "deliveryTable" table to display the values of the "order_code", "confirm_date", "estimated" and "address" fields of the {  PresentDeliveryOrder} objects.
	And after that it loads the order codes of the deliveries in deliveryNums combobox.
	@param location The location used to resolve relative paths for the root object, or {@code null} if the location is not known.
	@param resources The resources used to localize the root object, or {@code null} if the root object was not localized.
	*/
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		role_screen = LoginController.userLogin.getRole().toString();
		if (role_screen == "SubscriberScreen")
			cusORsub.setText("> Subscriber Menu");
		//initialize pick ups table
		orderNumPick.setCellValueFactory(new PropertyValueFactory<PickUpOrder,String>("order_code"));
		confirmDatePick.setCellValueFactory(new PropertyValueFactory<PickUpOrder,String>("confirm_date"));
		machineIdPick.setCellValueFactory(new PropertyValueFactory<PickUpOrder,String>("machine_id"));
		pickUpTable.setItems(getPickUpsOrder());
		orderNumPick.getStyleClass().add("colored-column");
		confirmDatePick.getStyleClass().add("colored-column");
		machineIdPick.getStyleClass().add("colored-column");
		pickUpTable.getStyleClass().add("colored-column");
		
		//initialize delivery table
		
		orderNumDelivery.setCellValueFactory(new PropertyValueFactory<PresentDeliveryOrder,String>("order_code"));
		confirmDateDelivery.setCellValueFactory(new PropertyValueFactory<PresentDeliveryOrder,String>("confirm_date"));
		estimatedDelivery.setCellValueFactory(new PropertyValueFactory<PresentDeliveryOrder,String>("estimated"));
		AddressDelivery.setCellValueFactory(new PropertyValueFactory<PresentDeliveryOrder,String>("address"));
		deliveryTable.setItems(getPresentDeliveryOrder());
		orderNumDelivery.getStyleClass().add("colored-column");
		confirmDateDelivery.getStyleClass().add("colored-column");
		estimatedDelivery.getStyleClass().add("colored-column");
		AddressDelivery.getStyleClass().add("colored-column");
		deliveryTable.getStyleClass().add("colored-column");
		
		//initialize comboBox
		for(int i=0;i<deliveriesTable.size();i++) {
			deliveryNum.add(deliveriesTable.get(i).getOrder_code());
		}
		
		deliveryNums.setItems(deliveryNum);
		errorMsg.setVisible(false);
	}
}