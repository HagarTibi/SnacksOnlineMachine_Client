package Controller;
import client.ClientUI;
import common.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;


import static Controller.LoginController.*;
import static client.Client.ordersReportdetails;



/**
 * A controller class for Monthly Orders report. Implements the Initializable interface to allow for the
 * initialization of the controller.
 * in this Class the Graphs are segmented by all the machines in a certain area.
 *
 * @author Hagar Tibi
 */public class OrdersReportController implements Initializable {
    public static OrdersReportController monthlyOrderReport;
    @FXML
    private ComboBox<String> SelectMachine;
    @FXML
    private Label mostPurchlb1;
    @FXML
    private Label totalOrderQuatitylb1;
    @FXML
    private Label Arealb1;


    @FXML
    private Label selectMachinelb;

    @FXML
    private Label Arealb;

    @FXML
    private Button Backbt;

    @FXML
    private Label CanceledOrderlb;

    @FXML
    private Label Datelb;


    @FXML
    private Button Xbt;

    @FXML
    private Button logout;

    @FXML
    private Label mostPurchlb;

    @FXML
    private PieChart pieChart;

    @FXML
    private Label totalOrderQuatitylb;

    @FXML
    private BorderPane workhere;
    @FXML
    private Label errorlb;

    private ChangeScene scene = new ChangeScene();
    ArrayList<String> MachineLocation = new ArrayList<String>();
    ArrayList<String> MachineOrderReportDetails = new ArrayList<String>();
    /**
     * Event handler for the ActionEvent that is triggered when the user
     * performs an action that logs out the user and switches to the login scene.
     *
     * @param actionEvent the ActionEvent that was triggered.
     */
    @FXML
    public void LogOutClicked(ActionEvent actionEvent) {

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
     * performs an action that switches back to the "Choose Month" scene.
     *
     * @param event the ActionEvent that was triggered.
     */

    @FXML
    void Back(ActionEvent event) {
        ordersReportdetails = new MonthlyOrdersReportReturns();
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
     * Initializes the controller by setting the data for the pie chart, date and area labels, most purchased
     * item label, and total orders count label using the data from the MonthlyOrdersReportReturns object.
     * Also adds the list of machine locations to the SelectMachine combo box and hides the error label.
     *
     * @param location The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    public void initialize(URL location, ResourceBundle resources) {

        MonthlyOrdersReportReturns monthlyOrdersReport = ordersReportdetails;
        String mostWantedItem = monthlyOrdersReport.getMostPurchasedItemName();
        ArrayList<String> Machinelocation = monthlyOrdersReport.getMachine_location();

        ArrayList<Double> precentageOfTotalOrders = monthlyOrdersReport.getPercentageOfMachineOrdersArray();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        String totalOrdersCount = monthlyOrdersReport.getTotalOrdersCount();


        for (int i = 0; i < precentageOfTotalOrders.size(); i++) {
            Double precen = precentageOfTotalOrders.get(i);
            pieChartData.add(new PieChart.Data(Machinelocation.get(i), precen));

        }
        pieChart.setData(pieChartData);
        pieChart.getData().forEach(data ->
        {
            String percent = String.format("%.2f%%", data.getPieValue());
            Tooltip toolTip = new Tooltip(percent);
            Tooltip.install(data.getNode(), toolTip);
        });

        Datelb.setText(monthlyOrdersReport.getDate());
        Arealb1.setText(monthlyOrdersReport.getArea());
        mostPurchlb1.setText(mostWantedItem);
        totalOrderQuatitylb1.setText(String.valueOf(monthlyOrdersReport.getTotalOrdersCount()));
        SelectMachine.getItems().addAll(ChooseMonthlyReportController.names);
        errorlb.setVisible(false);

    }
    /**

     This method is used to handle the press action of the "SelectMachine" combobox.
     When the user presses the combobox, the method checks if a value has been selected.
     If a value has not been selected, an error message is displayed.
     If a value has been selected, the value and the current date are added to the "MachineOrderReportDetails" list.
     Then, the method sends a message to the chat with the imported machine monthly order report data and the "MachineOrderReportDetails" list.
     Next, the method loads the "OrdersMachineReport.fxml" file and sets the scene to the loaded AnchorPane.
     Finally, it shows the stage.
     @param event - the ActionEvent that triggered the method.
     */
    @FXML
    void PressSelectedMachinecombobox(ActionEvent event) {
        if (SelectMachine.getValue() == null) {
            errorlb.setVisible(true);
            errorlb.setText("You Must Select one Machine");
        } else {
            MachineOrderReportDetails.add(SelectMachine.getValue());
            MachineOrderReportDetails.add(Datelb.getText());
            ClientUI.chat.accept((Object) new MsgHandler<String>(TypeMsg.import_machine_monthly_order_report_data, MachineOrderReportDetails));
            AnchorPane pane;
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/gui/OrdersMachineReport.fxml"));
                pane = loader.load();
                OrdersMachineReportContrloller.machineController = loader.getController();

            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Scene scene = new Scene(pane);
            ClientUI.getStage().setScene(scene);
            ClientUI.getStage().show();


        }
    }

    /**
     * Event handler for the disconnect button.It sends a disconnect request to the server.
     *
     * @param event the action event that triggers this handler
     */
    @FXML
    void X(ActionEvent event) {
    	ArrayList<Object> details = new ArrayList<>();
        details.add(LoginController.userLogin.getUser_id());
        MsgHandler disconnectToServer = new MsgHandler(TypeMsg.Request_disconnected, details);
        ClientUI.chat.accept((Object) disconnectToServer);

    }

}














