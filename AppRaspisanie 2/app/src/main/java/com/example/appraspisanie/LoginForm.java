package com.example.appraspisanie;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

@RequiresApi(api = Build.VERSION_CODES.O)
public class LoginForm extends AppCompatActivity {

    private static final String LOG_TAG = "myLogs";

    JsonToJava jtj = new JsonToJava();
    SharedPreferences mSettings;

    Button login;
    Button reg;

    TextView mail;
    TextView password;

    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "открылось");

        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        onLoginCreate();
    }

    private void onLoginCreate(){
        setContentView(R.layout.login_page);

        login = findViewById(R.id.buttots);
        reg = findViewById(R.id.buttots2);

        mail = findViewById(R.id.maillog);
        password = findViewById(R.id.passlog);

        checkBox = findViewById(R.id.checkBox2);

        mSettings = getSharedPreferences(VariablesList.APP_PREFERENCES, Context.MODE_PRIVATE);

        String l = mSettings.getString(VariablesList.EMAIL,"");
        String p = mSettings.getString(VariablesList.PASSWORD,"");

        if (l.length()>1) {
            if (p.length() > 1) {
                check_log(l, p);
            }
            Log.d(LOG_TAG, mSettings.getString(VariablesList.EMAIL, "") + "   " + mSettings.getString(VariablesList.PASSWORD, ""));

            mail.setText(l);
        }
    }

    //проверка пароля и авторизация
    private void check_log(String l, String p){
        try {
            String c = JsonToJava.getContent(VariablesList.Sait+"jenda_cheсklogin_m.php?email=" + l + "&pas=" + p);
            try {
                byte a = Byte.parseByte(String.valueOf(c.charAt(0)));
                //var.auth = Integer.parseInt(String.valueOf(c.charAt(0)));
                if (a == 1) {
                    String d = JsonToJava.getContent(VariablesList.Sait + "jenda_getInfo_m.php?email=" + l);
                    Log.d(LOG_TAG, d);
                    jtj.onPostExecuteCheckLog(d);
                    Log.d(LOG_TAG, VariablesList.status.toString());

                    if (VariablesList.status == 3) {
                        Intent intent = new Intent(LoginForm.this, AdminChose.class);
                        startActivity(intent);
                    } else if (VariablesList.status == 2 && VariablesList.check == 1) {
                        Intent intent = new Intent(LoginForm.this, TimeSheet.class);
                        startActivity(intent);
                    } else if (VariablesList.status == 2 && VariablesList.check == 0) {
                        onNewTeacher(l);
                    }
                    else if (VariablesList.status == 1) {
                        Intent intent = new Intent(LoginForm.this, TimeSheet.class);
                        startActivity(intent);
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Неправильный пароль или почта", Toast.LENGTH_SHORT).show();
                    //setContentView(R.layout.error_page);
                }
            } catch (NumberFormatException e) {
                setContentView(R.layout.error_page);
            }
        } catch (IOException e) {
            setContentView(R.layout.error_page);
            e.printStackTrace();
        }
    }

    public void loginPage(View v) {
        Log.d(LOG_TAG, "нажал на кнопку");

        onLoginCreate();
    }

    @SuppressLint("ResourceType")
    private void onNewTeacher(String l){
        setContentView(R.layout.setnewpassword);

        TextView p,p1;

        p = findViewById(R.id.setnewpass);
        p1 = findViewById(R.id.setnewpass2);

        findViewById(R.id.button21).setOnClickListener(v -> {
            if (p.getText().toString().equals(p1.getText().toString())){
                try {
                    String c = JsonToJava.getContent(VariablesList.Sait+"jenda_set_new_password_m.php?mail=" + l + "&password=" + p.getText().toString());
                    if (Byte.parseByte(String.valueOf(c.charAt(0))) == 1) {
                        Toast.makeText(getApplicationContext(), "Новый пароль успешно сохранен", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginForm.this, TimeSheet.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
