package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import client.ClientUI;
import common.AlertInTableStructureForManager;
import common.MsgHandler;
import common.TypeMsg;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text; 

//This is a controller for the alerts screen of the manager.
//these alerts are for items that below threshold levels.

public class ManageAlertsManagerController implements Initializable {
	public static ManageAlertsManagerController manageAlertsController;
	
	@FXML
	private ComboBox<String> AlertComboBox;
	
	@FXML
    private TableView<AlertInTableStructureForManager> AlertTable;

    @FXML
    private Button Backbt;

    @FXML
    private Button Xbt;

    @FXML
    private Label lbrole;
    
    @FXML
    private Button ViewAlert;

    @FXML
    private Button logout;

    @FXML
    private Text txtrole;

    @FXML
    private Text welcometxt;

    @FXML
    private BorderPane workhere;
    
    @FXML
    private ComboBox<String> whichAlertIdComboBox;

    @FXML
    private TableColumn<AlertInTableStructureForManager,String> alertIdColumn;

    @FXML
    private TableColumn<AlertInTableStructureForManager,String> machineIdColumn;
    
    @FXML
    private TableColumn<AlertInTableStructureForManager,String> dateColumn;
    
    @FXML
    private TableColumn<AlertInTableStructureForManager,String> statusColumn;
    
    @FXML
    private Text textForEmptyFields;
    
    public static ArrayList<Object> alertsInTable = null;
    
    public static ArrayList<String> alertsIdForComboBox = null;

    public static boolean isReadyToUpdateTable = false;
    
    public static String afterClickingSend = null;
    

    @SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
    	alertIdColumn.setCellValueFactory(new PropertyValueFactory<AlertInTableStructureForManager,String>("alertID"));
    	machineIdColumn.setCellValueFactory(new PropertyValueFactory<AlertInTableStructureForManager,String>("machineID"));
    	dateColumn.setCellValueFactory(new PropertyValueFactory<AlertInTableStructureForManager,String>("date"));
    	statusColumn.setCellValueFactory(new PropertyValueFactory<AlertInTableStructureForManager,String>("status"));
		alertIdColumn.getStyleClass().add("colored-column");
		machineIdColumn.getStyleClass().add("colored-column");
		dateColumn.getStyleClass().add("colored-column");
		statusColumn.getStyleClass().add("colored-column");
    	ObservableList<String> alertsInTable = null;
		ObservableList<String> list = FXCollections.observableArrayList("isw1","isw2","isw3","isw4");
		AlertComboBox.setItems(list);
		ArrayList<String> alertsForManagerPickUpComboBox = new ArrayList<String>(); //To initialize the combo box of "Select Alerts"
		setUpScreenInfo(); // Here we go to server with a request of the alerts that their status is only: IN_PROGRESS / ACTIVE
		while (!isReadyToUpdateTable) {}; // Waiting for server's response! 
        ObservableList<AlertInTableStructureForManager> listOfAlerts=getAlertsContentForManager();
		AlertTable.setItems(listOfAlerts); // Setting the Info to the table that the manager can watch 
		
