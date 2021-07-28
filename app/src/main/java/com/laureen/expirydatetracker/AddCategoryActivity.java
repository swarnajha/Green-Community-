package com.laureen.expirydatetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddCategoryActivity extends AppCompatActivity {
    private static final String TAG = "AddCategoryActivity";
    public static final int NOTIFY_DAYS_MIN = 1;
    public static final int NOTIFY_DAYS_MAX = 30;
    int imageCounter = 1, imageNum = 6;
    Button add_btn;
    TextView title;
    EditText name, days;
    ImageView icon, prev, next, img;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        title = findViewById(R.id.page_title);
        icon = findViewById(R.id.toolbar_icon);
        prev = findViewById(R.id.prev);
        img = findViewById(R.id.img);
        next = findViewById(R.id.next);
        add_btn = findViewById(R.id.add_cat);
        name = findViewById(R.id.name);
        days = findViewById(R.id.days);

        databaseHelper = new DatabaseHelper(AddCategoryActivity.this);

        //Change the page title
        title.setText(R.string.add_category);

        //Change the top left icon to a back button
        icon.setImageResource(R.drawable.left_arrow);
        icon.setContentDescription("Go Back");

        //if back icon clicked, go to category list activity
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
                if(imageCounter != 0)
                    setImage(imageCounter);
                else
                    imageCounter = 1;
            }
        });

        //show next image, if present
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageCounter++;
                if(imageCounter <= imageNum)
                    setImage(imageCounter);
                else
                    imageCounter = imageNum;
            }
        });

        //add the category to db
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cat_name;
                int notify_days;
                try {
                    cat_name = name.getText().toString();
                    notify_days = Integer.parseInt(days.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(AddCategoryActivity.this, "Enter Valid Input!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //validate input
                if(cat_name.equals("")) {
                    Toast.makeText(AddCategoryActivity.this, "Name is empty!", Toast.LENGTH_SHORT).show();
                    return;
                } else if(notify_days < NOTIFY_DAYS_MIN || notify_days > NOTIFY_DAYS_MAX) {
                    Toast.makeText(AddCategoryActivity.this, "Days should be between " + NOTIFY_DAYS_MIN +" and " + NOTIFY_DAYS_MAX +"!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //add to the db
                Category category = new Category(-1, cat_name, notify_days, imageCounter);
                boolean result = databaseHelper.addCategory(category);
                if(result) {
                    //if successful
                    Toast.makeText(AddCategoryActivity.this, "Category Added!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddCategoryActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(AddCategoryActivity.this, "An Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void setImage(int counter) {
        switch(counter) {
            case 1: img.setImageResource(R.drawable.pink_img); break;
            case 2: img.setImageResource(R.drawable.blue_img); break;
            case 3: img.setImageResource(R.drawable.yellow_img); break;
            case 4: img.setImageResource(R.drawable.red_img); break;
            case 5: img.setImageResource(R.drawable.white_img); break;
            case 6: img.setImageResource(R.drawable.green_img); break;
            default:
                Log.d(TAG, "setImage: invalid counter - " + counter);
        }
    }
}