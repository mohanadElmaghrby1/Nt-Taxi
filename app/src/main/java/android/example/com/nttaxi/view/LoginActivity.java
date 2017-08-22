package android.example.com.nttaxi.view;

import android.content.Intent;
import android.example.com.nttaxi.R;
import android.example.com.nttaxi.controller.networkController.RequestCallBack;
import android.example.com.nttaxi.controller.networkController.Service;
import android.example.com.nttaxi.model.Info;
import android.example.com.nttaxi.model.RootObject;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import static android.example.com.nttaxi.R.id.input_email;
import static android.example.com.nttaxi.R.id.input_password;

/**
 * full-screen activity that shows login UI
 */
public class LoginActivity extends BaseActivity  implements RequestCallBack {

    /**the email text in the login activity */
    private TextView email_txt ;

    /**the password text in the login activity */
    private TextView password_txt ;

    /**the logged user name */
    private String loggedUserName ;

    /** the log tag message*/
    private static final String LOG_TAG= LoginActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //get email and text from user UI
        email_txt=(TextView)findViewById(input_email);
        password_txt=(TextView)findViewById(input_password);
    }

    /**
     *
     * @param view
     */
    public void login(View view) {
        //getting the user inputs
        String emailString=email_txt.getText().toString();
        String passwordString=password_txt.getText().toString();

        //login to the application
        Service.login(this,emailString,passwordString,"client",this);
    }

    /**
     * open the app and display the content ,
     *   user now can access the app features
     */
    public void launchApplication(){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("email",email_txt.getText().toString());
        intent.putExtra("name",loggedUserName);
        startActivity(intent);
        //finish the login activity and prevent the user for come back
        finish();
    }

    /**
     * callBack success method
     * notified when user successfully do something
     * show a toast message with successfully login
     * @param response
     */
    @Override
    public void success(String response) {

        //this Gson parse and return base objecy contains all json response data
//        RootObject<Info> baseObject = new Gson().fromJson(response , RootObject.class); or
        //this Gson parse and return base objecy contains all json response data using generic
        RootObject<Info> baseObject = new Gson().fromJson(response ,new TypeToken<RootObject<Info>>(){}.getType());

        /*
        get login success if it equal 1 >> successfully login
                           else is equal 0>> email or password not found
         */
        int loginResult =baseObject.getSuccess();
        Log.v(LOG_TAG,String.valueOf(loginResult));

        /**
         * check if user successfully logged in
         */
        if (loginResult==1){

            //getting the name of logged user
            loggedUserName=baseObject.getInfo().get(0).getName();

            //show welcome message
            Toast.makeText(getApplicationContext(),
                    getString(R.string.successfull_login_welcome_message)+" "+loggedUserName,
                    Toast.LENGTH_LONG).show();

            //cash user token
            Info.setUSerCashedToken(this,baseObject.getInfo().get(0).getToken().toString());

            Log.v(LOG_TAG,"email= "+email_txt.getText().toString() +
                    " password= "+password_txt.getText().toString()+
                    " token= "+baseObject.getInfo().get(0).getToken().toString());

            //launch application if logged in
            launchApplication();
        }

        else{
            //show a info message
            Toast.makeText(this, getString(R.string.unsuccesfull_login_message),Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * callBack error method
     * notified when user wrongly do something
     * @param exc
     */
    @Override
    public void error(Exception exc) {
        Log.e(LOG_TAG,"Login : Error"+exc);
    }

    /**
     * open register activity
     * @param view
     */
    public void openRegisterActivity(View view) {
        Intent registerActivity = new Intent(this, RegisterActivity.class);
        startActivity(registerActivity);
        //finish the login activity and prevent the user for come back
        finish();
    }

}
