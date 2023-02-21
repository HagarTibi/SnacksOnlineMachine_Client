package Controller;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import client.Client;
import client.ClientUI;
import common.MsgHandler;
import common.Order;
import common.RemoteOrder;
import common.Sale;
import common.Subscribe;
import common.TypeMsg;
import common.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

// Subscriber screen controller.
// The first screen to watch after a subscriber is logged in.

public class SubscriberScreenController implements Initializable {
	public static SubscriberScreenController controller;
    public static Subscribe subscriber;

    @FXML
    private Button Neworderbt;

    @FXML
    private Button Paymentsbt;

    @FXML
    private Button Trackerbt;

    @FXML
    private Button Xbt;

    @FXML
    private Label lbrole;

    @FXML
    private Button logout;

    @FXML
    private Text name;

    @FXML
    private Text txtrole;

    @FXML
    private Text welcometxt;
    
    @FXML
    private Text errorMsg;
    
    @FXML
    private Text noRemoteOrder;

    @FXML
    private TextField orderCodeInput;

    @FXML
    private Text orderCodeMsg;

    @FXML
    private Button pickupBtn;
    
    @FXML
    private ImageView pickUpImg;
    
    private String screen_ek_ol;
    public static boolean made_first_order;
    public static boolean is_delay_payment;
    
    public static ArrayList<String> userPickupOrders;
    
    public ArrayList<Object> msg = new ArrayList<>();
    
    public static String remote_order_final_price;
    public static String remote_order_code;
    
