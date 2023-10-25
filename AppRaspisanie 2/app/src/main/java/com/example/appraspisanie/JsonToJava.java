package com.example.appraspisanie;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class JsonToJava {
    VariablesList var;

    HashMap<String, String> m;

    protected String[] mGroupsArray = new String[] { "Понедельник",
            "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};
    protected String[] GroupsArray = new String[] { "LessonNom",
            "LessonName", "Classroom", "Teacher"};

    private static final String LOG_TAG = "myLogs";
    public String name = new String();

    ArrayList<Map<String, String>> groupDataList = new ArrayList<>();

    ArrayList<Map<String, String>> timeDataItemList = new ArrayList<>();

    ArrayList<ArrayList<Map<String, String>>> timeDataList = new ArrayList<>();


    //таблица с классами
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onPostExecuteClass(String strJson) {
        JSONObject dataJsonObj;
        try {
            dataJsonObj = new JSONObject(strJson);
            JSONArray classes1 = dataJsonObj.getJSONArray("Classes");
            VariablesList.class1 = new ArrayList<>();
            VariablesList.class1nom = new ArrayList<>();
            VariablesList.class2 = new ArrayList<>();
            VariablesList.class2nom = new ArrayList<>();
            // 2. перебираем и выводим список имен
            for (int i = 0; i < classes1.length(); i++) {
                JSONObject name = classes1.getJSONObject(i);
                if (name.getInt("Corpus")==1) {
                    String d = name.getString("ClassroomN");
                    Integer f = name.getInt("Kod_room");

                    Log.d(LOG_TAG, d);
                    //                    Log.d(LOG_TAG, var.teachers.toString());

                    var.class1.add(d);
                    var.class1nom.add(f);
                }
                else if (name.getInt("Corpus")==2) {
                    String d = name.getString("ClassroomN");
                    Integer f = name.getInt("Kod_room");

                    Log.d(LOG_TAG, d);
//                    Log.d(LOG_TAG, var.teachers.toString());

                    var.class2.add(d);
                    var.class2nom.add(f);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

        public static String getContent(String path) throws IOException {
        BufferedReader reader = null;
        InputStream stream = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.connect();
            stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder buf = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                buf.append(line).append("\n");
            }
            return (buf.toString());
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onPostExecuteTimeSheet(String strJson) {
        Integer temp = 1;
        m = new HashMap<>();
        groupDataList = new ArrayList<>();
        timeDataList = new ArrayList<>();
        timeDataItemList = new ArrayList<>();

        // выводим целиком полученную json-строку
        JSONObject dataJsonObj;

        try {
            dataJsonObj = new JSONObject(strJson);
            JSONArray classes1 = dataJsonObj.getJSONArray("TimeSheet");

            for (String group : mGroupsArray) {
                // заполняем список атрибутов для каждой группы
                m = new HashMap<>();
                m.put("weekDay", group);
                groupDataList.add(m);
            }
            // 2. перебираем и выводим список имен
            for (int i = 0; i < classes1.length(); i++) {
                JSONObject name = classes1.getJSONObject(i);


                while (temp != name.getInt("WeekDay")) {
                    timeDataList.add(timeDataItemList);
                    timeDataItemList = new ArrayList<>();
                    temp++;
                }

                if (name.getInt("WeekType") == 3) {
                    Log.d(LOG_TAG, "Тип недели - 3" + " " + name.getInt("WeekType") + "  " + name.getInt("KodTS"));

                    m = new HashMap<>();
                    m.put("LessonNom", name.getString("LessonNom"));
                    m.put("LessonName", name.getString("LessonName"));
                    m.put("Classroom", name.getString("Classroom"));
                    m.put("Teacher", name.getString("Teacher"));

                    timeDataItemList.add(m);
                } else {

                    Date now = new Date();
                    int d = IsOdd(now);
                    Log.d(LOG_TAG, "исодд = " + d);

                    if (name.getInt("WeekType") == (d + 1)) {
                        Log.d(LOG_TAG, "Четная неделя" + " " + name.getInt("WeekType") + "  " + name.getInt("KodTS"));

                        m = new HashMap<>();
                        m.put("LessonNom", name.getString("LessonNom"));
                        m.put("Class", name.getString("Class"));
                        m.put("LessonName", name.getString("LessonName"));
                        m.put("Classroom", name.getString("Classroom"));
                        m.put("Teacher", name.getString("Teacher"));
                        timeDataItemList.add(m);
                    }
                }
                Log.d(LOG_TAG, m.toString());
            }
            while (temp != 6) {
                Log.d(LOG_TAG, temp+"!!!!!!!!!!!!"+timeDataItemList);
                timeDataList.add(timeDataItemList);
                timeDataItemList = new ArrayList<>();
                temp++;
            }
            timeDataList.add(timeDataItemList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Integer IsOdd(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(VariablesList.getNow());
        int c = calendar.get(Calendar.YEAR);
        int d = 2022;
        if (c<d){
            return calendar.get(Calendar.WEEK_OF_MONTH) % 2;
        }
        else
        {
            return (calendar.get(Calendar.WEEK_OF_MONTH) + 1) % 2;
        }

    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onPostExecuteCheckLog(String strJson) {
        // выводим целиком полученную json-строку
        JSONObject dataJsonObj;

        try {
            dataJsonObj = new JSONObject(strJson);
            JSONArray classes1 = dataJsonObj.getJSONArray("People");

            // 2. перебираем и выводим список имен
            if (classes1.length()<2) {
                for (int i = 0; i < classes1.length(); i++) {
                    JSONObject name = classes1.getJSONObject(i);
                        Log.d(LOG_TAG, "  " + name.getString("password"));

                        var.kod = name.getString("KodPpl");
                        var.lastnamefrom = name.getString("Lastname");
                        var.firstnamefrom = name.getString("Firstname");
                        var.name= name.getString("Name");
                        var.status = name.getInt("StatusPpl");
                        var.check = name.getInt("CheckStatus");
                        var.groupfrom = name.getInt("GroupPpl");
                }
            }
            else {
                //какое нибудь сообщение об ошибке
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected HashMap<Integer,String> onPostExecuteGroup(String strJson) {
        // выводим целиком полученную json-строку
        JSONObject dataJsonObj;
        HashMap<Integer,String> c = new HashMap<>();
        try {
            dataJsonObj = new JSONObject(strJson);
            JSONArray classes1 = dataJsonObj.getJSONArray("Groups");
            VariablesList.groupscollege = new ArrayList<>(classes1.length());
            VariablesList.groupsschool = new ArrayList<>(classes1.length());
            VariablesList.groupsnom = new HashMap<>(classes1.length());
            VariablesList.groupscollegenom = new ArrayList<>();
            VariablesList.groupsschoolnom = new ArrayList<>();
            // 2. перебираем и выводим список имен
                for (int i = 0; i < classes1.length(); i++) {
                    JSONObject name = classes1.getJSONObject(i);
                    if (name.getInt("Belongs") == 3) {
                        String d = name.getString("GroupName");
                        Integer f = name.getInt("KodGroup");

                        Log.d(LOG_TAG, d);
                        Log.d(LOG_TAG, VariablesList.groupsschool.toString());

                        VariablesList.groupsschoolnom.add(f);
                        VariablesList.groupsschool.add(d);
                    } else {
                        String d = name.getString("GroupName");
                        Integer f = name.getInt("KodGroup");

                        Log.d(LOG_TAG, d);
                        Log.d(LOG_TAG, VariablesList.groupscollege.toString());

                        VariablesList.groupscollege.add(d);
                        VariablesList.groupscollegenom.add(f);
                    }
                    c.put(name.getInt("KodGroup"),name.getString("GroupName"));
                }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return c;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected ArrayList <String> onPostExecuteGroup2(String strJson) {
        // выводим целиком полученную json-строку
        JSONObject dataJsonObj;
        ArrayList <String> c = new ArrayList<>();
        try {
            dataJsonObj = new JSONObject(strJson);
            JSONArray classes1 = dataJsonObj.getJSONArray("Groups");
            // 2. перебираем и выводим список имен
            for (int i = 0; i < classes1.length(); i++) {
                JSONObject name = classes1.getJSONObject(i);

                c.add(name.getString("GroupName"));
                RegistrationForm.gr.put(name.getInt("KodGroup"),name.getString("GroupName"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return c;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onPostExecuteTeachers(String strJson) {
        // выводим целиком полученную json-строку
        JSONObject dataJsonObj;
        try {
            dataJsonObj = new JSONObject(strJson);
            JSONArray classes1 = dataJsonObj.getJSONArray("Teachers");
            VariablesList.teachers = new ArrayList<>(classes1.length());
            VariablesList.teachersnom = new ArrayList<>(classes1.length());
            // 2. перебираем и выводим список имен
            for (int i = 0; i < classes1.length(); i++) {
                JSONObject name = classes1.getJSONObject(i);
                if (name.getInt("StatusPpl") == 2) {
                    String d = name.getString("Name");
                    Integer f = name.getInt("KodPpl");

                    Log.d(LOG_TAG, d);
                    Log.d(LOG_TAG, VariablesList.teachers.toString());

                    VariablesList.teachers.add(d);
                    VariablesList.teachersnom.add(f);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onPostExecuteLessons(String strJson) {
        // выводим целиком полученную json-строку
        JSONObject dataJsonObj;
        try {
            dataJsonObj = new JSONObject(strJson);
            JSONArray classes1 = dataJsonObj.getJSONArray("Lessons");
            VariablesList.lessons = new ArrayList<>(classes1.length());
            VariablesList.lessonsnom = new ArrayList<>(classes1.length());
            // 2. перебираем и выводим список имен
            for (int i = 0; i < classes1.length(); i++) {
                JSONObject name = classes1.getJSONObject(i);
                    String d = name.getString("LessonName");
                    Integer f = name.getInt("KodLesson");

                    Log.d(LOG_TAG, d);
//                    Log.d(LOG_TAG, var.teachers.toString());

                    VariablesList.lessons.add(d);
                    VariablesList.lessonsnom.add(f);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onPostExecuteNewUser(String strJson) {
        // выводим целиком полученную json-строку
        JSONObject dataJsonObj;
        try {
            dataJsonObj = new JSONObject(strJson);
            JSONArray classes1 = dataJsonObj.getJSONArray("Peoples");

            if (VariablesList.stranica==0){
                VariablesList.size=classes1.length();
            }
            else if (VariablesList.stranica==1){
                // 2. перебираем и выводим список имен
                for (int i = 0; i < classes1.length(); i++) {
                    JSONObject name = classes1.getJSONObject(i);
                    VariablesList.kodppl = name.getInt("KodPpl");
                    VariablesList.lastname = name.getString("Lastname");
                    VariablesList.firstname = name.getString("Firstname");
                    VariablesList.patronymic = name.getString("Patronymic");
                    VariablesList.emailppl = name.getString("email");
                    VariablesList.groupppl = name.getString("GroupPpl");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
