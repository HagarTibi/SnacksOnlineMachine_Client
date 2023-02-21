package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Controller.LoginController;
import client.ClientUI;
import common.ItemInMachine;
import common.ItemsAlert;
import common.MsgHandler;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.converter.IntegerStringConverter;

public class ViewAlertISWController implements Initializable { 			// need to change package to Controller!
	public static ViewAlertISWController viewAlertISWController;

    @FXML
    private Button Backbt;

    @FXML
    private Button Xbt;
    
    @FXML
    private Button editBtn;

    @FXML
    private Text alertNumtxt;

    @FXML
    private Button allowUpdate;
    
    @FXML
    private Text welcometxt;

    @FXML
    private Text datetxt;

    @FXML
    private Button logout;

    @FXML
    private TableView<ItemInMachine> machineItemsTable;
    
    @FXML
    private TableColumn<ItemInMachine,Integer> amountCol;
    
    @FXML
    private TableColumn<ItemInMachine, String> itemNameCol;

    @FXML
    private Text machinetxt;
    
    @FXML
    private Text thresholdtxt;
    
    @FXML
    private Text illigal_input;
    
    @FXML
    private Text underLevel;

    @FXML
    private BorderPane workhere;
    
    public static ArrayList<ItemInMachine> items = new ArrayList<>();
    
    public static int threshold;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	items.clear();
    	illigal_input.setVisible(false);
    	underLevel.setVisible(false);
    	ArrayList<Object> msg = new ArrayList<>();
    	msg.add(HomeScreenISWController.selectedAlert.getMachine_id()); 
    	ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.Get_Items_In_Machine,msg));
    	
    	//get threshold of machine.
    	ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.Get_Threshold,msg));
    	
    	itemNameCol.setCellValueFactory(new PropertyValueFactory<ItemInMachine,String>("item_name"));
    	amountCol.setCellValueFactory(new PropertyValueFactory<ItemInMachine,Integer>("item_amount_in_machine"));

    	machineItemsTable.setItems(getItems()); //set items on table
    	//set text
    	welcometxt.setText("Item Inventory: "+HomeScreenISWController.selectedAlert.getMachine_id());
    	alertNumtxt.setText(HomeScreenISWController.selectedAlert.getAlert_id());
    	machinetxt.setText(HomeScreenISWController.selectedAlert.getMachine_id());
    	datetxt.setText(HomeScreenISWController.selectedAlert.getDate());
    	thresholdtxt.setText(String.valueOf(threshold));
	
    }

    /**
     * Returns an ObservableList of ItemInMachine objects to be displayed in a table.
     * 
     * @return an ObservableList of ItemInMachine objects
     */
	private ObservableList<ItemInMachine> getItems() {
		ObservableList<ItemInMachine> ItemsToShowInTable = FXCollections.observableArrayList();
		for(ItemInMachine item : items) {
			ItemsToShowInTable.add(item);
		}
		return ItemsToShowInTable;
	}
	
	
	/**
	 * Event handler for the Edit Button. Makes the amount column in the machine items table editable,
	 * and allows the user to enter a new amount for an item. If the input is not a positive integer,
	 * an illegal input message is displayed. If the input is less than the threshold, a low level message is displayed.
	 * Otherwise, the new value is saved in the table and the table is refreshed.
	 * 
	 * @param event the ActionEvent triggered when the user clicks the Edit Button.
	 */
    @FXML
    void OnClickEditAmount(ActionEvent event) {
    	machineItemsTable.setEditable(true);
    	amountCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    	amountCol.setOnEditCommit(new EventHandler<CellEditEvent<ItemInMachine,Integer>>(){
    		@Override
    		public void handle(CellEditEvent<ItemInMachine,Integer> event) {
        		ItemInMachine item = event.getRowValue();
        		if(Integer.valueOf(event.getNewValue())<=0) {
        			//add condition of string
            		underLevel.setVisible(false);
        			illigal_input.setVisible(true);
        		}
        		else if (Integer.valueOf(event.getNewValue())<threshold) {
        			illigal_input.setVisible(false);
        			underLevel.setVisible(true);        	
        		}
        		else {
            		underLevel.setVisible(false);
        			illigal_input.setVisible(false);
        			item.setItem_amount_in_machine(Integer.valueOf(event.getNewValue()));
        		}
        		machineItemsTable.refresh();
    		}
    	});
    }
    
    
    /**
     * Event handler for the Allow Update button. Updates the items in a machine and the alert status
     * based on the current state of the machine items table. If there are no longer any items below the threshold,
     * the alert status is updated to "DONE". Otherwise, the items in the machine are updated.
     * 
     * @param event the ActionEvent triggered when the user clicks the Allow Update button
     */
    @FXML
    void ClickOnAllowUpdate(ActionEvent event) {
    	ArrayList<ItemInMachine> update = new ArrayList<>();
    	ObservableList<ItemInMachine> afterUpdate = machineItemsTable.getItems();
    	for(ItemInMachine i : afterUpdate) {
    		update.add(i);
    	}
    	if(checkThreshold(update,threshold)) {	
    		//update alert status to done
    		ArrayList<Object> msg = new ArrayList<>();
        	msg.add(HomeScreenISWController.selectedAlert.getMachine_id());
        	ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.Update_Alrt_Status_To_Done,msg));
    	}
    	//update items_in_machine
    	ArrayList<Object> msg = new ArrayList<>();
    	msg.add(HomeScreenISWController.selectedAlert.getMachine_id()); //machine_id
    	msg.add(update); //list of items in machine
    	msg.add(threshold); //threshold lvl of machine
    	ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.Update_Items_Amount,msg));
    	
    	Alert alertName = new Alert(AlertType.CONFIRMATION);
		alertName.setTitle("Message");
		alertName.setContentText("machine: "+HomeScreenISWController.selectedAlert.getMachine_id()+" have successfuly updated!");
		alertName.showAndWait();
		return;
    }
    
    
    /**
     * Returns true if all items in the specified list are above the specified threshold.
     * Otherwise, returns false.
     * 
     * @param afterUpdate the list of items to check
     * @param threshold the threshold to compare against
     * @return true if all items in the list are above the threshold, false otherwise
     */
    boolean checkThreshold(ArrayList<ItemInMachine> afterUpdate,int threshold) {
    	for(ItemInMachine item: afterUpdate) {
    		if(item.getItem_amount_in_machine()<threshold) {
    			return false;
    		}
    	}
    	return true;
    }
	
    /**
     * Action event that happens when the user press the back button.
     * The user will go back to isw home screen
     * @param e ActionEvent
     */
	@FXML // Back button
    void ClickOnBack(ActionEvent event) {
    	AnchorPane pane;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/HomeScreenISW.fxml"));
			pane = loader.load();
			HomeScreenISWController.homeScreenIswController = loader.getController();
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