package com.example.kalorimetre;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class CalculateActivity extends AppCompatActivity implements View.OnClickListener {
    private int userid =SharedPrefManager.getInstance(this).getKeyUserId();
    private String calorie_db =SharedPrefManager.getInstance(this).getKeyProductCalorie();

    private EditText editTextProduct, editTextQuantity;
    private Button buttonCalculate, buttonAddtoMymeals;
    private ProgressDialog progressDialog;
    private TextView textViewCalorie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);

        editTextProduct = (EditText) findViewById(R.id.editTextProduct);
        editTextQuantity= (EditText) findViewById(R.id.editTextQuantity);
        buttonCalculate = (Button) findViewById(R.id.buttonCalculate);
        buttonAddtoMymeals = (Button) findViewById(R.id.buttonAddtoMymeals);
        textViewCalorie = (TextView) findViewById(R.id.textViewCalorie);




        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        buttonCalculate.setOnClickListener(this);
        buttonAddtoMymeals.setOnClickListener(this);


    }

    private void chooseproducts(){
        final String products = editTextProduct.getText().toString().trim();
        final String quantity_temp = editTextQuantity.getText().toString();


        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_CALCULATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error")){
                                SharedPrefManager.getInstance(getApplicationContext())
                                        .chooseproducts(
                                                obj.getInt("id"),
                                                obj.getString("products"),
                                                obj.getString("calorie")
                                        );


                                Toast.makeText(
                                        getApplicationContext(),
                                        "Product Found",
                                        Toast.LENGTH_LONG
                                ).show();
                                 String calorie_db =SharedPrefManager.getInstance(null).getKeyProductCalorie();
                                final Integer quantity = Integer.parseInt(quantity_temp);
                                final Integer calorie_db_int = Integer.parseInt(calorie_db) * quantity / 100;
                                final String calorie_db_string = String.valueOf(calorie_db_int);
                                textViewCalorie.setText(calorie_db_string);

                            }else{
                                Toast.makeText(
                                        getApplicationContext(),
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Toast.makeText(
                                getApplicationContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("products", products);
                        return params;
            }

        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void addtomymeals(){
        final String products = editTextProduct.getText().toString().trim();
        final String calorie = textViewCalorie.getText().toString().trim();

        progressDialog.setMessage("Being Recorded...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_ADDTOMYMEALS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("userid",String.valueOf(userid));
                params.put("products",products);
                params.put("calorie",calorie);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }




    @Override
    public void onClick(View view) {
        if(view == buttonCalculate)
            chooseproducts();
        if (view == buttonAddtoMymeals)
            addtomymeals();

    }
}