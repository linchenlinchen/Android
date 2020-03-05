package com.example.l2595.labb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ButtonTouchReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context,Intent intent){
        Toast.makeText(context,"You read the text",Toast.LENGTH_LONG).show();
    }
}
