package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Controller.LoginController;
import Controller.MenuISWController;
import Controller.OrderConfirmPopUpController;
import client.ClientUI;
import common.AlertStatus;
import common.ItemsAlert;
import common.MsgHandler;
import common.TypeMsg;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class HomeScreenISWController implements Initializable {			// need to change package to Controller!
	
	public static HomeScreenISWController homeScreenIswController;

    @FXML
    private ComboBox<String> AlertComboBox;

    @FXML
    private TableView<ItemsAlert> AlertTable;

    @FXML
    private TableColumn<ItemsAlert, String> Alert_num_col;

    @FXML
    private Button Backbt;

    @FXML
    private Button ViewAlert;

    @FXML
    private Button Xbt;

    @FXML
    private TableColumn<ItemsAlert, String> date_col;

    @FXML
    private Label lbrole;

    @FXML
    private Button logout;

    @FXML
    private TableColumn<ItemsAlert, String> machine_id_col;

    @FXML
    private TableColumn<ItemsAlert, String> status_col;

    @FXML
    private Text txtrole;

    @FXML
    private Text welcometxt;
    
    @FXML
    private Text nullAlert;

    @FXML
    private BorderPane workhere;
    
    public static ArrayList<ItemsAlert> iwsAlerts = new ArrayList<>();
    
    public static String machine;
    
    public static ItemsAlert selectedAlert;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	//demo //get it from DB
    	nullAlert.setVisible(false);
    	iwsAlerts.clear();
    	ArrayList<Object> msg = new ArrayList<>();
    	String username = LoginController.userLogin.getUsername();

    	msg.add(username); 
    	ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.Get_Alert_For_ISW,msg));
    	
    	Alert_num_col.setCellValueFactory(new PropertyValueFactory<ItemsAlert,String>("alert_id"));
    	machine_id_col.setCellValueFactory(new PropertyValueFactory<ItemsAlert,String>("machine_id"));
    	date_col.setCellValueFactory(new PropertyValueFactory<ItemsAlert,String>("date"));
    	status_col.setCellValueFactory(new PropertyValueFactory<ItemsAlert,String>("status"));
    	
    	AlertTable.setItems(getItemsAlerts());
    	AlertComboBox.setItems(getAlertsNum());    	
    }
    
    /**
     * Event handler for the AlertComboBox. When the user selects an alert from the combo box,
     * the machine and selected alert are updated, and the ViewAlertISW window is displayed.
     * If no alert is selected, a null alert message is displayed.
     * 
     * @param event the ActionEvent triggered when the user clicks the AlertComboBox
     */
    @FXML
    public void clickOnHandleAlert(ActionEvent event) {
    	if(AlertComboBox.getValue()!=null) {
    		for(ItemsAlert alert : iwsAlerts) {
    			if(alert.getAlert_id().equals(AlertComboBox.getValue())){
    				machine=alert.getMachine_id();
    				selectedAlert=alert;
    			}
    		}	
    		AnchorPane pane;
    		try {
    			FXMLLoader loader = new FXMLLoader();
    			loader.setLocation(getClass().getResource("/gui/ViewAlertISW.fxml"));
    			pane = loader.load();
    			ViewAlertISWController.viewAlertISWController=loader.getController();
    		}
    		catch (IOException e) {
    			e.printStackTrace();
    			return;
    		}
    		Scene scene = new Scene(pane);
    		ClientUI.getStage().setScene(scene);
    		//ClientUI.getStage().set
    		ClientUI.getStage().show();
    		
    	}else {
    		nullAlert.setVisible(true);
    	}	
    }
    
    
    /**
     * Returns an ObservableList of ItemsAlerts to be displayed in a table.
     * 
     * @return an ObservableList of ItemsAlerts
     */
    public ObservableList<ItemsAlert> getItemsAlerts(){	
    	ObservableList<ItemsAlert> alertsToShowInTable = FXCollections.observableArrayList();
    	for(ItemsAlert ia : iwsAlerts) { //need to get arr from DB
    		alertsToShowInTable.add(ia);
    	}
    	return alertsToShowInTable;
    }
    
    
    /**
     * Returns an ObservableList of alert IDs.
     * 
     * @return an ObservableList of alert IDs
     */
    public ObservableList<String> getAlertsNum(){
    	ObservableList<String> alerts_num = FXCollections.observableArrayList();
    	for(ItemsAlert ia : iwsAlerts) {
    		alerts_num.add(ia.getAlert_id());
    	}
    	return alerts_num;
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
	 * This Method logout the client and back to login screen
	 * @author G-10 */
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


	
	/**
     * Action event that happens when the user press the back button.
     * The user will go back to isw home screen
     * @param e ActionEvent
     */
	@FXML
	void clickOnBack(ActionEvent e) {
		AnchorPane pane;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/MenuISW.fxml"));
			pane = loader.load();
			MenuISWController.menuISWController= loader.getController();
		}
		catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		Scene scene = new Scene(pane);
		ClientUI.getStage().setScene(scene);
		ClientUI.getStage().show();
	}
}


