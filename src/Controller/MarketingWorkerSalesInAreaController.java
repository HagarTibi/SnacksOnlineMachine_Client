
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class MarketingWorkerSalesInAreaController implements Initializable {

    public static MarketingWorkerSalesInAreaController marketworkercontroller;


    @FXML
    private AnchorPane anchorPaneIntoScroll;
    @FXML
    private Button Backbt;

    @FXML
    private Button Xbt;

    @FXML
    private ImageView imgonscroll;

    @FXML
    private Button logout;

    @FXML
    private ScrollPane scrollsales;



    @FXML
    private VBox vboxleftside;

    @FXML
    private BorderPane workhere;
    @FXML
    private Text txtSuccessfulUpdate;
    public String area;
    
    private List<Sale> listInArea;
    
    /**

    The initialize method is a built-in method in JavaFX that is automatically called when the FXML file associated with the controller is loaded.
    It is used to initialize the elements in the FXML file with default values or data from the application.
    In this specific implementation, it sets the value of the area variable to the area stored in the LoginController.
    It also sets the visibility of the "scrollsales" and "txtSuccessfulUpdate" elements to false.
    Finally, it calls the PresentSales method.
    @param location the location of the FXML file associated with the controller
    @param resources the resources associated with the FXML file
    */
	@Override
	public void initialize(URL location, ResourceBundle resources) {


		area=LoginController.area;	
		scrollsales.setVisible(false);
		txtSuccessfulUpdate.setVisible(false);

		PresentSales();
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
	 * This Method Present initinalize the scrollpane and adding all the sales
	 * In specific area
	 * @author G-10 */
    void PresentSales() {	
    	    vboxleftside.getChildren().removeAll(vboxleftside.getChildren());
    		AddCardSaleToScrollPane(area);//add all sale to scrollpane;
    	}
	/**
	 * This Method logout the client and back to login screen
	 * @author G-10 */
    @FXML
    void BackToMarketingWrokerMenu(ActionEvent event) {//change screen to marketing worker menu
    	
  		AnchorPane pane;
		try {
			FXMLLoader loader = new FXMLLoader();
			
			loader.setLocation(getClass().getResource("/gui/MarketingWorkerMain.fxml"));
			pane = loader.load();
			MarketingWorkerMenuController.marketworkermenucontroller=loader.getController();
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
	 * This Method give a feedback after change of sale status in specific area
	 * @param msg handler from server
	 * @author G-10 */
    public void feedBack(MsgHandler<Object> msg) {
	    txtSuccessfulUpdate.setVisible(true);
	    txtSuccessfulUpdate.setText(msg.getType().toString());
	  
	    txtSuccessfulUpdate.setFill(Color.GREEN);
    }
	/**
	 * This Method Adding all sale that define to 
	 * this area into the scroll pane
	 * @param area
	 * @author G-10 */
	public void AddCardSaleToScrollPane(String area){

				listInArea=Client.arrsale;
				if(listInArea!=null) {
				//vboxleftside.getChildren().removeAll();
				try {
					scrollsales.setVisible(true);
		    	    imgonscroll.setVisible(false);
					for(int i=0;i<listInArea.size();i++)
					{
					FXMLLoader fxmlLoader=new FXMLLoader();
					fxmlLoader.setLocation(getClass().getResource("/gui/SaleCard.fxml"));//load one sale card
					HBox cardbox=fxmlLoader.load();
					SaleCardController salecardcontroller=fxmlLoader.getController();
					
					salecardcontroller.SetData(listInArea.get(i),area);
			
					salecardcontroller.CheckActiveByNumberInArea(listInArea.get(i),area);
					vboxleftside.getChildren().add(cardbox);//add to the scroll pane left	
					}
				}

				catch(IOException e) {System.out.println("the load not success");}
				}
				else {
					Alert alertEmail = new Alert(AlertType.ERROR);
					alertEmail.setTitle("Error Sale In Area");
					alertEmail.setContentText("Not exist sales in area:"+ area);
					
					alertEmail.showAndWait();
					return;
				}
					
	}
}