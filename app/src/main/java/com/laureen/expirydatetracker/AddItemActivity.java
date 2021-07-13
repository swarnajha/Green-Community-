package com.laureen.expirydatetracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity {
    public static final int YEAR_MIN = 2000;
    public static final int YEAR_MAX = 2050;
    private static final String TAG = "AddItemActivity";

    int noOfDateBoxes = 1;
    int category_id;
    String category_name;

    LinearLayout container;
    TextView title;
    ImageView icon, add_date_box;
    Button add_btn;
    EditText expiry_date, item_name;

    Bundle bundle;
    DatabaseHelper databaseHelper;
    DatePickerDialog datePickerDialog;
    AlarmManager alarmManager;

    int year, month, day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        bundle = getIntent().getExtras();    //get category info to give back to item list
        category_id = Integer.parseInt((String) bundle.get("id"));    //get category id
        category_name = (String) bundle.get("name"); //get category name

        databaseHelper = new DatabaseHelper(AddItemActivity.this);

        title = findViewById(R.id.page_title);
        icon = findViewById(R.id.toolbar_icon);
        add_btn = findViewById(R.id.add_item);
        add_date_box = findViewById(R.id.add_date_box);
        expiry_date = findViewById(R.id.expiry_date);
        item_name = findViewById(R.id.item_name);
        container = findViewById(R.id.date_box_layout);

        //Change the page title
        title.setText(R.string.add_item);

        //Change the top left icon to a back button
        icon.setImageResource(R.drawable.left_arrow);
        icon.setContentDescription("Go Back");

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddItemActivity.this, ItemActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //add item(s) to the item list
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the item name
                String name = item_name.getText().toString();
                if(name.equals("")) {
                    Toast.makeText(AddItemActivity.this, "Please enter item name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Item[] items = new Item[noOfDateBoxes];
                for(int i = 0; i < noOfDateBoxes; ++i) {
                    //get the dates
                    LinearLayout ll = (LinearLayout) container.getChildAt(i);
                    EditText et_view = (EditText)ll.getChildAt(0);
                    items[i] = new Item(-1, item_name.getText().toString(), null, category_id);
                    items[i].setDate(et_view.getText().toString());
                    if(items[i].getDate().equals("")) {
                        Toast.makeText(AddItemActivity.this, "Please fill in the dates!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Log.d(TAG, "onClick: " + Arrays.toString(items));

                //add item(s) to db
                int result = 0;
                for(int i = 0; i < noOfDateBoxes; ++i) {
                    if (databaseHelper.addItem(items[i])) {
                        result++;
                        setAlarm(items[i]);
                        //scheduleNotification(getNotification( "5 second delay" ) , 5000 ) ;
                    }
                    else
                        result = noOfDateBoxes * -1;
                }
                if(result >= 0) {
                    Toast.makeText(AddItemActivity.this, "Added " + result + "row(s)!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddItemActivity.this, "An error occurred.", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(AddItemActivity.this, ItemActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void setDate(View view) {
        //date picker
        EditText et_view = (EditText) view;
        final Calendar cal = Calendar.getInstance();
        int cur_year = cal.get(Calendar.YEAR);
        int cur_month = cal.get(Calendar.MONTH);
        int cur_day = cal.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(AddItemActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year1, int month1, int day1) {
                year = year1;
                month = month1;
                day = day1;
                if(year < YEAR_MIN || year > YEAR_MAX) {
                    Toast.makeText(AddItemActivity.this, "Enter a valid date!", Toast.LENGTH_SHORT).show();
                    return;
                }
                et_view.setText(day + "/" + (month + 1) + "/" + year);
            }
        }, cur_year, cur_month, cur_day);
        datePickerDialog.show();
    }

    public void removeDateBox(View view) {
        //LinearLayout ll = (LinearLayout)view.getParent();
        noOfDateBoxes--;
        container.removeView((View)view.getParent());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addDateBox(View view) {
        //add another date box to the layout, below current one
        noOfDateBoxes++;

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final LinearLayout.LayoutParams et_params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        final LinearLayout.LayoutParams img_params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = 10;
        et_params.weight = 0.9f;
        img_params.weight = 0.1f;
        img_params.gravity = Gravity.CENTER_VERTICAL;

        LinearLayout ll = new LinearLayout(getApplicationContext());
        ll.setLayoutParams(params);
        ll.setPadding(5,5,5,5);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setBackgroundResource(R.drawable.text_box);

        EditText et = new EditText(getApplicationContext());
        et.setLayoutParams(et_params);
        //et.setPadding(5,5,5,5);
        et.setHint(R.string.date_hint);
        et.setHintTextColor(getResources().getColor(R.color.grey));
        et.setTextColor(getResources().getColor(R.color.white));
        et.setText(expiry_date.getText());
        et.setTextSize(20);
        Typeface typeface = getResources().getFont(R.font.lato);
        et.setTypeface(typeface);
        et.setFocusable(false);
        //et.setBackgroundColor(getResources().getColor(R.color.black));
        et.setOnClickListener(this::setDate);

        ImageView img = new ImageView(getApplicationContext());
        img.setLayoutParams(img_params);
        img.setPadding(5,0,5,0);
        img.setImageResource(R.drawable.ic_remove_blue);
        img.setOnClickListener(this::removeDateBox);

        ll.addView(et);
        ll.addView(img);

        container.addView(ll);
    }

    private void setAlarm(Item item) {
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), MyReceiver.class);
        intent.putExtra("name", item.getName());
        //intent.putExtra("date", item.getDate());
        //intent.putExtra("id", item.getId());
        //TODO: Make notifications update, if many present
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar1Notify = Calendar.getInstance();

        //TODO: Notify at the right time, not 10s after. Also, check if it has already expired
        calendar1Notify.setTimeInMillis(System.currentTimeMillis() + 10000);
        calendar1Notify.add(Calendar.DAY_OF_MONTH, 1);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar1Notify.getTimeInMillis(), pendingIntent);

//        String dateTime = item.getDate() + " " + "10:00";
//        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.getDefault());
//        Calendar c = Calendar.getInstance();
//        Date date1 = null;
//        try {
//            date1 = formatter.parse(dateTime);
//            assert date1 != null;
//            c.setTime(date1);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        c.add(Calendar.DATE, /*pass in the category's no of days */);
//        c.add(Calendar.DATE, 1);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTime().getTime(), pendingIntent);
//        try {
//            alarmManager.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
    }
//    private void scheduleNotification (Notification notification , int delay) {
//        Intent notificationIntent = new Intent( this, MyReceiver.class ) ;
//        notificationIntent.putExtra(MyReceiver.NOTIFICATION_ID , 1 ) ;
//        notificationIntent.putExtra(MyReceiver.NOTIFICATION , notification) ;
//        PendingIntent pendingIntent = PendingIntent.getBroadcast ( this, 0 , notificationIntent , PendingIntent.FLAG_UPDATE_CURRENT ) ;
//        long futureInMillis = SystemClock.elapsedRealtime () + delay ;
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE ) ;
//        assert alarmManager != null;
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP , futureInMillis , pendingIntent) ;
//    }
//
//    private Notification getNotification (String content) {
//        String NOTIFICATION_CHANNEL_ID = "10001" ;
//        String default_notification_channel_id = "default" ;
//        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id );
//        builder.setContentTitle( "Scheduled Notification" ) ;
//        builder.setContentText(content) ;
//        builder.setSmallIcon(R.drawable.toolbar_logo ) ;
//        builder.setAutoCancel( true ) ;
//        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
//        return builder.build() ;
//    }
}