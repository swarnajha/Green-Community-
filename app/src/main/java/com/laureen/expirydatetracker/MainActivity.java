package com.laureen.expirydatetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
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
    List<Category> categories;
    TextView title;
    ImageView icon;
    TableLayout category_list_table;
    FloatingActionButton fab;
    DatabaseHelper databaseHelper;

    //array to hold image for display
    Integer[] imageId = {
            R.drawable.pink_image_1,
            R.drawable.pink_image_2,
            R.drawable.pink_image_3,
            R.drawable.blue_image_1,
            R.drawable.blue_image_2,
            R.drawable.blue_image_3
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

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
        Toast.makeText(this, "You clicked on delete", Toast.LENGTH_SHORT).show();
    }

    private void viewItems(View view) {
        Category clicked_category = categories.get(view.getId());
        Toast.makeText(this, "You clicked on item: " + clicked_category.toString() + ", at position: " + view.getId(), Toast.LENGTH_SHORT).show();
        Log.d("Category List", "onItemClick: item clicked - "+ clicked_category.getId() + clicked_category.getName());
        Intent intent = new Intent(MainActivity.this, ItemActivity.class);
        intent.putExtra("id", String.valueOf(clicked_category.getId()));
        intent.putExtra("name", String.valueOf(clicked_category.getName()));
        intent.putExtra("days", String.valueOf(clicked_category.getDays()));
        startActivity(intent);
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ExpiryDateTracker";
            String description = "Channel for Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("tracker", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}