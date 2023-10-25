package com.example.appraspisanie;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@RequiresApi(api = Build.VERSION_CODES.O)
public class VariablesList {
    //protected static String Sait = "http://domenforwork.atwebpages.com/";
    protected static String Sait = "https://2soc.ru/m/";
    //protected static String Sait = "http://schedule.pkgod.ru/m/";

    protected static String kod; //код из БД
    protected static String firstnamefrom; //имя из БД
    protected static String lastnamefrom; //фамилия из БД
    protected static String name; //ФИО из БД
    protected static Integer groupfrom; //группа из БД
    protected static Integer status; //статус из БД
    protected static Integer check; //подтверждение из бд

    protected static ArrayList<String> groupscollege; //список групп из БД
    protected static ArrayList<Integer> groupscollegenom;
    protected static ArrayList<String> groupsschool; //список групп из БД
    protected static ArrayList<Integer> groupsschoolnom;
    protected static ArrayList<String> teachers; //список учсителей из БД
    protected static ArrayList<Integer> teachersnom;
    protected static ArrayList<String> lessons; //список предметов из БД
    protected static ArrayList<Integer> lessonsnom;
    protected static ArrayList<String> class1; //список классов 1 корпуса из БД
    protected static ArrayList<Integer> class1nom;
    protected static ArrayList<String> class2; //список классов 2 корпуса из БД
    protected static ArrayList<Integer> class2nom;

    protected static final String APP_PREFERENCES = "mysettings"; // это будет именем файла настроек
    protected static final String EMAIL = "email"; // почта
    protected static final String PASSWORD = "password"; // пароль

    protected static LocalDate now = LocalDate.now(); //переменные для расчёта времени
    protected static LocalDate WeekStart = now.with(DayOfWeek.MONDAY);
    protected static LocalDate WeekEnd = WeekStart.plusDays(6);

    protected static Integer stranica; //выбранная страница
    protected static Integer size; //количество новых пользователей
    protected static Integer kodppl;
    protected static String lastname;
    protected static String firstname;
    protected static String patronymic;
    protected static String emailppl;
    protected static String groupppl;



    protected static void getNextWeek()
    {
        now = now.plusWeeks(1);
        WeekStart = WeekStart.plusWeeks(1);
        WeekEnd = WeekStart.plusDays(6);
    }

    protected static void getLastWeek()
    {
        now = now.minusWeeks(1);
        WeekStart = WeekStart.minusWeeks(1);
        WeekEnd = WeekStart.plusDays(6);
    }

    protected static Date getNow()
    {
        Date date = Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return date;
    }

    protected static HashMap<Integer, String> groupsnom;



}

