package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
import client.ClientUI;
import common.ItemInOrder;
import common.MsgHandler;
import common.Order;
import common.RemoteOrder;
import common.TypeMsg;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

// This is a Screen that holds the summary of the order for the user.
// It will present him his new order code, final price and type of the order

public class OrderConfirmPopUpController implements Initializable {
	public static OrderConfirmPopUpController OrderConfirmController;

    @FXML
    private Button Okbt;

    @FXML
    private Button Xbt;

    @FXML
    private Label code;

    @FXML
    private Label locationAndMachine;

    @FXML
    private Label AreapickUp;

    @FXML
    private Label totalPrice;

    @FXML
    private Text welcometxt;

    @FXML
    private BorderPane workhere;
    
    @FXML
    private Label DeliveryAddress;
    
    @FXML
    private Label areaOfDelivery;
    
    @FXML
    private Label areaEK;
    
    @FXML
    private Label finishMsg;
    
    private String role_screen; // Customer --> "ClientScreen"  ,  Subscriber --> SubscriberScreen
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	role_screen = LoginController.userLogin.getRole().toString();
    	totalPrice.setText("Total price: " + String.format("%.2f", Client.order.getFinal_price())+" NIS");
    	code.setText("Order Code: "+Client.order.getOrder_code());
    	switch (Client.configuration.get(0).toString()) {
    	case "EK":
    		areaOfDelivery.setVisible(false);
    		DeliveryAddress.setVisible(false);
    		AreapickUp.setVisible(false);
    		finishMsg.setText("The machine prepares your order");
    		String machine_area = Client.configuration.get(1).toString();
    		String machine_location =  Client.configuration.get(3).toString();
    		String machine_name = Client.configuration.get(2).toString();
    		areaEK.setText("Area: "+machine_area);
    		locationAndMachine.setText("Machine: " + machine_location + ", " + machine_name);
    		break;
    	case "OL":
    		switch (OLMainController.orderType) {
    		case "REMOTE_PICKUP":
    			areaOfDelivery.setVisible(false);
    			DeliveryAddress.setVisible(false);
    			areaEK.setVisible(false);
    			finishMsg.setText("The machine saved your order");
    			String strToParse = OLMainController.machine_info; // string of format "Area Location machine_id"
    			String[] areaLocationID = strToParse.split(" ");
    			AreapickUp.setText("Pickup From: " + areaLocationID[0] + " Area");
    			locationAndMachine.setText("Machine: " +areaLocationID[1]+" "+areaLocationID[2]);
    			break;
    		case "REMOTE_DELIVERY":
    			AreapickUp.setVisible(false);
    			locationAndMachine.setVisible(false);
    			areaEK.setVisible(false);
    			finishMsg.setText("Time of delivery will be sent to you soon");
    			areaOfDelivery.setText("Area: " + OLMainController.machine_info);
    			DeliveryAddress.setText("Delivery Address: " + OLMainController.delivery_addr);
    			break;
    		default:
    			break;
    		}
    		break;
    	default:
			break;	
    	}  	
    }
    
    /**
     * Event handler for the "OK" button, navigate back to
     * the customer/subscriber home screen
     *
     * @param event Action Event for the "OK" button
     */
    public void clickOnOk(ActionEvent event) {
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