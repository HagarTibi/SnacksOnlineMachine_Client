package common;

import java.io.Serializable;

/**
 * Enum for alerts status for machines with items below 
 * threshold level: ACTIVE, IN_PROGRESS, DONE
 */

public enum AlertStatus implements Serializable{
	ACTIVE{
		public String toString(){
			return "ACTIVE";
		}
	},
	IN_PROGRESS{
		public String toString() {
			return "IN_PROGRESS";
		}
	},
	DONE{
		public String toString() {
			return "DONE";
		}
	}
}