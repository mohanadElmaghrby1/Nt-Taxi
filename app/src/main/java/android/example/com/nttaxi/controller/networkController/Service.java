package android.example.com.nttaxi.controller.networkController;

import android.content.Context;
import android.example.com.nttaxi.model.Info;

import java.util.HashMap;

/**
 * Created by mohanad on 31/07/17.
 */

public class Service {

    /** the Back end  API url*/
    private static String BackEND_API_URL="http://webldm.net:8080/";
    /**the login back end url */
    private static String LOGIN_WEB_PAGE="login.php";

    /**the login back end url */
    private static String GETLOCATIONS_WEB_PAGE="getlocations.php";

    /**the registeration back end url */
    private static String REGISTER_WEB_PAGEL="registration.php";

    /** the request page*/
    private static String REQUEST_RIDE_WEB_PAGE="requesttaxi.php";

    private static String RIDES_WEB_PAGE="user-rides-list.php";

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
        BaseRequest.doPost(BackEND_API_URL+LOGIN_WEB_PAGE, context,params,callback);

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
        BaseRequest.doPost(BackEND_API_URL+REGISTER_WEB_PAGEL, context,params,callback);

    }

    public static void getLocations(Context context , RequestCallBack callback ){
        //getting the user cashed token
        String token = Info.getUSerCashedToken(context);
        BaseRequest.doGet("http://webldm.net:8080/getlocations.php?" +
                "token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjM0QDEyMzQifQ." +
                "3P9vglT4sXGaGEBVxkrAbm36LFA3mvPStwCBplZVZP4", context,callback);

//        BackEND_API_URL+GETLOCATIONS_WEB_PAGE+"?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjM0QDEyMzQifQ.3P9vglT4sXGaGEBVxkrAbm36LFA3mvPStwCBplZVZP4
    }

    public static void requestRide(Context context , String driver_id
            , String driver_name , String driver_email , String name
            , String phone , String location , String droplocation ,
                                   String latitude, String longitude , String from_latitude , String to_latitude ,
                                   String from_longitude , String to_longitude , RequestCallBack callback){

        String token =Info.getUSerCashedToken(context);
        //define hashMap with query parameters
        HashMap<String , String> params=new HashMap<>();
        params.put("name",name);
        params.put("driver_id",driver_id);
        params.put("driver_email",driver_email);
        params.put("driver_name",driver_name);
        params.put("phone",phone);
        params.put("token",token);
        params.put("location",location);
        params.put("droplocation",droplocation);
        params.put("latitude",latitude);
        params.put("longitude",longitude);
        params.put("from_latitude",from_latitude);
        params.put("to_latitude",to_latitude);
        params.put("from_longitude",from_longitude);
        params.put("to_longitude",to_longitude);
        //call the Notifier to do a request with POST Method
        BaseRequest.doPost(BackEND_API_URL+REQUEST_RIDE_WEB_PAGE, context,params,callback);

    }


    public static void getRides(Context context , RequestCallBack callBack){

        String token =Info.getUSerCashedToken(context);
        //define hashMap with query parameters
        HashMap<String , String> params=new HashMap<>();
        params.put("token",token);
        BaseRequest.doPost(BackEND_API_URL+RIDES_WEB_PAGE , context ,params ,callBack);
    }
}
