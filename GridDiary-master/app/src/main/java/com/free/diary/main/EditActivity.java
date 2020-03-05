package com.free.diary.main;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.free.diary.DiaryApplication;
import com.free.diary.R;
import com.free.diary.entries.MyDatabaseHelper;
import com.free.diary.entries.Naming;
import com.gjiazhe.multichoicescirclebutton.MultiChoicesCircleButton;
import com.spark.submitbutton.SubmitButton;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class EditActivity extends AppCompatActivity implements Naming {
    private MyDatabaseHelper helper = new MyDatabaseHelper(this,"People.db",null,4);
    EditText editText1, editText2;
    SubmitButton bt_save;
    String title, content;

    //需要被返回的数据
    long re_id;
    boolean re_type;

    /////
    private String provider;//位置提供器
    private LocationManager locationManager;//位置服务
    private Location location;
    List<Address> addresses;

    ////
    WheelView wheel_emergency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ///////以下部分为底部的按钮及其响应事件/////


        final MultiChoicesCircleButton.Item item1 = new MultiChoicesCircleButton.Item("Location", getResources().getDrawable(R.drawable.ic_launcher_foreground), 30);

        MultiChoicesCircleButton.Item item2 = new MultiChoicesCircleButton.Item("DDL", getResources().getDrawable(R.drawable.ic_launcher_foreground), 90);

        MultiChoicesCircleButton.Item item3 = new MultiChoicesCircleButton.Item("Test", getResources().getDrawable(R.drawable.ic_launcher_foreground), 150);

        final List<MultiChoicesCircleButton.Item> buttonItems = new ArrayList<>();
        buttonItems.add(item1);
        buttonItems.add(item2);
        buttonItems.add(item3);

        MultiChoicesCircleButton multiChoicesCircleButton = (MultiChoicesCircleButton) findViewById(R.id.multiChoicesCircleButton);
        multiChoicesCircleButton.setButtonItems(buttonItems);

        multiChoicesCircleButton.setOnSelectedItemListener(new MultiChoicesCircleButton.OnSelectedItemListener() {
            @Override
            public void onSelected(MultiChoicesCircleButton.Item item, int index) {
                // Do something
                switch (index){
                    case 0:
                        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);//获得位置服务
                        provider = judgeProvider(locationManager);

                        if (provider != null) {//有位置提供器的情况
                            //为了压制getLastKnownLocation方法的警告
                            if (ActivityCompat.checkSelfPermission(EditActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                    ActivityCompat.checkSelfPermission(EditActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            location = locationManager.getLastKnownLocation(provider);
                            if (location != null) {
                                getLocation(location);//得到当前经纬度并开启线程去反向地理编码
                            } else {
                                Toast.makeText(EditActivity.this, "暂时无法获得当前位置", Toast.LENGTH_SHORT).show();
                            }
                        }else{//不存在位置提供器的情况
                        }
                        break;
                    case 1:
                        editText2.setText(editText2.getText()+"这门课的project的ddl是");
                    case 2:
                        editText2.setText(editText2.getText()+"考试时间是");
                        break;
                    default:
                        break;
                }
            }
        });

        //////////////////////////////////////
        ////////////wheel_emergency////////////////
        wheel_emergency = (WheelView)findViewById(R.id.wheel_emergency);

        //定义WheelView的style，比如选中文字大小和其他文字大小（这里WheelView已经封装了）
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextSize = 20;
        style.textSize = 16;

        wheel_emergency.setWheelAdapter(new ArrayWheelAdapter(this));
        wheel_emergency.setSkin(WheelView.Skin.Holo);
        wheel_emergency.setWheelData(createEmergencyDatas());
        wheel_emergency.setStyle(style);
        //////////////////////////////////////






        final Intent intent = getIntent();
        editText1 = (EditText) findViewById(R.id.memo_edit_title);
        editText2 = (EditText) findViewById(R.id.memo_edit_content);

        final long memo_id = intent.getLongExtra("id",0);
        if (memo_id != MEMO_TEMP_ID){
            title = intent.getStringExtra("t");
            content = intent.getStringExtra("c");

            editText1.setText(title);
            editText2.setText(content);
        }


        //当点击了保存
        bt_save = (SubmitButton) findViewById(R.id.memo_save_bt);
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int emergency = 0;
                switch (wheel_emergency.getSelectionItem().toString()){
                    case MOST_EMERGENCY:emergency = 1;break;
                    case EMERGENCY:emergency = 2;break;
                    case COMMON:emergency = 3;break;
                    case JUST :emergency = 4;break;
                    case RANDOM:emergency = 5;break;
                    default:break;
                };

                if(memo_id == MEMO_TEMP_ID) {
                    //如果是新增条目
                    if (editText1.getText().toString().equals("") && editText2.getText().toString().equals("")) {
                    }
                    else {
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("memo_title",editText1.getText().toString());
                        contentValues.put("memo_emergency",emergency);
                        contentValues.put("memo_content",editText2.getText().toString());
                        long id =  db.insert("People",null,contentValues);
                        contentValues.clear();

                        re_id = id;
                        re_type = MEMO_NEW;

                    }
                }
                else {
                    //如果是修改条目
                    SQLiteDatabase db = helper.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();

                    contentValues.put("memo_title", editText1.getText().toString());
                    contentValues.put("memo_content", editText2.getText().toString());
                    db.update("People", contentValues, "id = ?", new String[]{memo_id + ""});
                    contentValues.clear();

                    contentValues = new ContentValues();
                    contentValues.put("memo_emergency",emergency);
                    db.update("People",contentValues,"id = ?",new String[]{memo_id+""});
                    contentValues.clear();

                    re_id = memo_id;
                    re_type = MEMO_OLD;


                }

                Intent intent_new = new Intent(EditActivity.this,MemoActivity.class);
//                intent_new.putExtra("id",re_id);
//                intent_new.putExtra("new_or_old",re_type);
                startActivity(intent_new);

            }
        });
    }

    private List<String> createEmergencyDatas() {
            String[] strings = {"非常紧急","紧急","普通","一般","随意"};
            return Arrays.asList(strings);
    }



    public void update(int memo_id){
        //更新数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("memo_title", editText1.getText().toString());
        contentValues.put("memo_content", editText2.getText().toString());
        db.update("People", contentValues, "id = ?", new String[]{memo_id + ""});

        Intent intent1 = new Intent();
        String t = editText1.getText().toString();
        intent1.putExtra("t", t);
        intent1.putExtra("c", editText2.getText().toString());
        setResult(RESULT_OK, intent1);
        finish();
    }

    /////////////////////////////////////////

    private List<Address> getAddress(Location location) {
        List<Address> result = null;
        try {
            if (location != null) {
                Geocoder gc = new Geocoder(this, Locale.getDefault());
                result = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }




    public void getLocation(final Location location) {
        String latitude = location.getLatitude()+"";
        String longitude = location.getLongitude()+"";
        String url = "http://api.map.baidu.com/geocoder/v2/?ak=pPGNKs75nVZPloDFuppTLFO3WXebPgXg&callback=renderReverse&location="+latitude+","+longitude+"&output=json&pois=0";
//        new MyAsyncTask(url).execute();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("Run", "A new Thread");
                try {
                    final Location location1 = location;
                    addresses = getAddress(location1
                    );
                    if (addresses != null) {
                        Log.e("run: ", addresses.toString());
                        Message message = new Message();
                        message.what = 1;//信息内容
                        handler.sendMessage(message);//发送信息
                    }
                } catch (Exception e) {
                    Log.e("Exception", "ERRPOR");
                }
            }

        }).start();
    }



    private String judgeProvider(LocationManager locationManager) {
        List prodiverlist = locationManager.getProviders(true);
        if(prodiverlist.contains(LocationManager.NETWORK_PROVIDER)){
            return LocationManager.NETWORK_PROVIDER;
        }else if(prodiverlist.contains(LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;
        }else{
            Toast.makeText(EditActivity.this,"没有可用的位置提供器",Toast.LENGTH_SHORT).show();
        }
        return null;
    }


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    editText2.setText(editText2.getText() + " " + addresses.get(0).getFeatureName());
                    break;
                default:
                    break;
            }
        }
    };




    class MyAsyncTask extends AsyncTask {
        String url = null;//要请求的网址
        String str = null;//服务器返回的数据
        String address = null;
        public MyAsyncTask(String url){
            this.url = url;
        }




        protected void onPostExecute(Void aVoid) {
            try {
                str = str.replace("renderReverse&&renderReverse","");
                str = str.replace("(","");
                str = str.replace(")","");
                JSONObject jsonObject = new JSONObject(str);
                JSONObject address = jsonObject.getJSONObject("result");
                String city = address.getString("formatted_address");
                String district = address.getString("sematic_description");
                editText2.setText("当前位置："+city+district);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Object[] objects) {
            str = GetHttpConnectionData.getData(url);
            return null;
        }
    }
}


 class GetHttpConnectionData {

    String str = null;//网路请求往回的数据
    public static String getData(String url){//url网路请求的网址
        URL u = null;
        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection hc = null;
        InputStream inputStream = null;
        StringBuffer sb = null;
        BufferedReader br = null;
        try {
            hc = (HttpURLConnection) u.openConnection();
            hc.setRequestMethod("GET");
            inputStream = hc.getInputStream();
            sb = new StringBuffer();
            br = new BufferedReader(new InputStreamReader(inputStream));
            String len = null;
            while ((len=br.readLine())!=null){
                sb.append(len);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}




