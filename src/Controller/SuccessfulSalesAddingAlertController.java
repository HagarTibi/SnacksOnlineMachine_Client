package Controller;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import client.ClientUI;
import common.Sale;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
public class SuccessfulSalesAddingAlertController {
	public static SuccessfulSalesAddingAlertController successAlert;
    @FXML
    private Button backMenu;

    @FXML
    private HBox cardhboxpane;

    @FXML
    private ImageView image;

    @FXML
    private Text txtname;
    
    /**

    This method is used to handle the event when the user clicks on the "Back" button.
    It takes the user back to the Marketing Manager home screen.
    @param event The event that triggers the method, in this case the clicking of the "Back" button.
    */
    @FXML
    public void onClickBackMenu(ActionEvent event) {
    	AnchorPane pane;
		try {
			FXMLLoader loader =
					new FXMLLoader();
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
    }

}
