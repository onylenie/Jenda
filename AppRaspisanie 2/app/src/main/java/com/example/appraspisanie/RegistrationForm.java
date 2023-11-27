package com.example.appraspisanie;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegistrationForm extends Activity {

    private static final String LOG_TAG = "myLogs";
    JsonToJava jtj = new JsonToJava();
    protected static String fname;
    protected static String lname;
    protected static String patr;
    protected static String pass;
    protected static String login;

    TextView lastname, name, patronymic, email, password, password2;
    Integer gruppa;
    ImageView er, er2, er3, er4, er5, er6;
    Spinner groups;

    Integer error = 0;
    ArrayAdapter adapter;
    ArrayList <String> allGroups;
    protected static Map <Integer,String> gr = new HashMap<>();
    String content;
    SharedPreferences mSettings;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.registrationlayout);

        mSettings = getSharedPreferences(VariablesList.APP_PREFERENCES, Context.MODE_PRIVATE);

        lastname = findViewById(R.id.lastname);
        name = findViewById(R.id.name);
        patronymic = findViewById(R.id.patronymic);

        er = findViewById(R.id.imageView);
        er2 = findViewById(R.id.imageView2);
        er3 = findViewById(R.id.imageView3);
        er4 = findViewById(R.id.imageView4);

        groups = findViewById(R.id.groupReg);

        try {
            content = JsonToJava.getContent(VariablesList.Sait + "jenda_groups_m.php");
        } catch (IOException e) {
            e.printStackTrace();
        }

        allGroups = jtj.onPostExecuteGroup2(content);

        adapter = new ArrayAdapter(this, R.layout.forgroupchosedropspinner, allGroups);
        groups.setAdapter(adapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void regClick(View v) {
        Log.d(LOG_TAG, "нажал на кнопку");
        error = 0;
        //имя
        if (lastname.getText().length() <1) {
            error =1;
            er.setVisibility(View.VISIBLE);
        }
        else er.setVisibility(View.INVISIBLE);
        //фамилия
        if (name.getText().length() <1) {
            error =1;
            er2.setVisibility(View.VISIBLE);
        }
        else er2.setVisibility(View.INVISIBLE);
        //отчество
        if (patronymic.getText().length() <1) {
            error =1;
            er3.setVisibility(View.VISIBLE);
        }
        else er3.setVisibility(View.INVISIBLE);
        //группа
        /*if (groups.getSelectedItem().toString().equals(allGroups.get(0)))
        {
            error =1;
            er4.setVisibility(View.VISIBLE);
        }
        else {
            er4.setVisibility(View.INVISIBLE);
        }*/

        if (error==0) {
            fname = name.getText().toString();
            lname = lastname.getText().toString();
            patr = patronymic.getText().toString();
            gruppa = GetNom(groups.getSelectedItem().toString());

            setContentView(R.layout.registrationlayout2);

            email = findViewById(R.id.email);
            password = findViewById(R.id.password);
            password2 = findViewById(R.id.password2);

            er5 = findViewById(R.id.imageView5);
            er6 = findViewById(R.id.imageView7);
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void regClick2(View v) {
        Log.d(LOG_TAG, "нажал на вторую кнопку");
        error = 0;
        //почта
        if (email.getText().length() <1) {
            error =1;
            er5.setVisibility(View.VISIBLE);
        }
        else er5.setVisibility(View.INVISIBLE);

        //пароль длина
        if (password.getText().length() >= 4) {
            er6.setVisibility(View.INVISIBLE);
            //пароль совпадение
            if (password.getText().toString().equals(password2.getText().toString()))
            {
                er6.setVisibility(View.INVISIBLE);
            }
            else
            {
                error =1;
                er6.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            error =1;
            er6.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "Длина пароля должна быть минимум 4 символа", Toast.LENGTH_SHORT).show();
        }

        if (error==0) {
            pass = password.getText().toString();
            login = email.getText().toString();


        Log.d(LOG_TAG, login + " email; " + gruppa + " group;" + pass + " password; " +
                lname + " lastname; " + fname + " firstname;");

        String gr = null;
        try {
            gr = JsonToJava.getContent(VariablesList.Sait + "jenda_new_user_m.php?group=" + gruppa +
                    "&firstname=" + fname + "&lastname=" + lname + "&mail=" + login + "&patronymic=" + patr +
                    "&password=" + pass + "&status=1");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Integer.parseInt(String.valueOf(gr.charAt(0)))==1)
        {
            Toast.makeText(getApplicationContext(), "Регистрация прошла успешно", Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "Ваш аккаунт скоро будет подтвержден", Toast.LENGTH_LONG).show();
            VariablesList.groupfrom = gruppa;
            VariablesList.status = 1;

            SharedPreferences.Editor editor = mSettings.edit();
            editor.clear();
            editor.putString(VariablesList.EMAIL, login);
            editor.putString(VariablesList.PASSWORD, pass);
            editor.apply();
            super.finish();
        }
        else Toast.makeText(getApplicationContext(), "Во время регистрации произошла ошибка (" + gr + ")", Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Integer GetNom(String f){

        for (Map.Entry<Integer,String> entry : gr.entrySet())
        {
            if (f.equals(entry.getValue()))
            {
                return entry.getKey();
            }
        }
        return 0;
    };

}




