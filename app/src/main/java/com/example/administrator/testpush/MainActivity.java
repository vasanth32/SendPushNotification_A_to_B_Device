package com.example.administrator.testpush;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.onesignal.OneSignal;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    static String strSDesc = "ShortDesc";
    static String strIncidentNo = "IncidentNo";
    static String strDesc = "IncidentNo";
    String FCM_TOKEN = "";
    private PendingIntent pendingIntent;
    private String TAG="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         //Daily Notification Using AlarmManager
        Calendar now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();

        // we can set time by open date and time picker dialog
        int H = calendar.get(Calendar.HOUR);
        int M = calendar.get(Calendar.MINUTE);
        int S = calendar.get(Calendar.SECOND);

        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 0);

//          if(H==14 && M==47 && S==0) {
            Intent intent1 = new Intent(MainActivity.this, MyReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    MainActivity.this, 0, intent1,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) MainActivity.this
                    .getSystemService(MainActivity.this.ALARM_SERVICE);
        if (now.after(calendar)) {
            calendar.add(Calendar.DATE, 1);
        }
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
//        }
        //End Daily Notification



        Button notifyAnotherDevice = (Button) findViewById(R.id.notification_ToAnotherDevice);
        Button notifySelf = (Button)findViewById(R.id.notification_Self);


        notifyAnotherDevice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
//               Susbscribe Topic Notification
                FirebaseMessaging.getInstance().subscribeToTopic("V9694436");
                Toast.makeText(getApplicationContext(), "Topic Subscribed", Toast.LENGTH_LONG).show();
                //Generate Device Token
//              FCM_TOKEN = String.valueOf(FirebaseInstanceId.getInstance().getToken());
                //Another Device Token -
                FCM_TOKEN = "";
                //Call the token notification
                sendFCMPush();
            }
        });

        notifySelf.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                Notification();
            }
        });

    }

    private void sendFCMPush()
    {
        final String SERVER_KEY = "";
        String msg = "Device to Device";
        String title = "Notification"+"VR";
        String token = FCM_TOKEN;
        Toast.makeText(getApplicationContext(), FCM_TOKEN, Toast.LENGTH_LONG).show();
        Log.d("DeviceIDFinder",token.toString());

        JSONObject obj = null;
        JSONObject objData = null;
        JSONObject dataobjData = null;

        try {
            obj = new JSONObject();
            objData = new JSONObject();

            objData.put("body", msg);
            objData.put("title", title);
            objData.put("sound", "default");
            objData.put("icon", "icon_name"); //   icon_name
            objData.put("tag", token);
            objData.put("priority", "high");

            dataobjData = new JSONObject();
            dataobjData.put("text", msg);
            dataobjData.put("title", title);

            obj.put("to", token);
            //obj.put("priority", "high");

            obj.put("notification", objData);
            obj.put("data", dataobjData);
            Log.e("return here>>", obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, "https://fcm.googleapis.com/fcm/send", obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("True", response + "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("False", error + "");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "key=" + SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
    }

        public void Notification () {
            // Set Notification Title
            String strtitle = "TEST";
            // Set Notification Text
            String strtext = "TestPush";

            // Open NotificationView Class on Notification Click
            Intent intent = new Intent(this, notificationview.class);

            // Send data to NotificationView Class
            intent.putExtra("TEST", strtitle);
            intent.putExtra("You have Completed Succesfully", strtext);

            // Open NotificationView.java Activity
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            //Create Notification using NotificationCompat.Builder
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    // Set Icon
                    .setSmallIcon(R.drawable.ic_stat_ic_notification)
                    // Set Ticker Message
                    .setTicker("s")
                    // Set Title
                    .setContentTitle("TEST")
                    // Set Text
                    .setContentText("Your Test is Completed")
//                    // Add an Action Button below Notification
//                    .addAction(R.drawable.ic_stat_ic_notification, "Action Button", pIntent)
//                    // Set PendingIntent into Notification
//                    .setContentIntent(pIntent)
                    // Dismiss Notification
                    .setAutoCancel(true);

            // Create Notification Manager
            NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // Build Notification with Notification Manager
            notificationmanager.notify(0, builder.build());

        }
}