package Controller;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientUI;
import common.MsgHandler;
import common.TypeMsg;
import gui.HomeScreenISWController;
import gui.ViewAlertISWController;
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

public class MenuISWController implements Initializable {
	
	//Starting screen for the Inventory and Sales worker - manage inventory alerts
	
	public static MenuISWController menuISWController;

    @FXML
    private Button Xbt;

    @FXML
    private Label lbrole;

    @FXML
    private Button logout;

    @FXML
    private Text name;

    @FXML
    private Button showAlerts;

    @FXML
    private Text txtrole;

    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	name.setText(LoginController.userLogin.getFirst_name());
    }
    
    /**
	 * Action event to move into screen of local machines with items
	 * below threshold level
	 *
	 * @param event action event of "X" button
	 */
    @FXML
    void clickOnShowAlerts(ActionEvent event) {
    	AnchorPane pane;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/HomeScreenISW.fxml"));
			pane = loader.load();
			HomeScreenISWController.homeScreenIswController=loader.getController();
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
     * Event handler for the disconnect button.It sends a disconnect request to the server.
     *
     * @param event the action event that triggers this handler
     */
	@FXML
	void disconnectClient(ActionEvent event) {
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