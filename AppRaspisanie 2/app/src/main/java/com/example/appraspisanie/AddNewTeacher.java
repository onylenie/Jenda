package com.example.appraspisanie;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.security.SecureRandom;

//загрузка класса
public class AddNewTeacher extends AppCompatActivity {

    Button next, back;
    TextView name1, name2, name3;
    String fname, lname, pname, email, pass;
    CheckBox checkBox;

    @SuppressLint({"SetTextI18n", "CutPasteId"})
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnewteacher);

        //вызов первой формы
        SetTeacherData();
    }

    //форма с вводом данных об учителе
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("CutPasteId")
    protected void SetTeacherData(){
        setContentView(R.layout.addnewteacher);

        next = findViewById(R.id.getNewpassword);

        next.setOnClickListener(v -> {
            name1 = findViewById(R.id.newFirstame);
            name2 = findViewById(R.id.newLastname);
            name3 = findViewById(R.id.newPatronymic);
            checkBox = findViewById(R.id.checkBox3);

            fname = name1.getText().toString();
            lname = name2.getText().toString();
            pname = name3.getText().toString();

            if (!fname.equals("") && !lname.equals("")) {
                if(checkBox.isChecked()) {
                    email = "null";
                    pass = "null";

                    try {
                        String c = JsonToJava.getContent(VariablesList.Sait+"jenda_new_teacher_m.php?firstname=" + fname +
                                "&lastname=" + lname + "&patronymic=" + pname + "&mail=" + email + "&password=" + pass);
                        try {
                            int d = Integer.parseInt(String.valueOf(c.charAt(0)));

                            if (d == 1) {
                                Toast.makeText(getApplicationContext(), "Данные успешно сохранены", Toast.LENGTH_SHORT).show();

                                super.finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "При записи данных произошла ошибка", Toast.LENGTH_SHORT).show();
                            }
                        } catch (NumberFormatException e) {
                            setContentView(R.layout.error_page);
                        }
                    }
                    catch (IOException e) {
                        setContentView(R.layout.error_page);
                    }
                } else {
                    ConfirmTeacherData();
                }
            }
        });
    }


    //форма с подтверждением введенных данных
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    protected void ConfirmTeacherData() {
        setContentView(R.layout.confirmnewteacher);

        name1 = findViewById(R.id.textView14);
        name1.setText(fname + " " + lname + " " + pname);

        next = findViewById(R.id.button18);
        back = findViewById(R.id.button19);

        next.setOnClickListener(v -> {
            try {
                name3 = findViewById(R.id.newMail);
                email = name3.getText().toString();

                String c = JsonToJava.getContent(VariablesList.Sait+"jenda_new_teacher_check_m.php?email=" + email);
                try {
                    int d = Integer.parseInt(String.valueOf(c.charAt(0)));

                    if (d == 1) {
                        GetNewPassword();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Аккаунт с такой почтой уже создан. \nПроверьте правильность введенных данных", Toast.LENGTH_SHORT).show();
                        //setContentView(R.layout.error_page);
                    }
                } catch (NumberFormatException e) {
                    setContentView(R.layout.error_page);
                }
            } catch (IOException e) {
                setContentView(R.layout.error_page);
                e.printStackTrace();
            }
        });

        back.setOnClickListener(v -> {
            SetTeacherData();

            name1 = findViewById(R.id.newFirstame);
            name1.setText(fname);
            name2 = findViewById(R.id.newLastname);
            name2.setText(lname);
            name3 = findViewById(R.id.newPatronymic);
            name3.setText(pname);
        });
    }


    //форма с выводом почты и пароля
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void GetNewPassword(){
        setContentView(R.layout.getnewpassword);

        name1 = findViewById(R.id.textView56);
        name1.setText(email);

        pass = generateRandomPassword(8);
        name2 = findViewById(R.id.textView58);
        name2.setText(pass);

        next = findViewById(R.id.button20);

        next.setOnClickListener(v -> SetDataToDB());
    }

    //отправка данных в базу данных
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void SetDataToDB(){
        try {
            String c = JsonToJava.getContent(VariablesList.Sait+"jenda_new_teacher_m.php?firstname=" + fname +
                    "&lastname=" + lname + "&patronymic=" + pname + "&mail=" + email + "&password=" + pass);
            try {
                int d = Integer.parseInt(String.valueOf(c.charAt(0)));

                if (d == 1) {
                    //сохранение почты и пароля в буфер обмена
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("", "Почта: " + email + "  Пароль: " + pass);
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(getApplicationContext(), "Данные успешно записаны \nВременный пароль скопирован в буфер обемена", Toast.LENGTH_SHORT).show();

                    super.finish();
                } else {
                    Toast.makeText(getApplicationContext(), "При записи данных произошла ошибка", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                setContentView(R.layout.error_page);
            }
        }
        catch (IOException e) {
            setContentView(R.layout.error_page);
            e.printStackTrace();
        }
    }

    //генерация пароля
    public static String generateRandomPassword(int len)
    {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < len; i++)
        {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }
}

/*
button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", editText.getText().toString());
                clipboard.setPrimaryClip(clip);
            }
        });

 */