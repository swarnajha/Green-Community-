package com.laureen.expirydatetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "trackerDB.db";
    public static final String TABLE_NAME_1 = "Category";
    public static final String TABLE_NAME_2 = "Item";
    public static final String COLUMN_1 = "ID";
    public static final String COLUMN_2 = "Name";
    public static final String ITEM_COLUMN_3 = "Date";
    public static final String ITEM_COLUMN_4 = "CategoryID";
    public static final String CAT_COLUMN_3 = "Days";
    public static final String CAT_COLUMN_4 = "ImageName";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //called the 1st time db is accessed
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_NAME_1 + "( " +
                COLUMN_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_2 + " TEXT NOT NULL, " +
                ITEM_COLUMN_3 + " TEXT NOT NULL, " +
                ITEM_COLUMN_4 + " REFERENCES " + TABLE_NAME_1 + "(" + COLUMN_1 + ") ON DELETE CASCADE);";
        String CREATE_CAT_TABLE = "CREATE TABLE " + TABLE_NAME_2 + "( " +
                COLUMN_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_2 + " TEXT NOT NULL, " +
                CAT_COLUMN_3 + " INTEGER NOT NULL, " +
                CAT_COLUMN_4 + " TEXT NOT NULL);";
        db.execSQL(CREATE_CAT_TABLE);
        db.execSQL(CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
    }

    //View>Tool Windows>Dev File Explorer>com.blah.>get the db
    public Boolean addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_2, category.getName());
        cv.put(CAT_COLUMN_3, category.getDays());
        cv.put(CAT_COLUMN_4, category.getImageName());

        long result = db.insert(TABLE_NAME_1, null, cv);
        return result != -1;
    }

    public List<Category> getAllCategories() {
        List<Category> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TABLE_NAME_1 + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()) {
            //loop through the cursor
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int days = cursor.getInt(2);
                String imageName = cursor.getString(3);

                Category category = new Category(id, name, days, imageName);
                returnList.add(category);
            } while(cursor.moveToNext());
        }
        //close the cursor and db once done
        cursor.close();
        db.close();
        return returnList;
    }
}
