package com.example.skoal.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by skoal on 9/27/16.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "todo.db";
    public static final String TABLE_NAME = "todo_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "TaskName";
    public static final String COL_3 = "DueDate";
    public static final String COL_4 = "Priority";
    public static final String COL_5 = "Status";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,TaskName TEXT, DueDate Text, Priority Text,Status Text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }


    public boolean insertData(String taskName,String dueDate, String priority, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,taskName);
        contentValues.put(COL_3,dueDate);
        contentValues.put(COL_4,priority );
        contentValues.put(COL_5,status);

        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select ID as _id, TaskName, DueDate, Priority, Status from "+TABLE_NAME,null);
        return res;
    }

    public int delete(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID=?", new String[]{id});
    }

    public boolean update(String id, String taskName, String dueDate,String priority, String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, taskName);
        contentValues.put(COL_3, dueDate);
        contentValues.put(COL_4,priority);
        contentValues.put(COL_5, status);
        Log.i("======", dueDate);

        db.update(TABLE_NAME, contentValues, "ID=?", new String[]{id} );
        return true;
    }
}
