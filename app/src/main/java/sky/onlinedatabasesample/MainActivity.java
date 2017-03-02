package sky.onlinedatabasesample;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String jsonString;
    String json_string;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void userReg(View view){
        startActivity(new Intent(this, Insert.class));
    }

    public void getJson(View view){
        new JsonBackgroundTask().execute();
    }

    public void parseJson(View view){

        Intent intent = new Intent(this, DisplayListView.class);
        intent.putExtra("json_data", json_string);
        startActivity(intent);
    }

    class JsonBackgroundTask extends AsyncTask<Void, Void, String>{

        String jsonURL = "https://hellcrush.000webhostapp.com/jsongetdata.php";

        public JsonBackgroundTask() {
            super();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(jsonURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream is = httpURLConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder stringBuilder = new StringBuilder();
                while((jsonString = br.readLine()) != null ){
                    stringBuilder.append(jsonString+ "\n");
                }
                br.close();
                is.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            /*TextView tv = (TextView)findViewById(R.id.json_text);
            tv.setText(result);*/
            json_string = result;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
