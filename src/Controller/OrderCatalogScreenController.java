package Controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import client.Client;
import client.ClientUI;
import common.ItemInCatalog;
import common.ItemInOrder;
import common.MsgHandler;
import common.Order;
import common.Sale;
import common.Subscribe;
import common.TypeMsg;
import javafx.application.Platform;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

//Screen for the making of the order - presented to the customer/subscriber 
//after he press the "new order" button (in EK) or after he presses the "continue to order" button
// from the OL main . 
// It handles the information about the order and the cart if items

public class OrderCatalogScreenController implements Initializable {

	public static OrderCatalogScreenController orderCatalogScreenController;

	@FXML
    private Text errorMsg;
	
	@FXML
    private Button Backbt;

    @FXML
    private Button Xbt;
    
    @FXML
    private Button CancelOrderBtn;

    @FXML
    private Button CheckoutBtn;

    @FXML
    private Button cancel_item;

    @FXML
    private AnchorPane catalogScrollBar;

    @FXML
    private Button decreaseAmount;

    @FXML
    private Text errorTxt;

    @FXML
    private Button increaseAmount;

    @FXML
    private Button logout;

    @FXML
    private Text welcometxt;

    @FXML
    private Text updatedSum;
    
    @FXML
    private VBox catalogItemsLayout;
    
    @FXML
    private VBox cartLayout;
    
    @FXML
    private TextFlow pathEK;

    @FXML
    private TextFlow pathOL;
    
    @FXML
    private AnchorPane disscountPane;
    
    @FXML
    private ImageView saleImg;

    @FXML
    private Text saleText;
    
    @FXML
    private Text EK_cust_sub;

    @FXML
    private Text OL_cust_sub;
    
    public ArrayList <ItemInCatalog> catalog;
    public static float final_price;
 	public static String role_screen; //have to be static. Customer --> "ClientScreen"  ,  Subscriber --> SubscriberScreen
 	public static String area;
 	public static float discount;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		discount = 1;
		role_screen = LoginController.userLogin.getRole().toString();
		saleText.setVisible(false);
		ArrayList<Object> msg = new ArrayList<>();
		switch (Client.configuration.get(0).toString()) {
		case "EK":
			pathEK.setVisible(true);
			pathOL.setVisible(false);
			area = Client.configuration.get(1).toString();
			if (role_screen == "SubscriberScreen")
				EK_cust_sub.setText(">Subscriber Menu");
			msg.add(Client.configuration.get(2).toString());
			break;
		case "OL":
			pathEK.setVisible(false);
			pathOL.setVisible(true);
			if (role_screen == "SubscriberScreen")
				OL_cust_sub.setText(">Subscriber Menu");
			if (OLMainController.orderType == "REMOTE_PICKUP") {
				String strToParse = OLMainController.machine_info; 	// string of format "Area Location machine_id"
    			String[] areaLocationID = strToParse.split(" ");
    			area = areaLocationID[0];
				msg.add(areaLocationID[2]);
			}
			if (OLMainController.orderType == "REMOTE_DELIVERY") {
				area = OLMainController.machine_info; 				// string of format "Area"
			}
			break;
		default:
			break;
		}
		
		if (!Client.arrsale.isEmpty() || !SubscriberScreenController.made_first_order)
			// if there is sales in DB or the subscriber didn't made first order yet call method to handle it
			findAreaSale();
		
		if (!(OLMainController.orderType == "REMOTE_DELIVERY"))
			ClientUI.chat.accept(new MsgHandler<Object>(TypeMsg.Import_real_time_amounts,msg));
		else
			Client.itemsAmounts.clear();
		
		errorMsg.setVisible(false);
		
