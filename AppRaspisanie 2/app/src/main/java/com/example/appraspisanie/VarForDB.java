package com.example.appraspisanie;

import android.os.Build;

import androidx.annotation.RequiresApi;

public class VarForDB {
    public static Integer group;
    public static Integer LesName;
    public static Integer WeekDay;
    public static Integer LesNom;
    public static Integer WeekType;
    public static Integer Classroom;
    public static Integer DayStart;
    public static Integer MonthStart;
    public static Integer YearStart;
    public static Integer DayEnd;
    public static Integer MonthEnd;
    public static Integer YearEnd;
    public static Integer Teacher;

    public static Integer YearNew;
    public static Integer DayNew;
    public static Integer MonthNew;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void getGroup(String d) {
        int i =0;
        for (String c:
        VariablesList.groupscollege) {
            if (c.equals(d)) {
                group = VariablesList.groupscollegenom.get(i);
                break;
            }
            i++;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void getLesName(String d) {
        int i =0;
        for (String c:
                VariablesList.lessons) {
            if (c.equals(d)) {
                LesName = VariablesList.lessonsnom.get(i);
                break;
            }
            i++;
        }
    }

    public static void getWeekDay(String d) {
        int i =1;
        for (String c:
                ForTeacherAdd.days) {
            if (c.equals(d)) {
                WeekDay = i;
                break;
            }
            i++;
        }
    }

    public static void getLesNom(String d) {
        int i =0;
        for (String c:
                ForTeacherAdd.les) {
            if (c.equals(d)) {
                LesNom = ForTeacherAdd.lesnom[i];
                break;
            }
            i++;
        }
    }

    public static void getWeekType(String d) {
        switch (d) {
            case ("Нечетная"):
                WeekType =2;
                break;
            case ("Четная"):
                WeekType =1;
                break;
            case ("Всегда"):
                WeekType =3;
                break;
            default:
                WeekType =3;
                break;
        }
    }

    //приложение расписание ЛПА
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void getClassroom(String d, String a) {
        int i =0;
        if (a.equals("Книжный")) {
            for (String c :
                    VariablesList.class1) {
                if (c.equals(d)) {
                    Classroom = VariablesList.class1nom.get(i);
                    break;
                }
                i++;
            }
        } else {
            for (String c :
                    VariablesList.class2) {
                if (c.equals(d)) {
                    Classroom = VariablesList.class2nom.get(i);
                    break;
                }
                i++;
            }
        }
    }

    public static void getDataNew(String d,Integer a,Integer b) {
        int i = 1;
        DayNew = a;
        YearNew = b;
        for (String c:
                ForTeacherAdd2.month) {
            if (c.equals(d)) {
                MonthNew = i;
                break;
            }
            i++;
        }
    }

    public static void getDataStart(String d,Integer a,Integer b) {
        int i =1;
        DayStart = a;
        YearStart = b;
        for (String c: ForTeacherAdd2.month) {
            if (c.equals(d)) {
                MonthStart = i;
                break;
            }
            i++;
        }
    }
    public static void getDataEnd(String d,Integer a, Integer b) {
        int i =1;
        DayEnd = a;
        YearEnd = b;
        for (String c: ForTeacherAdd2.month) {
            if (c.equals(d)) {
                MonthEnd = i;
                break;
            }
            i++;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void getTeacher(String d) {
        int i =0;
        for (String c: VariablesList.teachers) {
            if (c.equals(d)) {
                Teacher = VariablesList.teachersnom.get(i);
                break;
            }
            i++;
        }
    }
}
