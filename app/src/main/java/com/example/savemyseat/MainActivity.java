package com.example.savemyseat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //declaring controls
    private EditText editTextCompName, editTextLocation, editTextCompType, editTextCompPhone;
    private Button buttonRegister;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assigning controls to layout controls
        editTextCompName = (EditText) findViewById(R.id.editTextCompName);
        editTextLocation = (EditText) findViewById(R.id.editTextLocation);
        editTextCompType = (EditText) findViewById(R.id.editTextCompType);
        editTextCompPhone = (EditText) findViewById(R.id.editTextCompPhone);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        progressDialog = new ProgressDialog(this);
        //activates the button action
        buttonRegister.setOnClickListener(this);
    }

    private void registerPremises(){
        //capturing user inputs
        String compName = editTextCompName.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        String compType = editTextCompType.getText().toString().trim();
        //Parsing String to suit variable type in DB
        String compPhone = editTextCompPhone.getText().toString().trim();

        //String Request defined within volley, link between PHP file and XAMPP DB through the use of volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    progressDialog.dismiss();

                    try{
                        JSONObject jsonObject = new JSONObject(response);

                        //displaying message to user from PHP file (DbOperations.php)
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    //linking error handling to messages created in PHP file also
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }


                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //hash map tp identify the variables for the DB connection
                Map<String,String> params = new HashMap<>();
                params.put("compName", compName);
                params.put("location", location);
                params.put("compType", compType);
                params.put("compPhone", compPhone);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    //action for when button is clicked
    public void onClick(View view) {
        if(view == buttonRegister)
            registerPremises();
    }
}