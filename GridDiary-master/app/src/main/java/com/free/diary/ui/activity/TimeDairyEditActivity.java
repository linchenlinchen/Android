package com.free.diary.ui.activity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.free.diary.R;
import com.free.diary.entries.TimeDatabaseHelper;
import com.scalified.fab.ActionButton;

import java.util.Calendar;

import me.james.biuedittext.BiuEditText;

public class TimeDairyEditActivity extends AppCompatActivity {

    TextView textView;
    BiuEditText text_biu;
    TimeDatabaseHelper timeDatabaseHelper = new TimeDatabaseHelper(this,"Time.db",null,1);
    String content;
    int tyear=2020, tmonth=1, tday=1;


    ActionButton actionButton;

    ////////////////
    private SensorManager sensorManager;
    private Vibrator vibrator;

    private static final String TAG = "TestSensorActivity";
    private static final int SENSOR_SHAKE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_dairy_edit);

        textView = (TextView) findViewById(R.id.tv_ddl_date);
        text_biu = (BiuEditText) findViewById(R.id.eidt_time_diary_content);
        /////////////
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        Calendar ca = Calendar.getInstance();
        final int[] mYear = {ca.get(Calendar.YEAR)};
        final int[] mMonth = {ca.get(Calendar.MONTH)};
        final int[] mDay = {ca.get(Calendar.DAY_OF_MONTH)};
        tyear = 2020;
        tmonth =1;
        tday = 1;


        actionButton = (ActionButton) findViewById(R.id.set_date_bt);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(TimeDairyEditActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                mYear[0] = year;tyear= year;
                                mMonth[0] = month+1;tmonth=month+1;
                                mDay[0] = dayOfMonth;tday=dayOfMonth;
                                final String data = (year) + "年-" +(month+1) + "月-" + dayOfMonth + "日 ";
                                textView.setText(data);
                            }
                        },
                        mYear[0], mMonth[0], mDay[0]);
                datePickerDialog.show();
            }
        });

    }
    ///////////////////////////
    /** Called when the activity is first created. */


    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {// 注册监听器
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {// 取消监听器
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    /**
     * 重力感应监听
     */
    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正
            Log.i(TAG, "x轴方向的重力加速度" + x +  "；y轴方向的重力加速度" + y +  "；z轴方向的重力加速度" + z);
            // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
            int medumValue = 19;// 三星 i9250怎么晃都不会超过20，没办法，只设置19了
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {
                vibrator.vibrate(200);
                Message msg = new Message();
                msg.what = SENSOR_SHAKE;
                handler.sendMessage(msg);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    /**
     * 动作执行
     */
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SENSOR_SHAKE:
                    Toast.makeText(TimeDairyEditActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    SQLiteDatabase db = timeDatabaseHelper.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("diary_year",tyear);
                    contentValues.put("diary_month",tmonth);
                    contentValues.put("diary_day",tday);
                    contentValues.put("diary_content",text_biu.getText().toString());
                    db.insert("Time",null,contentValues);
                    contentValues.clear();
                    finish();
//                    Log.i(TAG, "检测到摇晃，执行操作！");
                    break;
            }
        }

    };


}
