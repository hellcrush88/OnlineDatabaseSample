package sky.onlinedatabasesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DisplayListView extends AppCompatActivity {

    String json_string;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ContactAdapter contactAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list_view);
        json_string = getIntent().getExtras().getString("json_data");

        contactAdapter = new ContactAdapter(this, R.layout.row_layout);
        ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(contactAdapter);


        String name, email, contact, password;

        try {
            jsonObject = new JSONObject(json_string);
            int count = 0;
            jsonArray = jsonObject.getJSONArray("server_response");
            while(count < jsonArray.length()){
                JSONObject JO = jsonArray.getJSONObject(count);
                name = JO.getString("name");
                email = JO.getString("email");
                contact = JO.getString("contact");
                password = JO.getString("password");

                Contacts contacts = new Contacts(name, email, contact, password);
                contactAdapter.add(contacts);
                count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
