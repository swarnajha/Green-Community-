package com.laureen.expirydatetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemActivity extends AppCompatActivity {
    TextView title;
    ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        //Change the page title
        title = findViewById(R.id.page_title);
        title.setText(R.string.item_list_title);

        //Change the top left icon to a back button
        icon = findViewById(R.id.toolbar_icon);
        icon.setImageResource(R.drawable.left_arrow);
        icon.setContentDescription("Go Back");

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}