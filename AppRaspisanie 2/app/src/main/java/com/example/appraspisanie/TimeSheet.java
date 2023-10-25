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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TimeSheet extends AppCompatActivity {

    private static final String LOG_TAG = "myLogs";

    String[] groupFrom = new String[] { "weekDay" };
    int[] groupTo = new int[] { android.R.id.text1 };

    SharedPreferences mSettings;

    protected static Map <Integer,String> gr = new HashMap<>();

    Button login, reg;

    TextView mail, password, tv, data, contentView;

    CheckBox checkBox;

    ImageView img;
    ExpandableListView expandableListView;
    static SimpleExpandableListAdapter adapter;

    JsonToJava jtj = new JsonToJava();

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        mSettings = getSharedPreferences(VariablesList.APP_PREFERENCES, Context.MODE_PRIVATE);

        String l = mSettings.getString(VariablesList.EMAIL, "");
        String p = mSettings.getString(VariablesList.PASSWORD, "");

        if (l.length() > 1) {
            if (p.length() > 1) {
                check_log(l, p);
            }
            else {
                onLogin();
                Log.d(LOG_TAG, mSettings.getString(VariablesList.EMAIL, "") + "   " + mSettings.getString(VariablesList.PASSWORD, ""));
            }
        }
        else onLogin();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onLogin(){
        setContentView(R.layout.login_page);

        login = findViewById(R.id.buttots);
        reg = findViewById(R.id.buttots2);

        mail = findViewById(R.id.maillog);
        password = findViewById(R.id.passlog);

        checkBox = findViewById(R.id.checkBox2);

        mSettings = getSharedPreferences(VariablesList.APP_PREFERENCES, Context.MODE_PRIVATE);

        if (mSettings.getString(VariablesList.EMAIL,"").length()>1) {
            mail.setText(mSettings.getString(VariablesList.EMAIL,""));
        }

        findViewById(R.id.buttots).setOnClickListener(v -> {
            Log.d(LOG_TAG, "нажал на кнопку");
            Log.d(LOG_TAG, mail.getText().toString() + "  " + password.getText().toString());

            //считывание пароля и почты
            String l=mail.getText().toString();
            String p=password.getText().toString();

            //вводится логин и пароль в кэш
            SharedPreferences.Editor editor = mSettings.edit();
            editor.clear();
            editor.putString(VariablesList.EMAIL, l);

            if (checkBox.isChecked()) {
                editor.putString(VariablesList.PASSWORD, p);
            }
            editor.apply();

            Log.d(LOG_TAG, l);

            check_log(l,p);
        });

        findViewById(R.id.buttots2).setOnClickListener(v -> {
            Log.d(LOG_TAG, "нажал на кнопку");

            Intent intent = new Intent(this, RegistrationForm.class);
            startActivity(intent);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void check_log(String l, String p){
        try {
            String c = JsonToJava.getContent(VariablesList.Sait+"jenda_checklogin_m.php?email=" + l + "&pas=" + p);
            try {
                byte a = Byte.parseByte(String.valueOf(c.charAt(0)));
                //var.auth = Integer.parseInt(String.valueOf(c.charAt(0)));
                if (a == 1) {
                    String d = JsonToJava.getContent(VariablesList.Sait + "jenda_getInfo_m.php?email=" + l);
                    Log.d(LOG_TAG, d);
                    jtj.onPostExecuteCheckLog(d);
                    Log.d(LOG_TAG, VariablesList.status.toString());

                    if (VariablesList.status == 3) {
                        adminLog();
                    } else if (VariablesList.status == 2 && VariablesList.check == 1) {
                        studentLog();
                    } else if (VariablesList.status == 2 && VariablesList.check == 0) {
                        onNewTeacher(l);
                    }
                    else if (VariablesList.status == 1) {
                        studentLog();
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

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void adminLog(){
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
        Intent intent = new Intent(this, ForTeacherAdd.class);
        startActivity(intent);
    }

    public void onClick2(View v) {
        if (Integer.parseInt(tv.getText().toString()) != 0) {
            Intent intent = new Intent(this, ConfirmNewUser.class);
            startActivity(intent);
        }
    }

    public void onClick3(View v){
        Intent intent = new Intent(this, AddTimeChanges.class);
        startActivity(intent);
    }

    public void onClick4(View v){
        Intent intent = new Intent(this, AddNewTeacher.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick6(View v) {
        setContentView(R.layout.group_selection);
        String content;

        try {
            content = JsonToJava.getContent(VariablesList.Sait + "jenda_groups_m.php");
            jtj.onPostExecuteGroup(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Spinner spin = findViewById(R.id.spinnerDr);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.forgroupchosedropspinner, VariablesList.groupscollege);
        spin.setAdapter(adapter);

        Button button = findViewById(R.id.button1010);
        button.setOnClickListener(v1 -> {
            VarForDB.getGroup(spin.getSelectedItem().toString());
            VariablesList.groupfrom = VarForDB.group;
            Toast.makeText(getApplicationContext(), VariablesList.groupfrom+"  "+VarForDB.group, Toast.LENGTH_SHORT).show();
            studentLog();
        });
    }

    public void onClickExit(View v){
        setContentView(R.layout.adminchose);
    }


    public void onClickUncompleted(View v){
        Intent intent = new Intent(this, Uncomplited.class);
        startActivity(intent);
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void studentLog(){
        setContentView(R.layout.timesheet);
        if (VariablesList.status==3){
            findViewById(R.id.button25).setVisibility(View.VISIBLE);
        }
        expandableListView = findViewById(R.id.expList);
        contentView = findViewById(R.id.textView8);
        data = findViewById(R.id.textView25);

        data.setText(VariablesList.WeekStart.toString() + " --- " + VariablesList.WeekEnd.toString());
        try{
            String contentTS;
            if (VariablesList.status ==2) {
                contentTS = JsonToJava.getContent(VariablesList.Sait + "jenda_ts_reorg2_m.php?kod=" + VariablesList.kod+"&timestart="+VariablesList.WeekStart+"&timeend="+VariablesList.WeekEnd);
                //http://domenforwork.atwebpages.com/jenda_ts_reorg2_m.php?group=6&timestart=2022-01-1&timeend=2022-01-08

            } else {
                contentTS = JsonToJava.getContent(VariablesList.Sait + "jenda_ts_reorg2_m.php?group=" + VariablesList.groupfrom+"&timestart="+VariablesList.WeekStart+"&timeend="+VariablesList.WeekEnd);
            }

            Log.d(LOG_TAG, VariablesList.groupfrom + "/группа /////////////////////");
            jtj.onPostExecuteTimeSheet(contentTS);
            SetTextOnList();
        }
        catch (IOException ex){
            contentView.setText("Ошибка: " + ex.getMessage());
            Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
                        studentLog();
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



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick(View v) {
        try {
            img = findViewById(R.id.imageView6);
            String contentTS;
            int Help =1;

            if (VariablesList.status==1 || VariablesList.status==3) {
                contentTS = JsonToJava.getContent(VariablesList.Sait + "jenda_ts_reorg2_m.php?group=" + VariablesList.groupfrom+
                        "&timestart="+VariablesList.WeekStart+"&timeend="+VariablesList.WeekEnd);
            } else {
                contentTS = JsonToJava.getContent(VariablesList.Sait + "jenda_ts_reorg2_m.php?kod=" + VariablesList.kod+
                        "&timestart="+VariablesList.WeekStart+"&timeend="+VariablesList.WeekEnd);
            }

            Log.d(LOG_TAG, VariablesList.groupfrom + "/группа /////////////////////");

            try {
                Help = Integer.parseInt(String.valueOf(contentTS.charAt(0)));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            if (Help == 0)
            {
                img.setVisibility(View.VISIBLE);
            }
            else {
                img.setVisibility(View.GONE);
            }
            jtj.onPostExecuteTimeSheet(contentTS);
            SetTextOnList();
        }
        catch (IOException ex){
            Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show();
        }
    }


    private void SetTextOnList() {
        // список ID view-элементов, в которые будет помещены атрибуты элементов
        int[] childTo = new int[]{R.id.num, R.id.text1,R.id.check, R.id.text2};
        Button back = findViewById(R.id.button10);
        Button next = findViewById(R.id.button11);

        back.setVisibility(View.INVISIBLE);
        next.setVisibility(View.INVISIBLE);

        adapter = null;
        //expandableListView.setAdapter((ExpandableListAdapter) null);
        expandableListView.setVisibility(View.INVISIBLE);

        adapter = new SimpleExpandableListAdapter(
                this, jtj.groupDataList,
                R.layout.expandable_list_item, groupFrom,
                groupTo, jtj.timeDataList, R.layout.rows,
                jtj.GroupsArray, childTo);

        expandableListView.setAdapter(adapter);

        Date now = new Date();
        //раскрытие

        for(int i=0; i < adapter.getGroupCount(); i++)
            if (now.getDay() == i+1)
            {
                expandableListView.expandGroup(i);
            }

        expandableListView.setVisibility(View.VISIBLE);
        back.setVisibility(View.VISIBLE);
        next.setVisibility(View.VISIBLE);
    }



    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickNext(View v)
    {
        VariablesList.getNextWeek();
        data.setText(VariablesList.WeekStart.toString() + " --- " + VariablesList.WeekEnd.toString());
        onClick(v);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickLast(View v)
    {
        VariablesList.getLastWeek();
        data.setText(VariablesList.WeekStart.toString() + " --- " + VariablesList.WeekEnd.toString());
        onClick(v);
    }
}