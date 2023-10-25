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

public class ForTeacherAdd2 extends AppCompatActivity {

    private static final String LOG_TAG = "myLogs";

    ArrayAdapter<String> adapter1, adapter2, adapter3, adapter4, adapter5, adapter6;
    protected static String[] corp = new String[]{"Книжный", "Авиа"};
    protected static String[] month = new String[]{"Январь", "Февраль", "Март", "Апрель",
            "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};

    JsonToJava jtj = new JsonToJava();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_sheet_add2);

        TextView tv = findViewById(R.id.textView23);

        tv.setText("Выберите дату и учителя и нажмите кнопку");

        String content = null;

        try {
            content = jtj.getContent("http://domenforwork.atwebpages.com/jenda_teachers_m.php");
            jtj.onPostExecuteTeachers(content);
            content = jtj.getContent("http://domenforwork.atwebpages.com/jenda_classes_m.php");
            jtj.onPostExecuteClass(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Spinner sp1 = findViewById(R.id.spinner10);
        adapter1 = new ArrayAdapter(this, R.layout.forgroupchosedropspinner, corp);
        sp1.setAdapter(adapter1);


        Spinner sp2 = findViewById(R.id.spinner11);
        adapter2 = new ArrayAdapter(this, R.layout.forgroupchosedropspinner, VariablesList.class1);
        adapter3 = new ArrayAdapter(this, R.layout.forgroupchosedropspinner, VariablesList.class2);
        sp2.setAdapter(adapter2);



        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp1.getSelectedItem().toString().equals("Книжный"))
                {
                    sp2.setAdapter(adapter2);
                }
                else if (sp1.getSelectedItem().toString().equals("Авиа"))
                {
                    sp2.setAdapter(adapter3);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner sp3 = findViewById(R.id.spinner12);
        adapter4 = new ArrayAdapter(this, R.layout.forgroupchosedropspinner, VariablesList.teachers);
        sp3.setAdapter(adapter4);

        Spinner sp4 = findViewById(R.id.spinner13);
        adapter5 = new ArrayAdapter(this, R.layout.forgroupchosedropspinner, month);
        sp4.setAdapter(adapter5);

        Spinner sp5 = findViewById(R.id.spinner14);
        adapter6 = new ArrayAdapter(this, R.layout.forgroupchosedropspinner, month);
        sp5.setAdapter(adapter6);

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
                    String content1 = jtj.getContent(VariablesList.Sait + "/jenda_ts_new_m.php?group=" + VarForDB.group + "&LNa=" + VarForDB.LesName +
                            "&WD=" + VarForDB.WeekDay + "&LNo=" + VarForDB.LesNom + "&WT=" + VarForDB.WeekType + "&CR=" + VarForDB.Classroom +
                            "&DS=" + VarForDB.YearStart + "-" + VarForDB.MonthStart + "-" + VarForDB.DayStart +
                            "&DE=" + VarForDB.YearEnd + "-" + VarForDB.MonthEnd + "-" + VarForDB.DayEnd + "&Teacher=" + VarForDB.Teacher);
                    if (Integer.parseInt(String.valueOf(content1.charAt(0)))==1)
                    {
                        tv.setText("Успешно");
                        ForTeacherAdd2.super.finish();
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
