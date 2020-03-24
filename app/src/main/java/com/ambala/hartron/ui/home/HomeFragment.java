package com.ambala.hartron.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ambala.hartron.BuildConfig;
import com.ambala.hartron.DatabaseHelper;
import com.ambala.hartron.LoginActivity;
import com.ambala.hartron.R;
import com.ambala.hartron.StudentCornerActivity;
import com.ambala.hartron.WebViewActivity;

public class HomeFragment extends Fragment {

    private final String TAG = "HomeFragement";
    private ImageView hiit_iv;
    private ImageView hartron_iv;
    private HomeViewModel homeViewModel;
    private View center_Info;
    private View course_detail;
    private View login;
    private View photo_gallery;
    private View reach_us;
    private View placement_record;
    private View share_and_discount;

    DatabaseHelper mydb;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        mydb = new DatabaseHelper(getContext());

        hartron_iv = root.findViewById(R.id.hartron_banner);
        hiit_iv = root.findViewById(R.id.hiit_banner);

        final Cursor cursor = mydb.checkWhereUserExists();
        if (cursor.moveToNext()) {
            if (cursor.getString(5).equalsIgnoreCase("student_hiit")) {
                hartron_iv.setVisibility(View.GONE);
                hiit_iv.setVisibility(View.VISIBLE);
            }
        }

        // MYCODE
        center_Info = root.findViewById(R.id.center_info_LL);
        center_Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!haveNetwork()) {
                    Toast.makeText(getContext(), "No Internet Access", Toast.LENGTH_SHORT).show();
                } else {
                    String url = "http://103.87.24.58/hartron/android/AndroidCenterInfo.aspx";
                    Intent intent = new Intent(getContext(), WebViewActivity.class);
                    intent.putExtra("URL_STRING", url);
                    intent.putExtra("TITLE","Center Info");
                    startActivity(intent);
                }
            }
        });

        course_detail = root.findViewById(R.id.course_detail_LL);
        course_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!haveNetwork()) {
                    Toast.makeText(getContext(), "No Internet Access", Toast.LENGTH_SHORT).show();
                } else {
                    String url = "http://103.87.24.58/hartron/android/courses.aspx";
                    Intent intent = new Intent(getContext(), WebViewActivity.class);
                    intent.putExtra("URL_STRING", url);
                    intent.putExtra("TITLE","Course Detail");
                    startActivity(intent);
                }
            }
        });

        login = root.findViewById(R.id.login_LL);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseHelper mydb = new DatabaseHelper(getContext());
                Cursor cursor = mydb.checkWhereUserExists();
                if (cursor.moveToNext()) {
                    /*
                    Intent intent = new Intent(getContext(), StudentCornerActivity.class);
                    intent.putExtra("SERIAL_NO",cursor.getString(2));
                    startActivity(intent);

                     */
                    String serial_no = cursor.getString(2);
                    String user_type = cursor.getString(5);
                    Log.e("SANTOSH", "onCreate: FETCHED DATA: SERIAL NO: " + serial_no + " USER_TYPE  = " + user_type);

                    if (user_type.equalsIgnoreCase("student_hiit") || user_type.equalsIgnoreCase("student_hartron")) {

                        Intent intent = new Intent(getContext(), StudentCornerActivity.class);
                        intent.putExtra("SERIAL_NO", serial_no);
                        startActivity(intent);

                    } else if (user_type.equalsIgnoreCase("faculty")) {

                        String url = "http://103.87.24.58/hartron/android/ComingSoonFaculty.aspx";
                        Intent intent = new Intent(getContext(), WebViewActivity.class);
                        intent.putExtra("URL_STRING", url);
                        intent.putExtra("TITLE","Faculty Corner");
                        startActivity(intent);

                    } else if (user_type.equalsIgnoreCase("admin")) {

                        String url = "http://103.87.24.58/hartron/android/ComingSoonAdmin.aspx";
                        Intent intent = new Intent(getContext(), WebViewActivity.class);
                        intent.putExtra("URL_STRING", url);
                        intent.putExtra("TITLE","Admin Corner");
                        startActivity(intent);

                    }
                } else {
                    Log.e("SANTOSH", "user does not exist");

                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        photo_gallery = root.findViewById(R.id.photo_gallery_LL);
        photo_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!haveNetwork()) {
                    Toast.makeText(getContext(), "No Internet Access", Toast.LENGTH_SHORT).show();
                } else {

                    //String url = "http://103.87.24.58/hartron/android/HartronGallery.aspx?cid=0";
                    String url = "https://photos.app.goo.gl/nTCB6hd8VtEByMGS9";

                    Cursor cursor1 = mydb.getCompanyInfo();
                    if (cursor1.moveToNext()){
                        url = cursor1.getString(9);
                    }

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);

                    /*
                    Intent intent = new Intent(getContext(), WebViewActivity.class);
                    intent.putExtra("URL_STRING", url);
                    intent.putExtra("TITLE","Photo Gallery");
                    startActivity(intent);
                    */

                    /*
                    String url = "https://photos.app.goo.gl/nTCB6hd8VtEByMGS9";
                    PackageManager packageManager = getContext().getPackageManager();
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (intent.resolveActivity(packageManager) != null){
                        startActivity(intent);
                    }
                    */
                }
            }
        });

        reach_us = root.findViewById(R.id.reach_us_LL);
        reach_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!haveNetwork()) {
                    Toast.makeText(getContext(), "No Internet Access", Toast.LENGTH_SHORT).show();

                } else {
                    String url = "https://goo.gl/maps/TpbMiP69QtDdfxDU6";
                    /*
                    Intent intent = new Intent(getContext(), WebViewActivity.class);
                    intent.putExtra("URL_STRING", url);
                    startActivity(intent);

                     */
                    PackageManager packageManager = getContext().getPackageManager();
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent);
                    }
                }
            }
        });

        placement_record = root.findViewById(R.id.placement_record_LL);
        placement_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!haveNetwork()) {
                    Toast.makeText(getContext(), "No Internet Access", Toast.LENGTH_SHORT).show();
                } else {

                    String url = "http://103.87.24.58/hartron/android/Placementrecords.aspx?cid=0";
                    Intent intent = new Intent(getContext(), WebViewActivity.class);
                    intent.putExtra("URL_STRING", url);
                    intent.putExtra("TITLE","Placement Record");
                    startActivity(intent);

                }
            }
        });

        share_and_discount = root.findViewById(R.id.share_discount_LL);
        share_and_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "@string/app_name");
                    String shareMessage = "\nPlease install Hartron App on your mobile\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });


        return root;
    }

    private boolean haveNetwork() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}