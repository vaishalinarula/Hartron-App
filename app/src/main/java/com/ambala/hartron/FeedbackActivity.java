package com.ambala.hartron;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FeedbackActivity extends AppCompatActivity {

    private Button submit_btn;
    private ProgressBar progressBar;
    private EditText feedback_ET;
    private String serial_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        ActionBar actionBar = getSupportActionBar();      // REQUIRED TO MAKE CHANGES ON ACTION BAR
        //actionBar.setTitle(getResources().getString(R.string.app_name));
        actionBar.setTitle("Feedback");
        //actionBar.hide();

        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                serial_no = bundle.getString("SERIAL_NO");
            }
        }

        progressBar = findViewById(R.id.submit_progressbar);
        feedback_ET = findViewById(R.id.feedback_ET);

        submit_btn = findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!haveNetwork()){
                    Toast.makeText(FeedbackActivity.this,"No Internet Access",Toast.LENGTH_SHORT).show();
                }else {

                    String feedback = feedback_ET.getText().toString().trim();
                    if (feedback.length()<=0){
                        Toast.makeText(FeedbackActivity.this,"Please Enter valid feedback",Toast.LENGTH_SHORT).show();
                    }else{
                        progressBar.setVisibility(View.VISIBLE);
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(JsonPlaceHolderApi.mBASEURL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        JsonPlaceHolderApi api = retrofit.create(JsonPlaceHolderApi.class);
                        Call<List<FeedbackPost>> call = api.getFeedbackStatus(serial_no,feedback);
                        call.enqueue(new Callback<List<FeedbackPost>>() {
                            @Override
                            public void onResponse(Call<List<FeedbackPost>> call, Response<List<FeedbackPost>> response) {
                                if (!response.isSuccessful()) {
                                    progressBar.setVisibility(View.GONE);
                                    Log.d("SANTOSH", "onResponse: " + response.code());
                                    Toast.makeText(FeedbackActivity.this, "Server Problem \n Response code : " + response.code(), Toast.LENGTH_LONG).show();
                                    return;
                                }
                                Log.e("SANTOSH", "onResponse: BEFORE FOR LOOP ");

                                List<FeedbackPost> feedbackPostList = response.body();
                                Log.e("SANTOSH", "onResponse: "+feedbackPostList.size());

                               // FeedbackPost feedbackPost = feedbackPostList.get(0);
                               // Log.e("SANTOSH", "onResponse: "+feedbackPost.getFlag());


                                for (FeedbackPost feedbackPost : feedbackPostList){
                                    String status = feedbackPost.getFlag();
                                    Log.e("SANTOSH", "onResponse: INSIDE FOR LOOP "+status);
                                    if (status.equalsIgnoreCase("TRUE")){
                                        //  progressBar.setVisibility(View.GONE);

                                        Toast.makeText(getApplicationContext(), "Thanks for the valuable feedback !!!", Toast.LENGTH_LONG).show();
                                        FeedbackActivity.this.finish();
                                    }else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "Failed try again", Toast.LENGTH_LONG).show();
                                        Log.e("SANTOSH", "onResponse: "+feedbackPost.getFlag());
                                    }
                                }

                            }

                            @Override
                            public void onFailure(Call<List<FeedbackPost>> call, Throwable t) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Inactive Network Or Server Problem", Toast.LENGTH_LONG).show();
                                Log.d("SANTOSH", "Login onFailure: "+t.getMessage());
                            }
                        });
                    }
                }
            }
        });

    }

    private boolean haveNetwork() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
