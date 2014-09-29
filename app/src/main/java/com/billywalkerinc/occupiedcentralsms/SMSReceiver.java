package com.billywalkerinc.occupiedcentralsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by samwalker on 29/9/14.
 */
public class SMSReceiver extends BroadcastReceiver {
    // For receiving sms

    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent != null && intent.getAction() != null && ACTION.compareToIgnoreCase(intent.getAction()) == 0)
        {
            // TODO Sms Received Your code here
        }
    }
}
