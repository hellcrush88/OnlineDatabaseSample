package sky.onlinedatabasesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class Insert extends AppCompatActivity {
    EditText e_name, e_password, e_contact, e_email;
    String name, pass, contact, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        e_name = (EditText) findViewById(R.id.new_name);
        e_email = (EditText) findViewById(R.id.new_user_email);
        e_contact = (EditText) findViewById(R.id.new_user_contact);
        e_password = (EditText) findViewById(R.id.new_user_password);
    }

    public void userReg(View view){
        name = e_name.getText().toString().trim();
        pass = e_password.getText().toString().trim();
        contact = e_contact.getText().toString().trim();
        email = e_email.getText().toString().trim();

        String method = "register";
        BackgroundTask bgTask = new BackgroundTask(this);
        bgTask.execute(method, name, email, contact, pass);
        finish();
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
