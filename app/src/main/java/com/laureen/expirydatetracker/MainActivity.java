package com.laureen.expirydatetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView title;
    ImageView icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Change the page title
        title = findViewById(R.id.page_title);
        title.setText(R.string.category_list_title);

        //Change the top left icon to a back button
        icon = findViewById(R.id.toolbar_icon);
        icon.setImageResource(R.drawable.toolbar_logo);
        icon.setContentDescription("Logo");
    }
    public void gotoB(View v) {
        Intent intent = new Intent(MainActivity.this, AddCategoryActivity.class);
        startActivity(intent);
    }
}