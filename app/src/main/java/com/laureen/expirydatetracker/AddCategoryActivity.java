package com.laureen.expirydatetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddCategoryActivity extends AppCompatActivity {
    int imageCounter = 1, imageNum = 6;

    Button add;
    TextView title;
    EditText name, days;
    ImageView icon, prev, next, img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        title = findViewById(R.id.page_title);
        icon = findViewById(R.id.toolbar_icon);
        prev = findViewById(R.id.prev);
        img = findViewById(R.id.img);
        next = findViewById(R.id.next);
        add = findViewById(R.id.add_cat);
        name = findViewById(R.id.name);
        days = findViewById(R.id.days);

        //Change the page title
        title.setText(R.string.add_category);

        //Change the top left icon to a back button
        icon.setImageResource(R.drawable.left_arrow);
        icon.setContentDescription("Go Back");

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddCategoryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //show prev image, if present
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageCounter--;
                System.out.println("Prev " + imageCounter);
                if(imageCounter != 0)
                    setImage();
                else
                    imageCounter = 1;

            }
        });

        //show next image, if present
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageCounter = (imageCounter + 1) % imageNum;
                System.out.println("Next " + imageCounter);
                if(imageCounter <= imageNum)
                    setImage();
                else
                    imageCounter = imageNum;
            }
        });

        //add the category to db

        /*
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: add to db
                //if successful
                Toast.makeText(AddCategoryActivity.this, "Category Added!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddCategoryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        */

    }
    public void setImage() {
        switch(imageCounter) {
            case 1: img.setImageResource(R.drawable.pink_image_1); break;
            case 2: img.setImageResource(R.drawable.pink_image_2); break;
            case 3: img.setImageResource(R.drawable.pink_image_3); break;
            case 4: img.setImageResource(R.drawable.blue_image_1); break;
            case 5: img.setImageResource(R.drawable.blue_image_2); break;
            case 6: img.setImageResource(R.drawable.blue_image_3); break;
            default: System.out.println("An Error Occurred!");
        }
    }
}