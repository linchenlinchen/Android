package com.example.dell.mydiary.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dell.mydiary.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class RegisterActivity extends AppCompatActivity  implements View.OnClickListener {
    private Button register;
    private EditText id;
    private EditText pwd_1;
    private EditText pwd_2;
    private EditText emails;
    private String result;
    private String username;
    private String pwd1;
    private String email;
    private String pwd2;
    private int ResultCode=2;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        register = (Button) findViewById(R.id.register_do);
        register.setOnClickListener(this);
        id = findViewById(R.id.id_edit);
        pwd_1 = (EditText) findViewById(R.id.password_edit);
        pwd_2 = (EditText) findViewById(R.id.password_edit_1);
        emails = (EditText) findViewById(R.id.email_edit);
    }

    @Override
    public void onClick(final View v) {
        new Thread(){public void run() {

            switch (v.getId()) {
                case R.id.register_do:
                    username = id.getText().toString().trim();
                    email = emails.getText().toString().trim();
                    pwd1 = pwd_1.getText().toString().trim();
                    pwd2 = pwd_2.getText().toString().trim();

                    //System.out.println("email:"+email);

                    if(!pwd1.equals(pwd2)){
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "两次输入密码不一致，请重新输入！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        try {
                            //设置路径
                            String path="http://192.168.1.111:8080/MyWebsite/androidregister.do";
                            //?id="+username+"&password=" + pwd1+ "&email=" +email+"
                            //创建URL对象
                            URL url=new URL(path);
                            //创建一个HttpURLconnection对象
                            HttpURLConnection conn =(HttpURLConnection) url.openConnection();
                            //设置请求方法
                            conn.setRequestMethod("POST");
                            //设置请求超时时间
                            conn.setReadTimeout(5000);
                            //conn.setConnectTimeout(5000);
                            //Post方式不能设置缓存，需要手动设置
                            //conn.setUseCaches(false);
                            //准备要发送的数据
                            String data ="id="+URLEncoder.encode(username,"utf-8")+"&password="+URLEncoder.encode(pwd1,"utf-8")+"&email="+URLEncoder.encode(email,"utf-8");
                            //String data ="id="+ username +"&password="+ pwd1 +"&email="+ email+"";
                            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//使用的是表单请求类型
                            conn.setRequestProperty("Content-Length", data.length()+"");
                            conn.setDoInput(true);
                            conn.setDoOutput(true);
                            //连接
                            // conn.connect();
                            //获得返回的状态码
                            conn.getOutputStream().write(data.getBytes());
                            int code=conn.getResponseCode();
                            if(code==200){
                                //获得一个文件的输入流
                                InputStream inputStream= conn.getInputStream();
                                //result = StringTools.readStream(inputStream);
                                //更新UI
                                showToast(result);
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }


        };}.start();
    }
    public void showToast(final String content){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if(result.equals("success")){
                    Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.putExtra("id", username);
                    intent.putExtra("password", pwd1);
                    setResult(ResultCode, intent);
                    finish();
                }

            }
        });
    }


}
