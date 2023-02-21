package Controller;
import client.ClientUI;
import common.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.scene.control.Label;

import javafx.scene.control.Button;

import static Controller.LoginController.userLogin;
import static client.Client.machineOrdersReportdetails;

/**
 * A controller class for the orders machine report. Implements the Initializable interface to allow for
 * the initialization of the controller.
 * in this Class the Graphs are segmented by one Machine in selected area
 *
 * @author Hagar Tibi
 */
public class OrdersMachineReportContrloller implements Initializable {
    public static OrdersReportController orderController;
    public static OrdersMachineReportContrloller machineController;
    @FXML
    private Button Backbt;

    @FXML
    private Button Xbt;

    @FXML
    private Label datelb;

    @FXML
    private Label locationMachinelb;

    @FXML
    private Button logout;

    @FXML
    private Label mostPurchlb;

    @FXML
    private Label mostPurchlb1;

    @FXML
    private PieChart pieChart;

    @FXML
    private Label totalOrdersamountlb;

    @FXML
    private Label totalOrdersamountlb1;

    @FXML
    private BorderPane workhere;
    private ChangeScene scene = new ChangeScene();


    Double count=0.0;
    /**
     * Initializes the controller by setting the data for the pie chart, date label, most purchased item label,
     * total orders count label, and location machine label using the data from the MachineOrdersReport object.
     *
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MachineOrdersReport MachineOrdersReport =machineOrdersReportdetails;
        String mostWantedItem = MachineOrdersReport.getMostPurchasedItemName();
        String locationMachine = MachineOrdersReport.getLocation();
        String totalOrdersCount = MachineOrdersReport.getTotalOrdersCount();
        String date = MachineOrdersReport.getDate();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        HashMap<String, String> itemsCounter = MachineOrdersReport.getItemsAndAmountHashMap();
        for (Map.Entry<String, String> item : itemsCounter.entrySet()) {
            String itemName = item.getKey();
            String itemCounter = item.getValue();
            pieChartData.add(new PieChart.Data(itemName, Integer.parseInt(itemCounter)));
            count += Double.parseDouble(itemCounter);
        }
        count=count/100.0;
        pieChart.setData(pieChartData);
        pieChart.getData().forEach(data ->
        {
            String percent = String.format("%.2f%%", data.getPieValue()/count);
            Tooltip toolTip = new Tooltip(percent);
            Tooltip.install(data.getNode(), toolTip);
        });


        datelb.setText(MachineOrdersReport.getDate());
        mostPurchlb1.setText( mostWantedItem);
        totalOrdersamountlb1.setText(MachineOrdersReport.getTotalOrdersCount());
        locationMachinelb.setText(locationMachine);
    }

	/**
	Handles the event of the "Disconnect" button being clicked.
	Create a {  MsgHandler} object with {  TypeMsg#Request_disconnected} as the message type,
	and sends it to the server by calling {  ClientUI#chat} accept method.
	Then, it close the application by calling System.exit(0).
	@param event The event object associated with the button click
	@throws Exception in case of any failure in the process
	*/
	@FXML
    void disconnectClient(ActionEvent event) throws Exception {
		ArrayList<Object> details = new ArrayList<>();
        details.add(LoginController.userLogin.getUser_id());
        MsgHandler disconnectToServer = new MsgHandler(TypeMsg.Request_disconnected, details);
        ClientUI.chat.accept((Object) disconnectToServer);
    }

    public void LogOutClicked(javafx.event.ActionEvent actionEvent) {
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
     * Event handler for the ActionEvent that is triggered when the user
     * performs an action that logs out the user and switches to the login scene.
     *
     * @param actionEvent the ActionEvent that was triggered.
     */
    public void Back(javafx.event.ActionEvent actionEvent) {
        machineOrdersReportdetails = new MachineOrdersReport();
        AnchorPane pane;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gui/OrdersReport.fxml"));
            pane = loader.load();
            OrdersReportController.monthlyOrderReport = loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene scene = new Scene(pane);
        ClientUI.getStage().setScene(scene);
        ClientUI.getStage().show();


    }
}


