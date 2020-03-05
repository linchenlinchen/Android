package com.kosphere.test2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class login extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        final Button login = (Button) findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //提示框确定是否跳转
//                new AlertDialog.Builder(MainActivity.this).setTitle("Jump").setMessage("Ready to jump?")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent intent = new Intent(MainActivity.this, register.class);
//                                startActivity(intent);
//                            }})
//                        .setNegativeButton("No",null)
//                        .show();
                Intent intent = new Intent(login.this, description.class);
                startActivity(intent);
            }
        });
    }
}
