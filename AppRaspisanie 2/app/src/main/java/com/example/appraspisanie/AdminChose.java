package com.example.appraspisanie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class AdminChose extends AppCompatActivity {

    JsonToJava jtj = new JsonToJava();
    TextView tv;
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        VariablesList.stranica=0;

        setContentView(R.layout.adminchose);

        String content;
        tv = findViewById(R.id.textView34);

        try {
            content = JsonToJava.getContent(VariablesList.Sait + "jenda_userconfirm_m.php");
            String d = String.valueOf(content.charAt(0));
            if (d.equals("0"))
            {
                tv.setText("0");
            }
            else {
                jtj.onPostExecuteNewUser(content);

                tv.setText(VariablesList.size.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClick1(View v){
        Intent intent = new Intent(AdminChose.this, ForTeacherAdd.class);
        startActivity(intent);
    }


    public void onClick2(View v) {
        if (Integer.parseInt(tv.getText().toString()) != 0) {
            Intent intent = new Intent(AdminChose.this, ConfirmNewUser.class);
            startActivity(intent);
        }
    }

    public void onClick3(View v){
        Intent intent = new Intent(AdminChose.this, AddTimeChanges.class);
        startActivity(intent);
    }

    public void onClick4(View v){
        Intent intent = new Intent(this, AddNewTeacher.class);
        startActivity(intent);
    }

    public void onClick5(View v){
        Intent intent = new Intent(this, AddNewTeacher.class);
        startActivity(intent);
    }


    //http://domenforwork.atwebpages.com/jenda_userconfirm_m.php
}
