package com.ambala.hartron.ui.dashboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ambala.hartron.DatabaseHelper;
import com.ambala.hartron.R;

public class DashboardFragment extends Fragment {


    private static final int REQUEST_CALL = 1;
    private DashboardViewModel dashboardViewModel;

    private ImageView whatsapp_img_view;
    private TextView whatsapp_no_tv;

    private ImageView gmail_img_view;
    private TextView gmail_add_tv;

    private ImageView enquiry_img_view;
    private TextView enquiry_no_tv;

    private ImageView landline_no_img_view;
    private TextView landline_no_tv;

    private ImageView admin_no_img_view;
    private TextView admin_no_tv;

    private String number;

    DatabaseHelper mydb;

    private String mWhatsapp_no;
    private String mGmail_add;
    private String mEnquiry_no;
    private String mLandline_no;
    private String mAdmin_no;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        mydb = new DatabaseHelper(getContext());

        whatsapp_img_view = root.findViewById(R.id.whatsapp_img_view);
        whatsapp_no_tv = root.findViewById(R.id.whatsapp_no_tv);
        gmail_img_view = root.findViewById(R.id.mail_img_view);
        gmail_add_tv = root.findViewById(R.id.gmail_add_tv);
        enquiry_img_view = root.findViewById(R.id.enquiry_img_view);
        enquiry_no_tv = root.findViewById(R.id.enquiry_no_tv);
        landline_no_img_view = root.findViewById(R.id.landline_no_img_view);
        landline_no_tv = root.findViewById(R.id.landline_no_tv);
        admin_no_img_view = root.findViewById(R.id.admin_no_img_view);
        admin_no_tv = root.findViewById(R.id.admin_no_tv);

        getDataFromCompanyInfo();
        setDataToContactUs();


        whatsapp_img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager packageManager = getContext().getPackageManager();
                //String whatsapp_number = "+91 8930511211";
                String whatsapp_number = ""+mWhatsapp_no;
                Uri uri = Uri.parse("smsto:"+whatsapp_number);
                Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
                intent.setPackage("com.whatsapp");
                if (intent.resolveActivity(packageManager) != null){
                    startActivity(intent);
                }
            }
        });


        gmail_img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // String[] to = {"sahu18456@gmail.com"};
               // String[] to = {"cityhartron@gmail.com"};
                String[] to = {""+mGmail_add};
                Intent intent =  new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL,to);
               // intent.putExtra(Intent.EXTRA_SUBJECT,"MyOrder For "+name);
              //  intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
                startActivity(intent);
            }
        });


        enquiry_img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //number = "8930511211";
                number = mEnquiry_no;
                makePhoneCall();
            }
        });


        landline_no_img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //number = "0171255006";
                number = mLandline_no;
                makePhoneCall();
            }
        });


        admin_no_img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //number = "9996669962";
                number = mAdmin_no;
                makePhoneCall();
            }
        });


        return root;
    }

    private void getDataFromCompanyInfo(){
        Cursor cursor = mydb.getCompanyInfo();
        if (cursor.moveToNext()){
            mWhatsapp_no = cursor.getString(0);
            mGmail_add = cursor.getString(1);
            mEnquiry_no = cursor.getString(2);
            mLandline_no = cursor.getString(3);
            mAdmin_no = cursor.getString(4);
        }
    }

    private void setDataToContactUs(){
        whatsapp_no_tv.setText(mWhatsapp_no);
        gmail_add_tv.setText(mGmail_add);;
        enquiry_no_tv.setText(mEnquiry_no);
        landline_no_tv.setText(mLandline_no);
        admin_no_tv.setText(mAdmin_no);
    }

    private void makePhoneCall(){
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
        }else {
            String dial = "tel:"+number;
            startActivity(new Intent(Intent.ACTION_CALL,Uri.parse(dial)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makePhoneCall();
            }else {
                Toast.makeText(getContext(),"Permission DENIED",Toast.LENGTH_SHORT).show();
            }
        }
    }
}