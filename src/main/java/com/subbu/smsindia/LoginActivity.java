package com.subbu.smsindia;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;

    private String provider;

    private EditText username;

    private EditText password;

    private TextView invalidData;

    public static final String LOG_SMSINDIA = "SMS India";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.username_editview);
        password = (EditText) findViewById(R.id.password_editText);
        invalidData = (TextView) findViewById(R.id.invalid_text);
        spinner = (Spinner) findViewById(R.id.spinner);
        addProvider();
        spinner.setOnItemSelectedListener(this);
        Button login = (Button) findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname = username.getText().toString();
                String pwd = password.getText().toString();
                if (uname.equals("") || pwd.equals("")) {

                } else {
                    try {
                        Integer loginResult = new SendMessage().execute(uname, pwd, provider, "", "").get();
                        Log.d(LOG_SMSINDIA, loginResult + "");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void addProvider() {
        ArrayList<String> provider = new ArrayList<String>();
        provider.add("Indyarocks");
        provider.add("Way2Sms");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, provider);
        spinner.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        provider = adapterView.getItemAtPosition(i).toString();

        Toast.makeText(adapterView.getContext(), "You selected Month is: " + provider,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

class SendMessage extends AsyncTask<String, Void, Integer> {

    @Override
    protected Integer doInBackground(String... objects) {
        Integer result = -1;
        String username = objects[0];
        String password = objects[1];
        String provider = objects[2];
        String to = objects[3];
        String message = objects[4];
        String url = "http://ubaid.tk/sms/sms.aspx?uid=" + username + "&pwd="
                + password + "&msg=" + message + "&phone=" + to + "&provider=" + provider;
        URLConnection connection = null;
        try {
            connection = new URL(url).openConnection();
            connection.setRequestProperty("Accept-Charset", "utf-8");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream response = null;
        if (connection != null) {
            try {
                response = connection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(response));
        try {
            result = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}

