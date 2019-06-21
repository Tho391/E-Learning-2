package com.example.dardan.elearning;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Dardan on 11/17/2016.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    //region database sqlite
    //database
    private static final String DATABASE_NAME = "e_learning.db";
    private static final int DATABASE_VERSION = 1;
    //common column
    private static final String COLUMN_ID = "_id";
    //table category
    private static final String TABLE_CATEGORY = "category";
    private static final String COLUMN_COLOR = "color";
    private static final String COLUMN_HIGHSCORE = "highScore";
    private static final String COLUMN_THEME = "theme";
    private static final String COLUMN_TITLE = "title";
    //table thing and it's columns
    private static final String TABLE_THING = "thing";
    private static final String COLUMN_IMAGEPATH = "image";
    private static final String COLUMN_TEXT = "text";
    private static final String COLUMN_CATEGORY_ID = "categoryId";
    //create table category
    private static final String CREATE_TABLE_CATEGORY =
            "create table " + TABLE_CATEGORY
                    + "( "
                    + COLUMN_ID + " integer primary key autoincrement,"
                    + COLUMN_TITLE + " text,"
                    + COLUMN_IMAGEPATH + " text,"
                    + COLUMN_HIGHSCORE + " integer,"
                    + COLUMN_COLOR + " integer,"
                    + COLUMN_THEME + " integer"
                    + ");";

    //create table thing
    private static final String CREATE_TABLE_THING =
            "create table " + TABLE_THING
                    + "( "
                    + COLUMN_ID + " integer primary key autoincrement,"
                    + COLUMN_TEXT + " text,"
                    + COLUMN_IMAGEPATH + " text,"
                    + COLUMN_CATEGORY_ID + " integer"
                    + ");";
    private static final String[] CATEGORY_TABLE_COLUMNS = {
            COLUMN_ID,
            COLUMN_TITLE,
            COLUMN_IMAGEPATH,
            COLUMN_HIGHSCORE,
            COLUMN_COLOR,
            COLUMN_THEME};
    private static final String[] THING_TABLE_COLUMNS = {
            COLUMN_ID,
            COLUMN_TEXT,
            COLUMN_IMAGEPATH,
            COLUMN_CATEGORY_ID};
    //endregion


    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_THING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_THING);

        onCreate(db);
    }

    public int getCategoryCount() {
        String countQuery = "SELECT * FROM " + TABLE_CATEGORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count

        db.close();
        return count;
    }

    public void addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, category.getTitle());
        values.put(COLUMN_IMAGEPATH, category.getImagePath());
        values.put(COLUMN_HIGHSCORE, category.getHighScore());
        values.put(COLUMN_COLOR, category.getColor());
        values.put(COLUMN_THEME, category.getTheme());
        // insert 1 row to table
        db.insert(TABLE_CATEGORY, null, values);

        int lastCategoryId = getLastId(TABLE_CATEGORY);
        if (!db.isOpen())
            db = getWritableDatabase();
        for (Thing i : category.getThings()) {
            ContentValues values1 = new ContentValues();
            values1.put(COLUMN_TEXT, i.getText());
            values1.put(COLUMN_IMAGEPATH, i.getImagePath());
            values1.put(COLUMN_CATEGORY_ID, lastCategoryId);
            db.insert(TABLE_THING, null, values1);
        }
        // close connection
        db.close();
    }

    private int getLastId(String tableName) {
        String countQuery = "SELECT max(_id) FROM " + tableName;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int id = -1;
        if (cursor != null) {
            cursor.moveToFirst();
            id = cursor.getInt(0);
            cursor.close();
        }
        db.close();
        return id;
    }

    public int updateCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, category.getTitle());
        values.put(COLUMN_IMAGEPATH, category.getImagePath());
        values.put(COLUMN_HIGHSCORE, category.getHighScore());
        values.put(COLUMN_COLOR, category.getColor());
        values.put(COLUMN_THEME, category.getTheme());
        // updating row
        int update1 = db.update(TABLE_CATEGORY, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(category.getId())});

