package eu.happyit.smartbox.logistics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class Alarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences pref = context.getSharedPreferences("UserCredentials", Context.MODE_PRIVATE);
        String username = pref.getString("username", null);
        String password = pref.getString("password", null);

        Request request = new Request(context, username, password, "/user/changeMusic?username=" + username);
        request.execute();

        pref.edit().remove("nextAlarm").commit();
    }
}
