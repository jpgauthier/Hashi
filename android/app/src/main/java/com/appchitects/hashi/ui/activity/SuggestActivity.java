package com.appchitects.hashi.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.appchitects.hashi.R;
import com.appchitects.hashi.core.Api;
import org.json.JSONObject;

public class SuggestActivity extends AppCompatActivity {
    private EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        setupToolbar();

        editText = (EditText) findViewById(R.id.suggest_edit_text);
        setupButton();
    }

    private void setupButton() {
        Button button = (Button) findViewById(R.id.suggest_btn_send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                if (text.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please add a suggestion", Toast.LENGTH_LONG).show();
                } else {
                    final ProgressDialog progressDialog = ProgressDialog.show(SuggestActivity.this, "In progress", "Please sit tight!");

                    Api.postSuggestion(new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Thank you for your feedback!", Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Unable to send the suggestion: " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }, text);
                }
            }
        });
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Suggestion");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}