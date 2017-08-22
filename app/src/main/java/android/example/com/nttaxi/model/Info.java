
package android.example.com.nttaxi.model;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static android.content.Context.MODE_PRIVATE;

public class Info {

    /** the user cashed data id*/
    public static final String MY_PREFS_NAME = "login_cash_data";


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("token")
    @Expose
    public  String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public  String getToken() {
        return token;
    }

    /**
     * set the user cashed  token
     * @param context : the app context
     * @param token : the user returned token fro server
     */
    public static void setUSerCashedToken(Context context ,String token) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("token", token);
        editor.apply();
    }

    /**
     * getting the user cashed token
     * @param context
     * @return : user cashed token if found or none if not
     */
    public static String getUSerCashedToken(Context context){
        SharedPreferences prefs =context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String token=prefs.getString("token", "none");//"No token" is the default value
        return  token;
    }
}

