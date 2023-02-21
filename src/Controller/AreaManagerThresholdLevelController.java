package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import client.ClientUI;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class AreaManagerThresholdLevelController implements Initializable {
	
	public static AreaManagerThresholdLevelController areamanagercontroller;
	
	@FXML
	private AnchorPane AnchorPaneDetailPerMachine;

	@FXML
	private Button Backbt;
	
	@FXML
	private Button Xbt;
	
	@FXML
	private Text alerttext;
	
	@FXML
	private Text area_input;
	
	@FXML
	private Button buttonsaveupdate;
	
	@FXML
	private ComboBox<String> comboboxselectmachine;
	
	@FXML
	private Text currentTL;

	@FXML
	private TextField currentleveltxt;
	
	@FXML
	private TextField determinethresholdleveltxtfield;

	@FXML
	private AnchorPane editAnchorPaneSpace;

	@FXML
	private Label lbrole1;

	public static HashMap<String,String> machine_info= new HashMap<>();
	ObservableList<String> list;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ArrayList<String> id_location = new ArrayList<>();
		for (String key : machine_info.keySet()) {
			id_location.add(key);
		}
		list = FXCollections.observableArrayList(id_location);
		comboboxselectmachine.setItems(list);
		editAnchorPaneSpace.setVisible(false); 
		alerttext.setVisible(false);
		currentleveltxt.setEditable(false);
		area_input.setText(ManagerScreenController.area);
	}
	
	/**
	 * The method receives an `ArrayList` object called `machine_info` as a parameter. This `ArrayList` is expected to
	 * contain a list of strings, each representing the name of an item that has a quantity lower than the threshold level
	 * in the database.
	 * The method creates a new `ArrayList` object called `items_under_alert` and populates it with the names of the items
	 * from the `itemsAlertFromDB` list.
	 * @param itemsAlertFromDB An `ArrayList` object containing a list of strings representing the names of items under alert.
	 */
    public static void initiate_machine_info (ArrayList<Object> machine_info_DB) {
    	if (!machine_info_DB.isEmpty()) {
    		for (int i=0; i<machine_info_DB.size(); i+=3) {
    			machine_info.put((String)machine_info_DB.get(i)+ "-" +(String)machine_info_DB.get(i+1), 
    					(String)machine_info_DB.get(i+2)); 
    		}
    	}
    }
	
    /**
	 * @param event ActionEvent for when select machine
	 */
    @FXML
    void OnSelectMachine(ActionEvent event) {
    	String select_machine = comboboxselectmachine.getValue();
    	currentleveltxt.setText(machine_info.get(select_machine));
    	editAnchorPaneSpace.setVisible(true);
    } 
    
    /**
     * Method to chcek feedBack from Server
	 * @param msg MsgHandler for when the Client return from the 
	 * Server with validation message
	 */
    public void feedBack(MsgHandler<Object> msg) {
    	alerttext.setVisible(true);
    	alerttext.setText(msg.getType().toString());
    	alerttext.setFill(Color.GREEN);
    }
    
    /**
     * Method to chcek if String represent an integer number > 0
	 * @param str String of the new threshold that the manager enterd
	 * @return true if it is number > 0, false otherwise
	 */
    public static boolean isNumber(String str) {//check if threshold is legal number up from 0
    	for (int i = 0; i < str.length(); i++) {
    		if (!Character.isDigit(str.charAt(i))) {
    			return false;
    		}
    	}
    	return Double.parseDouble(str) >=0;
    }
    
    /**
     * The SaveThresholdLevel method is called when the user clicks the "Save" button on the ManagerScreen.
     * It retrieves the new threshold level entered by the user and checks if it is a valid number.
     * If it is valid, it creates an ArrayList and adds the selected machine's area, ID, and new threshold level to the list.
     * Then it creates a MsgHandler object and sends it to the server.
     * If the update is successful, the field is updated with the new threshold level and an alert message is displayed to the user.
     * If the new threshold level is not valid, an alert message is displayed to the user
     *
     * @param event the ActionEvent generated when the "Save" button is clicked
     */
    @FXML
    void SaveThresholdLevel(ActionEvent event) {
    	String newlevel = determinethresholdleveltxtfield.getText();
    	if(!determinethresholdleveltxtfield.getText().isEmpty() && isNumber(newlevel)) {
	    	ArrayList<Object> al = new ArrayList<>();
	    	al.add(ManagerScreenController.area);
	    	String[] id_location = comboboxselectmachine.getValue().split("-");
	    	al.add(id_location[0]);
	    	al.add(newlevel);
	    	try {
	    		ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.Update_ThresholdLevel,al));	
	    	} catch (Exception e) {
	    		System.out.println("fall in accept method");
	    		e.getStackTrace();
	    		return;
	    	}
	    	currentleveltxt.setText(newlevel);
	    }
    	else {
    		alerttext.setText("The threshold level not legal \nMust enter positive number");
    		alerttext.setVisible(true);
    		alerttext.setFill(Color.RED);
    	}
    }
    
    /**
     * Method to set visible new treshold level
	 * @param event ActionEvent for present threshold level
	 */
    @FXML
    void UpdateThresholdLevel(ActionEvent event) {
    	editAnchorPaneSpace.setVisible(true);
    }
	
    /**
     * Method to to check if threshold level is empty string
	 * @return true if it is empty, false otherwise
	 */
    boolean ExistThresholdLevelInMachine() {
    	return !currentleveltxt.getText().equals("");		
    }

    /**
     * This is the 'backButton' method.
     * When the button is clicked, the user returns to the manager home screen.
     * @param actionEvent The `ActionEvent` object generated when the "Back" button is clicked.
     */
	@FXML
	void backButton(ActionEvent event) {
		AnchorPane pane;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/ManagerScreen.fxml"));
			pane = loader.load();
			ManagerScreenController.controller = loader.getController();

		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Scene scene = new Scene(pane);
		ClientUI.getStage().setScene(scene);
		ClientUI.getStage().show();
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
		  
}

