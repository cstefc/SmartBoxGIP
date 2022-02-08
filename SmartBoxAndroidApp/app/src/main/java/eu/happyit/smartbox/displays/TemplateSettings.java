package eu.happyit.smartbox.displays;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import eu.happyit.smartbox.R;
import eu.happyit.smartbox.logistics.Request;
import eu.happyit.smartbox.logistics.FillingSpinners;
import eu.happyit.smartbox.logistics.Messages;

public class TemplateSettings extends AppCompatActivity {
    // User information
    private String username;
    private String password;
    // PinSet information
    private int r;
    private int g;
    private int b;
    // View elements
    private SeekBar redSeekbar;
    private SeekBar greenSeekbar;
    private SeekBar blueSeekbar;

    private Spinner pinSetSpinner;
    private Spinner templateSpinner;
    private Spinner rSpinner;
    private Spinner gSpinner;
    private Spinner bSpinner;
    private Spinner pinSetTemplate;

    private TextView preview;

    private EditText pinSetNameField;
    private EditText newTemplateName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_settings);
        defaultSettings();
    }

    public void getRGB(){
        r = redSeekbar.getProgress();
        g = greenSeekbar.getProgress();
        b = blueSeekbar.getProgress();
    }

    public void setSpinners (){
        // Making the requests ready
        Request templateRequest = new Request(this, username, password, "/light/showTemplates");
        Request pinSetRequest = new Request(this, username, password, "/light/showPinSet");
        try{
            // Sending the requests
            templateRequest.execute().get();
            pinSetRequest.execute().get();

            // Converting the responses into JSONArrays
            JSONArray templatesArray = new JSONArray(templateRequest.response);
            JSONArray pinSetsArray = new JSONArray(pinSetRequest.response);

            // Setting up template Spinner
            ArrayList<String> templateList = FillingSpinners.getArrayList(templatesArray, "templateName");
            FillingSpinners.setSpinner(this, templateList, templateSpinner);
            FillingSpinners.setSpinner(this, templateList, pinSetTemplate);

            //Setting up PinSetSpinner
            ArrayList<String> pinSetList= FillingSpinners.getArrayList(pinSetsArray, "pinSetName");
            FillingSpinners.setSpinner(this, pinSetList, pinSetSpinner);

            // defining PinLists
            ArrayList<Integer> pins = new ArrayList<>(Arrays.asList(2, 3, 4, 5 ,6, 7, 8, 9, 10, 11, 12, 13));
            FillingSpinners.setSpinner(this, pins, rSpinner);
            FillingSpinners.setSpinner(this, pins, gSpinner);
            FillingSpinners.setSpinner(this, pins, bSpinner);

        } catch (ExecutionException | InterruptedException | JSONException e){
            Log.d("Error", "Error in defaultSettings in template settings");
            e.printStackTrace();
        }
    }

    public void setDefaultVariables(){
        redSeekbar = findViewById(R.id.redSeekbar);
        greenSeekbar = findViewById(R.id.greenSeekbar);
        blueSeekbar = findViewById(R.id.blueSeekbar);

        pinSetSpinner = findViewById(R.id.placeSpinner);
        templateSpinner = findViewById(R.id.templateSpinner);
        rSpinner = findViewById(R.id.rSpinner);
        gSpinner = findViewById(R.id.gSpinner);
        bSpinner = findViewById(R.id.bSpinner);
        pinSetTemplate = findViewById(R.id.pinSetTemplate);

        preview = findViewById(R.id.preview);

        pinSetNameField = findViewById(R.id.pinSetName);
        newTemplateName = findViewById(R.id.newTemplateName);
    }

    public void defaultSettings () {
        setDefaultVariables();
        //Getting user info from sharedPreferences
        SharedPreferences pref = getSharedPreferences("UserCredentials",MODE_PRIVATE);
        username = pref.getString("username", null);
        password = pref.getString("password", null);
        setSpinners();
    }

    public void showPreview (View view){
        // Getting the RGB values
        getRGB();

        // Coloring the preview window
        preview.setBackgroundColor(Color.rgb(r,g,b));
    }

    public void makeNewTemplate (View view){
        // Getting the RGB value
        getRGB();
        // Testing that all values aren't null
        String templateName = newTemplateName.getText().toString().replaceAll(" ", "_").toLowerCase();
        if (templateName.isEmpty()){
            return;
        }
        // Sending the request
        sendRequest("/light/addTemplate?R="+r+"&G="+g+"&B="+b+"&templateName="+templateName, "Er bestaat al een sjabloon met deze naam!");
        openDashboard();
    }

    public void makeNewPinSet (View view){
        int rPin = (int) rSpinner.getSelectedItem();
        int gPin = (int) gSpinner.getSelectedItem();
        int bPin = (int) bSpinner.getSelectedItem();

        if (pinSetNameField.getText().toString().isEmpty()){
            Toast.makeText(this, "Vul een plaatsnaam in!", Toast.LENGTH_LONG).show();
        }

        String newPinSetPath = "/light/addPinSet?pinRed="+rPin+"&pinGreen="+gPin+"&pinBlue="+bPin
                +"&pinSetName="+pinSetNameField.getText().toString().replaceAll(" ", "_")
                +"&templateName="+pinSetTemplate.getSelectedItem().toString().toLowerCase();

        sendRequest(newPinSetPath, "Die plaatsnaam is al bezet!");
        openDashboard();
    }

    public void setColor (View view){
        String pinSetName = pinSetSpinner.getSelectedItem().toString().replaceAll(" ", "_");
        String templateName = templateSpinner.getSelectedItem().toString().replaceAll(" ", "_");

        if (pinSetName.isEmpty() || templateName.isEmpty()){
            Toast.makeText(this, "Er ging iets mis...", Toast.LENGTH_LONG).show();
            return;
        }

        sendRequest("/light/changePinSet?pinSetName="+pinSetName+"&newTemplateName="+templateName, "Error!");
        openDashboard();
    }

    public void openDashboard(){
        Intent intent = new Intent(TemplateSettings.this, Dashboard.class);
        startActivity(intent);
    }

    public void sendRequest(String path, String errorMessage){
        Request newRequest = new Request(this, username, password, path);
        try {
            newRequest.execute().get();
            if (newRequest.connection.getResponseCode() ==200) {
                new Messages().succes(this, "Gelukt.", false);
            } else {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.d("Error", "Error in sendRequest");
            e.printStackTrace();
        }
    }

}
