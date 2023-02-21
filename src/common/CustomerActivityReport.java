package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * A class representing a customer activity report. Implements the Serializable interface to allow for
 * serialization of the class.
 *
 * @author Hagar Tibi
 */
public class CustomerActivityReport implements Serializable {
    String Area,month,year,date;
    int count1,count2,count3;
    HashMap<String,Integer> mapofordersRange = new HashMap<>();


    public CustomerActivityReport() {

    }

    public HashMap<String, Integer> getMapofordersRange() {
        return mapofordersRange;
    }


    public String getArea() {
        return Area;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getDate() {
        return date;
    }

    public int getCount1() {
        return count1;
    }

    public int getCount2() {
        return count2;
    }

    public int getCount3() {
        return count3;
    }



    public CustomerActivityReport(HashMap<String, Integer> mapofordersRange, String area, String date, int count1, int count2, int count3) {
        this.Area = area;
        this.date=date;
        this.count1 = count1;
        this.count2 = count2;
        this.count3 = count3;
        this.mapofordersRange=mapofordersRange;
    }
}
