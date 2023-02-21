package Controller;

import client.ClientUI;
import common.*;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static client.Client.activityReportdetails;
import static client.Client.ordersReportdetails;
/**
 * this class allow the user (with the  appropriate permissions)
 * the types of reports throughout the years by type of year and month
 * @author G-10
 *
 */
public class ChooseMonthlyReportController  implements Initializable {
    public static ChooseMonthlyReportController chooseMonthlyReport;
   public static HashMap<String, String> AreaMachine = new HashMap();
    public static InventoryReport inventoryReport;
    public static ArrayList<String> names = new ArrayList<>();
    public static String area;
    @FXML
    private Button Backbt;
    @FXML
    private ComboBox<String> areaMachineMenu;
    @FXML
    private Label arealb;

    @FXML
    private ComboBox<String> MonthMenu;

    @FXML
    private Button Xbt;

    @FXML
    private ComboBox<String> YearMenu;

    @FXML
    private Text errorlb;

    @FXML
    private Button logout;

    @FXML
    private Text logouttxt;

    @FXML
    private ComboBox<String> reportTypeMenu;

    @FXML
    private Button showReportsbt;
    @FXML
    private Label lblArea;
    @FXML
    private Label welcomebacklb;

    @FXML
    private Text welcometxt;

    @FXML
    private Text welcometxt1;

    private ChangeScene scene = new ChangeScene();


    /**
     * this method puts relevance values in the Combo Box
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ArrayList<String> months = new ArrayList<String>(Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"));
        MonthMenu.getItems().addAll(months);
        ArrayList<String> years = new ArrayList<String>(Arrays.asList("2020", "2021", "2022", "2023"));
        YearMenu.getItems().addAll(years);
        ArrayList<String> reportsType = new ArrayList<String>(Arrays.asList("Inventory reports", "Orders reports", "Customers Activity Report"));
        reportTypeMenu.getItems().addAll(reportsType);
        names.clear();
        AreaMachine.clear();
        errorlb.setVisible(false);
        if(LoginController.userLogin.getRole().equals(Roles.DivisionManager)){
            area=CEOController.area;
        }
        else{
            area=ManagerScreenController.area;
        }
        lblArea.setText(area);
    }

    /**
     * This method get information from the screen about the report required to show, check if he is existing in database
     * if it does - it will show the relevance report but if not -  it will show error msg
     *
     * @param event mouse clicked on showReport button
     */
    @FXML
    void showReportsClicked(ActionEvent event) throws IOException {
        ArrayList<String> reportDetails = new ArrayList<String>();
        errorlb.setVisible(false);
        if (MonthMenu.getValue() == null || YearMenu.getValue() == null || reportTypeMenu.getValue() == null) {
            errorlb.setVisible(true);
            errorlb.setText("Viewing reports requires you to choose 'Year', 'Month' & Type!");

        } else {
            reportDetails.add(MonthMenu.getValue());
            reportDetails.add(YearMenu.getValue());
            reportDetails.add(area);

            switch (reportTypeMenu.getValue()) {
                case "Orders reports":
                    ClientUI.chat.accept((Object) new MsgHandler<String>(TypeMsg.import_order_monthly_report_data, reportDetails));
                    if (ordersReportdetails.getTotalOrdersCount().equals("0")) {
                        errorlb.setVisible(true);
                        errorlb.setText("No such report");
                        return;
                    } else {
                        AnchorPane pane;
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/gui/OrdersReport.fxml"));
                            pane = loader.load();
                            OrdersReportController.monthlyOrderReport= loader.getController();

                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                        Scene scene = new Scene(pane);
                        ClientUI.getStage().setScene(scene);
                        ClientUI.getStage().show();


                    }
                    break;
                case "Inventory reports":
                    if (areaMachineMenu.getValue() == null) {
                        errorlb.setVisible(true);
                        errorlb.setText("Viewing Inventory reports requires you to choose Machine!");
                        return;
                    }
                    if (AreaMachine.containsValue(areaMachineMenu.getValue())) {
                        for (Map.Entry<String, String> entry : AreaMachine.entrySet()) {
                            if (Objects.equals(entry.getValue(), areaMachineMenu.getValue())) {
                                reportDetails.add(entry.getKey());
                                break;
                            }
                        }
                        inventoryReport = new InventoryReport(reportDetails.get(0), reportDetails.get(1), reportDetails.get(3), areaMachineMenu.getValue());
                        ArrayList<InventoryReport> inventoryReports = new ArrayList<>();
                        inventoryReports.add(inventoryReport);
                        ClientUI.chat.accept(new MsgHandler<>(TypeMsg.Get_Inventory_report, inventoryReports));
                        if (inventoryReport.getItemsAndAmount().size() == 0) {
                            errorlb.setVisible(true);
                            errorlb.setText("The report does not exist");
                            return;

                        }
                        AnchorPane pane;
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/gui/InventoryReport.fxml"));
                            pane = loader.load();
                            InventoryReportController.inventoryController= loader.getController();

                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                        Scene scene = new Scene(pane);
                        ClientUI.getStage().setScene(scene);
                        ClientUI.getStage().show();
                    }
                    break;
                case "Customers Activity Report":
                    ClientUI.chat.accept((Object) new MsgHandler<String>(TypeMsg.import_customer_Activity_report_data, reportDetails));
                    if (activityReportdetails.getMapofordersRange().size() == 0) {
                        errorlb.setVisible(true);
                        errorlb.setText("No such report");
                        return;
                    } else {
                        AnchorPane pane;
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/gui/CustomersActivityLevelReport.fxml"));
                            pane = loader.load();
                            CustomersActivityLevelReportController.customercontroller= loader.getController();

                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                        Scene scene = new Scene(pane);
                        ClientUI.getStage().setScene(scene);
                        ClientUI.getStage().show();

                    }
                    break;
                }
        }
    }

