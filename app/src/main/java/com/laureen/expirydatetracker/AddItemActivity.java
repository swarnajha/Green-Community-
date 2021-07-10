package com.laureen.expirydatetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AddItemActivity extends AppCompatActivity {
    TextView title;
    ImageView icon, add_date;
    Button add;
    EditText date1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        title = findViewById(R.id.page_title);
        icon = findViewById(R.id.toolbar_icon);
        add = findViewById(R.id.add_cat);
        date1 = findViewById(R.id.expiry_date1);
        add_date = findViewById(R.id.add_date);

        //Change the page title
        title.setText(R.string.add_category);

        //Change the top left icon to a back button
        icon.setImageResource(R.drawable.left_arrow);
        icon.setContentDescription("Go Back");

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddItemActivity.this, ItemActivity.class);
                startActivity(intent);
            }
        });

    }
}