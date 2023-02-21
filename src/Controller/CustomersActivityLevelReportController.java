package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import client.ClientUI;
import common.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import static Controller.LoginController.userLogin;
import static client.Client.activityReportdetails;

/**
 * A controller class for the customers activity level report. Implements the Initializable interface to
 * allow for the initialization of the controller.
 *
 * @author Hagar Tibi
 */
public class CustomersActivityLevelReportController  implements Initializable {
public static CustomersActivityLevelReportController customercontroller;
    private ChangeScene scene = new ChangeScene();

    @FXML
    private Button Backbt;
    @FXML
    private StackedBarChart<String, Integer> customeReportChart;

    @FXML
    private Label Lowlb;

    @FXML
    private Label MachineLocationlb;

    @FXML
    private Button Xbt;

    @FXML
    private Label activitylevellb;

    @FXML
    private CategoryAxis customersquantity;

    @FXML
    private Button btnLogout;

    @FXML
    private Label reportdatelb;

    @FXML
    private BorderPane workhere;



    /**
     * Initializes a customer activity report by setting the date and area for the report, and then creating
     * a data series for a chart using the map of orders range from the ActivityReportdetails object. The
     * data series is then added to the CustomerActivityChart.
     *
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    public void initialize(URL location, ResourceBundle resources) {
        HashMap<String, Integer> mapofRange = new LinkedHashMap<>();
        String[] namesArray = {"Low 1-9","Medium 10-15","High 16-20","Very High 21+"};
        int i=0;
        CustomerActivityReport ActivitylevelReport = activityReportdetails;
        String date = ActivitylevelReport.getDate();
        String Area = ActivitylevelReport.getArea();
        mapofRange = ActivitylevelReport.getMapofordersRange();
        reportdatelb.setText("Date:" + date);
        MachineLocationlb.setText("Area:" + Area);
        for (Map.Entry<String, Integer> item : mapofRange.entrySet()) {
            XYChart.Series<String, Integer> series = new XYChart.Series<String,Integer>();
            String name = namesArray[i];
            series.setName(name);
            i++;
            XYChart.Data dataCustomers = new XYChart.Data((item.getKey()),item.getValue());
            series.getData().add(dataCustomers);
            customeReportChart.getData().add(series);
        }
        for(XYChart.Series<String,Integer>series1: customeReportChart.getData()){
            for(XYChart.Data<String,Integer>item1 : series1.getData()){
                Tooltip.install(item1.getNode(),new Tooltip( "Users amount:"+ item1.getYValue()));
            }
        }




    }
    /**
     * Event handler for the ActionEvent that is triggered when the user
     * performs an action that switches back to the "Choose Month" scene.
     *
     * @param //event the ActionEvent that was triggered.
     */

    public void Back(javafx.event.ActionEvent actionEvent) {

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
        Platform.runLater(() -> {
            ClientUI.getStage().setScene(scene);
            ClientUI.getStage().show();
        });
    }


    /**
     * Event handler for the ActionEvent that is triggered when the user
     * performs an action that logs out the user and switches to the login scene.
     *
     * @param actionEvent the ActionEvent that was triggered.
     */
    public void LogoutClicked(javafx.event.ActionEvent actionEvent) {
        ArrayList<Object>details=new ArrayList<>();
        details.add(userLogin.getUser_id());
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
     * @param actionEvent the action event that triggers this handler
     */
    public void disconnect(javafx.event.ActionEvent actionEvent) {
        ArrayList<Object> details = new ArrayList<>();
        details.add(LoginController.userLogin.getUser_id());
        MsgHandler disconnectToServer = new MsgHandler(TypeMsg.Request_disconnected, details);
        ClientUI.chat.accept((Object) disconnectToServer);

    }
}
