package sky.onlinedatabasesample;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
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

    String json_string;
    String jsonString;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ContactAdapter contactAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_list_view);
        json_string = getIntent().getExtras().getString("json_data");

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);

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
                if (contactAdapter != null) {
                    contactAdapter.clear();
                    new JsonBackgroundTask().execute();
                }
            }
        });

        jsonParse();


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Contacts contacts = contactAdapter.getItem(position);
                String mName = contacts.getName();
                Toast.makeText(getBaseContext(), mName, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void userReg(View view) {
        startActivity(new Intent(this, Insert.class));
    }

    public void jsonParse(){
        String name, email, contact, password;

        try {
            jsonObject = new JSONObject(json_string);
            int count = 0;
            jsonArray = jsonObject.getJSONArray("server_response");
            while (count < jsonArray.length()) {
                JSONObject JO = jsonArray.getJSONObject(count);
                name = JO.getString("name");
                email = JO.getString("email");
                contact = JO.getString("contact");
                password = JO.getString("password");

                Contacts contacts = new Contacts(name, email, contact, password);
                contactAdapter.addAll(contacts);
                count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class JsonBackgroundTask extends AsyncTask<Void, Void, String> {

        String jsonURL = "https://hellcrush.000webhostapp.com/jsongetdata.php";

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
}