    public void getMachineList(ArrayList<Object> msg){
        names.clear();
        AreaMachine.clear();
        for( int i = 0; i < msg.size(); i+=2) {
            AreaMachine.put(msg.get(i).toString(),msg.get(i+1).toString());
        }
        for ( String value : AreaMachine.values()) {
            names.add(value);
        }
    }

    /**
     * This method terminates choose monthly reports scene and opens the NetworkManager/Division Manger/Area manager menu
     * @param event mouse clicked on Back button
     * @throws Exception if MouseEvent Fails
     */

    @FXML
    void Back(ActionEvent event) {

        names.clear();
        ((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
        if(LoginController.userLogin.getRole().equals(Roles.DivisionManager)){
            AnchorPane pane;
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/gui/CEOScreen.fxml"));
                pane = loader.load();
                CEOController.ceoController = loader.getController();

            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Scene scene = new Scene(pane);
            ClientUI.getStage().setScene(scene);
            ClientUI.getStage().show();
        }
        else{
            AnchorPane pane;
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/gui/ManagerScreen.fxml"));
                pane = loader.load();
                ManagerScreenController.controller = loader.getController();

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

     This method is used to handle the press action of the "reportType" combobox.
     When the user presses the combobox, the method clears the "names" list.
     Then, it checks if the "names" list is empty. If it is, it creates an ArrayList with the "area" variable from the ChooseMonthlyReportController class,
     and sends a message to the chat with the type "TypeMsg.GetMachineList" and the created ArrayList.
     Next, it checks the value of the "reportTypeMenu" combobox, if the value is "Inventory reports", it makes the "areaMachineMenu" visible and adds all the names from "names" list to the "areaMachineMenu" combobox.
     if the value is different, it makes the "areaMachineMenu" invisible.
     @param event - the ActionEvent that triggered the method.
     */
    @FXML
    void reportType(ActionEvent event) {
        //names.clear();
        if (names.isEmpty()) {
                ArrayList<String> area = new ArrayList<>(Arrays.asList(ChooseMonthlyReportController.area));
                ClientUI.chat.accept((Object) new MsgHandler<String>(TypeMsg.GetMachineList, area));
                //areaMachineMenu.getItems().addAll(names);
            }
            if (reportTypeMenu.getValue().equals("Inventory reports")){
                areaMachineMenu.setVisible(true);
                areaMachineMenu.getItems().addAll(names);
            }
            else {
            areaMachineMenu.setVisible(false);
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
    /**
     * Event handler for the ActionEvent that is triggered when the user
     * performs an action that logs out the user and switches to the login scene.
     *
     * @param actionEvent the ActionEvent that was triggered.
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








