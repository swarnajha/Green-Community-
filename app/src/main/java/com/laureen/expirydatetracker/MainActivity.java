package com.laureen.expirydatetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    TextView title;
    ImageView icon;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.page_title);
        fab = findViewById(R.id.fab_cat);
        icon = findViewById(R.id.toolbar_icon);

        //Change the page title
        title.setText(R.string.category_list_title);

        //Change the top left icon to a back button
        icon.setImageResource(R.drawable.toolbar_logo);
        icon.setContentDescription("Logo");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCategoryActivity.class);
                startActivity(intent);
            }
        });
        //DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
        //List<Category> categories = databaseHelper.getAllCategories();
    }
}