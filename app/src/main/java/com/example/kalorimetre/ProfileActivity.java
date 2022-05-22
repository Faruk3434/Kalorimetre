package com.example.kalorimetre;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private int userid =SharedPrefManager.getInstance(this).getKeyUserId();
    private TextView textViewUsername, textViewUserEmail;
    private ListView listview;
    private Adapter adapter;
    Button buttonrefresh;
    Button buttonclear;
    Meals meals;
    public static ArrayList<Meals> mealsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        textViewUsername  = (TextView) findViewById(R.id.textViewUsername);
        listview = findViewById(R.id.lv);
        textViewUsername.setText(SharedPrefManager.getInstance(this).getUsername());
        adapter =new Adapter(this, mealsArrayList);
        listview.setAdapter(adapter);
        buttonrefresh = findViewById(R.id.buttonrefresh);
        buttonclear = findViewById(R.id.buttonclear);
        
        loadMeals();

    }

    private void clearMeals(){
        StringRequest stringRequest = new
                StringRequest(Request.Method.POST,
                Constants.URL_CLEAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getApplicationContext(), "Succesfully Cleared", Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("userid",String.valueOf(userid));
                        return params;
                    }
                };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void loadMeals() {
        StringRequest stringRequest = new
                StringRequest(Request.Method.POST,
                Constants.URL_LISTMYMEALS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mealsArrayList.clear();

                        try {
                            JSONArray object = new JSONArray(response);

                            for(int i=0; i<object.length(); i++){
                                JSONObject mealsobject = object.getJSONObject(i);
                                String listID =String.valueOf(i+1);
                                String ID = mealsobject.getString("id");
                                String products = mealsobject.getString("products");
                                String calorie = mealsobject.getString("calorie");

                                meals = new Meals(listID,ID,products,calorie);
                                mealsArrayList.add(meals);
                                adapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("userid",String.valueOf(userid));
                        return params;
                    }
                };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuLogout:
                SharedPrefManager.getInstance(this).logout();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.menuCalorimeter:
                startActivity(new Intent(this, CalculateActivity.class));
                break;
        }
        return true;
    }

    public void activate(View view) {
        if(view == buttonrefresh){
            mealsArrayList.clear();
            adapter.notifyDataSetChanged();
            loadMeals();
        }

        if(view == buttonclear){
            mealsArrayList.clear();
            adapter.notifyDataSetChanged();
            loadMeals();
            clearMeals();

        }
    }
}