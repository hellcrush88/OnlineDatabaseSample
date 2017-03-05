package sky.onlinedatabasesample;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Sky on 28-Feb-17.
 */

public class DeleteBackgroundTask extends AsyncTask<String, Void, String> {

    Context ctx;

    DeleteBackgroundTask(Context context){
        this.ctx = context;
    }
    @Override
    protected String doInBackground(String... params) {

        String delete_url = "https://hellcrush.000webhostapp.com/delete.php";
        String method = params[0];
        if(method.equals("delete")){
            String id = params[1];
            try {
                URL url = new URL(delete_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(id, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream is = httpURLConnection.getInputStream();
                is.close();
                return "Deleting succeeded";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(ctx, "Item Deleted", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
