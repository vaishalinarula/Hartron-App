package com.ambala.hartron;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private ImageView hartron_iv;
    private ImageView hiit_iv;

    private RadioGroup user_type_rg;
    private AppCompatRadioButton hartron_rb;
    private AppCompatRadioButton hiit_rb;
    private AppCompatRadioButton faculty_rb;
    private AppCompatRadioButton admin_rb;
    private AppCompatTextView hint_msg_tv;
    private EditText user_id_et;
    private EditText password_et;
    private Button login_button;
    private ProgressBar login_progress_bar;

    DatabaseHelper mydb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();      // REQUIRED TO MAKE CHANGES ON ACTION BAR
        //actionBar.setTitle(getResources().getString(R.string.app_name));
        actionBar.setTitle("Login");
        //actionBar.hide();

        mydb = new DatabaseHelper(this);

        hartron_iv = findViewById(R.id.login_banner_hartron);
        hiit_iv = findViewById(R.id.login_banner_hiit);
        user_type_rg = findViewById(R.id.user_type_rg);
        hartron_rb = findViewById(R.id.hartron_rb);
        hiit_rb = findViewById(R.id.hiit_rb);
        faculty_rb = findViewById(R.id.faculty_rb);
        admin_rb = findViewById(R.id.admin_rb);
        hint_msg_tv = findViewById(R.id.hint_msg_tv);
        user_id_et = findViewById(R.id.user_id_et);
        password_et = findViewById(R.id.password_et);
        login_button = findViewById(R.id.login_button);
        login_progress_bar = findViewById(R.id.login_progress_bar);

        setBannerAndPrintHintMsg();
        userLogin();
    }

    private void setBannerAndPrintHintMsg() {
        user_type_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (hartron_rb.isChecked()) {
                    hartron_iv.setVisibility(View.VISIBLE);
                    hiit_iv.setVisibility(View.GONE);

                    hint_msg_tv.setVisibility(View.VISIBLE);
                    hint_msg_tv.setText("Hartron Student User Id & Password(*)");
                } else if (hiit_rb.isChecked()) {
                    hartron_iv.setVisibility(View.GONE);
                    hiit_iv.setVisibility(View.VISIBLE);

                    hint_msg_tv.setVisibility(View.VISIBLE);
                    hint_msg_tv.setText("HIIT Student User Id & Password(*)");
                } else if (faculty_rb.isChecked()) {

                    hartron_iv.setVisibility(View.VISIBLE);
                    hiit_iv.setVisibility(View.GONE);

                    hint_msg_tv.setVisibility(View.VISIBLE);
                    hint_msg_tv.setText("Faculty User Id & Password(*)");
                } else if (admin_rb.isChecked()) {
                    hartron_iv.setVisibility(View.VISIBLE);
                    hiit_iv.setVisibility(View.GONE);

                    hint_msg_tv.setVisibility(View.VISIBLE);
                    hint_msg_tv.setText("Admin User Id & Password(*)");
                }
            }
        });
    }

    private void userLogin() {
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SANTOSH", "HELLO JI: ");
                if (!haveNetwork()) {
                    Toast.makeText(LoginActivity.this, "No Internet Access", Toast.LENGTH_SHORT).show();
                    return;
                } else if (hartron_rb.isChecked() || hiit_rb.isChecked() || faculty_rb.isChecked() || admin_rb.isChecked()) {

                    final String user_id = user_id_et.getText().toString().trim();
                    final String password = password_et.getText().toString().trim();
                    String u_type = "student_hartron";
                    if(hartron_rb.isChecked()){
                        u_type = "student_hartron";
                    }else if(hiit_rb.isChecked()){
                        u_type = "student_hiit";
                    }else if(faculty_rb.isChecked()){
                        u_type = "faculty";
                    }else if(admin_rb.isChecked()){
                        u_type = "admin";
                    }
                    final String user_type = u_type;
                    Log.d("SANTOSH", "HELLO JI 2: " + user_id + " " + password);

                    if (user_id.length() == 0 || password.length() == 0) {
                        Log.d("SANTOSH", "INSIDE IF " + user_id + " " + password);

                        Toast.makeText(LoginActivity.this, "Enter valid user id or password", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("SANTOSH", " INSIDE ELSE " + user_id + " " + password);

                        login_progress_bar.setVisibility(View.VISIBLE);

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(JsonPlaceHolderApi.mBASEURL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        JsonPlaceHolderApi api = retrofit.create(JsonPlaceHolderApi.class);
                        Call<List<UserLoginPost>> call = api.getUserLoginStatus(user_id, password);
                        call.enqueue(new Callback<List<UserLoginPost>>() {
                            @Override
                            public void onResponse(Call<List<UserLoginPost>> call, Response<List<UserLoginPost>> response) {
                                if (!response.isSuccessful()) {
                                    login_progress_bar.setVisibility(View.INVISIBLE);
                                    Log.d("SANTOSH", "onResponse: " + response.code());
                                    Toast.makeText(getApplicationContext(), "Server Problem \n Response code : " + response.code(), Toast.LENGTH_LONG).show();
                                    return;
                                }

                                login_progress_bar.setVisibility(View.INVISIBLE);

                                List<UserLoginPost> userLoginPostList = response.body();
                                for (UserLoginPost userLoginPost : userLoginPostList) {

                                    if (userLoginPost.getFlag().equalsIgnoreCase("TRUE")) {

                                        // DO YOUR STUFF HERE
                                        /** INSERT DATA TO SQL LITE DATABASE*/

                                        Log.e("SANTOSH", "onResponse: flag : " + userLoginPost.getFlag() + " srno : " + userLoginPost.getSrno() + " name : " + userLoginPost.getName() + " course id : " + userLoginPost.getCourseid() + " user_type : " + userLoginPost.getUser_type());

                                        boolean status = mydb.insertLoginTable(user_id, password, userLoginPost.getSrno(), userLoginPost.getName(), userLoginPost.getCourseid(), userLoginPost.getUser_type());
                                        if (status) {
                                            Log.d("SANTOSH", "onResponse: data inserted " + status);
                                        } else {
                                            Log.d("SANTOSH", "onResponse: problem in insertion " + status);
                                        }

                                        if( (user_type.equalsIgnoreCase("student_hartron") && user_type.equalsIgnoreCase(""+userLoginPost.getUser_type())) || (user_type.equalsIgnoreCase("student_hiit") && user_type.equalsIgnoreCase(""+userLoginPost.getUser_type()))){
                                            Intent intent = new Intent(LoginActivity.this, StudentCornerActivity.class);
                                            intent.putExtra("SERIAL_NO", userLoginPost.getSrno());
                                            startActivity(intent);
                                            LoginActivity.this.finish();
                                        }else if(user_type.equalsIgnoreCase("faculty") && user_type.equalsIgnoreCase(""+userLoginPost.getUser_type())){

                                            String url = "http://103.87.24.58/hartron/android/ComingSoonFaculty.aspx";
                                            Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
                                            intent.putExtra("URL_STRING", url);
                                            intent.putExtra("TITLE","Faculty Corner");
                                            startActivity(intent);

                                        }else if (user_type.equalsIgnoreCase("admin") && user_type.equalsIgnoreCase(""+userLoginPost.getUser_type())){

                                            String url = "http://103.87.24.58/hartron/android/ComingSoonAdmin.aspx";
                                            Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
                                            intent.putExtra("URL_STRING", url);
                                            intent.putExtra("TITLE","Admin Corner");
                                            startActivity(intent);

                                        }else {
                                            if (mydb.deleteRecordFromLoginTable() != -1){
                                                Log.d("LoginActivity","User Record deleted");
                                            }else{
                                                Log.d("LoginActivity","Problem in deletion of user or no record exist");
                                            }
                                            Toast.makeText(LoginActivity.this, " Seems you have selected invalid user type \n Please select valid user type", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Log.d("SANTOSH", "onResponse: flag =  " + userLoginPost.getFlag());
                                        Toast.makeText(getApplicationContext(), "Invalid user Id or password", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onFailure(Call<List<UserLoginPost>> call, Throwable t) {
                                login_progress_bar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "Inactive Network Or Server Problem", Toast.LENGTH_LONG).show();
                                Log.d("SANTOSH", "Login onFailure: " + t.getMessage());
                            }
                        });
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Choose User Type", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean haveNetwork() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
