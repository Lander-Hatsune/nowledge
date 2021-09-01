package com.example.nowledge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.nowledge.data.Singleton;
import com.example.nowledge.data.Uris;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.loginButton);
        EditText username = findViewById(R.id.editTextUsername);
        EditText password = findViewById(R.id.editTextPassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue reqQue = Singleton.getInstance
                        (getApplicationContext()).getRequestQueue();
                JSONObject obj = null;
                try {
                    obj = new JSONObject();
                    obj.put("username", username.getText());
                    obj.put("password", password.getText());
                } catch (JSONException e) {
                    Log.e("Login error:", e.toString());
                }

                Log.d("Login obj", obj.toString());
                JsonObjectRequest req = new JsonObjectRequest
                        (Request.Method.POST, Uris.getLogin(),
                                obj, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("Login success.", "");
                                Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Login error:", error.toString());
                            }
                        });
                Log.d("Request:", req.toString());
                reqQue.add(req);
            }
        });
    }
}