		catalog = Client.arrCatalog;
		AddItemToCart();
		try {
			for (int i=0; i<catalog.size(); i++) {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("/gui/ItemInCatalogCard.fxml"));
				HBox cardBox = fxmlLoader.load();
				ItemInCatalogCardController itemInCatalogCardController = fxmlLoader.getController();
				itemInCatalogCardController.setData_itemInCatalog(catalog.get(i));
				catalogItemsLayout.getChildren().add(cardBox);
			}
		}
		catch (IOException e) {System.out.println("the load not success");}
		updatedSum.setText(String.format("%.2f", final_price)+" NIS");
	}
	
	/**
	 * Finds and displays the current sale for the specified area, if one is active. 
	 * If the subscriber has not yet made their first order,
	 * displays a special sale of 20% for their first order.
	 * If the subscriber had made first order, it will search for active sale that
	 * is compatible to the current time and the area of the user
	 * 
	 * The method using static String for area that changes according to EK or OL formats.
	 */
	public void findAreaSale() {
		if(role_screen != "SubscriberScreen")
			return;
		if (!SubscriberScreenController.made_first_order) {
			saleText.setVisible(true);
			saleText.setText("Subscriber First Order - 20% Off!");
			String fullImgSrc = "/images/saleIMG/20%.png";
			Image image = new Image(getClass().getResourceAsStream(fullImgSrc));
			saleImg.setImage(image);
			discount = (float) 0.8;			// need to combine with final_price;
		}
		else {
			for (Sale s : Client.arrsale) {
				if (s.getAreaSale().get(area).equals("2")) {
					if (isCurrentTimeBetweenHours(s.getHours())) { 
						saleText.setVisible(true);
						saleText.setText(s.getDescription());
						//byte[] fullImgSrc = s.getImgURL(); // need to check!
						Image image = receiveImageForProduct(s);
						saleImg.setImage(image);
						float num = Float.parseFloat(s.getPercentage());
						discount = 1 - (num/100);
					}
				}
			}
		}
	}
	public Image receiveImageForProduct(Sale sale)
	{
		Image image = new Image(new ByteArrayInputStream(sale.getImgURL()));
		return image;
	}
	/**
	 * Returns whether the current time is between the start and end hours specified in the input string.
	 *
	 * @param hoursString a string in the format "HH:00-HH:00" that specifies the start and end hours
	 * @return true if the current time is between the start and end hours specified in the input string, false otherwise
	 */
	public static boolean isCurrentTimeBetweenHours(String hoursString) {
	    int startHour = Integer.parseInt(hoursString.substring(0, 2));
	    int endHour = Integer.parseInt(hoursString.substring(6, 8));		// need to check!

	    Calendar cal = Calendar.getInstance();
	    int currentHour = cal.get(Calendar.HOUR_OF_DAY);

	    return currentHour >= startHour && currentHour < endHour;
	}

	/**
	 * This method is for AddToCart Button
	 * When called, it uploads the items in the cart. 
	 * It is used when the user cancels an item, or the whole order, 
	 * so the items align accordingly
	 */
	public void AddItemToCart() {
    	cartLayout.getChildren().removeAll(cartLayout.getChildren());
    	try {
    		for(int i=0;i<Client.cart.size();i++) {
				FXMLLoader fxmlLoader=new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("/gui/AddToCartCard.fxml"));// load the item card to cart
				HBox cardboxright=fxmlLoader.load();
				AddToCartCardController addToCart=fxmlLoader.getController();
				addToCart.setData_itemInCart(Client.cart.get(i));
				cartLayout.getChildren().add(cardboxright);//add to the scroll pane right
    		}	
		}
		catch(IOException e) {
			System.out.println("the load not success");
			e.printStackTrace();
		}
	}
	
	/**
	 * Update the final price in the screen itself and also in static value
	 * that later is going to be assign to the order object.
	 * Also, there is use of the value discount (static float) that holds the % for discount on every
	 * item in case of an active sale
	 * @param item_price the item price to be added to the final price value
	 */
	public void updateFinalPrice(float item_price) {
		final_price += item_price*discount;
		if (final_price == 0)
			updatedSum.setText("0.00 NIS");
		else
			updatedSum.setText(String.format("%.2f", final_price)+" NIS"); //	***
	}
	
	/**
	 * Action event that navigates to the confirm order screen.
	 * It will move to the next string only if the cart is not empty and
	 * before it, also assign the items in the cart and the final price to the order object 
	 * @param event Action Event for when the user presses "Confirm" button
	 */
	@FXML
	public void GoToCheckout(ActionEvent event) {
		if (Client.cart.size() == 0)
			errorMsg.setVisible(true);
		else {
			errorMsg.setVisible(false);
			Client.order.setItems_in_order(Client.cart); 	// we now can connect the finished cart to our order
			Client.order.updateFinalPrice(); 		// also we can calculate our final price to be seen in the confirm order screen
			AnchorPane pane;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/gui/ConfirmOrder.fxml"));
				pane = loader.load();
				ConfirmOrderController.confirmOrderController = loader.getController();

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
	 * Clears all the items form the cart in the screen and update the cart itself 
	 * and the final price. There is a call for the AddItemToCart method to refreash the 
	 * cart gui in the screen
	 * @param event Action Event for when the user presses "Cancel All" button
	 */
	@FXML
	public void cancelAllItems(ActionEvent event) {
		for(int i=0;i<Client.cart.size();i++) {
			Client.cart.get(i).setAmount_in_cart(1);
		}
		Client.cart.clear();
		final_price = 0;
		updatedSum.setText("0.00 NIS");
		AddItemToCart();
	}
	
	/**
	 * Clears all the items form the cart in the screen and erase the cart and the order objects.
	 * Then, it navigates to the appropriate screen according to the "EK" or "OL" configuration 
	 * @param event Action Event for when the user presses "Back" button
	 */
	@FXML
	public void GoBackToHomeScreen(ActionEvent event) {
		// EK or OL
		cancelAllItems(event);
		Client.cart = null;
		Client.order = null;
		switch (Client.configuration.get(0).toString()) {
    	case "EK": // go back to home screen
    		AnchorPane pane1;
    		try {
    			FXMLLoader loader = new FXMLLoader();
    			loader.setLocation(getClass().getResource("/gui/"+role_screen+".fxml"));
    			pane1 = loader.load();
    			if (role_screen == "ClientScreen") 
    				ClientScreenController.controller = loader.getController();
    			else 
    				SubscriberScreenController.controller = loader.getController();
    		}
    		catch (IOException e) {
    			e.printStackTrace();
    			return;
    		}
    		Scene scene1 = new Scene(pane1);
    		ClientUI.getStage().setScene(scene1);
    		ClientUI.getStage().show();
    		break;
    	case "OL":
    		AnchorPane pane2;
    		try {
    			FXMLLoader loader = new FXMLLoader();
    			loader.setLocation(getClass().getResource("/gui/OLMain.fxml"));
    			pane2 = loader.load();
    			OLMainController.oLMainController = loader.getController();
    		}
    		catch (IOException e) {
    			e.printStackTrace();
    			return;
    		}
    		Scene scene2 = new Scene(pane2);
    		ClientUI.getStage().setScene(scene2);
    		ClientUI.getStage().show();
    		break;
		}
    }
	
	/**
     * Set the error message of "Must choose items in order" in case
     * the user didn't pick any items and tried to move to the next screen
     */
	public void setErrorMsg() {
		errorMsg.setVisible(false);
	}
	
	/**
     * Event handler for the disconnect button.It sends a disconnect request to the server.
     *
     * @param event the action event that triggers this handler
     */
	@FXML
    void disconnectClient(ActionEvent event) throws Exception {
		ArrayList<Object> details = new ArrayList<>();
        details.add(LoginController.userLogin.getUser_id());
        MsgHandler disconnectToServer = new MsgHandler(TypeMsg.Request_disconnected, details);
        ClientUI.chat.accept((Object) disconnectToServer);
    }
	
	/**
	 * Action event to return to Login screen
	 * @param actionEvent action event of "Logout" button
	 */
	@FXML
    public void LogOutClicked(ActionEvent actionEvent) {
		if (role_screen == "SubscriberScreen")
			if (SubscriberScreenController.scheduledFuture != null)
				SubscriberScreenController.scheduledFuture.cancel(true);
		else 
			if (ClientScreenController.scheduledFuture!=null)
				ClientScreenController.scheduledFuture.cancel(true);
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
	
}
