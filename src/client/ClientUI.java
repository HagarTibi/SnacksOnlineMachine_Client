package client;

import java.io.IOException;

import Controller.MainScaneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ClientUI extends Application {
	public static ClientController chat; //only one instance
	private static Stage stage;



	public static void main(String[] args) throws Exception
	{
		launch(args);
	} // end main

	@Override
	public void start(Stage stage) throws Exception {
		ClientUI.stage = stage;
		startMain();
	}

	public static void startMain() {
		BorderPane pane;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ClientUI.class.getResource("/gui/MainScene.fxml"));
			pane = loader.load();
			MainScaneController.mainController = loader.getController();
		}

		catch (Exception e) {
			e.printStackTrace();
			return;
		}
		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.show();
	}

	public static Stage getStage() {
		return stage;
	}

	public static void setChat(String gettxtfield, int i) {
		chat = new ClientController(gettxtfield,i);
	}

	private void setStage(Stage stage) {
		this.stage = stage;
	}
}
