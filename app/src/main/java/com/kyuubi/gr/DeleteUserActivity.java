package com.kyuubi.gr;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.kyuubi.gr.request.DeleteUserRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class DeleteUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);
        final EditText etUserName = (EditText) findViewById(R.id.etUsername);
        final Button bDeteleUser = (Button) findViewById(R.id.bDetele);

        bDeteleUser.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                final String username = etUserName.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Intent intent = new Intent(DeleteUserActivity.this, AdminMainActivity.class);
                                DeleteUserActivity.this.startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(DeleteUserActivity.this);
                                builder.setMessage("Delete Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                DeleteUserRequest deleteUserRequestRequest = new DeleteUserRequest(username,responseListener);
                RequestQueue queue = Volley.newRequestQueue(DeleteUserActivity.this);
                queue.add(deleteUserRequestRequest);
            }
        });
    }
}