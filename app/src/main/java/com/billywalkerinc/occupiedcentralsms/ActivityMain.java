package com.billywalkerinc.occupiedcentralsms;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
    public static final String BROADCAST_SENT_SMS = "com.billywalkerinc.occupiedcentralsms.sms.sent";
    public static final String KEY_SMS_SENT_RESULT = "sms_sent_result_code";

    public final String SETTING = "setting";
    public final String SEND_MSG = "send message";
    public final String PHONE = "91463653"; // set up the phone no. here 學聯(文律師）BY DEFAULT

    public final String SENT = "sent";
    public boolean sent;

    SmsManager mysmsManager;

    SMSReceiver smsReceiver;


    @ViewById
    Button btnSend;

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    @AfterViews
    void onViewInjected() {
        settings = getSharedPreferences(SETTING, 0);
        editor = settings.edit();
        sent = settings.getBoolean(SENT, false);
        mysmsManager = SmsManager.getDefault();
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
        sendSms(PHONE);
    }

    private void sendSms(String phoneNum){

        String smsTEXT = settings.getString(SEND_MSG, "none");


        Toast.makeText(this, smsTEXT, Toast.LENGTH_SHORT).show();

        if (smsTEXT.length() > 139) {
            Toast.makeText(this, "文字太多", Toast.LENGTH_SHORT).show();
        }

        try {
            //Initializing SMS Manager and sending the message to the typed phone number

            mysmsManager.sendTextMessage(phoneNum, null, smsTEXT, null, createDeliveredIntent());

            Toast.makeText(getApplicationContext(), "送出中!", Toast.LENGTH_LONG).show();

            //to close the app after sending your message, you can add the finish(); method at the end as shown below
            //finish();

        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "傳送失敗！", Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
    }

    private PendingIntent createDeliveredIntent(){
        Intent intent = new Intent();
        return PendingIntent.getBroadcast(this, RESULT_OK, intent, 0);
    }
}
