package forecast.iak.muhtar.iakforecast;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView tvWeatherData;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvWeatherData = (TextView)findViewById(R.id.tv_weather_data);

        /*String[] dummyDataWeather = {
          "Hari ini, 29 Apr - Mendung",
          "Besok, 30 Apr - Mendung",
          "Minggu, 1 Mei - Mendung",
          "Senin, 2 Mei - Mendung",
          "Selasa, 3 Mei - Mendung",
          "Rabu, 4 Mei - Mendung",
          "Kamis, 5 Mei - Mendung",
          "Jumat, 6 Mei - Mendung",
          "Sabtu, 7 Mei - Mendung",
        };

        for (String dummyDataDay : dummyDataWeather){
            tvWeatherData.append(dummyDataDay+"\n\n");
        }*/

        new FetchWeatherData().execute();

    }

    public class FetchWeatherData extends AsyncTask<Void,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            String strJSONWeather = null;

            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=Bandung&APPID=2939b4f9a70e7dd25e181b06ab14bc5d&mode=json&units=metric&cnt=5");

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();

                if (inputStream == null){
                    return null;
                } else {
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                }

                String line;
                while ((line = bufferedReader.readLine()) != null){
                    stringBuffer.append(line);
                }

                if (stringBuffer.length() == 0){
                    return null;
                } else {
                    strJSONWeather = stringBuffer.toString();
                    return strJSONWeather;
                }


            } catch (IOException e){
                Log.e("Error parsing", e.toString());
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            tvWeatherData.setText(s);
            Log.d("Cek Response ", s);
        }
    }
}
