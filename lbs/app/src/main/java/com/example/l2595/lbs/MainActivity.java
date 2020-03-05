package com.example.l2595.lbs;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();

        option.setIsNeedAddress(true);
//可选，是否需要地址信息，默认为不需要，即参数为false
//如果开发者需要获得当前点的地址信息，此处必须为true

        mLocationClient.setLocOption(option);
        //注册监听函数
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        mLocationClient = new LocationClient(getApplicationContext());
//        mLocationClient.registerLocationListener(new MyLocationListener());
        setContentView(R.layout.activity_main);
//        positionText = (TextView)findViewById(R.id.pos);
//        List<String> permissionList = new ArrayList<>();
//        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED){
//            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
//        }
//        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_PHONE_STATE ) != PackageManager.PERMISSION_GRANTED){
//            permissionList.add(Manifest.permission.READ_PHONE_STATE);
//        }
//        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED){
//            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }
//        if(!permissionList.isEmpty()){
//            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
//            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
//        }
//        else {
//            requestLocation();
//        }
//
    }
    public class MyLocationListener extends BDAbstractLocationListener{
        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息
        }
    }

//    private void initLocation() {
//        LocationClientOption option = new LocationClientOption();
//        //就是这个方法设置为true，才能获取当前的位置信息
//        option.setIsNeedAddress(true);
//        option.setOpenGps(true);
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//        option.setCoorType("gcj02");//可选，默认gcj02，设置返回的定位结果坐标系
//        //int span = 1000;
//        //option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
//        mLocationClient.setLocOption(option);
//    }
//
//    public class MyLocationListener extends BDAbstractLocationListener implements BDLocationListener {
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            //Receive Location
//            //经纬度
//            double lati = location.getLatitude();
//            double longa = location.getLongitude();
//            //打印出当前位置
//            Log.i("TAG", "location.getAddrStr()=" + location.getAddrStr());
//            //打印出当前城市
//            Log.i("TAG", "location.getCity()=" + location.getCity());
//            //返回码
//            int i = location.getLocType();
//        }
//    }

//    private String judgeProvider(LocationManager locationManager) {
//        List<String> prodiverlist = locationManager.getProviders(true);
//        if(prodiverlist.contains(LocationManager.NETWORK_PROVIDER)){
//            return LocationManager.NETWORK_PROVIDER;//网络定位
//        }else if(prodiverlist.contains(LocationManager.GPS_PROVIDER)) {
//            return LocationManager.GPS_PROVIDER;//GPS定位
//        }else{
//            Toast.makeText(MainActivity.this,"没有可用的位置提供器",Toast.LENGTH_SHORT).show();
//        }
//        return null;
//    }

//    public Location beginLocatioon() {
//        //获得位置服务
//        locationManager = activity.getLocationManager();
//        provider = judgeProvider(locationManager);
//        //有位置提供器的情况
//        if (provider != null) {
//            //为了压制getLastKnownLocation方法的警告
//            if (ActivityCompat.checkSelfPermission(activity.getMyContext(), Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(activity.getMyContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED) {
//                return null;
//            }
//            return locationManager.getLastKnownLocation(provider);
//        }else{
//            //不存在位置提供器的情况
//            Toast.makeText(activity.getMyContext(),"不存在位置提供器的情况",Toast.LENGTH_SHORT).show();
//        }
//        return null;
//    }
//    private void requestLocation(){
//        initLocation();
//        mLocationClient.start();
//    }
//    private void initLocation(){
//        LocationClientOption option = new LocationClientOption();
//
//        mLocationClient.setLocOption(option);
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode){
//            case 1:
//                if(grantResults.length > 1){
//                    for (int result : grantResults){
//                        if(result != PackageManager.PERMISSION_GRANTED){
//                            Toast.makeText(this,"必须同意所有权限才能使用本程序",Toast.LENGTH_LONG).show();
//                            finish();
//                            return;
//                        }
//                    }
//                    requestLocation();
//                }
//                else {
//                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
//                    finish();
//                }break;
//                default:
//        }
//    }
//
//    public class MyLocationListener extends BDAbstractLocationListener implements BDLocationListener{
//        @Override
//        public void onReceiveLocation(final BDLocation location){
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    StringBuilder currentPosition = new StringBuilder();
//                    currentPosition.append("纬度：").append(location.getLatitude()).append("\n");
//                    currentPosition.append("经度：").append(location.getLongitude()).append("\n");
//                    currentPosition.append("定位方式：");
//                    if(location.getLocType() == BDLocation.TypeGpsLocation){
//                        currentPosition.append("GPS");
//                    }else if(location.getLocType() == BDLocation.TypeNetWorkLocation){
//                        currentPosition.append("网络");
//                    }
//                    else {
//                        currentPosition.append( location.getLocType());
//                    }
//                    positionText.setText(currentPosition);
//                }
//            });
//        }
//
//        @Override
//        public void onConnectHotSpotMessage(String s,int i){
//
//        }
//    }
}
