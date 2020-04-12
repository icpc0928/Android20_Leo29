package tw.org.iii.leo.leo29;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private ContentResolver cr;
    private Uri uri = Settings.System.CONTENT_URI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS,
                            Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    123);

        }else {
            init();

        }




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        init();
    }

    private void init(){
        cr = getContentResolver();
        // content://Settings           設定資料的路徑
        // content://ContactContract    聯絡人資料
        // content://Calllog            通話紀錄
        // content://MediaStore

    }

    public void test1(View view) {
        //SELECT *  FROM Settings
        Cursor c = cr.query(uri, null, null, null, null);

        Log.v("leo", "count : " + c.getColumnCount());

        for (int i = 0; i < c.getColumnCount(); i++) {
            String field = c.getColumnName(i);
            Log.v("leo", field);
        }
        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex("name"));
            String value = c.getString(c.getColumnIndex("value"));
            Log.v("leo", name + ": " + value);
        }
        c.close();
    }


    public void test2(View view) {
//        Cursor c = cr.query(uri,null,
//                "name = ?",
//                new String[]{Settings.System.SCREEN_BRIGHTNESS},
//                null);
//
//        c.moveToNext();
//        String v = c.getString(c.getColumnIndex("value"));
//        Log.v("leo","v = " + v);
//        c.close();

        try {
            int v = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
            Log.v("leo", "V= " + v);
        } catch (Exception e) {
            Log.v("leo", e.toString());
        }

    }

    public void test3(View view) {
        uri = ContactsContract.Contacts.CONTENT_URI;
        Cursor c = cr.query(uri, null, null, null, null);

        for (int i = 0; i < c.getColumnCount(); i++) {
            String field = c.getColumnName(i);
            Log.v("leo", field);

        }
        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex("display_name"));
//            String value = c.getString(c.getColumnIndex("value"));
            Log.v("leo", name + ": ");
        }
        c.close();
    }


    public void test4(View view) {
        uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor c = cr.query(uri, null, null, null, null);

//        for (int i = 0; i < c.getColumnCount(); i++) {
//            String field = c.getColumnName(i);
//            Log.v("leo", field);
//
//        }

        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone= c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            Log.v("leo", name + ": "+phone);
        }
        c.close();
    }
    public void test5(View view){
        //Calllog.Calls.CONTENT_URI

        //CallLog.Calls.CACHED_NAME
        //CallLog.Calls.NUMBER
        //CallLog.Calls.TYPE  => INCOMING , OUTGOING , MISSED
        //CallLog.Calls.DATE
        //CallLog.Calls.DURATION

        uri = CallLog.Calls.CONTENT_URI;
        Cursor c = cr.query(uri, null, null, null, null);

        while(c.moveToNext()){
            String name = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));
            String num = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
            String type = "";
            if(CallLog.Calls.INCOMING_TYPE == c.getInt(c.getColumnIndex(CallLog.Calls.TYPE))){
                type = "in";
            }else if(CallLog.Calls.OUTGOING_TYPE == c.getInt(c.getColumnIndex(CallLog.Calls.TYPE))){
                type = "out";
            }else if (CallLog.Calls.MISSED_TYPE == c.getInt(c.getColumnIndex(CallLog.Calls.TYPE))){
                type = "miss";
            }
            Log.v("leo",type + " : "+ name + " : "+num);

        }


    }
}
