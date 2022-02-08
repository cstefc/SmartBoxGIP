package eu.happyit.smartbox.displays;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import eu.happyit.smartbox.R;
import eu.happyit.smartbox.logistics.Alarm;
import eu.happyit.smartbox.logistics.Messages;
import eu.happyit.smartbox.logistics.TimeMillis;

public class AlarmSettings extends AppCompatActivity {
   private TextView time;
   private TextView message;

   private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_settings);
        defaultSettings();
    }

    public void defaultVariables(){
        time = findViewById(R.id.time);
        message = findViewById(R.id.message);
        timePicker = findViewById(R.id.timePicker);
    }

    public void defaultSettings (){
        defaultVariables();

        SharedPreferences pref = getSharedPreferences("UserCredentials", MODE_PRIVATE);
        String hour = pref.getString("nextAlarm", null);
        if (hour != null){
            message.setText("Volgend alarm: ");
            time.setText(hour);
        } else{
            message.setText("Nog geen wekker ingesteld.");
            time.setText("");
        }
    }

    public void setAlarm (View view){
        int hours = timePicker.getHour();
        int minutes = timePicker.getMinute();

        // Setting the alarm
        TimeMillis timeInMillis = new TimeMillis(hours, minutes);
        long timeMillis = timeInMillis.getTimeInMillis();
        String date = timeInMillis.getTimeString();

        message.setText("Volgend alarm: ");
        time.setText(date);
        SharedPreferences pref = getSharedPreferences("UserCredentials", MODE_PRIVATE);
        pref.edit().putString("nextAlarm", date).apply();

        Intent intent = new Intent(this, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeMillis, pendingIntent);

        // Confirming and going back to the dashboard
        new Messages().succes(this, "Alarm ingesteld.", true);
    }

    public void deleteAlarm (View view){
        Intent intent = new Intent(this, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        SharedPreferences pref = getSharedPreferences("UserCredentials", MODE_PRIVATE);
        pref.edit().remove("nextAlarm").commit();
        new Messages().succes(this, "Alarm verwijderd.", true);
    }
}
