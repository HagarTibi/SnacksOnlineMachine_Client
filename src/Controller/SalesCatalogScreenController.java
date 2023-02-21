package Controller;



import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import client.Client;
import client.ClientUI;
import common.MsgHandler;
import common.Sale;
import common.TypeMsg;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**This screen presents the sales collection which a marketing worker can activate and manage */

public class SalesCatalogScreenController implements Initializable  {
	public static SalesCatalogScreenController salesCatalogController;

    @FXML
    private Button Backbt;

    @FXML
    private Button Xbt;

   
    @FXML
    private Button cancel_AllSales;

    @FXML
    private ScrollPane catalogScrollBar;
    
    @FXML
    private ScrollPane SalesToScrollBar;
    
    
    @FXML
    private Button initilizeSales;

    @FXML
    private Button logout;

    @FXML
    private Text welcometxt;
    
    @FXML
    private VBox vboxLeft;
    
    @FXML
    private VBox vboxRight;
    
    @FXML
    private AnchorPane left;
    
    @FXML
    private ScrollPane leftscroll;
    
    public List<Sale> salesBank;
    public static List<Sale> salestoadd;//list of sales that added to the cart
    

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

    The onClickBack method is responsible for handling the onClick event of a "back" button.
    When the button is clicked, it loads the MarketingHomeScreen.fxml file and sets it as the current scene,
    by creating a new AnchorPane object, loading the FXML file, and setting it as the scene of the stage.
    Also, it removes all the elements of "arrsale" list of the client to prepare for the next use.
    @param event the onClick event of the "back" button
    */
    @FXML
    void onClickBack(ActionEvent event) {
    	AnchorPane pane;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/MarketingHomeScreen.fxml"));
			pane = loader.load();
			MarketingManagerHomeController.marketingScreenController=loader.getController();
		}
		
		
		catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Scene scene = new Scene(pane);
		ClientUI.getStage().setScene(scene);
		//ClientUI.getStage().set
		ClientUI.getStage().show();
		Client.arrsale.removeAll(Client.arrsale);//empty the catalog for reload use
    }
    
    
    /*Presenting a catalog of sales from DB*/
    /**

    The AddCardSaleToScrollPaneLeft method is responsible for adding SaleCardManager.fxml to the scroll pane on the left side of the UI.
    It uses a for loop to iterate over the "arrsale" list of the Client class, loads the SaleCardManager.fxml file, creates a new HBox object,
    gets the SaleCardManagerController and calls its setData method to set the sale data, and finally adds the HBox object to the vboxLeft.
    @throws IOException if the load of SaleCardManager.fxml fails
    */
    public void AddCardSaleToScrollPaneLeft(){
		
		try {
			for(int i=0;i<Client.arrsale.size();i++)
			{
				FXMLLoader fxmlLoader=new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("/gui/SaleCardManager.fxml"));//load one sale card
				HBox cardbox=fxmlLoader.load();
				SaleCardManagerController saleCardManager=fxmlLoader.getController();
				saleCardManager.SetData(Client.arrsale.get(i));
				vboxLeft.getChildren().add(cardbox);//add to the scroll pane left	
			}
		}
		catch(IOException e) {System.out.println("the load not success");}
	}
    
    
    /*Presenting sales to initiate and choose your active locations*/
    /**

    The AddCardSaleToScrollPaneRight method is responsible for adding SaleCardToInitiateManager.fxml to the scroll pane on the right side of the UI.
    It first clears all the children of the vboxRight and then uses a for loop to iterate over the "salestoadd" list, loads the SaleCardToInitiateManager.fxml file, creates a new HBox object,
    gets the SaleCardToinitiateController and calls its setData method to set the sale data, and finally adds the HBox object to the vboxRight.
    @throws IOException if the load of SaleCardToInitiateManager.fxml fails
    */
    public void AddCardSaleToScrollPaneRight(){				
    	vboxRight.getChildren().removeAll(vboxRight.getChildren());
    	try {
    		for(int i=0;i<salestoadd.size();i++) {
				FXMLLoader fxmlLoader=new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("/gui/SaleCardToInitiateManager.fxml"));//load one sale card
				HBox cardboxright=fxmlLoader.load();
				SaleCardToinitiateController saleCardToinitiate=fxmlLoader.getController();
				saleCardToinitiate.SetData(salestoadd.get(i));
				vboxRight.getChildren().add(cardboxright);//add to the scroll pane right
    		}	
		}
		catch(IOException e) {System.out.println("the load not success");}
	}
    
    /*updating new area statuses for each sale the manager chose to initiate*/
    /**

    This method is called when the "Initiate All" button is clicked.
    It takes the sales that were selected by the user and sends them to the server to be updated.
    It also displays a successful alert notification to the user.
    @param event the action event that occurred when the button was clicked
    */
    @FXML
    public void onClickInitilizaAll(ActionEvent event) {
    	
    
    	ArrayList<Object>send=new ArrayList<Object>();
    	for(Sale s:salestoadd) {
    		send.add((Object)s);
    	}
    	MsgHandler<Object> msg=new MsgHandler<Object>(TypeMsg.Update_Sale_To_Worker,send);
       	try {
            	ClientUI.chat.accept((Object)msg);

        }catch(Exception e) {
       		System.out.println("fail in accept method");
        		//e.getStackTrace()
        		
        	}
        	Client.arrsale.removeAll(Client.arrsale);//empty the catalog for reload use
    	

    	//sccess notification
    	AnchorPane pane;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/SuccessfulSalesAddingAlert.fxml"));
			pane = loader.load();
			SuccessfulSalesAddingAlertController.successAlert=loader.getController();
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
	 * Clear all the sales that the manager add to the sale cart
	 * @author G-10 */
    @FXML
    public void onClickCancelAll(ActionEvent event) {
    	vboxRight.getChildren().clear();
    	salestoadd.removeAll(salestoadd);
    	Client.arrsale.removeAll(Client.arrsale);
    	MarketingManagerHomeController.marketingScreenController.onClickOrderCatalog(event);
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		salestoadd=new ArrayList<Sale>();
		AddCardSaleToScrollPaneLeft();	
	}

    



}