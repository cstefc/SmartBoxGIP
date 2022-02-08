package eu.happyit.smartbox.logistics;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FillingSpinners {

    public static ArrayList<String> getArrayList (JSONArray array, String neededValue){
        ArrayList<String> list = new ArrayList<>();
        try{
            for (int i = 0; i < array.length(); i++){
                JSONObject object = (JSONObject) array.get(i);
                String value = object.getString(neededValue).replaceAll("_", " ");
                list.add(value);
            }
        } catch (JSONException e){
            Log.d("Error", "Error in FillingSpinners");
            e.printStackTrace();
        }
        return list;
    }

    public static void setSpinner(Context context, ArrayList<?> list, Spinner spinner){
        ArrayAdapter<?> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
