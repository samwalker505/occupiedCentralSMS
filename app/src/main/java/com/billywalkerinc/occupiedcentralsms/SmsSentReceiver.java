package com.billywalkerinc.occupiedcentralsms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by Kelly on 20/9/2014.
 */
public class SmsSentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (null != intent) {
            boolean success = false;
            if(getResultCode() == Activity.RESULT_OK) {
                success = true;
            }

            broadcastMessageToActivity(context, success);
        }
    }

    private void broadcastMessageToActivity(Context context, boolean success) {
        Intent in = new Intent(ActivityMain.BROADCAST_SENT_SMS);
        in.putExtra(ActivityMain.KEY_SMS_SENT_RESULT, success);
        LocalBroadcastManager.getInstance(context).sendBroadcast(in);
    }
}
