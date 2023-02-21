package Controller;

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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DeliveryScreenController implements Initializable {
    public static DeliveryScreenController controller;
    @FXML
    private Button Xbt;


    @FXML
    private Button btnDeliveryOrders;

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
     * @param location the url
     * @param resourceBundle the resource bundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {

        txtName.setText(LoginController.userLogin.getFirst_name());
    }

    @FXML
    void deliveryOrders(ActionEvent event) {
        ArrayList<Object> details = new ArrayList<>();
        details.add(LoginController.area);
        ClientUI.chat.accept(new MsgHandler(TypeMsg.Get_Delivery_Orders,details));
        AnchorPane pane;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gui/DeliveryOrders.fxml"));
            pane = loader.load();
            DeliveryOrderController.controller = loader.getController();

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
