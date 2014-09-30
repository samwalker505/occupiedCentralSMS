package com.billywalkerinc.occupiedcentralsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by samwalker on 1/10/14.
 */
public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "已成功收到", Toast.LENGTH_SHORT).show();
    }

}
