package eu.happyit.smartbox.logistics;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import eu.happyit.smartbox.displays.Dashboard;

public class Messages {
    public void succes (Context context, String succesMessage, boolean sendThrough){
        Toast.makeText(context, succesMessage, Toast.LENGTH_LONG).show();
        if (sendThrough){
            Intent dashboard = new Intent(context,Dashboard.class);
            context.startActivity(dashboard);
        }
    }
}
