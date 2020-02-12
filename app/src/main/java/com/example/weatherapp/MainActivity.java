package com.example.weatherapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button search;
    EditText cityName;

    class Weather extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... address) {

            try{
                URL url = new URL(address[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream is = urlConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader( is );
                int data = isr.read();
                String content = "";
                char ch;
                while(data!= -1){
                    ch = (char)data;
                    content = content + data;
                    data = isr.read();
                }
                return content;
            }catch(Exception e){e.printStackTrace();}
            return null;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        search = (Button) findViewById(R.id.search);
        cityName = (EditText) findViewById(R.id.cityName);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cname=cityName.getText().toString().trim();
                if(cname.isEmpty()){
                    Toast.makeText(MainActivity.this, "Empty field", Toast.LENGTH_SHORT).show();
                    return;
                }
                String theex="https://samples.openweathermap.org/data/2.5/weather?q="+cname+"&appid=";
		theex = theex + "API_KEY";								//YOUR API KEY GOES HERE
                String content;
                Weather weather = new Weather();
                try {
                    content = weather.execute(theex).get();
                    JSONObject jsonObject = new JSONObject(content);
                    String mainhh = jsonObject.getString("main");
                    JSONObject mainPart = new JSONObject(mainhh);
                    String temp = mainPart.getString("temp");
                    double g = Double.parseDouble(temp);
                    g-=273.15;
                    temp=String.valueOf((int)g);
                    textView.setText(temp);
                    cityName.setText("");
                }catch (Exception e){e.printStackTrace();}
            }
        });
    }
}