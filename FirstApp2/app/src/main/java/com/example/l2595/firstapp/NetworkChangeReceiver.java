package com.example.l2595.firstapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.widget.Toast;

//public class NetworkChangeReceiver extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent){
//        Toast.makeText(context,"network changes",Toast.LENGTH_SHORT).show();
//        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//        if(networkInfo != null&&networkInfo.isAvailable()){
//            Toast.makeText(context,"network is avaliable",Toast.LENGTH_SHORT).show();
//        }
//    }
//}
