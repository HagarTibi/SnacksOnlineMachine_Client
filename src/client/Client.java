// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import Controller.*;


import common.*;
import gui.HomeScreenISWController;
import gui.ViewAlertISWController;
import javafx.application.Platform;

import ocsf.client.AbstractClient;

import java.io.IOException;
import java.util.ArrayList;
import Controller.AreaManagerThresholdLevelController;

import static Controller.ChooseMonthlyReportController.chooseMonthlyReport;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class Client extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */

  ChatIF clientUI; 
  public static ArrayList<Subscribe> arrsubscribe= new ArrayList<>();
  public static ArrayList<ItemInCatalog> arrCatalog = new ArrayList<>();
  public static ArrayList<ItemInCatalog> cart;
  public static Order order;
  public static ArrayList<PickUpOrder> pickUps=new ArrayList<PickUpOrder>();
  public static ArrayList<PresentDeliveryOrder> delivery=new ArrayList<PresentDeliveryOrder>();

  public static  ArrayList<RemoteOrder> deliveryOrders = new ArrayList<>();

  public static ArrayList<ItemInMachine> itemsAmounts = new ArrayList<>();
  public static CustomerActivityReport activityReportdetails = new CustomerActivityReport();
  public static MonthlyOrdersReportReturns ordersReportdetails = new MonthlyOrdersReportReturns();
  public static MachineOrdersReport machineOrdersReportdetails = new MachineOrdersReport();

  public static ArrayList<Object> configuration = new ArrayList<>();
  public static ArrayList<Sale> arrsale= new ArrayList<>();
  public static ArrayList<Object> arrrequestsusers=new ArrayList<>();

  public static boolean awaitResponse = false;
  public static User user;
  public static Customers cusTosub;

  public static MsgHandler<Object> serverResponse;


  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  

  public Client(String host, int port, ChatIF clientUI)
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    //openConnection();
  }

  //Instance methods ************************************************
   
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) throws Exception // receive msgHandler
  {
	 
	  arrsubscribe.clear();
	  awaitResponse = false;
	  if ((msg instanceof MsgHandler)) {
		  serverResponse = (MsgHandler<Object>) msg;
		  System.out.println("--> handleMessageFromServer" + serverResponse.getType());
		  System.out.println(serverResponse.getType().toString());
		  switch(serverResponse.getType()) {
            case Connected:
              break;
            case Disconnected:
                System.out.println("--> Disconnected");
				System.exit(0);
                break;
			  case Logged_Successful:
				if (LoginController.controller.checkIfLogIn(serverResponse))
					LoginController.successLogin = true;
				break;
            case Fast_LogIn:
                if (serverResponse.getMsg().get(0) instanceof User){
                    Subscribe subscriber = (Subscribe) serverResponse.getMsg().get(0);
                    subscriber.setIsLoggedIn(true);
                    LoginController.userLogin = subscriber;
                }
				break;
			case IsLogin_user:
				LoginController.controller.loadScreen();
				break;
			case Request_logout_successfuly:
				  LoginController.userLogin = new User();
				  break;
            case subscribers_data_imported:
                for(Object s: serverResponse.getMsg())
                    arrsubscribe.add((Subscribe)s);
                break;
		  	case Update_Threshold_successfully:
		  		AreaManagerThresholdLevelController.areamanagercontroller.feedBack(serverResponse);
		  		break;

		  	case Catalog_Imported: // gilad
		  		arrCatalog.clear();
		  		for(Object s: serverResponse.getMsg()) {
		  			((ItemInCatalog)s).setAmount_in_cart(1);
                    arrCatalog.add((ItemInCatalog)s);
		  		}
		  		break;
		  	case User_Credit_Data_Imported:     // gilad
				try {
				ConfirmOrderController.initiate_customer_credit_card(serverResponse.getMsg());
				}  catch (Exception e) {
					e.printStackTrace();
				}
				break;
		  	case Machine_Data_Updated: 	// raz
		  		try {
		  		ConfirmOrderController.initiate_items_under_alert(serverResponse.getMsg());
		  		}  catch (Exception e) {
					e.printStackTrace();
				}
		  		break;	
		  	case Order_Saved_To_DB:		// raz
		  		try {
		  		ConfirmOrderController.takeNewOrderCode(serverResponse.getMsg());
		  		}  catch (Exception e) {
		  			e.printStackTrace();
		  		}
		  		break;
		  	case Machines_Locations_Imported:
		  		try {
				OLMainController.initiate_machines_in_area(serverResponse.getMsg());
				}catch (Exception e) {
					e.printStackTrace();
				}
				break;

			  case import_order_monthly_report_data_successfully:
				  ordersReportdetails = (MonthlyOrdersReportReturns) serverResponse.getMsg().get(0);
				  break;
			  case import_machine_monthly_order_report_data_successfully:
				  machineOrdersReportdetails = (MachineOrdersReport) serverResponse.getMsg().get(0);
				  break;
			  case import_customer_Activity_report_data:
				  activityReportdetails = (CustomerActivityReport) serverResponse.getMsg().get(0);
				  break;

		  	case Sales_Data_Imported:
		  		for(Object s: serverResponse.getMsg())
                    arrsale.add((Sale)s);
		  		break;
		  	case Sales_Sent_To_Worker:
		  		System.out.println("New Sales Sent to worker");
		  		break;
		  	case Sales_Of_Specific_Area:

		  		arrsale.clear();//to initinalize the area
		  		 for(Object s: serverResponse.getMsg()) {
		  			 	System.out.println(((Sale)s).getName());
	                    arrsale.add((Sale)s);      
		  		 }
		  		break;
		  	case Update_Sale_Active_Success:
		  		
		  		System.out.println("Update activite success");

		  		break;
		  	case Create_New_Sale_Successfully:
		  		break;

		 	case Import_UserDetail_Fail:
		  		user=null;
		  		break;
		 	case Import_UserDetail_success:
		 		UserRequestsController.userRequestcontroller.importuser=true;
		  		user=(User)serverResponse.getMsg().get(0);	
		  		break;
		 	case addToCustomerRequest_success:
		 			UserRequestsController.userRequestcontroller.sendsuccess=(Boolean)serverResponse.getMsg().get(0);
		 		break;
		  	case Customer_updated_ToSubscriber_Successfuly:
		  		break;
		  	case New_Subscriber_Added_Successfuly:
		  		break;
		  	case Imported_Customer_Details_Success:
		  			
		  			Customers c=(Customers)serverResponse.getMsg().get(0);	
			  		cusTosub=c;
				  	break;
              case Show_Inventory_report:
                  if (serverResponse.getMsg().get(0) instanceof InventoryReport){
                      ChooseMonthlyReportController.inventoryReport = (InventoryReport)serverResponse.getMsg().get(0);
                  }
                  break;
              case Show_Machine:
                chooseMonthlyReport.getMachineList(serverResponse.getMsg());
                break;

              case ImportUserRequestToSpecficAreaSuccess:
            	  if(serverResponse.getMsg().size()==0)
            		  System.out.println("not have users request");
    
            	  arrrequestsusers=new ArrayList<>();
                  arrrequestsusers=serverResponse.getMsg();
            	  break;
              case ApproveUserToCustomerSuccess:
            	
            	  break;


			case Show_Delivery:
				if (serverResponse.getMsg().get(0) instanceof RemoteOrder){
					for(Object s: serverResponse.getMsg())
						deliveryOrders.add((RemoteOrder)s);
				}
				break;
              case Real_time_amounts_received:
            	  itemsAmounts.clear();
            	  for(Object s: serverResponse.getMsg()) 
                      itemsAmounts.add((ItemInMachine)s);
            	  break;
              case Alert_added_to_DB:
            	  break;
              case Answer_sub_first_order:
            	  SubscriberScreenController.made_first_order = (boolean)serverResponse.getMsg().get(0);
            	  break;
              case Sub_first_order_updated:
            	  break;
              case Delay_payments_checked:
            	  SubscriberScreenController.is_delay_payment = (boolean)serverResponse.getMsg().get(0);
            	  break;
              case Delay_payments_changed:
            	  break;
              case Update_ManageAlertsManagerScreen: //hen - for initialize function of the screen managerAlertManagerController
  				try {
  				ManageAlertsManagerController.initiate_AlertsForManager(serverResponse.getMsg());
  				}  catch (Exception e) {
  						e.printStackTrace();
  				}
  				break;
  				case Update_AfterManagerPressedSendButton: //hen - for sendBtn function of the screen managerAlertManagerController
  				try {
  				ManageAlertsManagerController.cameBackAfterManagerPressedOnSendButton(serverResponse.getMsg());
  				}  catch (Exception e) {
  						e.printStackTrace();
  				}
  				break;
  				case Remote_order_checkd:
  					if (LoginController.userLogin.getRole().toString() == "SubscriberScreen")
  						SubscriberScreenController.initiate_customer_pickup_orders(serverResponse.getMsg());
  					else
  						ClientScreenController.initiate_customer_pickup_orders(serverResponse.getMsg());
  					break;
  				case Remote_order_updated:
  					break;
  				case Alerts_For_ISW_Imported:
  			  		for(Object alert : serverResponse.getMsg()) {
  			  			HomeScreenISWController.iwsAlerts.add((ItemsAlert)alert);
  			  		}
  			  		break;	
  			  	case Items_Machine_Imported:
  			  		for(Object item : serverResponse.getMsg()) {
  			  			ViewAlertISWController.items.add((ItemInMachine)item);
  			  		}
  			  		break;
  			  	case ISW_Updated_Machine:
  			  		break;
  			  	case Alert_Status_Updated_To_Done:
  			  		break;	
  			  	case Threshold_Imported:
  			  		ViewAlertISWController.threshold=(int)serverResponse.getMsg().get(0);
  			  		break;
  			  	case Orders_Remote_Imported:
			  		break;
  			  	case Imported_PickUps_Success:
  			  		pickUps.clear();
  			  		for(Object o:serverResponse.getMsg()) {
  			  			PickUpOrder pick=(PickUpOrder)o;
  			  			pickUps.add(pick);
  			  		}
  			  		break;
  			  	case Imported_Delivery_Success:
  			  		delivery.clear();
  			  		for(Object o:serverResponse.getMsg()) {
  			  			PresentDeliveryOrder deli=(PresentDeliveryOrder)o;
  			  			delivery.add(deli);
  			  		}
            	  break;
  			  	case Update_order_Status_Success:
            	  break;
  			  	case Machine_tresholds_imported:
  			  		AreaManagerThresholdLevelController.initiate_machine_info(serverResponse.getMsg());
  			  		break;

		  	default:
				break;

		  	}
		  }
	  }
	  



  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  
  public void handleMessageFromClientUI(Object message)  
  {
    try
    {
    	openConnection();
       	awaitResponse = true;
    	sendToServer(message);
		// wait for response
		while (awaitResponse) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }
    catch(IOException e)
    {
    	//e.printStackTrace();
      //clientUI.display("Could not send message to server: Terminating client."+ e);
    	clientUI.display("Server is disconectted \nor IP is wrong from server IP ");
    	quit();
    }
  }
  
  /**
   * Hook method called each time an exception is thrown by the
   * client's thread that is waiting for messages from the server.
   * open main Scene - connect.
   * @param exception the exception raised.
   */
  protected void connectionException(Exception exception) {
	  Platform.runLater(() -> {
		  try {
			  ClientUI.startMain();
		  } catch (Exception e) {
			// TODO: handle exception
		} 
	  });
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
        closeConnection();
    }
    catch(IOException e) {

    }
    System.exit(0);
  }


}

