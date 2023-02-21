package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
import client.ClientUI;
import common.MsgHandler;
import common.TypeMsg;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

// his is a Screen that holds the summary of the order for the user 
// when he picked up his order from local machine
// It will present him his new order code, final price and type of the order

public class RemoteOrderPopUpController implements Initializable{
	public static RemoteOrderPopUpController remoteOrderPopUpController;

    @FXML
    private Button Okbt;

    @FXML
    private Button Xbt;

    @FXML
    private Label areaEK;

    @FXML
    private Label code;

    @FXML
    private Label finishMsg;

    @FXML
    private Label locationAndMachine;

    @FXML
    private Label totalPrice;

    @FXML
    private Text welcometxt;

    @FXML
    private BorderPane workhere;
    
    private String role_screen; // Customer --> "ClientScreen"  ,  Subscriber --> SubscriberScreen
    private String payed_price;
    private String order_code;

    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	role_screen = LoginController.userLogin.getRole().toString();
    	if (role_screen == "ClientScreen") {
    		payed_price = ClientScreenController.remote_order_final_price;
    		order_code = ClientScreenController.remote_order_code;
    	}
    	else {
    		payed_price = SubscriberScreenController.remote_order_final_price;
    		order_code = SubscriberScreenController.remote_order_code;
    	}
    	totalPrice.setText("Payed price: " + payed_price +" NIS");
    	code.setText("Order Code: "+order_code);
    	String machine_area = Client.configuration.get(1).toString();
		String machine_location =  Client.configuration.get(3).toString();
		String machine_name = Client.configuration.get(2).toString();
		areaEK.setText("Area: "+machine_area);
		locationAndMachine.setText("Machine: " + machine_location + ", " + machine_name);
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