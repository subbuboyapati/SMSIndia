package com.subbu.smsindia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

/**
 * Created by lavi on 7/2/13.
 */
public class MessageActivity extends Activity {

    private String username;

    private String password;

    private String provider;

    private String phoneNo;

    private String message;

    private EditText msg;

    private EditText phNo;

    private TextView response;

    private ImageButton logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        provider = intent.getStringExtra("provider");

        phNo = (EditText) findViewById(R.id.phone_edit);
        msg = (EditText) findViewById(R.id.msg_edit);
        response = (TextView) findViewById(R.id.response_text);

        Button sendBtn = (Button) findViewById(R.id.send_button);

        logout = (ImageButton) findViewById(R.id.logout_button);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNo = phNo.getText().toString();
                message = msg.getText().toString();

                Integer result = -1;
                try {
                    result = new SendMessage().execute(username, password, provider, phoneNo, message).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if (result == 1) {
                    response.setText("Message send successfully");
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });
    }

    public void logout() {
        username = null;
        password = null;
        provider = null;
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        createDialog();
//        logout();
    }

    private void createDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Confirm Exit");
        alertDialog.setMessage("Do you want to logout?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.exit(0);
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        alertDialog.show();
    }
}
