package Controller;

import client.ClientUI;
import common.ChangeScene;
import common.MsgHandler;
import common.Roles;
import common.TypeMsg;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import static Controller.LoginController.userLogin;



import static java.util.Collections.addAll;

public class CEOController implements Initializable {
    public static CEOController ceoController;
    public static String area;

    @FXML
    private Button Xbt;

    @FXML
    private Text lbrole;

    @FXML
    private Button logout;
    @FXML
    private Text name;

    @FXML
    private ComboBox<String> selectAreaBox;

    @FXML
    private Text welcaometxt;

    @FXML
    private Text welcaometxt1;
    private ChangeScene scene = new ChangeScene();

    @FXML
    private BorderPane workhere;


    @FXML
    void SelectAera(ActionEvent event) {
        area=selectAreaBox.getValue();
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

    @FXML
    void X(ActionEvent event) {
        ArrayList<Object> details = new ArrayList<>();
        details.add(LoginController.userLogin.getUser_id());
        MsgHandler disconnectToServer = new MsgHandler(TypeMsg.Request_disconnected, details);
        ClientUI.chat.accept((Object) disconnectToServer);

    }

    @FXML
    void logOutClicked(ActionEvent event) {
        ArrayList<Object>details=new ArrayList<>();
        details.add(userLogin.getUser_id());
        MsgHandler logoutCustomer = new MsgHandler(TypeMsg.Request_logout, details);
        ClientUI.chat.accept((Object) logoutCustomer);
        ((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
        scene.changeScreen(new Stage(), "/gui/Login.fxml");
    }


    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        ArrayList<String> Area = new ArrayList<String>(Arrays.asList("North","South","UAE"));
        selectAreaBox.getItems().addAll(Area);
        name.setText(userLogin.getFirst_name());
        lbrole.setText("Role:" + Roles.DivisionManager.toString());

    }





}

