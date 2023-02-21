package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientUI;
import common.MsgHandler;
import common.TypeMsg;
import javafx.application.Platform;
import javafx.event.ActionEvent;
/*This Screen is the Marketing department manger home screen
 * in this screen he can access to the sales to initialize*/
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/*The first screen that a marketing manager as seeing after he logged in*/
public class MarketingManagerHomeController implements Initializable{
	public static MarketingManagerHomeController marketingScreenController;

	@FXML
    private Button Xbt;

    @FXML
    private Button logout;

    @FXML
    private Button newSale;

    @FXML
    private Button orderCatalog;

    @FXML
    private Text txtrole;

    @FXML
    private Text welcometxt;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	welcometxt.setText(" Welcome Back " + LoginController.userLogin.getFirst_name());
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

Handles the event of generating a new sale.
Loads the GenerateNewSale.fxml file, creates a new scene with the loaded AnchorPane,
and sets the new scene on the stage.
@param event the event that triggers generating a new sale
*/

    @FXML
    void onClickGenerateNewSale(ActionEvent event) {
    		AnchorPane pane;
    		try {
    			FXMLLoader loader = new FXMLLoader();
    			//loader.setLocation(getClass().getResource("../gui/AreaManagerThresholdLevel.fxml"));
    			loader.setLocation(getClass().getResource("/gui/GenerateNewSale.fxml"));
    			pane = loader.load();
    			GenerateNewSaleController.newSale=loader.getController();
    		
    			    			
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

Handles the event of displaying the sales catalog.
Sends a request to the server to import all existing sales.
Then it loads the SalesCatalogScreen.fxml file, creates a new scene with the loaded AnchorPane,
and sets the new scene on the stage.
@param event the event that triggers displaying the sales catalog
*/
    @FXML
    void onClickOrderCatalog(ActionEvent event) {
    	MsgHandler<Object> msg=new MsgHandler<Object>(TypeMsg.Import_Excisting_Sales,null);
    	try {
        	ClientUI.chat.accept((Object)msg);

    	}catch(Exception e) {
    		System.out.println("fail in accept method");
    		//e.getStackTrace()
    		return;
    	}

    	AnchorPane pane;
		try {
			FXMLLoader loader = new FXMLLoader();
			//loader.setLocation(getClass().getResource("../gui/AreaManagerThresholdLevel.fxml"));
			loader.setLocation(getClass().getResource("/gui/SalesCatalogScreen.fxml"));
			pane = loader.load();
			SalesCatalogScreenController.salesCatalogController=loader.getController();
			
			
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
}

