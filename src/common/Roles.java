package common;

/**
 * An enumeration of the roles that a user can have within the system.
 */
public enum Roles {

	DeliveryMan {
		public String toString() {
			return "DeliveryScreen";
		}
	},
	AreaManager {
		public String toString() {
			return "ManagerScreen";
		}
	},
	MarketingManager {
		public String toString() {
			return "MarketingHomeScreen";
		}
	},
	MarketingWorker {
		public String toString() {
			return "MarketingWorkerMain";
		}
	},
	Customer {
		public String toString() {
			return "ClientScreen";
		}
	},
	Subscriber {
		public String toString() {
			return "SubscriberScreen";
		}
	},
	CustomerServiceWorker {
		public String toString() {
			return "CostumerServiceMain";
		}
	},
	InventoryAndSalesWorker {
		public String toString() {
			return "MenuISW";
		}
	},
	DivisionManager{
		public String toString() {
			return "CEOScreen";
		}
	},

	NULL {
		public String toString() {
			return "NULL";
		}
	},

}