		for (int i=0;i<listOfAlerts.size();i++) // Initializing the comboBox of the "Select Alert" with the only ACTIVE alerts ID
			if (listOfAlerts.get(i).getStatus().equals("ACTIVE")) 
			alertsForManagerPickUpComboBox.add(listOfAlerts.get(i).getAlertID());
		ObservableList<String> listContainingAlertsInfo = FXCollections.observableArrayList(alertsForManagerPickUpComboBox);
		whichAlertIdComboBox.setItems(listContainingAlertsInfo);
		textForEmptyFields.setVisible(false);
		// Set up info in the table 
	}
    
    /**
	 * SetUpScreenInfo method requests from the server the content of the items_alerts table 
	 * and present the manager the alerts in his area.
	 */
    public static void setUpScreenInfo() {
    	ArrayList<Object> data_for_server = new ArrayList<Object>();
		data_for_server.add(LoginController.userLogin.getUsername());
		MsgHandler getAlertsForManager = new MsgHandler(TypeMsg.Update_ManageAlertsManagerScreen,data_for_server);
		ClientUI.chat.accept((Object) getAlertsForManager); // Here we go to server
	}
    
    /**
	 * Server's response is calling this function for ending the while loop in initialize function
	 * 
	 * @param alertsInTableAfterDB alerts from the DB
	 */
	public static void initiate_AlertsForManager(ArrayList<Object> alertsInTableAfterDB) { 
		alertsInTable =(ArrayList<Object>) alertsInTableAfterDB;
		isReadyToUpdateTable = true;
	}
    
	/**
	 * This method will parse the arrayList that came back from the server. 
	 * The parse will first display ACTIVE alerts and only then IN_PROGRESS alerts
	 * 
	 * @return ObservableList of alerts to present to the manager
	 */ 
    public ObservableList<AlertInTableStructureForManager> getAlertsContentForManager() {
        ObservableList<AlertInTableStructureForManager> itemsToShowInTable = FXCollections.observableArrayList();
        int k=0;
        int alertTableSizeByFour=alertsInTable.size()/4;
        for (int i=0; i<alertTableSizeByFour; i++ ) {
        	k=i*4;
        	if (alertsInTable.get(k+3).equals("ACTIVE"))
        		itemsToShowInTable.add(new AlertInTableStructureForManager((String) alertsInTable.get(k), (String)alertsInTable.get(k+1),(String) alertsInTable.get(k+2),(String)alertsInTable.get(k+3)));
        }
       
        k=0;
        for (int i=0; i<alertTableSizeByFour; i++ ) {
        	k=i*4;
        	if (alertsInTable.get(k+3).equals("IN_PROGRESS"))
        		itemsToShowInTable.add(new AlertInTableStructureForManager((String) alertsInTable.get(k), (String)alertsInTable.get(k+1),(String) alertsInTable.get(k+2),(String)alertsInTable.get(k+3)));
        }
        return itemsToShowInTable;
    }
    
    /**
     * This method is called when the "View Alert" button is clicked. 
     * It retrieves the selected values from the "Select Worker" and "Select Alert" combo boxes and checks if they are not null.
     * If they are null, it shows an error message to the user.
     * Otherwise, it takes the selected alert ID and worker, and sends a message to the server to update the status of the alert.
     * It then updates the alert table with the updated information and populates the combo boxes with the updated data.
     *
     * @param event the action event of clicking the "View Alert" button
     */
    @FXML
    void whenClickingViewAlertBtn(ActionEvent event) {
    	String whichEmployee = AlertComboBox.getValue();
    	String whichAlertId = whichAlertIdComboBox.getValue();
    	if (whichEmployee == null || whichAlertId == null || whichEmployee.equals("Select Worker") || whichAlertId.equals("Select Alert")) {
    		textForEmptyFields.setVisible(true);
    		AlertComboBox.getSelectionModel().select("Select Worker");
    		whichAlertIdComboBox.getSelectionModel().select("Select Alert");
			return;
    	}
		textForEmptyFields.setVisible(false);
    	//set the text that you created (RED TEXT) to unvisible
    	ArrayList<Object> data_for_server = new ArrayList<Object>();
		data_for_server.add(whichAlertId);
		data_for_server.add(whichEmployee);
		int k=0;
		String whichMachineId;
		k = alertsInTable.indexOf(whichAlertId);
		whichMachineId=(String) alertsInTable.get(k+1);
		data_for_server.add(whichMachineId);
		data_for_server.add(LoginController.userLogin.getUsername());
		MsgHandler updateAlertsAfterManagerPressedSendBtn = new MsgHandler(TypeMsg.Update_AfterManagerPressedSendButton,data_for_server);
		ClientUI.chat.accept((Object) updateAlertsAfterManagerPressedSendBtn);
		while (afterClickingSend.equals(null)) {   };
		if (afterClickingSend.equals("Success")){
			AlertTable.getColumns().removeAll();
			AlertTable.refresh();
			isReadyToUpdateTable = false;
			setUpScreenInfo();
			while (!isReadyToUpdateTable) {};
	        ObservableList<AlertInTableStructureForManager> listOfAlerts=getAlertsContentForManager();
			AlertTable.setItems(listOfAlerts);
			ArrayList<String> alertsForManagerPickUpComboBox = new ArrayList<String>();
			for (int i=0;i<listOfAlerts.size();i++)
				if (listOfAlerts.get(i).getStatus().equals("ACTIVE"))
				alertsForManagerPickUpComboBox.add(listOfAlerts.get(i).getAlertID());
			ObservableList<String> listContainingAlertsInfo = FXCollections.observableArrayList(alertsForManagerPickUpComboBox);
			whichAlertIdComboBox.setItems(listContainingAlertsInfo);
			ObservableList<String> list = FXCollections.observableArrayList("isw1","isw2","isw3","isw4");
			AlertComboBox.setItems(list);
			AlertComboBox.getSelectionModel().select("Select Worker");
		}
		else {
			Alert a = new Alert(AlertType.ERROR,"An error has occured\nPlease contact administrators!");
			a.setAlertType(AlertType.ERROR);
			a.show();
			return;
		}
		
		
    }
	
    /**
	 * MySQLConnection can return two strings: 
     * 1) "Could Not Update Alert!", Means that the update was failed
     * 2) "Updated Alert Successfully", Means that the update went successfully 
	 * 
	 * @param responseFromServerString answer string from the server
	 */
	public static void cameBackAfterManagerPressedOnSendButton(ArrayList<Object> responseFromServerString) { 
		String res = (String)responseFromServerString.get(0);
		if (res.equals("Could Not Update Alert!")) // need to pop-up alert
			afterClickingSend = "Failed";
		else if (res.equals("Updated Alert Successfully"))  // need to reload screen
			afterClickingSend = "Success";
	}
	
	/**
     * This is the 'GoBackToHomeScreen' method.
     * When the button is clicked, the user returns to manager home screen.
     * @param actionEvent The `ActionEvent` object generated when the "Back" button is clicked.
     */
	@FXML // for the back button
	public void GoBackToHomeScreen(ActionEvent event) {
		AnchorPane pane1;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/ManagerScreen.fxml"));
			pane1 = loader.load();
			ManagerScreenController.controller = loader.getController();
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Scene scene1 = new Scene(pane1);
		ClientUI.getStage().setScene(scene1);
		ClientUI.getStage().show();
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
	 * Action event to return to Login screen
	 * @param event action event of "Logout" button
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




