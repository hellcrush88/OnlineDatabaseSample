package sky.onlinedatabasesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class Insert extends AppCompatActivity {
    EditText e_name, e_url;
    String name, url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        e_name = (EditText) findViewById(R.id.new_name);
        e_url = (EditText)findViewById(R.id.new_url);
    }

    public void userReg(View view){
        name = e_name.getText().toString().trim();
        url = e_url.getText().toString().trim();
        if(Patterns.WEB_URL.matcher(url).matches()){
            String method = "register";
            BackgroundTask bgTask = new BackgroundTask(this);
            bgTask.execute(method, "0" , name, url);
            finish();
        }else{
            Toast.makeText(this, "Please enter valid URL with http:/ format", Toast.LENGTH_LONG).show();
        }
    }

/*
    EditText textField = (EditText) findViewById(R.id.webaddressURL);
    String enteredUrl = textField.getText().toString();
  if (Patterns.WEB_URL.matcher(enteredUrl).matches()) {
        Toast.makeText(this, "URL is valid!", Toast.LENGTH_LONG).show();
    }
  else {
        Toast.makeText(this, "URL is invalid!", Toast.LENGTH_LONG).show();
    }
    */
}
