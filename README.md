# SendPushNotification_A_to_B_Device



Push Notification Device To Device (Android studio + Firebase)
Git -version- 2.21.0 


NOTE: You Have to Use Your Device ID And FCM ServerKey

Tasks Done
Trigger Notification within the android device
Send Notification to Another Device
Daily Notification Using AlarmManager




Trigger Notification within the android device

Add Dependencies

dependencies {

...

compile "com.android.support:support-v4:24.1.1"

}

Add a  button 
<Button
   android:id="@+id/notification_Self"
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"
   android:layout_centerInParent="true"
   android:layout_marginStart="8dp"
   android:layout_marginTop="8dp"
   android:layout_marginEnd="8dp"
   android:layout_marginBottom="8dp"
   android:text="Self Notification"
   app:layout_constraintBottom_toBottomOf="parent"
   app:layout_constraintEnd_toEndOf="parent"
   app:layout_constraintStart_toStartOf="parent"
   app:layout_constraintTop_toTopOf="parent"
   app:layout_constraintVertical_bias="0.624" />



Code For Custom Notification

<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
   xmlns:app="http://schemas.android.com/apk/res-auto"
   xmlns:tools="http://schemas.android.com/tools"
   android:layout_width="match_parent"
   android:layout_height="match_parent"
   tools:context=".notificationview">
   <TextView
       android:id="@+id/lbltitle"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"
       android:text="t" />

   <TextView
       android:id="@+id/lbltext"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"
       android:layout_below="@+id/lbltitle"
       android:text="t" />

   <TextView
       android:id="@+id/title"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"
       android:layout_toRightOf="@+id/lbltitle" />

   <TextView
       android:id="@+id/text"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"
       android:layout_below="@+id/title"
       android:layout_toRightOf="@+id/lbltext" />
</android.support.constraint.ConstraintLayout>





MainActivity.java

notifySelf.setOnClickListener(new View.OnClickListener() {

   public void onClick(View arg0) {

       Notification();
   }
});



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






New class for custom notification

public class notificationview extends AppCompatActivity {
   String title;
   String text;
   TextView txttitle;
   TextView txttext;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_notificationview);

       // Create Notification Manager
       NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
       // Dismiss Notification
       notificationmanager.cancel(0);

       // Retrive the data from MainActivity.java
       Intent i = getIntent();

       title = i.getStringExtra("title");
       text = i.getStringExtra("text");

       // Locate the TextView
       txttitle = (TextView) findViewById(R.id.title);
       txttext = (TextView) findViewById(R.id.text);

       // Set the data into TextView
       txttitle.setText(title);
       txttext.setText(text);
   }




Send Notification to Another Device ( To Implement In Real Time)
First we have to get the device token id when their login to our app.
Store the device id in our own server database.
When we want to send notification to another device get their device id from our server first
And Send the device id and message to  firebase API,Then firebase will send it to the particular user.

Daily Notification Using AlarmManager ( To Implement In Real Time)
Need to show Notification to current User (Updated or Deleted Notification)
In this case we have to keep the notification message in Strings.xml.
Wherever we want the notification(Deleted / Updated) we can trigger the notification by the required string.
If we want to store the notification history, We can store it in our server database. Here no need to call the firebase API.

