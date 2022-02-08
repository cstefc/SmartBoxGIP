package eu.happyit.smartbox.displays;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import eu.happyit.smartbox.R;
import eu.happyit.smartbox.domain.PinSets;
import eu.happyit.smartbox.domain.Templates;
import eu.happyit.smartbox.logistics.Request;
import eu.happyit.smartbox.logistics.FillingSpinners;
import eu.happyit.smartbox.logistics.Messages;

public class generalSettings extends AppCompatActivity {
    // User information
    private String username;
    private String password;
    ArrayList<String> templateNames;
    ArrayList<String> pinSetNames;

    // View elements
    private Spinner removeTemplateSpinner;
    private Spinner removePinSetSpinner;
    private Spinner renameTemplateSpinner;
    private Spinner renamePinSetSpinner;

    private EditText newTemplateNameField;
    private EditText newPinSetField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        defaultSetting();
    }

    public void setDefaultVariables(){
        removeTemplateSpinner = findViewById(R.id.templateDeleteSpinner);
        removePinSetSpinner = findViewById(R.id.pinSetDeleteSpinner);
        renameTemplateSpinner = findViewById(R.id.templateRenameSpinner);
        renamePinSetSpinner = findViewById(R.id.pinSetRenameSpinner);
        newTemplateNameField = findViewById(R.id.templateNewName);
        newPinSetField = findViewById(R.id.pinSetNewName);
    }

    public void defaultSetting() {
        setDefaultVariables();
        //Getting the username and password
        SharedPreferences pref = getSharedPreferences("UserCredentials", MODE_PRIVATE);
        this.username = pref.getString("username", null);
        this.password = pref.getString("password", null);

        //Sending the request
        try {
            Request templateRequest = new Request(this, username, password, "/light/showTemplates");
            Request pinSetRequest = new Request(this, username, password, "/light/showPinSet");

            templateRequest.execute().get();
            pinSetRequest.execute().get();

            //Getting the arrayLists
            JSONArray templates = new JSONArray(templateRequest.response);
            JSONArray pinSets = new JSONArray(pinSetRequest.response);

            templateNames = FillingSpinners.getArrayList(templates, "templateName");
            pinSetNames = FillingSpinners.getArrayList(pinSets, "pinSetName");

            // Adding the lists to the spinners
            FillingSpinners.setSpinner(this, pinSetNames, removePinSetSpinner);
            FillingSpinners.setSpinner(this, pinSetNames, renamePinSetSpinner);

            FillingSpinners.setSpinner(this, templateNames, removeTemplateSpinner);
            FillingSpinners.setSpinner(this, templateNames, renameTemplateSpinner);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Error", "Error in defaultSettings in general settings");
        }
    }

    public void deleteTemplate(View view) {
        String templateName = removeTemplateSpinner.getSelectedItem().toString().replaceAll(" ", "_");

        if (templateName.isEmpty()){
            Toast.makeText(this, "Probeer op nieuw!", Toast.LENGTH_SHORT).show();
        }
        else{
            sendRequest("/light/deleteTemplate?templateName=" + templateName, "Het sjabloon wordt nog gebruikt!");
            openDashboard();
        }
    }

    public void deletePinSet(View view) {
        String pinSetName = removePinSetSpinner.getSelectedItem().toString().replaceAll(" ", "_");
        if (pinSetName.isEmpty()){
            Toast.makeText(this, "Probeer op nieuw!", Toast.LENGTH_LONG).show();
        }
        else{
            sendRequest("/light/deletePinSet?pinSetName=" + pinSetName, "Er ging iets mis!");
            openDashboard();
        }
    }

    public void renameTemplate(View view){
        String templateName = renameTemplateSpinner.getSelectedItem().toString();
        String newTemplateName = newTemplateNameField.getText().toString().replaceAll(" ", "_");

        if (newTemplateName.isEmpty() || templateName.isEmpty() || isElementOf(templateNames, templateName)){
            Toast.makeText(this, "Vul een sjabloon naam in!", Toast.LENGTH_LONG).show();
        }
        else{
            sendRequest("/light/renameTemplate?"+"templateName="+templateName+"&newTemplateName="+newTemplateName, "De nieuwe naam bestaat al!");
            openDashboard();
        }
    }

    public void renamePinSet(View view){
        Spinner pinSetSpinner = findViewById(R.id.pinSetRenameSpinner);
        String pinSetName = pinSetSpinner.getSelectedItem().toString();
        String newPinSetName = newPinSetField.getText().toString().replaceAll(" ", "_");

        if (newPinSetName.isEmpty() || pinSetName.isEmpty() || isElementOf(pinSetNames, pinSetName)){
            Toast.makeText(this, "Vul een plaatsnaam in!", Toast.LENGTH_LONG).show();
        } else{
            sendRequest("/light/renamePinSet?username="+username+"&oldPinSetName="+pinSetName+"&newPinSetName="+newPinSetName, "De nieuwe naam bestaat al!");
            openDashboard();
        }

    }

    public void changePassword (View view){
        EditText oldPasswordField = findViewById(R.id.oldPassword);
        EditText newPasswordField = findViewById(R.id.newPassword);
        EditText newPassword2Field = findViewById(R.id.newPassword2);

        String oldPassword = oldPasswordField.getText().toString();
        String newPassword = newPasswordField.getText().toString();
        String newPassword2 = newPassword2Field.getText().toString();

        if (oldPassword.equals(password) && newPassword.equals(newPassword2)){
            // Saving the new password
            getSharedPreferences("UserCredentials" ,MODE_PRIVATE)
                    .edit()
                    .putString("password", newPassword)
                    .apply();

            sendRequest("/user/changePassword?username="+username+"&password="+oldPassword+"&newPassword="+newPassword, "Probeer opnieuw");
            openDashboard();
        } else{
            Toast.makeText(this, "De gegevens zijn foutief!", Toast.LENGTH_LONG).show();
        }
    }

    public void openDashboard(){
        Intent intent = new Intent(generalSettings.this, Dashboard.class);
        startActivity(intent);
    }

    public void sendRequest(String path, String errorMessage){
        Request newRequest = new Request(this, username, password, path);
        try {
            newRequest.execute().get();
            if (newRequest.connection.getResponseCode() == 200) {
                new Messages().succes(this, "Succes!", false);
            } else {
                new Messages().succes(this, errorMessage, false);
            }
        } catch (Exception e) {
            Log.d("Error", "Error in deleteTemplate");
            e.printStackTrace();
        }
    }

    public boolean isElementOf(ArrayList<String> list, String value){
        for (Iterator<String> it = list.iterator(); it.hasNext();){
            if (it.next().equals(value)){
                return true;
            }
        }
        return false;
    }
}
