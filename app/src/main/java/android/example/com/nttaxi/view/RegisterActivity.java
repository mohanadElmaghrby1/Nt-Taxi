package android.example.com.nttaxi.view;

import android.content.Intent;
import android.example.com.nttaxi.R;
import android.example.com.nttaxi.controller.networkController.RequestCallBack;
import android.example.com.nttaxi.controller.networkController.Service;
import android.example.com.nttaxi.controller.networkController.Validate;
import android.example.com.nttaxi.model.Info;
import android.example.com.nttaxi.model.RootObject;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RegisterActivity extends BaseActivity implements RequestCallBack {

    /** the Log Tag*/
    private static String LOG_TAG= RegisterActivity.class.getName();

    /** the  register user name */
    private EditText etxt_name;

    /** the  register user phone number */
    private EditText etxt_phone_number;

    /** the  register user email */
    private EditText etxt_email;

    /** the  register user password */
    private EditText etxt_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //getting the register texts
        etxt_name= (EditText)findViewById(R.id.txt_name);
        etxt_phone_number = (EditText)findViewById(R.id.txt_phone_number);
        etxt_email = (EditText)findViewById(R.id.txt_email);
        etxt_password = (EditText)findViewById(R.id.txt_password);

    }


    public void register( View view){

        //getting register user data
        String userName;
        String userPhoneNumber;
        String userEmail ;
        String userPassword;

        userName = etxt_name.getText().toString();
        userPhoneNumber = etxt_phone_number.getText().toString();
        userEmail = etxt_email.getText().toString();
        userPassword=etxt_password.getText().toString();

        //check user data
        boolean isCorrect =Validate.checkRegisterUserDate(userName,userPhoneNumber,userEmail,userPassword,this);

        if (isCorrect){
            Service.register(this,userName,userPhoneNumber,userEmail,userPassword,"client",this);
        }

    }

    public void openLoginActivity(View view) {
        Intent loginActivity =new Intent(this , LoginActivity.class);
        startActivity(loginActivity);
    }

    /**
     * open the app and display the content
     * user now can access the app
     */
    public void launchApplication(){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("email",etxt_email.getText().toString());
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
            String loggedUserName=baseObject.getInfo().get(0).getName();

            //cash user token
            Info.setUSerCashedToken(this,baseObject.getInfo().get(0).getToken().toString());

            //show a info message
            Toast.makeText(this, getString(R.string.successfullu_registerd),Toast.LENGTH_LONG).show();

            //launch application if logged in
            launchApplication();
        }

        else if (loginResult==0 &&baseObject.getMessage().equals("already registered")){
            //show a info message
            Toast.makeText(this, getString(R.string.register_email_already_used),Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void error(Exception exc) {
        Log.e(LOG_TAG,"Error in Register"+exc);
    }

}
