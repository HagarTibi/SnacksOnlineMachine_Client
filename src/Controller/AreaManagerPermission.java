package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
import client.ClientUI;
import common.MsgHandler;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AreaManagerPermission implements Initializable {
	 public static AreaManagerPermission  controller;
    @FXML
    private Button Backbt;

    @FXML
    private Button Xbt;

    @FXML
    private AnchorPane anchorPaneIntoScroll;

    @FXML
    private Button logout;

    @FXML
    private ScrollPane scrollpro;

    @FXML
    private VBox vboxside;
    
    public ArrayList<Object> listInArea;

    @FXML
    private BorderPane workhere;

    @FXML
    private Text txtsuccess;
    
    /**
	adds cards of requests users to the scroll pane.
	card contains the user name,id and his request button to approve
     */
    public void AddCardSaleToScrollPane(){
    	if(Client.arrrequestsusers.size()!=0) {
    		
				listInArea=Client.arrrequestsusers;
				//vboxleftside.getChildren().removeAll();
				try {
					scrollpro.setVisible(true);
		    	//
					for(int i=0;i<listInArea.size();i=i+2)
					{
					FXMLLoader fxmlLoader=new FXMLLoader();
					fxmlLoader.setLocation(getClass().getResource("/gui/ReuestUserToCustomerCard.fxml"));//load one customer card
					HBox cardbox=fxmlLoader.load();
					RequestUserCardController requestuser=fxmlLoader.getController();

					requestuser.SetData((String)listInArea.get(i),(String)listInArea.get(i+1));
					vboxside.getChildren().add(cardbox);//add to the scroll pane left	
					}
				}

    		catch(IOException e) {System.out.println("the load not success");}
				}	
    	else {
					Alert alertPermission = new Alert(AlertType.ERROR);
					alertPermission.setTitle("Error not more request");
					alertPermission.setContentText("Not exist more user request");

			         alertPermission.showAndWait();
				}		
		}
   
/**

sets the text of the txtsuccess textfield to the input string and makes it visible.
@param str the string to be displayed in the txtsuccess textfield
*/
 public void feedBack(String str,String send) {
    	txtsuccess.setVisible(true);
	    txtsuccess.setText(str);
	    Alert alertas = new Alert(AlertType.INFORMATION);
        alertas.setTitle("The user get message");
        alertas.setContentText(send);
        alertas.showAndWait();
        return;

    }

/**

Handles the event of going back to the manager menu screen.
Loads the managerScreen.fxml file, creates a new scene with the loaded AnchorPane,
and sets the new scene on the stage.
@param event the event that triggers going back to the manager menu screen
*/
    @FXML
    void BackToAreaManagerMenu(ActionEvent event) {
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
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		txtsuccess.setVisible(false);
		vboxside.getChildren().removeAll(vboxside.getChildren());
		AddCardSaleToScrollPane();

	}
}

