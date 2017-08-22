package android.example.com.nttaxi.view;

import android.content.Intent;
import android.example.com.nttaxi.R;
import android.example.com.nttaxi.model.Info;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {


    /** the log tag message*/
    private static final String LOG_TAG= SplashActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /*get the user cashed token
        * i it equal none then open login activity
        **/
        String userCashedToken = Info.getUSerCashedToken(this);

        /*
        if user cashed token equal to none then the user is logged out
        and then open LoginActivity
         */
        if (userCashedToken.equals("none")){
            //start LoginActivity
            startActivity(new Intent(this, LoginActivity.class));
            Log.v(LOG_TAG , "No Cashed Token");
        }
        else {
            //start MainActivity
            startActivity(new Intent(this, MainActivity.class));
            Log.v(LOG_TAG , "Cashed Token="+userCashedToken);
        }

        //finish the current activity and remove from stack
        finish();

    }

}
