package com.appchitects.hashi.ui.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.appchitects.hashi.HashiApplication;
import com.appchitects.hashi.R;
import com.appchitects.hashi.core.Api;
import com.appchitects.hashi.core.model.Restaurant;
import com.appchitects.hashi.ui.location.GPSTracker;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class SplashScreenActivity extends AppCompatActivity {

    private GPSTracker gpsTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
    }

    @Override
    public void onResume() {
        super.onResume();
        startApp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }

    private void startApp() {
        gpsTracker = HashiApplication.getInstance().getGpsTracker(SplashScreenActivity.this);

        // Letting 5 chances to the gps
        int gpsChances = 5;
        boolean canGetLocation = false;
        while (gpsChances > 0) {
            canGetLocation = gpsTracker.canGetLocation();
            if (canGetLocation) break;
            gpsChances--;
        }

        if (canGetLocation) {
            Location location = gpsTracker.getLocation();
            Api.getRestaurants(
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray jsonRestaurants) {
                            List<Restaurant> restaurants = Api.parseRestaurants(jsonRestaurants);
                            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                            mainIntent.putParcelableArrayListExtra("restaurants", (ArrayList<Restaurant>) restaurants);
                            startActivity(mainIntent);
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Splash Screen Rest", error.getMessage());
                            Toast.makeText(getApplicationContext(), "Unable to reach server, please check your network connection", Toast.LENGTH_SHORT).show();
                        }
                    }, location);

            Api.getIngredients(new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Splash Screen Ing", error.getMessage());
                    Toast.makeText(getApplicationContext(), "Unable to reach server, please check your network connection", Toast.LENGTH_SHORT).show();
                }
            });

            Api.getSideDishes(new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Splash Screen Side", error.getMessage());
                    Toast.makeText(getApplicationContext(), "Unable to reach server, please check your network connection", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            gpsTracker.showSettingsAlert();
        }
    }
}