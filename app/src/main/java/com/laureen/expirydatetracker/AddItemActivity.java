package com.laureen.expirydatetracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddItemActivity extends AppCompatActivity {
    public static final int YEAR_MIN = 2000;
    public static final int YEAR_MAX = 2050;
    private static final String TAG = "AddItemActivity";

    int noOfDateBoxes = 1;
    int category_id, category_days;
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
        category_days = Integer.parseInt((String) bundle.get("days"));

        databaseHelper = new DatabaseHelper(AddItemActivity.this);

        title = findViewById(R.id.page_title);
        icon = findViewById(R.id.toolbar_icon);
        add_btn = findViewById(R.id.add_item);
        add_date_box = findViewById(R.id.add_date_box);
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
                if (name.equals("")) {
                    Toast.makeText(AddItemActivity.this, "Please enter item name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Item[] items = new Item[noOfDateBoxes];
                for (int i = 0; i < noOfDateBoxes; ++i) {
                    //get the dates
                    LinearLayout ll = (LinearLayout) container.getChildAt(i);
                    EditText et_view = (EditText) ll.getChildAt(0);
                    items[i] = new Item(-1, item_name.getText().toString(), et_view.getText().toString(), category_id);
                    //items[i].setDate();
                    if (items[i].getDate().equals("")) {
                        Toast.makeText(AddItemActivity.this, "Please fill in the dates!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Log.d(TAG, "onClick: " + Arrays.toString(items));

                //add item(s) to db
                int result = 0;
                for (int i = 0; i < noOfDateBoxes; ++i) {
                    int id = databaseHelper.addItem(items[i]);
                    if (id != -1) {
                        result++;
                        items[i].setId(id);
                        setAlarm(items[i]);
                    } else
                        result = noOfDateBoxes * -1;
                }
                if (result >= 0) {
                    Toast.makeText(AddItemActivity.this, "Added " + result + " item(s)!", Toast.LENGTH_SHORT).show();
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

        //get current date
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
                if (year < YEAR_MIN || year > YEAR_MAX) {
                    Toast.makeText(AddItemActivity.this, "Enter a valid date!", Toast.LENGTH_SHORT).show();
                    return;
                }
                et_view.setText(day + "/" + (month + 1) + "/" + year);
            }
        }, cur_year, cur_month, cur_day);
        datePickerDialog.show();
    }

    public void removeDateBox(View view) {
        noOfDateBoxes--;
        container.removeView((View) view.getParent());
    }

    public void addDateBox(View view) {
        //add another date box to the layout, below current one
        noOfDateBoxes++;

        LayoutInflater inflater = this.getLayoutInflater();
        LinearLayout date_box = (LinearLayout) inflater.inflate(R.layout.date_box, container, false);
        ImageView btn = date_box.findViewById(R.id.add_date_box);
        btn.setOnClickListener(this::removeDateBox);
        btn.setImageResource(R.drawable.ic_remove_blue);
        container.addView(date_box);
    }

    private void setAlarm(Item item) {
        //TODO: Make notifications update, if many present

        //Format: DD/MM/YYYY or D/MM/YYYY or DD/M/YYYY or D/M/YYYY
        String date = item.getDate();
        Log.d(TAG, "setAlarm: Date is " + date);
        int endIndex = (date.charAt(1) == '/') ? 1 : 2;
        int set_day = Integer.parseInt(date.substring(0, endIndex));
        int endIndex2 = (date.charAt(endIndex + 2) == '/') ? endIndex + 2 : endIndex + 3;
        int set_month = Integer.parseInt(date.substring(endIndex + 1, endIndex2));
        int set_year = Integer.parseInt(date.substring(endIndex2 + 1));

        Calendar c = Calendar.getInstance();
        Log.d(TAG, "setAlarm: init" + c.getTime());
        c.setTimeInMillis(System.currentTimeMillis());
        Log.d(TAG, "setAlarm: " + set_year + "/" + set_month + "/" + set_day);
        c.set(Calendar.YEAR, set_year);
        c.set(Calendar.MONTH, set_month - 1);
        c.set(Calendar.DAY_OF_MONTH, set_day);

        Calendar cur = Calendar.getInstance();
        if(cur.after(c)) {
            //if current date is after expiry date
            //i.e., item has already expired
            item.setExpiry(true);
        }

        Log.d(TAG, "setAlarm:  after initial setup" + c.getTime());
        //subtract notify days
        c.add(Calendar.DAY_OF_MONTH, -category_days);
        Log.d(TAG, "setAlarm: after adding days" + c.getTime());
        //set alarm at 8.00 AM
//        c.set(Calendar.HOUR_OF_DAY, 12);
//        c.set(Calendar.MINUTE, 40);
//        c.set(Calendar.SECOND, 0);
        c.add(Calendar.SECOND, 30);
        Log.d(TAG, "setAlarm: after setting time" + c.getTime());
        Log.d(TAG, "setAlarm: " + c.toString());


        Log.d(TAG, "setAlarm: " + cur.getTime());
        if (cur.before(c)) {
            //set alarm only if notify date is after current date
            Log.d(TAG, "setAlarm: hello");
            alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getApplicationContext(), MyReceiver.class);
            intent.putExtra("name", item.getName());
            intent.putExtra("days", category_days + "");
            int requestCode = item.getId();
            Log.d(TAG, "setAlarm: requestCode = " + requestCode);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        } else {
            //otherwise, no alarm to set as item is expired
            //item.setExpiry(true);
        }
    }
}