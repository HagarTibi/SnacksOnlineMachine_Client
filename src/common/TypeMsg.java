package common;

/**
 * An enumeration of messages that can be sent between clients and servers.
 */
public enum TypeMsg {


	Request_connect {
		public String toString() {
			return "Request connect";
		}
	},
	Request_disconnected {
		public String toString() {
			return "Request disconnected";
		}
	},
	Disconnected {
		public String toString() {
			return "Disconnected";
		}
	},
	ServerDisconnected{
		public String toString() {
			return "Server Disconnected";
		}
	},
	Connected {
		public String toString() {
			return "Connected";
		}
	},
	Request_Login {
		public String toString() {
			return "Request Login";
		}
	},
	Set_isLogin{
		public String toString() {
			return "Set user isLogin into DB";
		}
	},
	IsLogin_user{
		public String toString() {
			return "User isLogin Successfully";
		}
	},
	Request_logout {
		public String toString() {
			return "User asks to logout";
		}
	},
	Update_subscribe {
		public String toString() {
			return "update subscribe number";
		}
	},
	Logged_Successful {
		public String toString() {
			return "Logged Successful";
		}
	},
	Present_subscribers{
		public String toString() {
			return "present subscribers";
		}
	},
	subscribers_data_imported{
		public String toString() {
			return "subscribers data imported succesfully";
		}
	},
	Update_subscriber_successfully{
		public String toString() {
			return "Update subscriber successfully";
		}
	},
	Update_ThresholdLevel{
		public String toString() {
			return "Update Threshold Level";
		}
	},
	Update_Threshold_successfully{
		public String toString() {
			return "Update threshold level successfully";
		}
	},

	Fast_LogIn {
		public String toString() {
			return "Fast LogIn Subxcribe";
		}
	},


	Import_Excisting_Sales{
		public String toString() {
			return "Import excisting sales";
		}
	},

	Sales_Data_Imported{
		public String toString() {
			return "Sales imported successefuly";
		}
	},
	Update_Sale_To_Worker{
		public String toString() {
			return "Sales sent to worker";
		}
	},
	Sales_Sent_To_Worker{
		public String toString() {
			return "Sales sent to worker";
		}
	},
	Present_all_sales_in_specific_area{//type msg to present sales in specific area
		public String toString() {
			return "Present request of sales from: ";
		}
	},

	import_order_monthly_report_data {
		public String toString() {
			return  "Import Orders report Details";
		}
	},
	import_order_monthly_report_data_successfully {
		public String toString() {
			return " Import Orders report Details successfully";
		}
	},
	import_machine_monthly_order_report_data_successfully{
		public String toString() {
			return "import machine monthly order report data successfully";
		}
	},
	Sales_Of_Specific_Area{
		public String toString() {
			return "Sales of specific area entry";
		}
	},
	Update_Active_Sale_InArea{
		public String toString() {
			return "Update activity of sale in area ";
		}

	},
	Update_Sale_Active_Success{
		public String toString() {
			return "Update of activity of sale success";
		}
	},
	Create_New_Sale{
		public String toString() {
			return "Create new sale request by marketing manager";
		}
	},
	Create_New_Sale_Successfully{
		public String toString() {
			return "Successfully to create new sale";
		}
	},
	import_user_detail{
		public String toString() {
			return "request user";
		}
	},
	Import_UserDetail_Fail{
		public String toString() {
			return "the user not exist";
		}
	},
	Import_UserDetail_success{
		public String toString() {
			return "the user exist ";
		}
	},
	addToCustomerRequest{
		public String toString() {
			return "request user want to be customer ";
		}

	},
	addToCustomerRequest_success{
		public String toString() {
			return "request user want to be customer ";
		}

	},
	update_customer_to_Sub{
		public String toString() {
			return "Update customer to subscriber";
		}
	},
	Customer_updated_ToSubscriber_Successfuly{
		public String toString() {
			return "Customer updated to subscriber successfuly";
		}
	},

	Insert_customer_to_Subscribers_tab{
		public String toString() {
			return "Inserting new subscriber to table";
		}
	},
	New_Subscriber_Added_Successfuly{
		public String toString() {
			return "New subscriber added successfuly!";
		}
	},
	Import_customer_detail{
		public String toString() {
			return "Import customer details...";
		}
	},
	Imported_Customer_Details_Success{
		public String toString() {
			return "Customer data imported successfully";
		}
	},

	Import_Catalog{		// gilad
		public String toString() {
			return "Import catalog";
		}
	},
	Catalog_Imported{ 	// gilad
		public String toString() {
			return "Catalog data imported succesfully";
		}
	},
	Import_User_Credit_Data{ // gilad
		public String toString() {
			return "Importing the user's credit data to confirm order screen";
		}
	},
	User_Credit_Data_Imported{ // gilad
		public String toString() {
			return "User's credit data imported succesfully";
		}
	},
	Update_Machine_Data{ 	// raz
		public String toString(){
			return "Update machine data";
		}
	},
	Machine_Data_Updated{	// raz
		public String toString() {
			return "Update machine data successfuly";
		}
	},
	Save_Order_To_DB{		// raz
		public String toString() {
			return "Save order in data base";
		}
	},
	Order_Saved_To_DB{		// raz
		public String toString() {
			return "Order saved successfuly in data base";
		}
	},
	Import_Machines_Locations {
		public String toString() {
			return "Importing the location of machines";
		}
	},
	Machines_Locations_Imported {
		public String toString() {
			return "The locations of machines imported successfuly";
		}
	},


