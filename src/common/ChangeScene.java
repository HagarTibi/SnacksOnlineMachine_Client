package common;

import Controller.OrdersReportController;
import client.ClientUI;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import static Controller.OrdersReportController.monthlyOrderReport;

/**
 * Entity for loading different screens of gui
 */

public class ChangeScene {

    public void changeScreen(Stage primaryStage, String path)
    {
        AnchorPane pane;
        try {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


}
