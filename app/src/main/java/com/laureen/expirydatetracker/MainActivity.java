package com.laureen.expirydatetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView title;
    ListView category_list;
    ImageView icon;
    FloatingActionButton fab;
    DatabaseHelper databaseHelper;
    ArrayAdapter<Category> categoryArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.page_title);
        fab = findViewById(R.id.fab_cat);
        icon = findViewById(R.id.toolbar_icon);
        category_list = findViewById(R.id.category_list);

        databaseHelper = new DatabaseHelper(MainActivity.this);
        //List<Category> categories = databaseHelper.getAllCategories();
        categoryArrayAdapter = new ArrayAdapter<Category>(MainActivity.this, android.R.layout.simple_list_item_1, databaseHelper.getAllCategories());
        category_list.setAdapter(categoryArrayAdapter);

        //Change the page title
        title.setText(R.string.category_list_title);

        //Change the top left icon to the logo
        icon.setImageResource(R.drawable.toolbar_logo);
        icon.setContentDescription("Logo");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCategoryActivity.class);
                startActivity(intent);
            }
        });

        category_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("CAtegory List", "onItemClick: item clicked - "+ l);
            }
        });
    }
}