	Get_Inventory_report{
		public String toString() {
			return "Get Inventory Report";
		}
	},
	Show_Inventory_report{
		public String toString() {
			return "Show Inventory Report";
		}
	},
	GetMachineList{
		public String toString() {
			return "Get machine List by area";
		}
	},
	Show_Machine{
		public String toString() {
			return "Show machine list";
		}
	},
	Request_logout_successfuly	{
		public String toString() {
			return "Request logout successfuly";
		}
	},
	ApprovedUserCustomer{
		public String toString() {
			return "Request to aprrove user to customer";
		}
	},
	ApproveUserToCustomerSuccess{
		public String toString() {
			return "The Approve Success Of user: ";
		}
	},
	ImportUserRequestToSpecficArea{
		public String toString() {
			return "successful import users request to manager";
		}
	},
	ImportUserRequestToSpecficAreaSuccess{
		public String toString() {
			return "request to get the users request list";
		}
	},


	Get_Delivery_Orders{
		public String toString() {
			return "Get Delivery Orders";
		}
	},
	Show_Delivery{
		public String toString() {
			return "Show_Delivery Orders";
		}
	},

	Import_real_time_amounts{
		public String toString() {
			return "Import real time items amounts in local machine";
		}
	},
	Real_time_amounts_received{
		public String toString() {
			return "Real time items amount imported successfuly";
		}
	},
	Add_alert_to_DB{
		public String toString() {
			return "Update items_alert table";
		}
	},
	Alert_added_to_DB{
		public String toString() {
			return "Alert was saved to items_alert table";
		}
	},
	Check_sub_first_order{
		public String toString() {
			return "Check first order discount for subscriber";
		}
	},
	Answer_sub_first_order{
		public String toString() {
			return "Answer was received about subscriber first order";
		}
	},
	Import_sales_for_order{
		public String toString() {
			return "Importing deals information";
		}
	},
	Update_sub_first_order {
		public String toString() {
			return "Update subscriber first order";
		}
	},
	Sub_first_order_updated {
		public String toString() {
			return "Subscriber first order updated";
		}
	},
	Check_delay_payments {
		public String toString() {
			return "Check delay payments status";
		}
	},
	Delay_payments_checked {
		public String toString() {
			return "Returnd delay payments status";
		}
	},
	Change_delay_payments {
		public String toString() {
			return "Change delay payments status";
		}
	},
	Delay_payments_changed {
		public String toString() {
			return "Delay payments status changed";
		}
	},
	Update_ManageAlertsManagerScreen{
		public String toString() {
			return "Update Manage Alerts Manager controller screen";
		}
	},
	Update_AfterManagerPressedSendButton{
		public String toString() {
			return "Update alert status after manager pressed send button";
		}
	},
	Check_exist_remote_order{
		public String toString() {
			return "Import remote order for pickup if exist";
		}
	},
	Remote_order_checkd{
		public String toString() {
			return "Remote order for pickup checked";
		}
	},
	Update_remote_order{
		public String toString() {
			return "update pickup date for remote order";
		}
	},
	Remote_order_updated{
		public String toString() {
			return "Remote order entry is now updated";
		}
	},
	Get_Alert_For_ISW{
		public String toString() {
			return "get Alerts for ISW";
		}
	},
	Alerts_For_ISW_Imported{
		public String toString() {
			return "Alerts have been successfuly imported";
		}
	},
	Get_Items_In_Machine{
		public String toString() {
			return "get items and its amount from specific machine";
		}
	},
	Items_Machine_Imported{
		public String toString() {
			return "items from specific machine have been imported";
		}
	},
	Update_Items_Amount{
		public String toString() {
			return "ISW update items amount in machine";
		}
	},
	ISW_Updated_Machine{
		public String toString() {
			return "ISW updated successfuly items amount in machine";
		}
	},
	Update_Alrt_Status_To_Done{
		public String toString() {
			return "ISW Update Alerts Status to DONE";
		}
	},
	Alert_Status_Updated_To_Done{
		public String toString() {
			return "ISW updated successfuly alert status to Done";
		}
	},
	Get_Threshold{
		public String toString() {
			return "get threshold of machine";
		}
	},
	Threshold_Imported{
		public String toString() {
			return "threshold has been successfuly imported";
		}
	},
	Update_Alrt_Status_To_CONFIRM{
		public String toString() {
			return "update orders reomte table";
		}
	},
	Orders_Remote_Imported{
		public String toString() {
			return "orders_remote has been updated successfuly";
		}
	},
	Import_PickUps_Orders{
		public String toString() {
			return"Import pick ups orders";
		}
	},
	Impotrt_Delivery_Orders{
		public String toString() {
			return"Import Delivery orders";
		}
	},
	Imported_PickUps_Success{
		public String toString() {
			return "Pick Ups imported successfuly";
		}
	},
	Imported_Delivery_Success{
		public String toString() {
			return "Delivries imported successfuly ";
		}
	},
	Update_order_Status{
		public String toString() {
			return "Updating order status";
		}
	},
	Update_order_Status_Success{
		public String toString() {
			return"order status updated successfuly";
		}
	},
	Import_machine_tresholds{
		public String toString() {
			return"Importing machines of area";
		}
	},
	Machine_tresholds_imported{
		public String toString() {
			return"Machine tresholds imported successfully";
		}
	},
	import_machine_monthly_order_report_data{
		public String toString() {
			return "import machine monthly order report data";
		}
	},
	import_customer_Activity_report_data{
		public String toString() {
			return "import_customer_Activity_report_data";
		}
	},
	AlreadyExsit{
		public String toString(){return "Server was disconnected during the month change and the reports were already generated";}
	},
	GenerateSuccessfully{
		public String toString(){return "All reports were created successfully";}
	},
	GenerateFailed {
		public String  toString(){ return "Some error occurred while trying to create reports, please try again manually";}
	},



}