    private ScheduledExecutorService scheduledExecutorService;
    public static ScheduledFuture scheduledFuture;
    private boolean isNewOrder;

    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	if (scheduledFuture!=null)
    		scheduledFuture.cancel(true);
        scheduledExecutorService = null;
        isNewOrder = false;
    	userPickupOrders = new ArrayList<>();
    	name.setText(LoginController.userLogin.getFirst_name());
    	noRemoteOrder.setVisible(false);
		orderCodeMsg.setVisible(false);
		orderCodeInput.setVisible(false);
		errorMsg.setVisible(false);
		if (Client.configuration.get(0).toString().equals("OL")) {
			pickupBtn.setVisible(false);
			pickUpImg.setVisible(false);
		}
		else // in "EK"
			Trackerbt.setDisable(true);
	}
	
    /**
	 * Inner method used by the timer thread to navigate 
	 * back to the subscriber home screen
	 */
    private void navigateBackToClientScreen() {
        if (isNewOrder) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gui/ClientScreen.fxml"));
            AnchorPane pane1;
            try {
                pane1 = loader.load();
            } catch (IOException ex) {
                ex.printStackTrace();
                return;
            }
            Scene scene1 = new Scene(pane1);
            if (scene1 != null) {
                Platform.runLater(() -> {
                    ClientUI.getStage().setScene(null);
                    ClientUI.getStage().setScene(scene1);
                    Platform.runLater(() -> {
                        ClientUI.getStage().show();
                    });
                });
            }
        }
    }
    
    /**
	 * Action event to start new order process. 
	 * Depends on the "EK" or "OL" configuration, we will go to different screens.
	 * For "EK" the customer will start his buying. For "OL" the customer will choose first
	 * what kind of remote order he want in the OL main screen.
	 * Also, in case of new order, we run timer for the subscriber to finish his order. If after
	 * 20 minutes the user hasn't finished, there will be automatic return to the subscriber home screen 
	 *
	 * @param event action event of "new order" button
	 */
	@FXML
	void pressNewOrderBtn (ActionEvent event) {
		isNewOrder = true;
	    if(scheduledFuture == null || scheduledFuture.isDone()){
	        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
	        scheduledFuture = scheduledExecutorService.schedule(this::navigateBackToClientScreen, 20, TimeUnit.MINUTES);
	    }
		
		ArrayList<Object> msg = new ArrayList<>();
		msg.add(LoginController.userLogin.getUser_id());
		ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.Check_sub_first_order,msg));
		MsgHandler importCatalog = new MsgHandler<>(TypeMsg.Import_Catalog, null);
		ClientUI.chat.accept((Object)importCatalog);
		
		if (made_first_order) { 	
			// if user is subscriber and already made first order, then check for Sales
			ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.Import_sales_for_order,null));
		}
		
		Client.cart = new ArrayList<>();
		switch (Client.configuration.get(0).toString()) {
		case "EK":
			Client.order = new Order();
			OrderCatalogScreenController.final_price = Client.order.getFinal_price();
			screen_ek_ol = "OrderCatalogScreen";
			break;
		case "OL":
			Client.order = new RemoteOrder();
			OrderCatalogScreenController.final_price = Client.order.getFinal_price();
			screen_ek_ol = "OLMain";
			break;
		default:
			break;
		}
		AnchorPane pane1;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/"+screen_ek_ol+".fxml"));
			pane1 = loader.load();
			if (screen_ek_ol == "OrderCatalogScreen") 
				OrderCatalogScreenController.orderCatalogScreenController = loader.getController();
			else 
				OLMainController.oLMainController = loader.getController();
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
	 * Action event to check and to perform pick up of order that was made in "OL" configuration. 
	 * The method is checks if the customer has a remote pick up order that waiting for him in this machine.
	 * If yes, it will allow him to enter his order code. If not, the button will be disabled.
	 *
	 * @param event action event of pickup orders button
	 */
	@FXML
	void pressOnPickup(ActionEvent event) {
		msg.clear();
		msg.add(LoginController.userLogin.getUser_id());
		msg.add(Client.configuration.get(2).toString());
		ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.Check_exist_remote_order,msg));
		if (!userPickupOrders.isEmpty()) {
			orderCodeMsg.setVisible(true);
			orderCodeInput.setVisible(true);
		}
		else {
			noRemoteOrder.setVisible(true);
			pickupBtn.setDisable(true);
			pickUpImg.setDisable(true);
		}	
	}
	
	/**
	 * Listener event to check the input of the user for order code.
	 * if the input is not a number, there will be an error message.
	 * if the input is a number, there will be a call to another method
	 * to check if the static array of the remote orders of the customer contains
	 * a remote order with order code that matches his input
	 */
	@FXML
	private void textListener() {
	// Add an action listener to the text field
		orderCodeInput.setOnAction(event -> {
	    // Get the input text
	    String input = orderCodeInput.getText();

	    // Check if the input is a number
	    try {
	    	int number = Integer.parseInt(input);
	    	checkOrderCode(number);
	      } catch (NumberFormatException e) {
	    	  errorMsg.setText("Wrong Input - order code is a number");
	    	  errorMsg.setVisible(true);
	      }
	    });
	}
	
	/**
	 * Method to check if the static array of the remote orders of the customer contains
	 * a remote order with order code that matches his input
	 * @param number order code that the user had entered to the text field
	 */
	public void checkOrderCode (int number) {
		remote_order_code = ""+number;
		for (int i=0; i<userPickupOrders.size(); i+=2) {
			if (userPickupOrders.get(i).equals(remote_order_code)) {
				remote_order_final_price = userPickupOrders.get(i+1);
				msg.clear();
				msg.add(remote_order_code);
				msg.add(giveMeCurrentTimeFormat(true));
				ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.Update_remote_order,msg));
				AnchorPane pane;
				try {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("/gui/RemoteOrderPopUp.fxml"));
					pane = loader.load();
					RemoteOrderPopUpController.remoteOrderPopUpController=loader.getController();
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
				Scene scene = new Scene(pane);
				ClientUI.getStage().setScene(scene);
				ClientUI.getStage().show();
			}
		}
		errorMsg.setText("Wrong Order Code");
		errorMsg.setVisible(true);
	}
	
	/**
	 * Static method that gets the ArrayList of remote orders of the customer
	 * that were imported from the server into the client.
	 *
	 * @param event action event of pickup orders button
	 */
	public static void initiate_customer_pickup_orders(ArrayList<Object> pickup_orders) {
		if (!pickup_orders.isEmpty()) {
			for (int i=0; i<pickup_orders.size(); i++) {
				userPickupOrders.add((String)pickup_orders.get(i));
			}
		}
	}
	
	@FXML
	/**
	 * Action event when the user clicks on "Show Active Orders" to see
	 * his active remote orders in the next screen
	 *
	 * @param event action event of "Show Active Orders" button
	 */
	void onClickTracker(ActionEvent event) {		
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
	* Method to change the user's payment method between "Delay Payment" and "Regular Payment".
	* It first checks the user's current payment method by sending a message to the server
	* and then displays an alert to confirm the user's choice to change the payment method. 
	* If the user confirms, it sends another message to the server.
	* If the user does not confirm, the event handler does nothing.
	* @param event an ActionEvent that triggers this method when the button is pressed.
	*/
	@FXML
	void ChangeDelayPaymentStatus (ActionEvent event) {				
		ArrayList<Object> msg = new ArrayList<>();
		msg.add(LoginController.userLogin.getUser_id());
		String out_header, out_content;
		ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.Check_delay_payments,msg));
		if (is_delay_payment) {
			out_header = "You are currently on \"Delay Payment\" method.";
			out_content = "Are you sure you want to change to \"Regular Payment\" method?";
		}
		else {
			out_header = "You are currently on \"Regular Payment\" method.";
			out_content = "Are you sure you want to change to \"Delay Payment\" method?";
		}
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Change Payment Method");
        alert.setHeaderText(out_header);
        alert.setContentText(out_content);

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == yesButton) {
        	ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.Change_delay_payments,msg));
        } else {
        	// do nothing
        }
    }
	
	/**
	 * Action event to return to Login screen
	 * @param event action event of "Logout" button
	 */
	@FXML
    public void LogOutClicked(ActionEvent actionEvent) {
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
}
