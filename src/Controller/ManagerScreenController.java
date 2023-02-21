package Controller;

import client.Client;
import client.ClientUI;
import common.MsgHandler;
import common.TypeMsg;
import common.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
/**
 * The controller class for the "Manager Screen" scene.
 * It contains event handlers for the various buttons and UI elements in the scene.
 * @author G-10
 */

public class ManagerScreenController implements Initializable {

    public static ManagerScreenController controller;
    public static User areaManager;
    public static String area;

    @FXML
    private Button Xbt;

    @FXML
    private Button btnInventory;

    @FXML
    private Button btnInventoryAlert;

    @FXML
    private Button btnPermission;

    @FXML
    private Button btnReports;

    @FXML
    private ImageView imgLogout;

    @FXML
    private Label lbrole;

    @FXML
    private Button logout;

    @FXML
    private Text txtName;

    @FXML
    private Text txtrole;

    @FXML
    private Text welcometxt;

    /**
     * Initializes the controller class.
     * It is used to initialize the controller class and perform any necessary setup for the UI elements.
     * @param url the url
     * @param resourceBundle the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        area = LoginController.area;
        areaManager = LoginController.userLogin;
        txtrole.setText("Area Manager- " + area);
        txtName.setText(LoginController.userLogin.getFirst_name());
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
     * Event handler for the ActionEvent that is triggered when the user
     * performs an action that switches to the "Manage Inventory" scene.
     *
     * @param event the ActionEvent that was triggered.
     */
    @FXML
    void thresholdLevel(ActionEvent event) {
    	ArrayList<Object> details = new ArrayList<>();
        details.add(area);
    	ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.Import_machine_tresholds,details));
        AnchorPane pane;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gui/AreaManagerThresholdLevel.fxml"));
            pane = loader.load();
            AreaManagerThresholdLevelController.areamanagercontroller = loader.getController();

        } catch (IOException e) {

            e.printStackTrace();
            return;
        }
        Scene scene = new Scene(pane);
        ClientUI.getStage().setScene(scene);
        ClientUI.getStage().show();
    }


    /**
     * Event handler for the ActionEvent that is triggered when the user
     * performs an action that switches to the "Manage Permissions" scene.
     *
     * @param event the ActionEvent that was triggered.
     */
    
    @FXML
    void managePermissions(ActionEvent event) {
    	ArrayList<String> area=new ArrayList<>();
    	area.add(LoginController.area);
    	ClientUI.chat.accept((Object)new MsgHandler<>(TypeMsg.ImportUserRequestToSpecficArea,area));
    	if(Client.arrrequestsusers.size()!=0) {
        AnchorPane pane;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gui/ManagerPermissionsUsers.fxml"));
            pane = loader.load();
            AreaManagerPermission.controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene scene = new Scene(pane);
        ClientUI.getStage().setScene(scene);
        ClientUI.getStage().show();
    	}
    	else {
    		Alert alertPermission = new Alert(AlertType.ERROR);
			alertPermission.setTitle("System Message");
			alertPermission.setContentText("No more approvel requests");
	        alertPermission.showAndWait();
			return;
    	}
    }
    
    /**
     * Event handler for the ActionEvent that is triggered when the user
     * performs an action that switches to the "Manage Alerts" scene.
     *
     * @param event the ActionEvent that was triggered.
     */
    @FXML
    void manageAlert(ActionEvent event) {
        AnchorPane pane;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gui/ManageAlertsManager.fxml"));
            pane = loader.load();
            ManageAlertsManagerController.manageAlertsController = loader.getController();

        } catch (IOException e) {

            e.printStackTrace();
            return;
        }
        Scene scene = new Scene(pane);
        ClientUI.getStage().setScene(scene);
        ClientUI.getStage().show();
    }
    
    /**
     * Event handler for the ActionEvent that is triggered when the user
     * performs an action that switches to the "View Reports" scene.
     *
     * @param event the ActionEvent that was triggered.
     */
    @FXML
    void viewReports(ActionEvent event) {

        AnchorPane pane;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gui/ChooseMonthlyReportScreen.fxml"));
            pane = loader.load();
            ChooseMonthlyReportController.chooseMonthlyReport = loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene scene = new Scene(pane);
        ClientUI.getStage().setScene(scene);
        ClientUI.getStage().show();

    }

    /**
     * Event handler for the ActionEvent that is triggered when the user
     * performs an action that logs out the user and switches to the login scene.
     *
     * @param actionEvent the ActionEvent that was triggered.
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
}