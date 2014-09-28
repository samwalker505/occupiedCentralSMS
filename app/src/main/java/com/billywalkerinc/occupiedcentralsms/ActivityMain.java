package com.billywalkerinc.occupiedcentralsms;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_main)
public class ActivityMain extends ActionBarActivity {
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


    @AfterViews
    void onViewInjected() {
        smsManager = SmsManager.getDefault();
        settings = getSharedPreferences(SETTING, 0);
        editor = settings.edit();
        sent = settings.getBoolean(SENT, false);
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
            smsManager.sendTextMessage(PHONE, null, settings.getString(SEND_MSG, "msg not set"), null, null);
            Toast.makeText(ActivityMain.this, "傳送成功", Toast.LENGTH_SHORT).show();
            sent = true;
            editor.putBoolean(SENT, true);
            editor.commit();
        }
    }

}
