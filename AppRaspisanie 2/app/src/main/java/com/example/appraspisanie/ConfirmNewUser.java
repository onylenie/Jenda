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

public class ConfirmNewUser extends AppCompatActivity {

    JsonToJava jtj = new JsonToJava();
    String content;
    TextView tv1,tv2,tv3,tv4,tv5,tv6;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.confirmuser);

        String content;

        VariablesList.stranica=1;

        try {
            content = JsonToJava.getContent(VariablesList.Sait + "jenda_userconfirm_m.php");

            if (String.valueOf(content.charAt(0)).equals("0")) {

                super.finish();
            }

            jtj.onPostExecuteNewUser(content);

            tv1 = findViewById(R.id.textView26);
            tv2 = findViewById(R.id.textView27);
            tv3 = findViewById(R.id.textView28);
            tv4 = findViewById(R.id.textView29);
            tv5 = findViewById(R.id.textView31);
            tv6 = findViewById(R.id.textView33);

            tv1.setText(VariablesList.kodppl.toString());
            tv2.setText(VariablesList.lastname);
            tv3.setText(VariablesList.firstname);
            tv4.setText(VariablesList.patronymic);
            tv5.setText(VariablesList.emailppl);
            tv6.setText(VariablesList.groupppl);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick3(View v){
        try {
            content = JsonToJava.getContent(VariablesList.Sait + "jenda_userconfirm2_m.php?kod=" + VariablesList.kodppl + "&vis=1");
            if (Integer.parseInt(String.valueOf(content.charAt(0))) == 1) {

                Intent intent = new Intent(ConfirmNewUser.this, ConfirmNewUser.class);
                startActivity(intent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick4(View v){
        try {
            content = JsonToJava.getContent(VariablesList.Sait + "jenda_userconfirm2_m.php?kod=" + VariablesList.kodppl + "&vis=0");
            if (Integer.parseInt(String.valueOf(content.charAt(0))) == 1) {

                Intent intent = new Intent(ConfirmNewUser.this, ConfirmNewUser.class);
                startActivity(intent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //UPDATE `Peoples` SET `CheckStatus` = '1' WHERE `Peoples`.`KodPpl` = 57;
    //http://domenforwork.atwebpages.com/jenda_userconfirm_m.php
}
