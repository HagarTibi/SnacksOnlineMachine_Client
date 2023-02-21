package Controller;


import client.ClientUI;
import common.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static Controller.LoginController.userLogin;

/**
 * The controller class for the "Inventory Report" scene.
 * It contains event handlers for the various buttons and UI elements in the scene.
 * @author G-10
 */

public class InventoryReportController implements Initializable {
    public static InventoryReportController inventoryController;
    ObservableList<ProductStatus> list;
    private ChangeScene scene = new ChangeScene();
    @FXML
    private Button Backbt;

    @FXML
    private Label MachineLocationlb;

    @FXML
    private Button Xbt;

    @FXML
    private ImageView btnLogout;

    @FXML
    private CategoryAxis category;

    @FXML
    private TableView<ProductStatus> countThresholdUnavailable;

    @FXML
    private BarChart<String, Integer> inventorychart;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblThreshold;

    @FXML
    private Button logout;

    @FXML
    private NumberAxis number;

    @FXML
    private TableColumn<ProductStatus, String> products;

    @FXML
    private TableColumn<ProductStatus, Integer> threshold;

    @FXML
    private Text txtD;

    @FXML
    private Label txtDate;

    @FXML
    private Label txtLocation;

    @FXML
    private Label txtTreshold;

    @FXML
    private TableColumn<ProductStatus, Integer> unavailable;

    @FXML
    private Text welcometxt;

    /**
     * Event handler for the ActionEvent that is triggered when the user
     * performs an action that switches back to the "Choose Month" scene.
     *
     * @param event the ActionEvent that was triggered.
     */
    @FXML
    void backButton(ActionEvent event) {

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
     * Event handler for the ActionEvent that is triggered when the user
     * performs an action that logs out the user and switches to the login scene.
     *
     * @param actionEvent the ActionEvent that was triggered.
     */
    @FXML
    void logoutBtn(ActionEvent actionEvent) {
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
     * Initializes the controller class.
     * It is used to initialize the controller class and perform any necessary setup for the UI elements.
     * @param location the url
     * @param resourceBundle the resource bundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {

        txtDate.setText(ChooseMonthlyReportController.inventoryReport.getMonth() +"/" + ChooseMonthlyReportController.inventoryReport.getYear());
        txtLocation.setText(ChooseMonthlyReportController.area);
        txtD.setText(ChooseMonthlyReportController.inventoryReport.getLocation());
        txtTreshold.setText(ChooseMonthlyReportController.inventoryReport.getThreshold_level().toString());
        XYChart.Series dataSeries = new XYChart.Series();
        addChart(dataSeries);
        products.setCellValueFactory(new PropertyValueFactory<>("name"));
        threshold.setCellValueFactory(new PropertyValueFactory<>("thresholdCount"));
        unavailable.setCellValueFactory(new PropertyValueFactory<>("unavailableCount"));
        products.getStyleClass().add("colored-column");
        threshold.getStyleClass().add("colored-column");
        unavailable.getStyleClass().add("colored-column");
        countThresholdUnavailable.borderProperty();
        countThresholdUnavailable();


    }

    /**
     * Populates the "Count Threshold Unavailable" list with the data from the inventory report.
     */
    @FXML
    void countThresholdUnavailable() {
        ArrayList<ProductStatus> table = ChooseMonthlyReportController.inventoryReport.getProductStatus();
        list= FXCollections.observableArrayList();
        list.addAll(table);

        countThresholdUnavailable.setItems(list);
    }

    /**
     * Adds a data series to the inventory chart.
     *
     * @param dataSeries the data series to add to the chart.
     */
    public void addChart(XYChart.Series dataSeries){
        HashMap<String,Integer> items = ChooseMonthlyReportController.inventoryReport.getItemsAndAmount();
        for (Map.Entry<String,Integer> item: items.entrySet()) {
            dataSeries.getData().add(new XYChart.Data(item.getKey(), item.getValue()));
        }
        inventorychart.getData().add(dataSeries);
        for(XYChart.Series<String,Integer>series1: inventorychart.getData()){
            for(XYChart.Data<String,Integer>item1 : series1.getData()){
                Tooltip.install(item1.getNode(),new Tooltip( "Users amount:"+ item1.getYValue()));
            }
        }

    }
}
