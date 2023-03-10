package Controller;

import client.Client;
import client.ClientUI;
import common.DeliveryOrder;
import common.MsgHandler;
import common.RemoteOrder;
import common.TypeMsg;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static client.Client.deliveryOrders;

public class DeliveryOrderController implements Initializable {
    public static DeliveryOrderController controller;
    public static ArrayList<RemoteOrder> remote = new ArrayList<>();
    static final Integer distance = 3;
    static final Integer drone_availability = 3;
    static final Integer delivery_loading_time = 2;
    ObservableList<RemoteOrder> list;
    @FXML
    private TableView<RemoteOrder> DeliveryOrders;

    @FXML
    private TableColumn<RemoteOrder, String > Purchase;
    
    //@FXML
    //private TableColumn<RemoteOrder,String> order_code;

    @FXML
    private Button Xbt;

    @FXML
    private TableColumn<RemoteOrder, String >address;

    @FXML
    private TableColumn<RemoteOrder, String > estimate;

    @FXML
    private ImageView imgLogout;

    @FXML
    private Label lbrole;

    @FXML
    private Button logout;

    @FXML
    private TableColumn<RemoteOrder, String > name;

    @FXML
    private TableColumn<RemoteOrder, String > received;

    @FXML
    private TableColumn<RemoteOrder, String> status;

    @FXML
    private Text txtName;

    @FXML
    private Text txtrole;

    @FXML
    private Text welcometxt;

    @FXML
    private BorderPane workhere;
    
    @FXML
    private Button editbt;
    
    @FXML
    private Button updatebt;

    @FXML
    private Text errortxt;

    /**
     * Initializes the controller class.
     * It is used to initialize the controller class and perform any necessary setup for the UI elements.
     * @param location the url
     * @param resourceBundle the resource bundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {

        errortxt.setVisible(false);
        name.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        address.setCellValueFactory(new PropertyValueFactory<>("delivery_address"));
        Purchase.setCellValueFactory(new PropertyValueFactory<>("order_confirmed_date"));
        estimate.setCellValueFactory(new PropertyValueFactory<>("estimated_delivery_date"));
        received.setCellValueFactory(new PropertyValueFactory<>("order_received_date"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        name.getStyleClass().add("colored-column");
        address.getStyleClass().add("colored-column");
        Purchase.getStyleClass().add("colored-column");
        estimate.getStyleClass().add("colored-column");
        received.getStyleClass().add("colored-column");
        status.getStyleClass().add("colored-column");
        addDeliveryOrdersTable();

    }
    
    /**
     * Method to add the delivery orders to the table view
     *
     * @param
     * @return void
     */
    private void addDeliveryOrdersTable() {

        ArrayList<RemoteOrder> table = new ArrayList<>();
        for(RemoteOrder delivery : deliveryOrders) {
            RemoteOrder temp = new RemoteOrder(delivery.getOrder_code(),delivery.getCustomer_id(),delivery.getOrder_confirmed_date(),
                    delivery.getStatus(),delivery.getEstimated_delivery_date(),delivery.getOrder_received_date(),
                    delivery.getDelivery_address());
            table.add(temp);
        }
        deliveryOrders.clear();
        list= FXCollections.observableArrayList();
        list.addAll(table);

        DeliveryOrders.setItems(list);
    }

