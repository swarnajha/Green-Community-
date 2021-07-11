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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ItemActivity extends AppCompatActivity {
    TextView title;
    ListView item_list;
    ImageView icon;
    FloatingActionButton fab;
    DatabaseHelper databaseHelper;
    ArrayAdapter<Item> itemArrayAdapter;

    String category_name;
    int category_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        //get category details
        Bundle bundle = getIntent().getExtras();
        Log.d("Item List", "bundle - "+ bundle.get("id") + bundle.get("name"));
        category_id = Integer.parseInt((String) bundle.get("id"));    //get category id
        category_name = (String) bundle.get("name"); //get category name

        title = findViewById(R.id.page_title);
        fab = findViewById(R.id.fab_item);
        icon = findViewById(R.id.toolbar_icon);
        item_list = findViewById(R.id.item_list);

        databaseHelper = new DatabaseHelper(ItemActivity.this);
        itemArrayAdapter = new ArrayAdapter<Item>(ItemActivity.this, android.R.layout.simple_list_item_1, databaseHelper.getAllItems(category_id));
        item_list.setAdapter(itemArrayAdapter);

        //Change the page title
        title = findViewById(R.id.page_title);
        title.setText(getResources().getString(R.string.item_list_title) + category_name);  //returns a number weirdly if you use without the whole thing
        //Log.d("Item List", "onCreate: title - " + getResources().getString(R.string.item_list_title) + ", " + category_name);

        //Change the top left icon to a back button
        icon = findViewById(R.id.toolbar_icon);
        icon.setImageResource(R.drawable.left_arrow);
        icon.setContentDescription("Go Back");

        //go back to category list
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //go to add item activity
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemActivity.this, AddItemActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.d("Category List", "onItemClick: item clicked - "+ l);
                Item clicked_item = (Item) parent.getItemAtPosition(position);
                Toast.makeText(ItemActivity.this, "Item clicked: " + clicked_item.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}