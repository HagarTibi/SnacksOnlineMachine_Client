package Controller;

import client.Client;
import client.ClientUI;

import common.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;


import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.org.apache.bcel.internal.generic.InstructionConstants.Clinit;

/** Controller class for the Main Scane screen of the client application.
 * @author G-10
 */
public class MainScaneController implements Initializable { 

		public static MainScaneController mainController;
		public static boolean IpisEqualToServerIp;
		static class Delta {
			double x, y;
		}
	    @FXML
	    private Button Xbt;

	    @FXML
	    private Button btnConnect;

	    @FXML
	    private Label txtError;

	    @FXML
	    private Label txtServerIP;

	    @FXML
	    private TextField txtfieldserveip;



	/** Event handler for the show login button.
	 * It is responsible for validating the server IP input and establishing a connection to the server.
	 * If the connection is successful, the login screen is displayed.
	 *
	 * @param event the action event that triggers this handler
	 */
	    @FXML
	    void ShowLogIn(ActionEvent event) {
			txtError.setVisible(false);
		 	if (!validateIp()) {
				 txtError.setVisible(true);
				 txtError.setText("You must enter Legal IP");
				 txtError.setAlignment(Pos.CENTER);
				 txtfieldserveip.setPromptText("Enter Legal IP");
				 return;
			 }

			MsgHandler connectToServer = new MsgHandler(TypeMsg.Request_connect, null);
			 try{
				ClientUI.setChat(txtfieldserveip.getText(),5555);
				
				//txtError.setVisible(true);
					//txtError.setText("Server is disconectted \nor IP is wrong");
				ClientUI.chat.accept((Object)connectToServer);


			 } catch (Exception e){
		
				 txtError.setVisible(true);
				 txtError.setText("Can't connect to the server");
				 }
			Client.configuration.clear();
			setConfiguration();
			AnchorPane pane;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/gui/Login.fxml"));
				pane = loader.load();
				LoginController.controller = loader.getController();

			}
			catch (IOException e) {
				e.printStackTrace();
				return;
			}
			Scene scene = new Scene(pane);
			Platform.runLater(() -> {
				ClientUI.getStage().setScene(scene);
				ClientUI.getStage().show();

				// Get the size of the screen
				Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

				// Calculate the center of the screen
				double x = screenBounds.getMinX() + screenBounds.getWidth() / 2 - ClientUI.getStage().getWidth() / 2;
				double y = screenBounds.getMinY() + screenBounds.getHeight() / 2 - ClientUI.getStage().getHeight() / 2;

				// Set the stage at the center of the screen
				ClientUI.getStage().setX(x);
				ClientUI.getStage().setY(y);
			});
			ClientUI.getStage().show();
	    }

	void setConfiguration(){

		StringBuilder sb = new StringBuilder();
		try {
			List<String> lines = Files.readAllLines(Paths.get("C:/Users/classroom/Desktop/ekrut/configuration.txt"));
			for (String line : lines) {
				sb.append(line);
			}
			String[] temp = sb.toString().split(":", 0);
			if (temp[1].contains("OL")){
				Client.configuration.add(temp[1]);
				Client.configuration.add("");
				Client.configuration.add("");
			}
			else {
				Client.configuration.add(temp[1]);
				Client.configuration.add(temp[3]);
				Client.configuration.add(temp[5]);
				Client.configuration.add(temp[7]);
			}
		} catch (IOException e) {
			txtError.setVisible(true);
			txtError.setText("Can't set configuration");
		}

	}

	/**
	 * Validates the IP address entered in the text field.
	 *
	 * @return true if the IP address is valid, false otherwise.
	 */
	private boolean validateIp() {
		String IPV4_PATTERN =
				"^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";
		Pattern pattern = Pattern.compile(IPV4_PATTERN);
		Matcher matcher = pattern.matcher(txtfieldserveip.getText());
		if (txtfieldserveip.getText().equals("") || !(matcher.matches())){
			return false;
		}
		return true;
	}


	/**
	 * Initializes the controller class.
	 * It is used to initialize the controller class and perform any necessary setup for the UI elements
	 * @param url the url
	 * @param resourceBundle the resource bundle
	 */
	@Override
	    public void initialize(URL url, ResourceBundle resourceBundle) {
			IpisEqualToServerIp=true;
	    	txtError.setVisible(false);
	    }

	/**
	 * Event handler for the disconnect button.It sends a disconnect request to the server.
	 *
	 * @param event the action event that triggers this handler
	 */
	@FXML
	void disconnectClient(ActionEvent event)  {
		System.exit(0);
	}
}


