package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    TextView text,text2,temp,tempMin,tempMax;
    EditText edit;
    public void click(View view){
        String city = edit.getText().toString();
        InputMethodManager manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(edit.getWindowToken(),0);
        try {
            String encodedName = URLEncoder.encode(city, "UTF-8");
            DownloadTask task = new DownloadTask();
            task.execute("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=ffae026029d87a78734df54231244f39");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public class DownloadTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try{
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data != -1){
                    result += (char)data;
                    data = reader.read();
                }
                return result;

            }catch(Exception e){
                Log.i("Failed","Failed");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try{
                JSONObject jsonObject = new JSONObject(result);
                String weather = jsonObject.getString("weather");
                //String main = jsonObject.getString("main");
                JSONArray arr = new JSONArray(weather);
                for(int i = 0; i < arr.length(); i++){

                    JSONObject object = arr.getJSONObject(i);
                    text2.setText(object.getString("main"));
                    Log.i("main", object.getString("main"));
                    Log.i("description", object.getString("description"));
                }
                JSONObject main = jsonObject.getJSONObject("main");
                //DecimalFormat df2 = new DecimalFormat("#.##");

                Double tempp = Double.parseDouble(main.getString("temp")) - 273;
                String temppd = String.format("%.2f", tempp);
                Double tempp_min = Double.parseDouble(main.getString("temp_min")) - 273;
                String temppd_min = String.format("%.2f", tempp_min);
                Double tempp_max = Double.parseDouble(main.getString("temp_max")) - 273;
                String temppd_max = String.format("%.2f", tempp_max);

                temp.setText("Temp:"+temppd +" C");
                tempMin.setText("Min temp:"+temppd_min+" C");
                tempMax.setText("Max temp:"+temppd_max+ " C");

            }catch(Exception e){
                Log.i("Failed2","Failed2");
                e.printStackTrace();
            }
            System.out.println(result);
            //text.setText(result);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit = (EditText)findViewById(R.id.editText);
        text = (TextView)findViewById(R.id.textView);
        text2 = (TextView)findViewById(R.id.textView2);
        temp = (TextView)findViewById(R.id.textView7);
        tempMin = (TextView)findViewById(R.id.textView4);
        tempMax = (TextView)findViewById(R.id.textView6);
    }
}
