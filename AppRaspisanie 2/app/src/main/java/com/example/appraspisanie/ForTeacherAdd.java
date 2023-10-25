package com.example.appraspisanie;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class ForTeacherAdd extends AppCompatActivity {

    private static final String LOG_TAG = "myLogs";

    ArrayAdapter<String> adapter, adapter2, adapter3, adapter4, adapter5, adapter6, adapter7, adapter8, adapter9, adapter10, adapter11;
    protected static String[] days = new String[]{"Понедельник",
            "Вторник", "Среда", "Четверг", "Пятница"};
    protected static String[] les = new String[]{"1-2 пара",
            "3-4 пара", "5-6 пара", "7-8 пара", "9-10 пара"};
    protected static Integer[] lesnom = new Integer[]{7, 8, 9, 10, 11};
    protected static String[] type = new String[]{"Всегда",
            "Нечетная", "Четная"};

    protected static String[] corp = new String[]{"Книжный", "Авиа"};
    protected static String[] month = new String[]{"Январь", "Февраль", "Март", "Апрель",
            "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};

    JsonToJava jtj = new JsonToJava();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_sheet_add);

        String content;

        try {
            content = JsonToJava.getContent(VariablesList.Sait + "jenda_groups_m.php");
            jtj.onPostExecuteGroup(content);
            content = JsonToJava.getContent(VariablesList.Sait + "jenda_lessons_m.php");
            jtj.onPostExecuteLessons(content);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Spinner sp1 = findViewById(R.id.spinner5);
        adapter = new ArrayAdapter(this, R.layout.forgroupchosedropspinner, VariablesList.groupscollege);
        sp1.setAdapter(adapter);

        Spinner sp2 = findViewById(R.id.spinner6);
        adapter2 = new ArrayAdapter(this, R.layout.forgroupchosedropspinner, VariablesList.lessons);
        sp2.setAdapter(adapter2);

        Spinner sp3 = findViewById(R.id.spinner7);
        adapter3 = new ArrayAdapter(this, R.layout.forgroupchosedropspinner, days);
        sp3.setAdapter(adapter3);

        Spinner sp4 = findViewById(R.id.spinner8);
        adapter4 = new ArrayAdapter(this, R.layout.forgroupchosedropspinner, les);
        sp4.setAdapter(adapter4);

        Spinner sp5 = findViewById(R.id.spinner9);
        adapter5 = new ArrayAdapter(this, R.layout.forgroupchosedropspinner, type);
        sp5.setAdapter(adapter5);


        Button btn = findViewById(R.id.button7);
        btn.setOnClickListener(v -> {
            VarForDB.getGroup(sp1.getSelectedItem().toString());
            VarForDB.getLesName(sp2.getSelectedItem().toString());
            VarForDB.getWeekDay(sp3.getSelectedItem().toString());
            VarForDB.getLesNom(sp4.getSelectedItem().toString());
            VarForDB.getWeekType(sp5.getSelectedItem().toString());

            next();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void next(){

        setContentView(R.layout.time_sheet_add2);

        TextView tv = findViewById(R.id.textView23);

        tv.setText("Выберите дату и учителя и нажмите кнопку");

        String content = null;

        try {
            content = JsonToJava.getContent(VariablesList.Sait + "jenda_teachers_m.php");
            jtj.onPostExecuteTeachers(content);
            content = JsonToJava.getContent(VariablesList.Sait + "jenda_classes_m.php");
            jtj.onPostExecuteClass(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Spinner sp1 = findViewById(R.id.spinner10);
        adapter6 = new ArrayAdapter(this, R.layout.forgroupchosedropspinner, corp);
        sp1.setAdapter(adapter6);


        Spinner sp2 = findViewById(R.id.spinner11);
        adapter7 = new ArrayAdapter(this, R.layout.forgroupchosedropspinner, VariablesList.class1);
        adapter8= new ArrayAdapter(this, R.layout.forgroupchosedropspinner, VariablesList.class2);
        sp2.setAdapter(adapter7);



        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp1.getSelectedItem().toString().equals("Книжный"))
                {
                    sp2.setAdapter(adapter7);
                }
                else if (sp1.getSelectedItem().toString().equals("Авиа"))
                {
                    sp2.setAdapter(adapter8);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner sp3 = findViewById(R.id.spinner12);
        adapter9 = new ArrayAdapter(this, R.layout.forgroupchosedropspinner, VariablesList.teachers);
        sp3.setAdapter(adapter9);

        Spinner sp4 = findViewById(R.id.spinner13);
        adapter10 = new ArrayAdapter(this, R.layout.forgroupchosedropspinner, month);
        sp4.setAdapter(adapter10);

        Spinner sp5 = findViewById(R.id.spinner14);
        adapter11 = new ArrayAdapter(this, R.layout.forgroupchosedropspinner, month);
        sp5.setAdapter(adapter11);

        TextView et = findViewById(R.id.editTextNumber);
        TextView et2 = findViewById(R.id.editTextNumber2);
        TextView et3 = findViewById(R.id.editTextNumber3);
        TextView et4 = findViewById(R.id.editTextNumber4);


        Button btn = findViewById(R.id.button8);
        btn.setOnClickListener(v -> {
            try {
                VarForDB.getClassroom(sp2.getSelectedItem().toString(), sp1.getSelectedItem().toString());
                VarForDB.getDataStart(sp4.getSelectedItem().toString(), Integer.parseInt(et.getText().toString()), Integer.parseInt(et2.getText().toString()));
                VarForDB.getDataEnd(sp5.getSelectedItem().toString(), Integer.parseInt(et3.getText().toString()), Integer.parseInt(et4.getText().toString()));
                VarForDB.getTeacher(sp3.getSelectedItem().toString());

                Log.d(LOG_TAG, "/jenda_ts_new_m.php?group=" + VarForDB.group + "&LNa=" + VarForDB.LesName +
                        "&WD=" + VarForDB.WeekDay + "&LNo=" + VarForDB.LesNom + "&WT=" + VarForDB.WeekType + "&CR=" + VarForDB.Classroom +
                        "&DS=" + VarForDB.YearStart + "-" + VarForDB.MonthStart + "-" + VarForDB.DayStart +
                        "&DE=" + VarForDB.YearEnd + "-" + VarForDB.MonthEnd + "-" + VarForDB.DayEnd + "&Teacher=" + VarForDB.Teacher);

                try {
                    String content1 = jtj.getContent(VariablesList.Sait + "jenda_ts_new_m.php?group=" + VarForDB.group + "&LNa=" + VarForDB.LesName +
                            "&WD=" + VarForDB.WeekDay + "&LNo=" + VarForDB.LesNom + "&WT=" + VarForDB.WeekType + "&CR=" + VarForDB.Classroom +
                            "&DS=" + VarForDB.YearStart + "-" + VarForDB.MonthStart + "-" + VarForDB.DayStart +
                            "&DE=" + VarForDB.YearEnd + "-" + VarForDB.MonthEnd + "-" + VarForDB.DayEnd + "&Teacher=" + VarForDB.Teacher);
                    if (Integer.parseInt(String.valueOf(content1.charAt(0)))==1)
                    {
                        tv.setText("Успешно");
                        super.finish();
                    }
                    else
                        tv.setText("Произошла ошибка");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (NumberFormatException e) {
                TextView tv1 = findViewById(R.id.textView23);
                tv1.setText("Произошла ошибка!! \n проверьте правильность ввода данных");
                e.printStackTrace();
            }
        });
    }
}
