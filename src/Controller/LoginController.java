package Controller;

import client.Client;
import client.ClientUI;
import common.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;


import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/** Controller class for the login screen of the client application.
 * @author G-10
 */

public class LoginController implements Initializable {
    public static LoginController controller;
    public static  User userLogin;
    public static String area="";
    public static boolean successLogin=false;
    @FXML
    private Button Xbt;

    @FXML
    private Button btnFastLogIn;

    @FXML
    private ImageView imgFastLogIn;

    @FXML
    private Label lblConfiguration;

    @FXML
    private Hyperlink linktoregister;

    @FXML
    private Button loginbtn;

    @FXML
    private Text txtError;

    @FXML
    private PasswordField txtpassword;

    @FXML
    private TextField txtuser;

    @FXML
    private Text welcometxt;

    @FXML
    private BorderPane workhere;

    /**
     * Initializes the controller class.
     * It is used to initialize the controller class and perform any necessary setup for the UI elements
     * @param url the url
     * @param resourceBundle the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        txtError.setVisible(false);
        imgFastLogIn.styleProperty();
        //imgFastLogIn.getStyleClass().add("imgFastLogIn");
        if (Client.configuration.get(1).toString().equals(""))
            lblConfiguration.setText(Client.configuration.get(0).toString());
        else {
            lblConfiguration.setText(Client.configuration.get(0).toString() + " " +
                    Client.configuration.get(1).toString() + " " + Client.configuration.get(3).toString());
        }
        lblConfiguration.setAlignment(Pos.CENTER);
    }
    /**
     * Event handler for the login button.
     * It is responsible for validating the user input and sending a login request to the server.
     * @param event the action event that triggers this handler
     */
    @FXML
    void LoginCostumer(ActionEvent event) {
        txtError.setVisible(false);
        if (!validateInput()) {
            setErrorTxtNull();
            return;
        }
        txtError.setVisible(false);
        ArrayList<Object> userDetails = new ArrayList<>();
        userDetails.add(txtuser.getText());
        userDetails.add(txtpassword.getText());

        MsgHandler loginCostumer = new MsgHandler(TypeMsg.Request_Login, userDetails);
        ClientUI.chat.accept((Object) loginCostumer);
        if (successLogin){
            ArrayList<String> user_id = new ArrayList<>();
            user_id.add(LoginController.userLogin.getUser_id());
            ClientUI.chat.accept(new MsgHandler<>(TypeMsg.Set_isLogin, user_id));
        }
        else {return;}
    }
    /**
     * Event handler for the fast login button.
     * It sends a fast login request to the server and then loads the subscriber screen.
     * @param event the action event that triggers this handler
     */
    @FXML
    void fastLogIn(ActionEvent event) {

        MsgHandler fastLogin = new MsgHandler(TypeMsg.Fast_LogIn, null);
        ClientUI.chat.accept((Object)fastLogin);
        AnchorPane pane;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gui/SubscriberScreen.fxml"));
            pane = loader.load();
            SubscriberScreenController.controller = loader.getController();


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
     * Validates the user input for the login form.
     *checks that the username and password fields are not empty and do not contain any special characters.
     * If either of these conditions is not met, the method returns false. Otherwise, it returns true.
     * @return true if the input is valid, false otherwise
     */
    private boolean validateInput() {
        String strPattern = "[^a-zA-Z0-9_]";
        Pattern p = Pattern.compile(strPattern);
        Matcher user = p.matcher(txtuser.getText());
        Matcher password = p.matcher(txtpassword.getText());
        if ((txtuser.getText().equals("")) || (txtpassword.getText().equals("")) || (user.find()) || (password.find())){
            return false;
        }
        return true;
    }

    /**
     * Event handler for the disconnect button.It sends a disconnect request to the server.
     *
     * @param event the action event that triggers this handler
     */
    @FXML
    void disconnectClient(ActionEvent event)  {
        MsgHandler disconnectToServer = new MsgHandler(TypeMsg.Request_disconnected, null);
        ClientUI.chat.accept((Object) disconnectToServer);
    }
    /**
     * Sets the error message for an unsuccessful login due to the user already being logged in.
     */
    public void setErrorTxtAlreadyLoggedIN() {
        txtError.setVisible(true);
        txtError.setText("Logged UnSuccessful already logged in");
//        txtuser.setText("");
//        txtpassword.setText("");
    }
    /**
     * Sets the error message for an unsuccessful login due to the user not existing.
     */
    public void setErrorTxtUserNotExists() {
        txtError.setVisible(true);
        txtError.setText("Wrong username OR password! Try again!");
        txtuser.setText("");
        txtpassword.setText("");
    }
    /**
     * Sets the error message for an unsuccessful login due to missing input.
     */
    public void setErrorTxtNull() {
        txtError.setVisible(true);
        txtError.setText("You must enter USER NAME and PASSWORD");
        txtuser.setText("");
        txtpassword.setText("");
    }
    /**
     * Sets the error message for an unsuccessful action due to the Managers or Workers user being in EK configuration.
     */
    public void setErrorTxtConfiguration() {
        txtError.setVisible(true);
        txtError.setText("In EK configuration you have no ability to perform actions");
        txtuser.setText("");
        txtpassword.setText("");
    }

    /**
     * Checks the server's response to a login request and performs the appropriate action.
     * If the user is not already logged in, the method loads the appropriate screen and returns true.
     * If the user is already logged in, the method sets an error message and returns false.
     * If the response does not contain a user object or the user is not allowed to perform actions in EK configuration,
     * the method sets an error message and returns false.
     *
     * @param responseServer the server's response to the login request
     * @return true if the login was successful, false otherwise
     */
    public boolean checkIfLogIn(MsgHandler responseServer) {
        String area= "";
        if ((!(responseServer.getMsg() == null)) && (!(responseServer.getMsg().get(0).equals("")))) {
            if (responseServer.getMsg().get(1) != null){
                area = responseServer.getMsg().get(1).toString();
            }
            if (responseServer.getMsg().get(0) instanceof User) {
                User user = (User) responseServer.getMsg().get(0);
                if ((!(user.getRole().toString().contains("Client")|| user.getRole().toString().contains("Subscriber"))
                        && Client.configuration.get(0).toString().contains("EK"))){
                    setErrorTxtConfiguration();
                    successLogin = false;
                    return false;
                }
                if (user.getRole().equals(Roles.NULL)){
                    Platform.runLater(() -> {
                        Alert alertName = new Alert(Alert.AlertType.INFORMATION);
                        alertName.setTitle("Register");
                        alertName.setContentText("You must register as a customer to make purchases \n Network Service representative phone: 0547825425");
                        alertName.showAndWait();
                    });
                    successLogin = false;
                    return false;
                }
                if (user.getIsLoggedIn()) {
                    setErrorTxtAlreadyLoggedIN();
                    successLogin = false;
                    return false;
                } else {
                    userLogin = new User(user);
                    this.area = area;
                    //loadScreen(user,area);
                    return true;
                }
            }
            //In case the user login input was invalid (username/password) - error label will be shown
            setErrorTxtUserNotExists();
            return false;
        }
        //In case the user login input was invalid (username/password) - error label will be shown
        setErrorTxtUserNotExists();
        return false;
    }

    /**
     * Loads the appropriate screen based on the user's role and area.
     * It sets the userLogin field to the user object passed as a parameter and sets the area field.
     * It then uses an FXMLLoader to load the appropriate FXML file for the user's role.
     * If the user's role is NULL, the method displays an alert message indicating that the user must register.
     */
    public void loadScreen(){
        String fxmlName = "";
        //userLogin = new User(user);

        userLogin.setIsLoggedIn(true);
        AnchorPane pane;
        try {
            FXMLLoader loader = new FXMLLoader();
            switch(userLogin.getRole()) {
                case DeliveryMan:
                    fxmlName = "/gui/" + userLogin.getRole().toString() + ".fxml";
                    loader.setLocation(getClass().getResource(fxmlName));
                    pane = loader.load();
                    DeliveryScreenController.controller = loader.getController();
                    break;
                case AreaManager:
                    fxmlName = "/gui/" + userLogin.getRole().toString() + ".fxml";
                    loader.setLocation(getClass().getResource(fxmlName));
                    pane = loader.load();
                    ManagerScreenController.controller = loader.getController();
                    break;
                case MarketingManager:
                    fxmlName = "/gui/" + userLogin.getRole().toString() + ".fxml";
                    loader.setLocation(getClass().getResource(fxmlName));
                    pane = loader.load();
                    MarketingManagerHomeController.marketingScreenController = loader.getController();
                    break;
                case MarketingWorker:
                    fxmlName = "/gui/" + userLogin.getRole().toString() + ".fxml";
                    loader.setLocation(getClass().getResource(fxmlName));
                    pane = loader.load();
                    MarketingWorkerMenuController.marketworkermenucontroller = loader.getController();
                    break;
                case Customer:
                    fxmlName = "/gui/" + userLogin.getRole().toString() + ".fxml";
                    loader.setLocation(getClass().getResource(fxmlName));
                    pane = loader.load();
                    ClientScreenController.controller = loader.getController();
                    break;
                case Subscriber:
                    fxmlName = "/gui/" + userLogin.getRole().toString() + ".fxml";
                    loader.setLocation(getClass().getResource(fxmlName));
                    pane = loader.load();
                    SubscriberScreenController.controller = loader.getController();
                    break;
                case CustomerServiceWorker:
                    fxmlName = "/gui/" + userLogin.getRole().toString() + ".fxml";
                    loader.setLocation(getClass().getResource(fxmlName));
                    pane = loader.load();
                    CustomerServiceController.controller = loader.getController();
                    break;
                case InventoryAndSalesWorker: // gilad
                	fxmlName = "/gui/" + userLogin.getRole().toString() + ".fxml";
                    loader.setLocation(getClass().getResource(fxmlName));
                    pane = loader.load();
                    MenuISWController.menuISWController = loader.getController();
                    break;
               // case NULL:

                  //  return;
               case DivisionManager:
                    fxmlName = "/gui/" + userLogin.getRole().toString() + ".fxml";
                    loader.setLocation(getClass().getResource(fxmlName));
                    pane = loader.load();
                    CEOController.ceoController = loader.getController();
                    break;
                default:
                    pane = loader.load();
            }
            Scene scene = new Scene(pane);
            Platform.runLater(() -> {
                ClientUI.getStage().setScene(scene);
                ClientUI.getStage().show();
            });
        }catch (IOException e) {
            e.printStackTrace();
            return;
        }

    }
    /**
     * Event handler for the update to subscriber button.
     *  It displays an alert message indicating that the user should contact the network service.
     *
     * @param event the action event that triggers this handler
     */
    @FXML
    void updatetoSubscriber(ActionEvent event) {
        Alert alertName = new Alert(Alert.AlertType.INFORMATION);
        alertName.setTitle("Register");
        alertName.setContentText("Contact to Network Service representative by phone: 0547825425");
        alertName.showAndWait();
    }
}