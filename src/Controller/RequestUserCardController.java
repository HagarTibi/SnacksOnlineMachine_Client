package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


import client.Client;
import client.ClientUI;
import common.MsgHandler;
import common.Sale;
import common.TypeMsg;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class RequestUserCardController implements Initializable{


    @FXML
    private Button buttonon;

    @FXML
    private HBox cardhboxpane;

    @FXML
    private Text txtid;

    @FXML
    private Text txtname;


/**

Handles the event of approving a user request.
It sends the user's ID and name to the server to be approved and then sends a request to import the remaining requests for the specific area.
Then it will load the ManagerPermissionsUsers.fxml file and create a new scene with the loaded AnchorPane and set the new scene on the stage.
If there are no more requests to approve it will redirect to the manager menu screen.
@param event the event that triggers approving the user request
*/
    @FXML
    void OnClickAprroveUser(ActionEvent event) {
    //
    	ArrayList<String> userrequestdetail=new ArrayList<>();
    	userrequestdetail.add(txtid.getText());
    	userrequestdetail.add(txtname.getText());
    	ClientUI.chat.accept((Object)new MsgHandler<String>(TypeMsg.ApprovedUserCustomer,userrequestdetail));
    	ArrayList<String> area=new ArrayList<>();
    	area.add(LoginController.area);
    	ClientUI.chat.accept((Object)new MsgHandler<>(TypeMsg.ImportUserRequestToSpecficArea,area));
    	AreaManagerPermission.controller.feedBack("The system send to "+txtname.getText()+" approved","The system send to "+ txtname.getText()+" id:"+txtid.getText()+" approved to be customer in Ekrut ");
    	if(Client.arrrequestsusers.size()!=0) {
    		
        AnchorPane pane;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/gui/ManagerPermissionsUsers.fxml"));
            pane = loader.load();
            AreaManagerPermission.controller = loader.getController();

            
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Scene scene = new Scene(pane);
        ClientUI.getStage().setScene(scene);
        ClientUI.getStage().show();

        
    	}
    	else {
    		
			Alert alertPermission = new Alert(AlertType.ERROR);
			alertPermission.setTitle("Error t");
			alertPermission.setContentText(txtname.getText()+" Approve but not have more request");
	         alertPermission.showAndWait();
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

    }

/**

sets the text of the txtname and txtid textfields to the input id and name respectively.
@param id the id to be displayed in the txtid textfield
@param name the name to be displayed in the txtname textfield
*/
    public void SetData(String id,String name) {//set the card
        txtname.setText(name);
        txtid.setText(id);
    }


/**

This method is automatically called by JavaFX when the corresponding FXML file is loaded.
It can be used to perform any setup for the controller before the GUI is displayed to the user.
@param location The location of the FXML file that was used to create this controller
@param resources The resources that were used to create this controller
*/

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}