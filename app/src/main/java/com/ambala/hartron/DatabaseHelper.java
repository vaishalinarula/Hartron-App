package com.ambala.hartron;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "hartron.db";
    public static final String LOGIN_TABLE_NAME = "login";
    public static final String LOGIN_TABLE_COL_1 = "USER_ID";
    public static final String LOGIN_TABLE_COL_2 = "PASSWORD";
    public static final String LOGIN_TABLE_COL_3 = "SERIAL_NO";
    public static final String LOGIN_TABLE_COL_4 = "NAME";
    public static final String LOGIN_TABLE_COL_5 = "COURSE_ID";
    public static final String LOGIN_TABLE_COL_6 = "USER_TYPE";

    public static final String COMPANY_INFO_TABLE_NAME = "companyinfo";
    public static final String COMPANY_INFO_TABLE_COL_1 = "WHATSAPP_NO";
    public static final String COMPANY_INFO_TABLE_COL_2 = "GMAIL_ADD";
    public static final String COMPANY_INFO_TABLE_COL_3 = "ENQUIRY_NO";
    public static final String COMPANY_INFO_TABLE_COL_4 = "LANDLINE_NO";
    public static final String COMPANY_INFO_TABLE_COL_5 = "ADMINISTRATION_NO";
    public static final String COMPANY_INFO_TABLE_COL_6 = "INSTAGRAM_LINK";
    public static final String COMPANY_INFO_TABLE_COL_7 = "FACEBOOK_LINK";
    public static final String COMPANY_INFO_TABLE_COL_8 = "TWITTER_LINK";
    public static final String COMPANY_INFO_TABLE_COL_9 = "YOUTUBE_LINK";
    public static final String COMPANY_INFO_TABLE_COL_10 = "GALLERY_LINK";
    public static final String COMPANY_INFO_TABLE_COL_11 = "LAST_UPDATED";



    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+ LOGIN_TABLE_NAME +" ("+ LOGIN_TABLE_COL_1 +" TEXT,"+ LOGIN_TABLE_COL_2 +" TEXT,"+ LOGIN_TABLE_COL_3 +" TEXT,"+ LOGIN_TABLE_COL_4 +" TEXT,"+LOGIN_TABLE_COL_5+" TEXT,"+LOGIN_TABLE_COL_6+" TEXT)");
        sqLiteDatabase.execSQL("create table "+ COMPANY_INFO_TABLE_NAME +" ("+ COMPANY_INFO_TABLE_COL_1 +" TEXT,"+ COMPANY_INFO_TABLE_COL_2 +" TEXT,"+ COMPANY_INFO_TABLE_COL_3 +" TEXT,"+ COMPANY_INFO_TABLE_COL_4 +" TEXT,"+COMPANY_INFO_TABLE_COL_5+" TEXT,"+COMPANY_INFO_TABLE_COL_6+" TEXT,"+COMPANY_INFO_TABLE_COL_7+" TEXT,"+COMPANY_INFO_TABLE_COL_8+" TEXT,"+COMPANY_INFO_TABLE_COL_9+" TEXT,"+COMPANY_INFO_TABLE_COL_10+" TEXT,"+COMPANY_INFO_TABLE_COL_11+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+LOGIN_TABLE_NAME);
        sqLiteDatabase.execSQL("create table "+ LOGIN_TABLE_NAME +" ("+ LOGIN_TABLE_COL_1 +" TEXT,"+ LOGIN_TABLE_COL_2 +" TEXT,"+ LOGIN_TABLE_COL_3 +" TEXT,"+ LOGIN_TABLE_COL_4 +" TEXT,"+LOGIN_TABLE_COL_5+" TEXT,"+LOGIN_TABLE_COL_6+" TEXT)");
    }

    /***      ***      INSERT METHODS      ***       ***/

    public boolean insertLoginTable(String user_id, String password, String serial_no, String name, String course_id, String user_type){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LOGIN_TABLE_COL_1,user_id);
        contentValues.put(LOGIN_TABLE_COL_2,password);
        contentValues.put(LOGIN_TABLE_COL_3,serial_no);
        contentValues.put(LOGIN_TABLE_COL_4,name);
        contentValues.put(LOGIN_TABLE_COL_5,course_id);
        contentValues.put(LOGIN_TABLE_COL_6,user_type);
        long result = db.insert(LOGIN_TABLE_NAME,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertDefaultDataIntoCompanyInfoTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COMPANY_INFO_TABLE_COL_1,"8930511211");
        contentValues.put(COMPANY_INFO_TABLE_COL_2,"cityhartron@gmail.com");
        contentValues.put(COMPANY_INFO_TABLE_COL_3,"8930511211");
        contentValues.put(COMPANY_INFO_TABLE_COL_4,"0171255006");
        contentValues.put(COMPANY_INFO_TABLE_COL_5,"9996669962");
        contentValues.put(COMPANY_INFO_TABLE_COL_6,"https://www.instagram.com/hartronskillcentreambala/");
        contentValues.put(COMPANY_INFO_TABLE_COL_7,"https://www.facebook.com/Hartronambalacity02/");
        contentValues.put(COMPANY_INFO_TABLE_COL_8,"https://www.twitter.com");
        contentValues.put(COMPANY_INFO_TABLE_COL_9,"https://www.youtube.com");
        contentValues.put(COMPANY_INFO_TABLE_COL_10,"https://photos.app.goo.gl/nTCB6hd8VtEByMGS9");
        contentValues.put(COMPANY_INFO_TABLE_COL_11,"16012019");

        long result = db.insert(COMPANY_INFO_TABLE_NAME,null,contentValues);

        if(result == -1)
            return false;
         //   Log.d("DatabaseHelper", "onUpgrade: DEFAULT DATA INSERTED");
        else
            return true;
         //   Log.e("DatabaseHelper", "onUpgrade: PROBLEM IN DEFAULT DATA INSERTION");

    }

    public boolean insertIntoCompanyInfoTable(String whatsapp_no, String gmail_address, String enquiry_no, String landline_no, String administration_no, String instagram_link, String facabook_link, String twitter_link, String youtube_link, String gallery_link, String last_updated){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COMPANY_INFO_TABLE_COL_1,whatsapp_no);
        contentValues.put(COMPANY_INFO_TABLE_COL_2,gmail_address);
        contentValues.put(COMPANY_INFO_TABLE_COL_3,enquiry_no);
        contentValues.put(COMPANY_INFO_TABLE_COL_4,landline_no);
        contentValues.put(COMPANY_INFO_TABLE_COL_5,administration_no);
        contentValues.put(COMPANY_INFO_TABLE_COL_6,instagram_link);
        contentValues.put(COMPANY_INFO_TABLE_COL_7,facabook_link);
        contentValues.put(COMPANY_INFO_TABLE_COL_8,twitter_link);
        contentValues.put(COMPANY_INFO_TABLE_COL_9,youtube_link);
        contentValues.put(COMPANY_INFO_TABLE_COL_10,gallery_link);
        contentValues.put(COMPANY_INFO_TABLE_COL_11,last_updated);

        long result = db.insert(COMPANY_INFO_TABLE_NAME,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    /*** ****       UTILITY METHOD    ***     ****/

    public Cursor checkWhereUserExists(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+LOGIN_TABLE_NAME+" ",null);
        return res;
    }
   public Cursor getUserName()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select "+LOGIN_TABLE_COL_4+" from "+LOGIN_TABLE_NAME+" ",null);
        return res;
    }

    public Cursor getCompanyInfo(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+COMPANY_INFO_TABLE_NAME+" ",null);
        return res;
    }

    /***
     *  DELETE METHOD
     */

    public int deleteRecordFromLoginTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            return db.delete(""+LOGIN_TABLE_NAME,null,null);
        }catch (Exception e){

        }
        return -1;
    }

    public int deleteRecordFromCompanyInfoTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            return db.delete(""+COMPANY_INFO_TABLE_NAME,null,null);
        }catch (Exception e){

        }
        return -1;
    }

    /***
     * UPDATE METHOD
     */

    public boolean updateCompanyInfoTable(String whatsapp_no, String gmail_address, String enquiry_no, String landline_no, String administration_no, String instagram_link, String facabook_link, String twitter_link, String youtube_link, String gallery_link, String last_updated){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COMPANY_INFO_TABLE_COL_1,whatsapp_no);
        contentValues.put(COMPANY_INFO_TABLE_COL_2,gmail_address);
        contentValues.put(COMPANY_INFO_TABLE_COL_3,enquiry_no);
        contentValues.put(COMPANY_INFO_TABLE_COL_4,landline_no);
        contentValues.put(COMPANY_INFO_TABLE_COL_5,administration_no);
        contentValues.put(COMPANY_INFO_TABLE_COL_6,instagram_link);
        contentValues.put(COMPANY_INFO_TABLE_COL_7,facabook_link);
        contentValues.put(COMPANY_INFO_TABLE_COL_8,twitter_link);
        contentValues.put(COMPANY_INFO_TABLE_COL_9,youtube_link);
        contentValues.put(COMPANY_INFO_TABLE_COL_10,gallery_link);
        contentValues.put(COMPANY_INFO_TABLE_COL_11,last_updated);

        int result = db.update(COMPANY_INFO_TABLE_NAME,contentValues,""+COMPANY_INFO_TABLE_COL_11+" != ?",new String[] { ""+last_updated });
        Log.d("DatabaseHelper", "updateQuestionMaster: RESULT CODE = "+result);
        if(result == -1)
            return false;
        else
            return true;
    }

}
