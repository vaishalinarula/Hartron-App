package com.ambala.hartron.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ambala.hartron.DatabaseHelper;
import com.ambala.hartron.R;

import java.util.List;

public class NotificationsFragment extends Fragment {

    private ImageView insta_img_view;
    private ImageView facebook_img_view;
    private ImageView twitter_img_view;
    private ImageView youtube_img_view;

    private NotificationsViewModel notificationsViewModel;

    DatabaseHelper mydb;

    private String mInstagram_link;
    private String mFacebook_link;
    private String mTweeter_link;
    private String mYoutube_link;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        mydb = new DatabaseHelper(getContext());
        getDataFromCompanyInfo();

        insta_img_view = root.findViewById(R.id.insta_img_view);
        insta_img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!haveNetwork()) {
                    Toast.makeText(getContext(), "No Internet Access", Toast.LENGTH_SHORT).show();
                } else {

                    /*
                    String url = "https://www.instagram.com/hartronskillcentreambala/";
                    Intent intent = new Intent(getContext(), WebViewActivity.class);
                    intent.putExtra("URL_STRING", url);
                    intent.putExtra("TITLE","Instagram");
                    startActivity(intent);

                     */
                    //Uri uri = Uri.parse("https://www.instagram.com/hartronskillcentreambala/");
                    Uri uri = Uri.parse("" + mInstagram_link);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setPackage("com.instagram.android");

                    if (isIntentAvailable(getContext(), intent)) {
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    }
                }
            }
        });

        facebook_img_view = root.findViewById(R.id.facebook_img_view);
        facebook_img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!haveNetwork()) {
                    Toast.makeText(getContext(), "No Internet Access", Toast.LENGTH_SHORT).show();
                } else {

                    /*
                    String url = "https://m.facebook.com/Hartronambalacity02/";
                    Intent intent = new Intent(getContext(), WebViewActivity.class);
                    intent.putExtra("URL_STRING", url);
                    intent.putExtra("TITLE","Facebook");
                    startActivity(intent);

                     */
                    // PackageManager packageManager = getContext().getPackageManager();

                    //String url = "https://www.facebook.com/Hartronambalacity02/";
                    String url = "" + mFacebook_link;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);

                }
            }
        });

        twitter_img_view = root.findViewById(R.id.twitter_img_view);
        twitter_img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!haveNetwork()) {
                    Toast.makeText(getContext(), "No Internet Access", Toast.LENGTH_SHORT).show();
                } else {
                    String url = "" + mTweeter_link;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }
            }
        });

        youtube_img_view = root.findViewById(R.id.youtube_img_view);
        youtube_img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!haveNetwork()) {
                    Toast.makeText(getContext(), "No Internet Access", Toast.LENGTH_SHORT).show();
                } else {
                    String url = "" + mYoutube_link;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }
            }
        });

        return root;
    }

    private void getDataFromCompanyInfo() {
        Cursor cursor = mydb.getCompanyInfo();
        if (cursor.moveToNext()) {
            mInstagram_link = cursor.getString(5);
            mFacebook_link = cursor.getString(6);
            mTweeter_link = cursor.getString(7);
            mYoutube_link = cursor.getString(8);
        }
    }

    private boolean isIntentAvailable(Context ctx, Intent intent) {
        final PackageManager packageManager = ctx.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private boolean haveNetwork() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}