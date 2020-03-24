package com.ambala.hartron;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements WSCallerVersionListener {

    private static final String TAG = "MainActivity";
    boolean isForceUpdate = false;

    private ProgressBar progressBar;
    DatabaseHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydb = new DatabaseHelper(this);

        progressBar = findViewById(R.id.main_activity_progressBar);

        /***
         * INITIAL DOWNLOAD
         */
        defaultDataInsertionIntoCompanyInfo();
        initialDownload();

        if (haveNetwork())
            new GooglePlayStoreAppVersionNameLoader(getApplicationContext(), this).execute();


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    /** CODE FOR UPDATE POP-UP **/
    @Override
    public void onGetResponse(boolean isUpdateAvailable) {
        Log.e("ResultAPPMAIN", String.valueOf(isUpdateAvailable));
        if (isUpdateAvailable) {
            showUpdateDialog();
        }
    }

    /**
     * Method to show update dialog
     */
    public void showUpdateDialog() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MainActivity.this);

        alertDialogBuilder.setTitle(MainActivity.this.getString(R.string.app_name));
        alertDialogBuilder.setMessage(MainActivity.this.getString(R.string.update_message));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.update_now, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                dialog.cancel();
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isForceUpdate) {
                    finish();
                }
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
    }

    private void defaultDataInsertionIntoCompanyInfo(){
        Cursor cursor = mydb.getCompanyInfo();
        if (!cursor.moveToNext()){
            boolean status = mydb.insertDefaultDataIntoCompanyInfoTable();
            if (status){
                Log.d(TAG, "defaultDataInsertionIntoCompanyInfo: DATA INSERTED");
            }else {
                Log.e(TAG, "defaultDataInsertionIntoCompanyInfo: PROBLEM IN INSERTION OF DATA");
            }
        }else{
            Log.d(TAG, "defaultDataInsertionIntoCompanyInfo: DEFAULT DATA ALREADY EXISTS");
        }
    }

    private void initialDownload(){

            if (!haveNetwork()){
                Log.e(TAG, "NO INTERNET");
            }else{
                progressBar.setVisibility(View.VISIBLE);

                Retrofit retrofit = new Retrofit.Builder()
                        //   .baseUrl("http://swachhnoida.000webhostapp.com/")
                        .baseUrl(JsonPlaceHolderApi.mBASEURL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                Call<List<CompanyInfoPost>> call = jsonPlaceHolderApi.getCompanyInfo("1");
                call.enqueue(new Callback<List<CompanyInfoPost>>() {
                    @Override
                    public void onResponse(Call<List<CompanyInfoPost>> call, Response<List<CompanyInfoPost>> response) {
                        if (!response.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, " Initial Downloading Failed\n Response code : " + response.code(), Toast.LENGTH_LONG).show();
                            Log.d(TAG, "onResponse: response code: " + response.code());
                            return;
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                        List<CompanyInfoPost> companyInfoPostList = response.body();
                        if (companyInfoPostList.size() > 0){
                            CompanyInfoPost companyInfoPost = companyInfoPostList.get(0);
                            Log.d(TAG, "onResponse: DATA RETRIVED FROM SERVER: whatsapp no: "+companyInfoPost.getWhatsapp_no()+" : gmail : "+companyInfoPost.getGmail_address()+" : enquiry no: "+companyInfoPost.getEnquiry_no()+" : landline no : "+companyInfoPost.getLandline_no()+" : admin no : "+companyInfoPost.getAdministration_no()+" : inst link : "+companyInfoPost.getInstagram_link()+" : fb link : "+companyInfoPost.getFacebook_link()+" : twitter : "+companyInfoPost.getTwitter_link()+" : youtube : "+companyInfoPost.getYoutube_link()+ " : gallery :"+companyInfoPost.getGallery_link()+" : last_updated :"+companyInfoPost.getLast_updated());

                            Cursor cursor = mydb.getCompanyInfo();
                            if (cursor.moveToNext()){
                                String last_updated = cursor.getString(10);
                                Log.e("SANTOSH", "initialDownload: FETCHED DATA FROM DATABASE : "+last_updated+" : FROM SERVER: "+companyInfoPost.getLast_updated());
                                if (!last_updated.equalsIgnoreCase(companyInfoPost.getLast_updated())){
                                    boolean status = mydb.updateCompanyInfoTable(""+companyInfoPost.getWhatsapp_no(),""+companyInfoPost.getGmail_address(),""+companyInfoPost.getEnquiry_no(),""+companyInfoPost.getLandline_no(),""+companyInfoPost.getAdministration_no(),""+companyInfoPost.getInstagram_link(),""+companyInfoPost.getFacebook_link(),""+companyInfoPost.getTwitter_link(),""+companyInfoPost.getYoutube_link(),""+companyInfoPost.getGallery_link(),""+companyInfoPost.getLast_updated());
                                    if (status) {
                                        Log.d(TAG, "CONTENT UPDATED INTO COMPANY INFO TABLE");
                                    } else {
                                        Log.e(TAG, "PROBLEM UPDATE INTO COMPANY INFO TABLE");
                                    }
                                }else {
                                    Log.d(TAG, "onResponse: COMPANY INFO IS ALREADY UP TO DATE");
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CompanyInfoPost>> call, Throwable t) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, " Initial Downloading Failed\n Slow network or Server Problem", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "RESPONSE FAILED WHILE RETRIVING COMPANY INFO " + t.getMessage());
                    }
                });
            }
       // }

    }

    private boolean haveNetwork()  {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
