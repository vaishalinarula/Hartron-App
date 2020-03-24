package com.ambala.hartron;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class SplaceScreenActivity extends AppCompatActivity {

    private ImageView hatron_iv;
    private ImageView hiit_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splace_screen);

        getSupportActionBar().hide();

        hatron_iv = findViewById(R.id.splash_screen_hartron_iv);
        hiit_iv = findViewById(R.id.splash_screen_hiit_iv);
        DatabaseHelper mydb = new DatabaseHelper(this);
        Cursor cursor = mydb.checkWhereUserExists();
        if(cursor.moveToNext()){
            if(cursor.getString(5).equalsIgnoreCase("student_hiit")){
                hatron_iv.setVisibility(View.GONE);
                hiit_iv.setVisibility(View.VISIBLE);
            }
        }

        SplashScreenShow s = new SplashScreenShow();
        s.start();
    }

    private class SplashScreenShow extends Thread {
        public void run() {
            try {
                sleep(2000);
            } catch (Exception e) { }
            Intent i = null;
                i = new Intent(SplaceScreenActivity.this, MainActivity.class);
            startActivity(i);
            SplaceScreenActivity.this.finish();
        }
    }

}
