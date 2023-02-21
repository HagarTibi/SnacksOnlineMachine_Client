package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientUI;
import common.MsgHandler;
import common.Subscribe;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

	public class MarketingWorkerMenuController implements Initializable{
		 public static MarketingWorkerMenuController marketworkermenucontroller;

		    @FXML
		    private Text txtrole;

		    @FXML
		    private Text welcometxt;

		    @FXML
		    private Button Xbt;

		    @FXML
		    private Button logout;

		    @FXML
		    private BorderPane workhere;

		    @FXML
		    private Button btnPresentSaleOfSpecificArea;



	    ObservableList<String> list;// Observable list to enter Subscribe to the table 
	    
	    
	    /**

	    The method OnClickPresentSalesInArea is responsible for handling a button click event. When the button is clicked, it sends a request to the server to bring all the sales in a specific area.
	    It then loads the MarketingWorkerSalesInArea.fxml file and sets the scene to display the information of the sales in the specific area.
	    @param event the button click event that triggers the method
	    */
	    @FXML
	    void OnClickPresentSalesInArea(ActionEvent event) {
	    		ArrayList<String> lstarea=new ArrayList<String>();
	    		lstarea.add(LoginController.area);
	    		ClientUI.chat.accept((Object)new MsgHandler<String>(TypeMsg.Present_all_sales_in_specific_area,lstarea));//send requst to bring sales in area
	    		AnchorPane pane;
	    		try {
	    			FXMLLoader loader = new FXMLLoader();
	    			//loader.setLocation(getClass().getResource("../gui/AreaManagerThresholdLevel.fxml"));
	    			loader.setLocation(getClass().getResource("/gui/MarketingWorkerSalesInArea.fxml"));
	    			pane = loader.load();
	    			MarketingWorkerSalesInAreaController.marketworkercontroller=loader.getController();
	    		
	    			
	    			//marketworkercontroller1.txtlocation.setText(area);//set text of the location in sale catalog
	    			
	    		}
	    		catch (IOException e) {
	    			e.printStackTrace();
	    			return;
	    		}
	    		Scene scene = new Scene(pane);
	    		ClientUI.getStage().setScene(scene);
	    		//ClientUI.getStage().set
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
	 
	 
	 /**

	 The initialize method is a built-in method in JavaFX that is automatically called when the FXML file associated with the controller is loaded.
	 It is used to initialize the elements in the FXML file with default values or data from the application.
	 In this specific implementation, it sets the text of the "welcometxt" and "txtrole" Text objects to welcome the user by name and display their role and the area they are responsible for.
	 @param location the location of the FXML file associated with the controller
	 @param resources the resources associated with the FXML file
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		welcometxt.setText("Welcome Back " +LoginController.userLogin.getFirst_name());
		txtrole.setText("Marketing Worker - " + LoginController.area);
	}
}