package android.example.com.nttaxi.view;

import android.content.Intent;
import android.example.com.nttaxi.R;
import android.example.com.nttaxi.controller.networkController.RequestCallBack;
import android.example.com.nttaxi.controller.networkController.Service;
import android.example.com.nttaxi.controller.networkController.Validate;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends BaseActivity implements RequestCallBack {

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
        //show a info message
        Toast.makeText(this, "You have been successfully Registered ",Toast.LENGTH_SHORT).show();
        //launch application if logged in
        launchApplication();
    }

    @Override
    public void error(Exception exc) {
        //show a info message
        Toast.makeText(this, "You Are Already Register",Toast.LENGTH_SHORT).show();
    }

}
