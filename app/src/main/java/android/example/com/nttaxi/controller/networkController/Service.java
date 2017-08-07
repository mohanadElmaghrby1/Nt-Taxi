package android.example.com.nttaxi.controller.networkController;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by mohanad on 31/07/17.
 */

public class Service {

    /**the login back end url */
    private static String LOGIN_BACK_END_API_URL="http://nt-taxi.000webhostapp.com/login.php";

    /**the registeration back end url */
    private static String REGISTER_BACK_END_API_URL="http://nt-taxi.000webhostapp.com/registration.php";

    /**
     * login to the site
     * @param context : the application conext
     * @param email : the user email
     * @param password : the user password
     * @param category : the user category (client , driver)
     * @param callback : the interface callback to do something
     */
    public static void login(Context context ,String email , String password , String category , RequestCallBack callback){
        //define hashMap with query parameters
        HashMap<String , String> params=new HashMap<>();
        params.put("email",email);
        params.put("password",password);
        params.put("category",category);
        //call the Notifier to do a request with POST Method
        BaseRequest.doPost(LOGIN_BACK_END_API_URL, context,params,callback);

    }

    /**
     *
     * @param context
     * @param name
     * @param phoneNumber
     * @param email
     * @param password
     * @param category
     * @param callback
     */
    public static void register(Context context ,String name , String phoneNumber , String email , String password, String category , RequestCallBack callback){
        //define hashMap with query parameters
        HashMap<String , String> params=new HashMap<>();
        params.put("name",name);
        params.put("number",phoneNumber);
        params.put("email",email);
        params.put("password",password);
        params.put("category",category);
        //call the Notifier to do a request with POST Method
        BaseRequest.doPost(REGISTER_BACK_END_API_URL, context,params,callback);

    }
}
