package eu.happyit.smartbox.logistics;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

public class Request extends AsyncTask<Void, Void, Void> {
    public HttpURLConnection connection;
    public String response;
    private String username;
    private String password;
    private String path;
    private Context context;

    public Request(Context context, String username, String password, String path){
        this.username = username;
        this.password = password;
        this.path = path;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
            try {
                Log.d("Request", "Begin");
                String url = "http://smartbox.happyit.eu:80" + path;
                final String name = username;
                final String pass = password;

                //Setting the authentication settings
                Authenticator.setDefault(new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(name, pass.toCharArray());
                    }
                });

                //Setting up the connection and opening it
                this.connection = (HttpURLConnection) new URL(url).openConnection();
                this.connection.setUseCaches(true);
                this.connection.connect();

                //Getting the response
                BufferedReader br = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
                response = br.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (isNetworkConnected()){
            super.onPostExecute(aVoid);
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Duw op Ok om af te sluiten.")
                    .setTitle("Geen internet verbinding...");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((Activity) context).finish();
                    System.exit(0);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private boolean isNetworkConnected () {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
