package sky.onlinedatabasesample;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DisplayListView extends AppCompatActivity {

    static String json_string;
    String jsonString;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ContactAdapter contactAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    static NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_list_view);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        networkInfo = connectivityManager.getActiveNetworkInfo();

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);

        if(networkInfo == null){
            Toast.makeText(this, "There is no internet connection", Toast.LENGTH_LONG).show();
        }

        contactAdapter = new ContactAdapter(this, R.layout.row_layout);

        ListView list = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.emptyView);
        list.setEmptyView(emptyView);
        list.setAdapter(contactAdapter);

        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                /*boolean enable = false;
                if (listView != null && listView.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                mSwipeRefreshLayout.setEnabled(enable);*/
                if (listView.getChildAt(0) != null) {
                    mSwipeRefreshLayout.setEnabled(listView.getFirstVisiblePosition() == 0 && listView.getChildAt(0).getTop() == 0);
                }
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(networkInfo != null && networkInfo.isConnected()){
                    if (contactAdapter != null) {
                        contactAdapter.clear();
                        new JsonBackgroundTask().execute();
                    }
                }else{
                    Toast.makeText(DisplayListView.this, "There is no internet connection available",
                            Toast.LENGTH_SHORT).show();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(DisplayListView.this, "Please try again later",
                            Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    return;
                }
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Contacts contacts = contactAdapter.getItem(position);
                /*String mName = String.valueOf(contacts.getId());
                Toast.makeText(getBaseContext(), mName, Toast.LENGTH_LONG).show();*/
                // Convert the String URL into a URI object (to pass into the Intent constructor)

                String url = contacts.getUrl();
                if (!url.startsWith("http://") && !url.startsWith("https://")){
                    url = "http://" + url;
                }

                Uri earthquakeUri = Uri.parse(url);

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DisplayListView.this);
                builder.setMessage("Delete this item?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Contacts contacts = contactAdapter.getItem(position);
                        String method = "delete";
                        String idNumber = String.valueOf(contacts.getId());
                        DeleteBackgroundTask deleteBackgroundTask = new DeleteBackgroundTask(DisplayListView.this);
                        deleteBackgroundTask.execute(method, idNumber);
                        new JsonBackgroundTask().execute();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked the "Cancel" button, so dismiss the dialog
                        // and continue editing the pet.
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    public void userReg(View view) {
        startActivity(new Intent(this, Insert.class));
    }

    public void jsonParse(){
        if(networkInfo != null && networkInfo.isConnected()){
            String name, url;
            int id;

            try {
                jsonObject = new JSONObject(json_string);
                int count = 0;
                jsonArray = jsonObject.getJSONArray("server_response");
                while (count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    id = JO.getInt("id");
                    name = JO.getString("name");
                    url = JO.getString("url");

                    Contacts contacts = new Contacts(id, name, url);
                    contactAdapter.addAll(contacts);
                    count++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            return;
        }

    }



    public class JsonBackgroundTask extends AsyncTask<Void, Void, String> {

        String jsonURL = "https://hellcrush.000webhostapp.com/jsongetdata2.php";

        public JsonBackgroundTask() {
            super();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(jsonURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream is = httpURLConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder stringBuilder = new StringBuilder();
                while ((jsonString = br.readLine()) != null) {
                    stringBuilder.append(jsonString + "\n");
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
            if(networkInfo == null){
                return;
            }
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            /*TextView tv = (TextView)findViewById(R.id.json_text);
            tv.setText(result);*/
            contactAdapter.clear();
            contactAdapter.notifyDataSetChanged();
            json_string = result;
            mSwipeRefreshLayout.setRefreshing(false);
            jsonParse();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(networkInfo != null){
            new JsonBackgroundTask().execute();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(networkInfo != null){
            new JsonBackgroundTask().execute();
        }
    }
}
