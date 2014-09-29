package com.billywalkerinc.occupiedcentralsms;

import android.app.PendingIntent;
import android.content.*;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import java.util.GregorianCalendar;


@EActivity(R.layout.activity_main)
public class ActivityMain extends ActionBarActivity {
    public static final String BROADCAST_SENT_SMS = "com.billywalkerinc.occupiedcentralsm.sms.sent";
    public static final String KEY_SMS_SENT_RESULT = "sms_sent_result_code";

    public final String SETTING = "setting";
    public final String SEND_MSG = "send message";
    public final String PHONE = "1234567890"; // set up the phone no. here
    public final int DELAY = 2000;

    public final String SENT = "sent";
    public boolean sent;

    Handler touchHandler = new Handler();
    SendSMS sendSMS = new SendSMS();


    @ViewById
    Button btnSend;

    SharedPreferences settings;
    SharedPreferences.Editor editor;
    SmsManager smsManager;


    private BroadcastReceiver bReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BROADCAST_SENT_SMS)) {
                if(intent.getBooleanExtra(KEY_SMS_SENT_RESULT, false)) {
                    Toast.makeText(ActivityMain.this, "傳送成功", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(ActivityMain.this, "傳送失敗, 請重試", Toast.LENGTH_LONG).show();
                }
            }
        }
    };


    @AfterViews
    void onViewInjected() {
        smsManager = SmsManager.getDefault();
        settings = getSharedPreferences(SETTING, 0);
        editor = settings.edit();
        sent = settings.getBoolean(SENT, false);
    }


    @Override
    protected void onStart() {
        super.onStart();

        /** broadcast **/
        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);

        IntentFilter filter4SendSms = new IntentFilter();
        filter4SendSms.addAction(BROADCAST_SENT_SMS);
        bManager.registerReceiver(bReceiver, filter4SendSms);
    }

    @Override
    protected void onStop() {
        super.onStop();

        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        bManager.unregisterReceiver(bReceiver);
    }

    @Click(R.id.btnSettings)
    void gotoSetting() {
        ActivitySettings_.intent(this).start();
    }

    @LongClick(R.id.btnSend)
    void sendSMSAction() {
        if (!settings.getBoolean("set", false)) {
            Toast.makeText(ActivityMain.this, "請先設定", Toast.LENGTH_SHORT).show();
            ActivitySettings_.intent(ActivityMain.this).start();
            return;
        }
        touchHandler.post(new SendSMS());
    }

    class SendSMS implements Runnable {
        @Override
        public void run() {
            Intent intent = new Intent(BROADCAST_SENT_SMS);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(ActivityMain.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            smsManager.sendTextMessage(PHONE, null, settings.getString(SEND_MSG, "msg not set"), pendingIntent, null);
            Toast.makeText(ActivityMain.this, "傳送中", Toast.LENGTH_SHORT).show();
            sent = true;
            editor.putBoolean(SENT, true);
            editor.commit();
        }
    }

}
