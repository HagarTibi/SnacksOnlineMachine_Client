package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import client.Client;
import client.ClientUI;
import common.MsgHandler;
import common.Subscribe;
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
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class SubscriberRequestsController implements Initializable {
	public static SubscriberRequestsController subReqController;

    @FXML
    private Button Backbt;

    @FXML
    private Button Xbt;

    @FXML
    private Button logout;

    @FXML
    private Button searchbtn;

    @FXML
    private TextField txtCreditNum;

    @FXML
    private TextField txtEmail;


    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtuserID;

    @FXML
    private Button updateMember;

    @FXML
    private BorderPane workhere;
    
    private Subscribe sub;
    

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
	 * This Method back to menu of Customer Service worker
	 * @author G-10 */
    @FXML
    void onClickBack(ActionEvent event) {
    	AnchorPane pane;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/CostumerServiceMain.fxml"));
			pane = loader.load();
			CustomerServiceController.controller=loader.getController();
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
    /*searching user id from DB and display his details on the name, phone and email textFields */
    @FXML
    void OnClickSearchCustomer(ActionEvent event) {
    	//need to bring all his detail (credit card need add in DB)
    	String userid=txtuserID.getText();
    	if(LegalId(userid)) {
    		//need send request to bring the user detail
    		ArrayList<Object> arruser=new ArrayList<>();
    		arruser.add((Object)userid);
    		ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.Import_customer_detail,arruser));//with avishai
    	if(Client.cusTosub.getCustomer_id()!=null) {
    		if(Client.cusTosub.getCustomer_id().equals("0000")) {
    			System.out.println("user is not found!");
    			Alert alertNotFound = new Alert(AlertType.ERROR);
    			alertNotFound.setTitle("Customer wasn't Found");
    			alertNotFound.setContentText("The id is not legal customer id");

    			alertNotFound.showAndWait();
				txtuserID.clear();
				txtName.clear();
				txtPhone.clear();
				txtCreditNum.clear();
				txtEmail.clear();
				return;
    		}
    		else {	
    		txtName.setText(Client.cusTosub.getFirst_name()+" "+Client.cusTosub.getLast_name());
    		txtPhone.setText(Client.cusTosub.getPhoneNumber());
    		txtCreditNum.setText(Client.cusTosub.getCredit_card_num());
    		txtEmail.setText(Client.cusTosub.getEmailAddress());
    		}
    	}
    	else {
    	System.out.println("return null");}
    	}
		else {
			Alert alertNotLegalNumber = new Alert(AlertType.ERROR);
			alertNotLegalNumber.setTitle("Not legal number");
			alertNotLegalNumber.setContentText("The customer id not legal number (Only digit please)");

			alertNotLegalNumber.showAndWait();
		}
    }
    /**
	 * This Method check if the customer worker put legal id 
	 * (Validation)
	 * @author G-10 */
	 public static boolean LegalId(String str)
	    {
	        // Return false if the string
	        // has empty or null
	        if (str.equals("") || str.equals(null)) {
	            return false;
	        }
	        ArrayList<Character> alphabets = new ArrayList<Character>();
	        String alpha = "0123456789";
	        for(int i=0;i<alpha.length();i++)
	        {
	            alphabets.add(alpha.charAt(i));
	        }
	        // Traverse the string from
	        // start to end
	        for (int i = 0; i < str.length(); i++) {
	            // Check if the specified
	            // character is not a letter then
	            // return false,
	            // else return true
	            if (!alphabets.contains(str.charAt(i))) {
	                return false;
	            }
	        }
	        return true;
	    }
	 /*verify that txtField is filled properly*/
	    public static boolean LegalString(String str)
	    {
	        // Return false if the string
	        // has empty or null
	        if (str.equals("") || str.equals(null)) {
	            return false;
	        }
	        ArrayList<Character> alphabets = new ArrayList<Character>();
	        String alpha = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ%:!@,. -0123456789";
	        for(int i=0;i<alpha.length();i++)
	        {
	            alphabets.add(alpha.charAt(i));
	        }
	        // Traverse the string from
	        // start to end
	        for (int i = 0; i < str.length(); i++) {
	            // Check if the specified
	            // character is not a letter then
	            // return false,
	            // else return true
	            if (!alphabets.contains(str.charAt(i))) {
	                return false;
	            }
	        }
	        return true;
	    }
	 
	    /**
		 * This Method upgrade the customer that the Customer service search
		 * event handler by click on Update to subcriber button
		 * @author G-10 */
	 @FXML
	    void OnClickUpdateToMember(ActionEvent event) {
	    	//need to send this user to the area manager request.
	    	if(LegalString(txtName.getText())&&LegalString(txtPhone.getText())&&LegalString(txtCreditNum.getText())&&LegalString(txtEmail.getText())&&!Client.cusTosub.isIs_subscriber()) {
	    		Client.cusTosub.setIs_subscriber(true);
	    		sub=new Subscribe(Client.cusTosub,null,"0","0");
	    		ArrayList<Object> updateCus=new ArrayList<>();
	    		updateCus.add((Object)Client.cusTosub);
	    		ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.update_customer_to_Sub,updateCus));
	    		ArrayList<Object> updateSubTable=new ArrayList<>();
	    		updateSubTable.add((Object)sub);
	    		ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.Insert_customer_to_Subscribers_tab,updateSubTable));
	    		Alert alertSuccess = new Alert(AlertType.CONFIRMATION);
				alertSuccess.setTitle("New Subscriber Added");
				alertSuccess.setContentText("New Subscriber added successfully!");
				
				ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.OK_DONE);
				alertSuccess.getButtonTypes().setAll(buttonTypeOk);

				Optional<ButtonType> result = alertSuccess.showAndWait();
				if (result.get() == buttonTypeOk) {
				    // close the dialog
					alertSuccess.close();
				}
				txtuserID.clear();
				txtName.clear();
				txtPhone.clear();
				txtCreditNum.clear();
				txtEmail.clear();
				return;
	    	}
	    	else {  			    
				if(!LegalString(txtName.getText())) {
						Alert alertName = new Alert(AlertType.ERROR);
						alertName.setTitle("Name field empty");
						alertName.setContentText("The id is not found or other problem accured");

						alertName.showAndWait();
						return;
				}
				if(!LegalString(txtPhone.getText())){
					Alert alertPhone = new Alert(AlertType.ERROR);
					alertPhone.setTitle("Phone field empty");
					alertPhone.setContentText("The id is not found or other problem accured");
					
					alertPhone.showAndWait();
					return;
				}
				if(!LegalString(txtEmail.getText())){
					Alert alertEmail = new Alert(AlertType.ERROR);
					alertEmail.setTitle("Email field empty");
					alertEmail.setContentText("The id is not found or other problem accured");
					
					alertEmail.showAndWait();
					return;
				}
				if(Client.cusTosub.isIs_subscriber()) {
					Alert alertAlreadySub = new Alert(AlertType.ERROR);
					alertAlreadySub.setTitle("Already Subscriber!");
					alertAlreadySub.setContentText("This user is already subscriber!!");
					
					alertAlreadySub.showAndWait();
					return;
				}
	    	}
	 	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		txtName.setEditable(false);
		txtPhone.setEditable(false);
		txtCreditNum.setEditable(false);
		txtEmail.setEditable(false);
		
	}
	 
	 
	 
	 
}