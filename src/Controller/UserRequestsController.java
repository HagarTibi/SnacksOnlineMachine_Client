package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import client.Client;
import client.ClientUI;
import common.Customers;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class UserRequestsController implements Initializable{
	 public static UserRequestsController userRequestcontroller;

    @FXML
    private Button Xbt;

    @FXML
    private ComboBox<String> comboboxselectAreaManager;

    @FXML
    private Button logout;

    @FXML
    private Button searchbutton;

    @FXML
    private Button sendbutton;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPhoneNumber;

    @FXML
    private TextField txtcreditcardcvv;

    @FXML
    private TextField txtcreditcardnumber;

    @FXML
    private ComboBox<String> monthExp;
    
    @FXML
    private ComboBox<String> yearExp;

    @FXML
    private TextField txtuseridtosearch;

    @FXML
    private BorderPane workhere;
    private ObservableList<String> months;
    private ObservableList<String> years;
    private ObservableList<String> areas;
    private Customers newCustomer;
    public boolean sendsuccess;
    public boolean importuser;
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
     * Handles the event when the search button is clicked.
     * Fetches the user details for the given user ID and displays them in the corresponding text fields.
     * 
     * @param event the action event that triggered this method
     */
    @FXML
    void OnClickSearchUser(ActionEvent event) {
    	
    	clearField();
    	String userid=txtuseridtosearch.getText();
    	if(LegalNumber(userid)) {
    		//need send request to bring the user detail
    		ArrayList<Object> arruser=new ArrayList<>();
    		arruser.add((Object)userid);
    		importuser=false;
    		ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.import_user_detail,arruser));
    		if(importuser) {
    			txtName.setText(Client.user.getFirst_name()+" "+Client.user.getLast_name());
    			txtPhoneNumber.setText(Client.user.getPhoneNumber());
    			txtEmail.setText(Client.user.getEmailAddress());
    		}
    		else {
    			Alert alertNotFound = new Alert(AlertType.ERROR);
    			alertNotFound.setTitle("User wasn't Found");
    			alertNotFound.setContentText("The user id  not legal id / have role");

    			alertNotFound.showAndWait();

    			}
      		}
    		else {
    			Alert alertNotLegalNumber = new Alert(AlertType.ERROR);
    			alertNotLegalNumber.setTitle("Not legal number");
    			alertNotLegalNumber.setContentText("The user id not legal number (Only digit please)");

    			alertNotLegalNumber.showAndWait();

    		
    	}
 
    }
    /**
     * Check if the number is legal string only letter with numbers and special
     * char like :,!%.- ' ' (validation check)
     * @param String str that check every text field of user detail
     * @author G-10
     */
    
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
     * Handles the event when the send button is clicked.
     * Validates the input fields and sends a request to add the user to the customer request table, along with the selected area manager.
     * Displays an alert message to confirm the request has been sent.
     * @author G-10
     * @param event the action event that triggered this method
     */
    @FXML
    void OnClickSendUserToAreaManager(ActionEvent event) {

    	//need to send this user to the area manager request.
    	if(LegalString(txtName.getText())&&LegalNumber(txtPhoneNumber.getText())&&LegalNumber(txtcreditcardnumber.getText())&&txtcreditcardnumber.getText().length()==16&&LegalNumber(txtcreditcardcvv.getText())&&txtcreditcardcvv.getText().length()==3&&LegalString(txtEmail.getText())&&monthExp.getValue()!=null&&yearExp.getValue()!=null&&comboboxselectAreaManager.getValue()!=null) {
    		newCustomer=new Customers(Client.user,buildCreditCardNumString(txtcreditcardnumber.getText()),buildCreditCardExpString(),txtcreditcardcvv.getText(),false);
    		//add user to table of customer_reuest with the area of treatment
    		ArrayList<Object> arrcustomerreqdetail=new ArrayList<>();
    		arrcustomerreqdetail.add((Object)newCustomer);
    		arrcustomerreqdetail.add((Object)comboboxselectAreaManager.getValue());
    		sendsuccess=false;
    		ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.addToCustomerRequest,arrcustomerreqdetail));
        	
    		Alert alert = new Alert(AlertType.CONFIRMATION);
    		alert.setTitle("Request sent to Area Manager");
    		String str;
    		if(sendsuccess) {str="the user added to customer reuests";}
    		else { str="the user already in the customer request waiting for manager approve";}
    		String areaManagerName = comboboxselectAreaManager.getValue();
    		alert.setContentText("The request has been sent to the Area Manager \n" + str);
    		comboboxselectAreaManager.setValue(null);

    		ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.OK_DONE);
    		alert.getButtonTypes().setAll(buttonTypeOk);

    		Optional<ButtonType> result = alert.showAndWait();
    		if (result.get() == buttonTypeOk) {
    		    // close the dialog
    		    alert.close();
    		}
            
            clearField();
            txtuseridtosearch.clear();
            return;
    		}

    	else {  			    
			if(!LegalString(txtName.getText())) {//if the full name not legal
					Alert alertName = new Alert(AlertType.ERROR);
					alertName.setTitle("Name field empty");
					alertName.setContentText("The name not legal");
					alertName.showAndWait();
					return;
			}
			if(!LegalNumber(txtPhoneNumber.getText())){
				Alert alertPhone = new Alert(AlertType.ERROR);
				alertPhone.setTitle("Phone not legal or field empty");
				alertPhone.setContentText("The phone number not legal");
				
				alertPhone.showAndWait();
				return;
			}
			if(!LegalNumber(txtcreditcardnumber.getText())||txtcreditcardnumber.getText().length()!=16){
				Alert alertcreditcardnumber = new Alert(AlertType.ERROR);
				alertcreditcardnumber.setTitle("This number card not legal or field empty");
				alertcreditcardnumber.setContentText("Please fill card number currect (only 16 digit)");
				
				alertcreditcardnumber.showAndWait();
				return;
			}	
			if(monthExp.getValue()==null) {
			Alert alertMonth = new Alert(AlertType.ERROR);
			alertMonth.setTitle("month exp not legal or field empty");
			alertMonth.setContentText("Please fill month exp field");
			alertMonth.showAndWait();
			return;

			}
			if(yearExp.getValue()==null) {
				Alert alertYear = new Alert(AlertType.ERROR);
				alertYear.setTitle("year exp not legal or field empty");
				alertYear.setContentText("Please fill year exp field");
				alertYear.showAndWait();
				return;

			}
			
			if(!LegalNumber(txtcreditcardcvv.getText())||txtcreditcardcvv.getText().length()!=3){
				Alert alertcvv = new Alert(AlertType.ERROR);
				alertcvv.setTitle("Error");
				alertcvv.setContentText("This cvv card number not legal or field empty (only 3 digit)"
						+ "");
				
				alertcvv.showAndWait();
				return;
			}	
			if(!LegalString(txtEmail.getText())){
				Alert alertEmail = new Alert(AlertType.ERROR);
				alertEmail.setTitle("Email not legal or field empty");
				alertEmail.setContentText("The id is not found or other problem accured");
				
				alertEmail.showAndWait();
				return;
			}	
		
		if(comboboxselectAreaManager.getValue()==null) {
			Alert alertarea= new Alert(AlertType.ERROR);
			alertarea.setTitle("Area is not chosen");
			alertarea.setContentText("Please choose an area to send");

			alertarea.showAndWait();
			return;
		}
	}
 }
    /**
     * Handles with feedback to approve button
     
     * Displays an alert message to confirm the approve send
     * @author G-10
     * @param event the action event that triggered this method
     */
    public void FeedBackSend(String str) {	//Alert of user added to customers request or already exist there

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Request sent to Area Manager");
        String areaManagerName = comboboxselectAreaManager.getValue();
        alert.setContentText("The request has been sent to the Area Manager: " + areaManagerName + "\n" + str);
        comboboxselectAreaManager.setValue(null);
        alert.showAndWait();
        clearField();
        txtuseridtosearch.clear();
    }
    /**
     * Check if the number is legal number (validation check)
      * @param String str that check every text field of user detail
     * @author G-10
     */
	 public static boolean LegalNumber(String str)//check if string contain only digits
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
	 /**

	 *This method is used to initialize all the comboboxes in the application.
	 *the comboboxes monthExp, yearExp, and comboboxselectAreaManager accordingly.
	 *The months ArrayList contains all the months from January to December,
	 *the years ArrayList contains all the years from 2023 to 2030,
	 *and the areas ArrayList contains the values "North", "South", and "UAE".
	 @author G-10
	 */
	 @FXML
	    public void initiateComboBox() {//initinalize all the combobox
	    	ArrayList<String> month = new ArrayList<>();
	    	for(int i=1;i<13;i++) {
	    		if(i<10)
	    			month.add('0'+String.valueOf(i));
	    		else
	    			month.add(String.valueOf(i));	
	    	}
	    	months=FXCollections.observableArrayList(month);
	    	
	    	ArrayList<String> year=new ArrayList<>();
	    	for(int i=2023;i<2031;i++) {
	    		year.add(String.valueOf(i));
	    	}
	    	years=FXCollections.observableArrayList(year);
	    	ArrayList<String>area=new ArrayList<>();
	    	area.add("North");
	    	area.add("South");
	    	area.add("UAE");
	    	areas=FXCollections.observableArrayList(area);
	    		
	    	
	    	
	    	//initiate comboBoxes
	    	monthExp.setItems(months);
	    	yearExp.setItems(years);
	    	comboboxselectAreaManager.setItems(areas);
	    }
	 /**
	 This method is used to build a string representation of a credit card expiration date in the format "MM/YYYY".

		@author G-10
	 */
	 	public String buildCreditCardExpString() {
	 		//build exp like month/year
	 		String creditCard;
	 		creditCard=monthExp.getValue()+"/"+yearExp.getValue();
	 		return creditCard;
	 	}
	 	
	 	/**
	 	This method is used to format a given credit card number by adding a space every four digits.
	 	The resulting string, which is the formatted credit card number, is returned.
	 	@author G-10
	 	@param number a string representing an unformatted credit card number
	 	 */

	 	public String buildCreditCardNumString(String number) {

	 		StringBuilder sb = new StringBuilder(number);
	 		for (int i=4; i<sb.length(); i+=5)
	 		    sb.insert(i, ' ');
	 		return sb.toString();
	 	}

	 	/**
	 	Clears all fields of the form as per user request.
	 	@author G-10
	 	*/
	 	public void clearField() {//clear all the field of form user request
	    	 
	    	comboboxselectAreaManager.setValue(null);
	    	yearExp.setValue(null);
	    	monthExp.setValue(null);

	    	txtcreditcardcvv.clear();
	    	txtEmail.clear();
	    	txtName.clear();
	    	
	    	txtPhoneNumber.clear();
	    	txtcreditcardnumber.clear();
	 	}
	 	/**
	 	Handles the event when the user clicks the "back" button.
	 	@param event the event that triggered this method
	 	@author G-10
	 	*/
	    @FXML
	    void OnClickBack(ActionEvent event) {

			 AnchorPane pane;
				try {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("/gui/CostumerServiceMain.fxml"));
					pane = loader.load();
					CustomerServiceController.controller = loader.getController();

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

	    Initializes the elements of the form and sets their properties.
	    @author G-10
	    */
		@Override
		public void initialize(URL location, ResourceBundle resources) {
			txtEmail.setEditable(false);
			txtPhoneNumber.setEditable(false);
			txtName.setEditable(false);
			initiateComboBox();

		
		}
}