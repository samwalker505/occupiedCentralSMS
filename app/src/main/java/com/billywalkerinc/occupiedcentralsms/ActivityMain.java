package com.billywalkerinc.occupiedcentralsms;

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
    public final String address = "65335300"; // set up the phone no. here
    public final int DELAY = 2000;

    public final String SENT = "sent";
    public boolean sent;



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
//        Toast.makeText(this, "trigger", Toast.LENGTH_SHORT).show(); // for debug
        sendSms();
    }
    private void sendSms() {
        try {
            String SENT= "SMS_SENT";
            PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);

            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context arg0, Intent arg1)
                {
                    int resultCode = getResultCode();
                    switch (resultCode)
                    {
                        case Activity.RESULT_OK:
                            Toast.makeText(getBaseContext(), "SMS sent",Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Toast.makeText(getBaseContext(), "Generic failure",Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toast.makeText(getBaseContext(), "No service",Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Toast.makeText(getBaseContext(), "Null PDU",Toast.LENGTH_LONG).show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toast.makeText(getBaseContext(), "Radio off",Toast.LENGTH_LONG).show();
                            break;
                    }
                }}, new IntentFilter(SENT));

            SmsManager smsMgr = SmsManager.getDefault();
            smsMgr.sendTextMessage(address, null, settings.getString(SEND_MSG, "none"), sentPI, null); //TODO get the sms msg from preference;

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage()+"!\n"+"Failed to send SMS", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
