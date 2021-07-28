package com.laureen.expirydatetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    List<Category> categories;
    TextView title;
    ImageView icon;
    TableLayout category_list_table;
    FloatingActionButton fab;
    DatabaseHelper databaseHelper;

    //array to hold image for display
    Integer[] imageId = {
            R.drawable.pink_img,
            R.drawable.blue_img,
            R.drawable.yellow_img,
            R.drawable.red_img,
            R.drawable.white_img,
            R.drawable.green_img
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.page_title);
        fab = findViewById(R.id.fab_cat);
        icon = findViewById(R.id.toolbar_icon);
        category_list_table = findViewById(R.id.cat_table);
        databaseHelper = new DatabaseHelper(MainActivity.this);

        //Change the page title
        title.setText(R.string.category_list_title);

        //Change the top left icon to the logo
        icon.setImageResource(R.drawable.toolbar_logo);
        icon.setContentDescription("Logo");

        //Populate the table by adding rows dynamically
        LayoutInflater inflater = this.getLayoutInflater();
        categories = databaseHelper.getAllCategories();

        for(int i = 0; i < categories.size(); ++i) {
            TableRow rowView = (TableRow) inflater.inflate(R.layout.category_row_item, category_list_table, false);
            //customise the title and image
            TextView cat_title = rowView.findViewById(R.id.cat_title);
            cat_title.setText(categories.get(i).getName());
            ImageView cat_img = rowView.findViewById(R.id.cat_img);
            cat_img.setImageResource(imageId[categories.get(i).getImageNo() - 1]);
            ImageView cat_del = rowView.findViewById(R.id.cat_del);
            cat_del.setOnClickListener(this::deleteCategory);
            rowView.setId(i);
            rowView.setOnClickListener(this::viewItems);
            //add to table
            category_list_table.addView(rowView);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCategoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void deleteCategory(View view) {
        TableRow rowView = (TableRow)view.getParent();
        int id = rowView.getId();
        int[] itemIds = databaseHelper.getAllItemIds(categories.get(id).getId());  //get all item ids
        int result = databaseHelper.removeCategory(categories.get(id));
        if(result == 1) {
            Toast.makeText(this, "Category successfully deleted!", Toast.LENGTH_SHORT).show();
            categories.remove(id);
            category_list_table.removeView(rowView);

            //delete all item alarms
            for (Integer itemId : itemIds) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getApplicationContext(), MyReceiver.class);
                int requestCode = itemId;
                Log.d(TAG, "deleteCategory: requestCode = " + requestCode);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
                alarmManager.cancel(pendingIntent);
            }
        } else {
            Toast.makeText(this, "An error occurred.", Toast.LENGTH_SHORT).show();
        }
    }

    private void viewItems(View view) {
        Category clicked_category = categories.get(view.getId());
        Toast.makeText(this, "Notifies " + clicked_category.getDays() + " days before", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, ItemActivity.class);
        intent.putExtra("id", String.valueOf(clicked_category.getId()));
        intent.putExtra("name", String.valueOf(clicked_category.getName()));
        intent.putExtra("days", String.valueOf(clicked_category.getDays()));
        startActivity(intent);
    }

}