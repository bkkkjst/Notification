package com.example.jsontest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class TestDBActivity extends Activity {
    //กำหนดที่อยู่ของไฟล์ php ในการดึงข้อมูล
    //หากใช้เครื่องที่รัน Emulator เป็น Server ให้ใช้ IP 10.0.2.2 (เครื่องเราเอง)
   public static final String KEY_SERVER = "http://119.59.97.11/android/getData.php";

   private TextView txtJson; //Text แสดงข้อมูล JSON
   private TextView txtResult; //Text แสดงข้อมูลที่ได้จาก Server

   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   setContentView(R.layout.activity_test_db);

   //เตรียม View
   txtJson = (TextView) findViewById(R.id.txtJson);
   txtResult = (TextView) findViewById(R.id.txtResult);

  // ทำการดึงข้อมูลโดยใช้ฟังก์ชัน getServerData()
   txtResult.setText("RESULT : n" + getServerData());
    }

private String getServerData() {
String returnString = "";
InputStream is = null;
String result = "";
//ส่วนของการกำหนดตัวแปรเพื่อส่งให้กับ php
//ส่วนนี้สามารถประยุกต์ไปใช้ในการเพิ่มข้อมูลให้กับ Server ได้
//จากตัวอย่างส่งค่า moreYear ที่มีค่า 1990
ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
nameValuePairs.add(new BasicNameValuePair("moreYear", "1990"));

//ส่วนของการเชื่อมต่อกับ http เพื่อดึงข้อมูล
try {
HttpClient httpclient = new DefaultHttpClient();
HttpPost httppost = new HttpPost(KEY_SERVER);
httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
HttpResponse response = httpclient.execute(httppost);
HttpEntity entity = response.getEntity();
is = entity.getContent();
} catch (Exception e) {
Log.e("log_tag", "Error in http connection " + e.toString());
}

//ส่วนของการแปลงผลลัพธ์ให้อยู่ในรูปแบบของ String
try {
BufferedReader reader = new BufferedReader(new InputStreamReader(
is, "iso-8859-11"), 8);
StringBuilder sb = new StringBuilder();
String line = null;
while ((line = reader.readLine()) != null) {
sb.append(line + "n");
}
is.close();
result = sb.toString();
} catch (Exception e) {
Log.e("log_tag", "Error converting result " + e.toString());
}

//ส่วนของการแปลงข้อมูล JSON ออกมาในรูปแบบของข้อมูลทั่วไปเพื่อนำไปใช้
try {
//แสดงผลออกมาในรูปแบบของ JSON
txtJson.setText("JSON : n" + result);

JSONArray jArray = new JSONArray(result);
for (int i = 0; i < jArray.length(); i++) {
JSONObject json_data = jArray.getJSONObject(i);

//พิมพ์ Log ดูเพื่อป้องกันข้อผิดพลาด
Log.i("log_tag",
 "id: " + json_data.getInt("id") + ", name: "
   + json_data.getString("name") + ", sex: "
   + json_data.getString("sex") + ", birthyear: "
   + json_data.getInt("birthyear"));

//นำข้อมูลใส่ตัวแปรเพื่อไปแสดงต่อ
returnString += "nID : " + json_data.getInt("id")
 + "nName : " + json_data.getString("name")
 + "nSex : " + json_data.getString("sex")
 + "nBirthyear: " + json_data.getInt("birthyear")
 + "n";
}
} catch (JSONException e) {
Log.e("log_tag", "Error parsing data " + e.toString());
}

//ส่งผลลัพธ์ไปแสดงใน txtResult
return returnString;
}
}
