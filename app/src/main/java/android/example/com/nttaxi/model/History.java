package android.example.com.nttaxi.model;

/**
 * Created by mohanad on 10/08/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class History {

    @SerializedName("ridelist")
    @Expose
    private List<Ridelist> ridelist = null;
    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Ridelist> getRidelist() {
        return ridelist;
    }

    public void setRidelist(List<Ridelist> ridelist) {
        this.ridelist = ridelist;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}