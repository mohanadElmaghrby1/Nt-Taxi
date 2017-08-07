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
     * open the app and display the content
     * user now can access the app
     */
    public void launchApplication(){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("email",email_txt.getText().toString());
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

        baseObject.getSuccess();
        Log.v("gson",baseObject.getSuccess()+"");
        //show a info message
        Toast.makeText(this, "welcome",Toast.LENGTH_LONG).show();
        //launch application if logged in
        launchApplication();
    }

    /**
     * callBack error method
     * notified when user wrongly do something
     * @param exc
     */
    @Override
    public void error(Exception exc) {
        //show a info message
        Toast.makeText(this, "Please Check Your Email or Password",Toast.LENGTH_SHORT).show();
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
