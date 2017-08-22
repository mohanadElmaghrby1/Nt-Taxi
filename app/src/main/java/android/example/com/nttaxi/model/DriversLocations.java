package android.example.com.nttaxi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by mohanad on 09/08/17.
 */

public class DriversLocations {

    @SerializedName("location")
    @Expose
    private ArrayList<Location> location = null;
    @SerializedName("success")
    @Expose
    private int success;

    public ArrayList<Location> getLocation() {
        return location;
    }

    public void setLocation(ArrayList<Location> location) {
        this.location = location;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

}