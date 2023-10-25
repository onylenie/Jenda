package com.example.appraspisanie;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class fortest  {

    private String[] mGroupsArray = new String[] { "Понедельник",
            "Вторник", "Среда", "Четверг", "Пятница" };

    private ProgressDialog downloadProgress;

    private static final String LOG_TAG = "myLogs";

    public SimpleAdapter adapter;

    // список атрибутов групп для чтения
    String groupFrom[] = new String[] { "Belongs" };
    // список ID view-элементов, в которые будет помещены атрибуты групп
    int groupTo[] = new int[] { android.R.id.text1 };



    HashMap<String, String> m;

    ArrayList<Map<String, String>> groupDataList;
    ArrayList<Map<String, String>> classesDataList;
    //ArrayList<Map<String, String>> groupDataList;
    //ArrayList<Map<String, String>> groupDataList;

    ArrayList<ArrayList<Map<String, String>>> childDataList;
    ArrayList<ArrayList<Map<String, String>>> classDataList;
    //ArrayList<ArrayList<Map<String, String>>> childDataList;

    ArrayList<Map<String, String>> childDataItemList;
    ArrayList<Map<String, String>> classDataItemList;


    //aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    public String getContent(String path) throws IOException {
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

    protected void onPostExecute(String strJson) {

        m = new HashMap<>();

        groupDataList = new ArrayList<>();
        childDataList = new ArrayList<>();
        childDataItemList = new ArrayList<>();
        //super.onPostExecute(strJson);
        // выводим целиком полученную json-строку
        Log.d(LOG_TAG, "****** [START] **************************************");
        Log.d(LOG_TAG, strJson);
        Log.d(LOG_TAG, strJson.substring(0, 6));
        if (strJson.substring(0, 6).equals("Ошибка")) {
            downloadProgress.dismiss();
        } else {

            JSONObject dataJsonObj = null;
            // String secondName = "";

            if (strJson.equals("")) {
                Log.d(LOG_TAG, "strJson пуста");
                downloadProgress.dismiss();
//                data.clear();
//                adapter.notifyDataSetChanged();
                return;
            }

            try {
                dataJsonObj = new JSONObject(strJson);
                JSONArray groups = dataJsonObj.getJSONArray("Groups");
                int success = dataJsonObj.getInt("success");
                if (success == 0) {
                    try {
                        String message = dataJsonObj.getString("message");
                        Log.d(LOG_TAG, "message: " + message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(LOG_TAG, "Exception message: " + e);
                    }
                }

                Log.d(LOG_TAG, "success: " + success);

                // 2. перебираем и выводим список имен

                for (int i = 0; i < groups.length(); i++) {
                    JSONObject name = groups.getJSONObject(i);

                    m = new HashMap<String, String>();
                    m.put("KodGroup", name.getString("KodGroup"));
                    m.put("GroupName", name.getString("GroupName"));
                    m.put("Visible", name.getString("Visible"));
//                    m.put(ATTRIBUTE_BELONGS, name.getString("Belongs"));
                    childDataItemList.add(m);
                }
                childDataList.add(childDataItemList);

                for (int i =0; i < groups.length(); i++)
                {
                    JSONObject name = groups.getJSONObject(i);
                    m = new HashMap<>();
                    m.put("Belongs",name.getString("Belongs"));
                    groupDataList.add(m);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(LOG_TAG, "Exception: " + e);
            }
            Log.d(LOG_TAG, "--=[DATA]=--");
            //Log.d(LOG_TAG, String.valueOf(data));
            Log.d(LOG_TAG, "--=[m]=--");
            Log.d(LOG_TAG, String.valueOf(m));
            Log.d(LOG_TAG, "***** [FINISH] ***************************************");
            //Log.d(LOG_TAG, String.valueOf(data.size()));
//            downloadProgress.dismiss();
        }
    }


    //таблица с классами
    protected void onPostExecuteClass(String strJson) {

        m = new HashMap<>();

        classDataItemList = new ArrayList<>();
        classDataList = new ArrayList<>();

        // выводим целиком полученную json-строку
        if (strJson.substring(0, 6).equals("Ошибка")) {
            downloadProgress.dismiss();
        } else {

            JSONObject dataJsonObj = null;
            // String secondName = "";

            if (strJson.equals("")) {
                Log.d(LOG_TAG, "strJson пуста");
                downloadProgress.dismiss();
//                data.clear();
//                adapter.notifyDataSetChanged();
                return;
            }

            try {
                dataJsonObj = new JSONObject(strJson);
                JSONArray classes1 = dataJsonObj.getJSONArray("Classes");
                int success = dataJsonObj.getInt("success");
                if (success == 0) {
                    try {
                        String message = dataJsonObj.getString("message");
                        Log.d(LOG_TAG, "message: " + message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(LOG_TAG, "Exception message: " + e);
                    }
                }

                Log.d(LOG_TAG, "success: " + success);

                // 2. перебираем и выводим список имен

                for (int i = 0; i < classes1.length(); i++) {
                    JSONObject name = classes1.getJSONObject(i);
                    if ( name.getInt("Visible") == 1) {
                        m = new HashMap<String, String>();
                        m.put("Kod_room", name.getString("Kod_room"));
                        m.put("ClassroomN", name.getString("ClassroomN"));
                        m.put("Corpus", name.getString("Corpus"));
                        classDataItemList.add(m);
                    }
                }
                classDataList.add(childDataItemList);

                /*
                for (int i =0; i < classes1.length(); i++)
                {
                    JSONObject name = classes1.getJSONObject(i);
                    m = new HashMap<>();
                    m.put("Belongs",name.getString("Belongs"));
                    groupDataList.add(m);
                }*/

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(LOG_TAG, "Exception: " + e);
            }
        }
    }

}
