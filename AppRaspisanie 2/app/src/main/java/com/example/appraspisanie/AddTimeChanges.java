package com.example.appraspisanie;

import static com.example.appraspisanie.ForTeacherAdd.les;
import static com.example.appraspisanie.ForTeacherAdd2.corp;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class AddTimeChanges extends AppCompatActivity {

    public ArrayAdapter adapter;
    public ArrayAdapter<String> adapter2;
    public ArrayAdapter<String> adapter3;
    public ArrayAdapter<String> adapter4;
    public ArrayAdapter<String> adapter5;
    public ArrayAdapter<String> adapter6;
    public ArrayAdapter<String> adapter7;
    public ArrayAdapter<String> adapter8;

    protected static String[] month = new String[]{"Январь", "Февраль", "Март", "Апрель",
            "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};

    JsonToJava jtj = new JsonToJava();

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_change);

        try {
            String content = JsonToJava.getContent(VariablesList.Sait + "jenda_groups_m.php");
            jtj.onPostExecuteGroup(content);
            content = JsonToJava.getContent(VariablesList.Sait + "jenda_lessons_m.php");
            jtj.onPostExecuteLessons(content);
            content = JsonToJava.getContent(VariablesList.Sait + "jenda_teachers_m.php");
            jtj.onPostExecuteTeachers(content);
            content = JsonToJava.getContent(VariablesList.Sait + "jenda_classes_m.php");
            jtj.onPostExecuteClass(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Spinner sp1 = findViewById(R.id.spinner20);
        adapter = new ArrayAdapter(this, R.layout.forgroupchosedropspinner, VariablesList.groupscollege);
        sp1.setAdapter(adapter);

        Spinner sp2 = findViewById(R.id.spinner21);
        adapter2 = new ArrayAdapter(this, R.layout.forgroupchosedropspinner, les);
        sp2.setAdapter(adapter2);

        Spinner sp3 = findViewById(R.id.spinner22);
        adapter3 = new ArrayAdapter(this, R.layout.forgroupchosedropspinner, VariablesList.lessons);
        sp3.setAdapter(adapter3);

        Spinner sp4 = findViewById(R.id.spinner24);
        adapter4 = new ArrayAdapter(this, R.layout.forgroupchosedropspinner, VariablesList.teachers);
        sp4.setAdapter(adapter4);

        Spinner sp5 = findViewById(R.id.spinner25);
        adapter5 = new ArrayAdapter(this, R.layout.forgroupchosedropspinner, corp);
        sp5.setAdapter(adapter5);

        Spinner sp6 = findViewById(R.id.spinner23);
        adapter6 = new ArrayAdapter(this, R.layout.forgroupchosedropspinner, VariablesList.class1);
        adapter7 = new ArrayAdapter(this, R.layout.forgroupchosedropspinner, VariablesList.class2);
        sp6.setAdapter(adapter6);

        Spinner sp7 = findViewById(R.id.spinner26);
        adapter8 = new ArrayAdapter(this, R.layout.forgroupchosedropspinner, month);
        sp7.setAdapter(adapter8);

        TextView et = findViewById(R.id.editTextNumberDecimal2);
        TextView et2 = findViewById(R.id.editTextNumberDecimal);

        sp5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp5.getSelectedItem().toString().equals("Книжный"))
                {
                    sp6.setAdapter(adapter6);
                }
                else if (sp5.getSelectedItem().toString().equals("Авиа"))
                {
                    sp6.setAdapter(adapter7);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(v -> {
            if (et.getText().toString().equals("") || et2.getText().toString().equals("")) {
            }
            else
            {
                VarForDB.getGroup(sp1.getSelectedItem().toString());
                VarForDB.getLesNom(sp2.getSelectedItem().toString());
                VarForDB.getLesName(sp3.getSelectedItem().toString());
                VarForDB.getTeacher(sp4.getSelectedItem().toString());
                VarForDB.getClassroom(sp6.getSelectedItem().toString(), sp5.getSelectedItem().toString());
                VarForDB.getDataNew(sp7.getSelectedItem().toString(), Integer.parseInt(et.getText().toString()), Integer.parseInt(et2.getText().toString()));

                try {
                    try {
                        String content = JsonToJava.getContent(VariablesList.Sait + "jenda_timechange_new_m.php?group=" + VarForDB.group + "&lesname=" + VarForDB.LesName +
                                "&lesnom=" + VarForDB.LesNom  + "&classroom=" + VarForDB.Classroom +
                                "&data=" + VarForDB.YearNew + "-" + VarForDB.MonthNew + "-" + VarForDB.DayNew +
                                "&teacher=" + VarForDB.Teacher);
                        //data=2021-12-24&teacher=47
                        if (Integer.parseInt(String.valueOf(content.charAt(0))) == 1) {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Замена записана",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                            super.finish();
                        }
                        else {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Произошла ошибка. Возможно уже есть замены для этой группы или учителя",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            } catch (NumberFormatException e) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Произошла ошибка. Проверьте правильность введеннёх данных",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                e.printStackTrace();
                }
            }
        });
    }

}
