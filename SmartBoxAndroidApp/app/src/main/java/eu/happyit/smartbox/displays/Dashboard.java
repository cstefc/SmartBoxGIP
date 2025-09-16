package eu.happyit.smartbox.displays;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import eu.happyit.smartbox.R;
import eu.happyit.smartbox.domain.PinSets;
import eu.happyit.smartbox.logistics.FillingSpinners;
import eu.happyit.smartbox.logistics.Request;
import eu.happyit.smartbox.logistics.Messages;

public class Dashboard extends AppCompatActivity {
    // User information
    private String username;
    private String password;
    private boolean isLight;

    private Set<PinSets> pinSets = new HashSet<>();

    // Elements on view
    private Spinner selectPinSet;

    private ImageView lightIndicator;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        defaultSettings();
        }

        public void setDefaultVariables(){
            selectPinSet = findViewById(R.id.selectPinSet);
            lightIndicator = findViewById(R.id.lightIndicator);
            isLight = true;
        }

        public void defaultSettings(){
        setDefaultVariables();

        try {
            SharedPreferences pref = getSharedPreferences("UserCredentials", MODE_PRIVATE);
            //Getting the username and password
            this.username = pref.getString("username", null);
            this.password = pref.getString("password", null);

            Request pinSetRequest = new Request(this, username, password, "/light/showPinSet");
            pinSetRequest.execute().get();
            Log.d("response", pinSetRequest.response);
            JSONArray pinSets = new JSONArray(pinSetRequest.response);
            for (int i = 0; i<pinSets.length(); i++){
                this.pinSets.add(new PinSets(pinSets.getJSONObject(i)));
            }
            ArrayList<String> pinSetName = FillingSpinners.getArrayList(pinSets, "pinSetName");
            FillingSpinners.setSpinner(this, pinSetName, selectPinSet);

            Request lightRequest = new Request(this, username, password, "/light/setLight?pinSetName="+selectPinSet.getSelectedItem().toString()+"&state="+isLight);
            lightRequest.execute();
            lightIndicator.setImageResource(R.drawable.on_light);

        }catch (Exception e) {
                Log.d("Error", "JSONException");
        }

    }

        public void changeLightState(View v) {
        // Getting the selected pinSetName
        Object obj = selectPinSet.getSelectedItem();
        if (obj == null){
            Toast.makeText(this,"Maak eerst een plaats aan.", Toast.LENGTH_LONG).show();
            return;
        }
        String pinSetName = obj.toString();
        // Sending a request to change the status and updating the indicator
        for (PinSets pinSet : pinSets) {
            if (pinSet.getPinSetName().equals(pinSetName)) {
                Request request = new Request(this, username, password, "/light/setLight?pinSetName=" + pinSetName+"&state="+!isLight);
                request.execute();
                if (isLight) {
                    isLight = false;
                    lightIndicator.setImageResource(R.drawable.off_light);
                    new Messages().succes(this, "licht is uit.", false);
                } else {
                    isLight = true;
                    lightIndicator.setImageResource(R.drawable.on_light);
                    new Messages().succes(this, "Licht is aan.", false);
                }
            }
        }
    }

        public void changeLockDown (View view){
            Request changeLockDown = new Request(this, username, password, "/user/setLockDown?state="+true);
            changeLockDown.execute();
        }

        public void changeMusic (View view){
            Request newRequest = new Request(this, username, password, "/user/changeMusic?username=" + username);
            newRequest.execute();
        }

        // Functions for opening an intent
        public void exit (View v){
            getSharedPreferences("UserCredentials", MODE_PRIVATE).edit().remove("username").commit();
            getSharedPreferences("UserCredentials", MODE_PRIVATE).edit().remove("password").commit();

            Intent intent = new Intent(Dashboard.this, LogIn.class);
            startActivity(intent);
        }

        public void openSettings (View view){
            Intent intent = new Intent(Dashboard.this, GeneralSettings.class);
            startActivity(intent);
        }

        public void openTemplateSettings (View view){
            Intent intent = new Intent(Dashboard.this, TemplateSettings.class);
            startActivity(intent);
        }

        public void openAlarmSettings (View view){
            Intent intent = new Intent(Dashboard.this, AlarmSettings.class);
            startActivity(intent);
        }
}