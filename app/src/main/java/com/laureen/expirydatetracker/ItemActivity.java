package com.laureen.expirydatetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ItemActivity extends AppCompatActivity {
    TextView title;
    LinearLayout item_list;
    ImageView icon;
    FloatingActionButton fab;
    DatabaseHelper databaseHelper;
    List<Item> items;
    //ArrayAdapter<Item> itemArrayAdapter;

    String category_name;
    int category_id, category_days;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        //get category details
        Bundle bundle = getIntent().getExtras();
        Log.d("Item List", "bundle - "+ bundle.get("id") + bundle.get("name"));
        category_id = Integer.parseInt((String) bundle.get("id"));    //get category id
        category_name = (String) bundle.get("name"); //get category name
        category_days = Integer.parseInt((String) bundle.get("days"));

        title = findViewById(R.id.page_title);
        fab = findViewById(R.id.fab_item);
        icon = findViewById(R.id.toolbar_icon);
        item_list = findViewById(R.id.item_list);

        databaseHelper = new DatabaseHelper(ItemActivity.this);
//        itemArrayAdapter = new ArrayAdapter<Item>(ItemActivity.this, android.R.layout.simple_list_item_1, databaseHelper.getAllItems(category_id));
//        item_list.setAdapter(itemArrayAdapter);

        //Change the page title
        title = findViewById(R.id.page_title);
        title.setText(getResources().getString(R.string.item_list_title) + " " + category_name);
        //returns a number if you use without the whole thing
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

        //Populate the list by adding rows dynamically
        LayoutInflater inflater = this.getLayoutInflater();
        items = databaseHelper.getAllItems(category_id);

        for(int i = 0; i < items.size(); ++i) {
            RelativeLayout rowView = (RelativeLayout) inflater.inflate(R.layout.items_row_item, item_list, false);
            //customise the title and date
            TextView item_title = rowView.findViewById(R.id.item_title);
            item_title.setText(items.get(i).getName());
            TextView item_date = rowView.findViewById(R.id.item_date);
            item_date.setText("Expires on " + items.get(i).getDate() + "!");
            ImageView item_del = rowView.findViewById(R.id.item_del);
            item_del.setOnClickListener(this::deleteItem);
            rowView.setId(i);
            rowView.setOnClickListener(this::onClickItem);
            //add to list
            item_list.addView(rowView);
        }
    }

    private void onClickItem(View view) {
        Item clicked_item = items.get(view.getId());
        Toast.makeText(this, "You clicked on item: " + clicked_item.toString() + ", at position: " + view.getId(), Toast.LENGTH_SHORT).show();
    }

    private void deleteItem(View view) {
        RelativeLayout rowView = (RelativeLayout) view.getParent();
        int id = rowView.getId();
        int result = databaseHelper.removeItem(items.get(id));
        if(result == 1) {
            Toast.makeText(this, "Item successfully deleted!", Toast.LENGTH_SHORT).show();
            items.remove(id);
            item_list.removeView(rowView);
            //TODO: Delete alarm of the item
        } else {
            Toast.makeText(this, "An error occurred.", Toast.LENGTH_SHORT).show();
        }
    }
}