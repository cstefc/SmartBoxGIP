package eu.happyit.smartbox.displays;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import eu.happyit.smartbox.R;
import eu.happyit.smartbox.logistics.Request;
import eu.happyit.smartbox.logistics.Messages;

public class LogIn extends AppCompatActivity {
    public String username;
    public String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        if (isSaved()){
            Intent intent = new Intent(LogIn.this, Dashboard.class);
            startActivity(intent);
        }


    }

    public boolean isSaved (){
        SharedPreferences pref = getSharedPreferences("UserCredentials",MODE_PRIVATE);
         username = pref.getString("username", null);
         password = pref.getString("password", null);
        return username != null && password != null;
    }

    public void tryLogIn (View view){
        EditText usernameBox = findViewById(R.id.username);
        EditText passwordBox = findViewById(R.id.password);

        String username = usernameBox.getText().toString();
        String password = passwordBox.getText().toString();
        try {
            Request sendRequest = new Request(this, username, password, "/user/verify");
            Log.d("sendRequest", "username: "+ username + ", password: "+password);
            sendRequest.execute().get();
            int responseCode = sendRequest.connection.getResponseCode();

            if (responseCode == 200) {
                    getSharedPreferences("UserCredentials", MODE_PRIVATE)
                            .edit()
                            .putString("username", username)
                            .putString("password", password)
                            .commit();

                    new Messages().succes(this, "Gelukt.", false);
                    Intent intent = new Intent(LogIn.this, Dashboard.class);
                    startActivity(intent);
                    return;
            }
            Toast.makeText(this, "Probeer opnieuw! (" + responseCode + ")", Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Log.d("Not Done", "LogIn Screen");
            e.printStackTrace();
        }
    }
}