//        db.close();
//        if (!db.isOpen()) {
//            db = getWritableDatabase();
//        }

        for (Thing i : category.getThings()) {
            ContentValues values1 = new ContentValues();
            values.put(COLUMN_TEXT, i.getText());
            values.put(COLUMN_IMAGEPATH, i.getImagePath());
            values.put(COLUMN_CATEGORY_ID, category.getId());

            db.update(TABLE_THING, values1, COLUMN_CATEGORY_ID + " = ?",
                    new String[]{String.valueOf(category.getId())});
        }

        db.close();
        return update1;
    }

    public void deleteCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (Thing i : category.getThings()) {
            db.delete(TABLE_THING, COLUMN_CATEGORY_ID + " = ?",
                    new String[]{String.valueOf(category.getId())});
        }
        db.delete(TABLE_CATEGORY, COLUMN_ID + " = ?",
                new String[]{String.valueOf(category.getId())});
        db.close();
    }

    public ArrayList<Category> getAllCategory() {
        ArrayList<Category> categoryList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORY;
        ArrayList<Integer> idList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            // Browse on the cursor, and add to the list.
            do {
                int id = -1;
                if (cursor.getString(0) != null) {
                    id = Integer.parseInt(cursor.getString(0));
                    idList.add(id);
                }
            } while (cursor.moveToNext());

            for (int i : idList) {
                categoryList.add(getCategory(i));
            }
        } else {
            db.close();
            return null;
        }
        cursor.close();
        db.close();
        return categoryList;
    }

    public Category getCategory(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CATEGORY, CATEGORY_TABLE_COLUMNS,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);


        Category category = new Category();
        if (cursor != null) {
            cursor.moveToFirst();

            category.setId(cursor.getInt(0));
            category.setTitle(cursor.getString(1));
            category.setImagePath(cursor.getString(2));
            category.setHighScore(cursor.getInt(3));
            category.setColor(cursor.getInt(4));
            category.setTheme(cursor.getInt(5));
            cursor.close();
//            if (!db.isOpen()) {
//                db = getWritableDatabase();
//            }

            Cursor cursor1 = db.query(TABLE_THING, THING_TABLE_COLUMNS,
                    COLUMN_CATEGORY_ID + " = ?",
                    new String[]{String.valueOf(id)}, null, null, null, null);
            if (cursor1 != null && cursor1.moveToFirst()) {

                ArrayList<Thing> things = new ArrayList<>();
                do {
                    Thing thing = new Thing();

                    thing.setId(cursor1.getInt(0));
                    thing.setText(cursor1.getString(1));
                    thing.setImagePath(cursor1.getString(2));
                    // add to list
                    things.add(thing);
                } while (cursor1.moveToNext());
                cursor1.close();

                category.setThings(things);
            }
        }
        db.close();
        // return note
        return category;
    }

    public Category getCategory(String title) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CATEGORY, CATEGORY_TABLE_COLUMNS,
                COLUMN_TITLE + "=?",
                new String[]{title}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Category category = new Category();
        if (cursor != null) {
            category.setId(cursor.getInt(0));
            category.setTitle(cursor.getString(1));
            category.setImagePath(cursor.getString(2));
            category.setHighScore(cursor.getInt(3));
            category.setColor(cursor.getInt(4));
            category.setTheme(cursor.getInt(5));

            cursor.close();
//            db.close();
//            if (!db.isOpen()) {
//                db = getWritableDatabase();
//            }

            Cursor cursor1 = db.query(TABLE_THING, THING_TABLE_COLUMNS,
                    COLUMN_CATEGORY_ID + " = ?",
                    new String[]{String.valueOf(category.getId())}, null, null, null, null);
            ArrayList<Thing> things = new ArrayList<>();
            if (cursor1 != null) {
                cursor1.moveToFirst();
                do {
                    Thing thing = new Thing();

                    thing.setId(cursor1.getInt(0));
                    thing.setText(cursor1.getString(1));
                    thing.setImagePath(cursor1.getString(2));
                    // add to list
                    things.add(thing);
                } while (cursor1.moveToNext());
                cursor1.close();
            }
            category.setThings(things);
        }
        db.close();
        // return note
        return category;
    }

    public void updateHighScore(String categoryTitle, int highScore) {
        Category category = getCategory(categoryTitle);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_HIGHSCORE, highScore);
        // updating row
        db.update(TABLE_CATEGORY, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(category.getId())});
        db.close();
    }

}
