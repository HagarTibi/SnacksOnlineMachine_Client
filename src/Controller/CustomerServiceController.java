package Controller;

import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


import client.ClientUI;
import common.MsgHandler;
import common.TypeMsg;
import javafx.application.Platform;
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

public class CustomerServiceController implements Initializable{
	public static CustomerServiceController controller;

    @FXML
    private Button Xbt;

    @FXML
    private Button logout;

    @FXML
    private Button subsReqbtn;

    @FXML
    private Text txtrole;

    @FXML
    private Button userReqbtn;

    @FXML
    private Text welcometxt;

    @FXML
    private BorderPane workhere;
    /**
    Initializes the welcome text to welcome back and the name of customer service
    @author G-10
    */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	welcometxt.setText("Welcome Back "+LoginController.userLogin.getFirst_name());
    }
    /**

    Handles the event when the customer service clicks clicks the "Subscriber Requests" button
    @param event the event that triggered this method

    @author G-10
    */
    @FXML
    public void onClickSubscriberRequests(ActionEvent event) {
    	AnchorPane pane;
		try {
			FXMLLoader loader = new FXMLLoader();
			//loader.setLocation(getClass().getResource("../gui/AreaManagerThresholdLevel.fxml"));
			loader.setLocation(getClass().getResource("/gui/SubscriberRequests.fxml"));
			pane = loader.load();
			SubscriberRequestsController.subReqController=loader.getController();
		
			    			
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Scene scene = new Scene(pane);
		ClientUI.getStage().setScene(scene);
		//ClientUI.getStage().set
		ClientUI.getStage().show();
    }
	
    /**
	 * This Method disconnect client by X button
	 * @author G-10 */
    @FXML
    void disconnectClient(ActionEvent event) throws Exception {
        ArrayList<Object> details = new ArrayList<>();
        details.add(LoginController.userLogin.getUser_id());
        MsgHandler disconnectToServer = new MsgHandler(TypeMsg.Request_disconnected, details);
        ClientUI.chat.accept((Object) disconnectToServer);
    }

    /**
	 * This Method logout the client and back to login screen
	 * @author G-10 */
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
	 * This Method when the customer service clicks the "User Requests" button
	 * action hand
	 * @author G-10 */
    @FXML
    public void onClickUserRequests(ActionEvent event) {
    	AnchorPane pane;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/UserReqeusts.fxml"));
			pane = loader.load();
			UserRequestsController.userRequestcontroller=loader.getController();
		
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Scene scene = new Scene(pane);
		ClientUI.getStage().setScene(scene);
		//ClientUI.getStage().set
		ClientUI.getStage().show();
    }

    
    


}