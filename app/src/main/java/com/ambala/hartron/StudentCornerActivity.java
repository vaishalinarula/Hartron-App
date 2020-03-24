package com.ambala.hartron;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class StudentCornerActivity extends AppCompatActivity {

    private TextView user_name_tv;
    private View profile;
    private View notification;
    private View attendance;
    private View syllabus;
    private View other_courses;
    private View exam_status;
    private View feedback;

    String serial_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_corner);

        ActionBar actionBar = getSupportActionBar();      // REQUIRED TO MAKE CHANGES ON ACTION BAR
        //actionBar.setTitle(getResources().getString(R.string.app_name));
        actionBar.setTitle("Student Corner");
        //actionBar.hide();

        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                serial_no = bundle.getString("SERIAL_NO");
            }
        }


        user_name_tv = findViewById(R.id.user_name_tv);
        setUserName();

        profile = findViewById(R.id.student_profile_LL);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!haveNetwork()){
                    Toast.makeText(StudentCornerActivity.this,"No Internet Access",Toast.LENGTH_SHORT).show();
                }else {
                    String url = "http://103.87.24.58/hartron/StudentHome.aspx?srno="+serial_no;
                    Intent intent = new Intent(StudentCornerActivity.this, WebViewActivity.class);
                    intent.putExtra("URL_STRING", url);
                    intent.putExtra("TITLE","Profile");
                    startActivity(intent);
                }
            }
        });

        notification  = findViewById(R.id.student_notifications_LL);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!haveNetwork()){
                    Toast.makeText(StudentCornerActivity.this,"No Internet Access",Toast.LENGTH_SHORT).show();
                }else {
                    String url = "http://103.87.24.58/hartron/android/ShowNotifications.aspx?id="+serial_no;
                    Intent intent = new Intent(StudentCornerActivity.this, WebViewActivity.class);
                    intent.putExtra("URL_STRING", url);
                    intent.putExtra("TITLE","Notifications");
                    startActivity(intent);
                }
            }
        });

        attendance = findViewById(R.id.attendance_detail_LL);
        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!haveNetwork()){
                    Toast.makeText(StudentCornerActivity.this,"No Internet Access",Toast.LENGTH_SHORT).show();
                }else {
                    String url = "http://103.87.24.58/hartron/StudentAttendance.aspx?srno="+serial_no;
                    Intent intent = new Intent(StudentCornerActivity.this, WebViewActivity.class);
                    intent.putExtra("URL_STRING", url);
                    intent.putExtra("TITLE","Attendance Detail");
                    startActivity(intent);
                }
            }
        });

        syllabus = findViewById(R.id.syllabus_detail_LL);
        syllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!haveNetwork()){
                    Toast.makeText(StudentCornerActivity.this,"No Internet Access",Toast.LENGTH_SHORT).show();
                }else {
                    String url = "http://103.87.24.58/hartron/StudentsSyllabus.aspx?srno="+serial_no;
                    Intent intent = new Intent(StudentCornerActivity.this, WebViewActivity.class);
                    intent.putExtra("URL_STRING", url);
                    intent.putExtra("TITLE","Syllabus");
                    startActivity(intent);
                }
            }
        });

        other_courses = findViewById(R.id.other_course_detail_LL);
        other_courses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!haveNetwork()){
                    Toast.makeText(StudentCornerActivity.this,"No Internet Access",Toast.LENGTH_LONG).show();
                }else {
                    String url = "http://103.87.24.58/hartron/android/courses.aspx";
                    Intent intent = new Intent(StudentCornerActivity.this, WebViewActivity.class);
                    intent.putExtra("URL_STRING", url);
                    intent.putExtra("TITLE","Courses");
                    startActivity(intent);
                }
            }
        });

        exam_status = findViewById(R.id.exam_status_LL);
        exam_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!haveNetwork()){
                    Toast.makeText(StudentCornerActivity.this,"No Internet Access",Toast.LENGTH_LONG).show();
                }else {
                    String url = "http://103.87.24.58/hartron/ExamStatus.aspx?id="+serial_no;
                    Intent intent = new Intent(StudentCornerActivity.this, WebViewActivity.class);
                    intent.putExtra("URL_STRING", url);
                    intent.putExtra("TITLE","Exam Status");
                    startActivity(intent);
                }
            }
        });

        feedback = findViewById(R.id.feedback_LL);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentCornerActivity.this,FeedbackActivity.class);
                intent.putExtra("SERIAL_NO",serial_no);
                startActivity(intent);
            }
        });

    }

    private void setUserName(){
        DatabaseHelper db=new DatabaseHelper(this);
        Cursor cursor=db.getUserName();
        if(cursor.moveToNext())
            user_name_tv.setText("Hello, "+cursor.getString(0));
    }

    private boolean haveNetwork() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