    /**
     * Method to handle the editing of status column of the delivery orders table view.
     * 
     * @param event  ActionEvent event generated by clicking the Edit button.
     * @return void
     */
    @FXML
    void EditClicked(ActionEvent event) {
        DeliveryOrders.setEditable(true);
        status.setCellFactory(TextFieldTableCell.forTableColumn());
        errortxt.setVisible(false);
        status.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<RemoteOrder,String>>(){
            @Override
            public void handle(TableColumn.CellEditEvent<RemoteOrder, String> event) {
                RemoteOrder item = event.getRowValue();
                if(item.getOrder_received_date()==null && event.getNewValue().equals("DONE")) {
                	errortxt.setVisible(true);
                    return;
                }
                else if(event.getNewValue().equals("CONFIRM") || event.getNewValue().equals("DONE")){
                    errortxt.setVisible(false);
                    item.setStatus(event.getNewValue());
                }
                else{
                    errortxt.setVisible(true);
                    return;
                }
                DeliveryOrders.refresh();
            }

        });

    }
    
    /**
     * Method to handle the updating of delivery orders table view.
     * This method is invoked when the user clicks the Update button. It creates a new list of RemoteOrder objects
     * called 'updatelist' and retrieves the current table data by calling 'getItems()' on the DeliveryOrders table view.
     * Then it iterates through this list and checks each order's status, if status equals 'CONFIRM' and the estimate date is null
     * Then it will call a helper method 'getNewDate' passing in certain parameters. This helper method returns an estimated delivery date
     * and this date is set to the current order. 
     * Next, the current order is added to the updatelist and msg list, after that all the rows in the list variable is cleared 
     * and all the orders in the update list are added to it. Finally, the method sets the items for the DeliveryOrders table view 
     * to this updated list.
     * 
     * @param event ActionEvent event generated by clicking the Update button.
     * @return void
     */
    @FXML
    void UpdateClicked(ActionEvent event) {
        ArrayList<RemoteOrder> updatelist = new ArrayList<>();
        ObservableList<RemoteOrder> tableList = DeliveryOrders.getItems();
        ArrayList<Object> msg = new ArrayList<>();

        for(RemoteOrder RO : tableList){
            if((RO.getStatus().equals("CONFIRM")) && (RO.getEstimated_delivery_date()==null)){ //check if status equals confirm && estimate date == null
            	String estimatedDate =  getNewDate(RO.getOrder_confirmed_date(),distance,drone_availability,delivery_loading_time);
            	RO.setEstimated_delivery_date(estimatedDate);
                Alert alertName = new Alert(Alert.AlertType.CONFIRMATION);
                alertName.setTitle("Name field empty");
                alertName.setContentText("Estimated delivery date for order "+RO.getOrder_code()+" set to: "+RO.getEstimated_delivery_date()
                        +"\na message was sent to customer "+RO.getCustomer_id()+" mail: ekrutbraude-G10@gmail.com");

        		ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.OK_DONE);
        		alertName.getButtonTypes().setAll(buttonTypeOk);

        		Optional<ButtonType> result = alertName.showAndWait();
        		if (result.get() == buttonTypeOk) {
        		    // close the dialog
        			alertName.close();
        		}
            }
            updatelist.add(RO);
            msg.add(RO);
        }
        //deliveryOrders.clear();
        list.clear();
        list.addAll(updatelist);
        DeliveryOrders.setItems(list);
        
        //msg.add(updatelist);
        ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.Update_Alrt_Status_To_CONFIRM, msg));

    }

    /**
     * Method to get the day of the month from a given date string.
     *
     * @param dateString a string representation of a date in the format "yyyy-MM-dd HH:mm"
     * @return int representing the day of the month, returns -1 if the date string is in an invalid format
     */
    public int getDayFromDateString(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = format.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    /**
     * Method to calculate and return a new estimated delivery date based on distance, drone availability, and delivery loading time.
     *
     * @param dateString a string representation of a date in the format "yyyy-MM-dd HH:mm"
     * @param distance an int value representing the distance of delivery location
     * @param droneAvailability an int value representing the availability of drone for the delivery
     * @param deliveryLoadingTime an int value representing the loading time for the delivery
     * @return a string representation of the new estimated delivery date in the format "yyyy-MM-dd HH:mm"
     */
    public String getNewDate(String dateString, final int distance,final int droneAvailability, final int deliveryLoadingTime) {
        int day = getDayFromDateString(dateString);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = format.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, day + distance + droneAvailability + deliveryLoadingTime);
            Date newDate = calendar.getTime();
            return format.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
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
    @FXML
    void Back(ActionEvent event) {

        AnchorPane pane;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gui/DeliveryScreen.fxml"));
            pane = loader.load();
            DeliveryScreenController.controller = loader.getController();
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene scene = new Scene(pane);
        ClientUI.getStage().setScene(scene);
        ClientUI.getStage().show();

    }


}