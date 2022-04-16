package com.example.notificationlistener;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static  final String DATABASE_NAME = "Messenger.db";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE MESSAGES(ID INTEGER PRIMARY KEY AUTOINCREMENT, MESSAGE TEXT NOT NULL, REPLY TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS MESSAGES");
        this.onCreate(sqLiteDatabase);
    }

    public long insertReply(String message, String reply){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("MESSAGE",message.toLowerCase(Locale.ROOT));
        contentValues.put("REPLY", reply.toLowerCase(Locale.ROOT));
        long res = sqLiteDatabase.insert("MESSAGES",null,contentValues);
        return res;
    }

    public int deleteReply(int id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int res = sqLiteDatabase.delete("MESSAGES", "ID=?", new String[]{Integer.toString(id)});
        return res;
    }

    public Cursor getReply(String message){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query( "MESSAGES",new String[]{"REPLY"},"MESSAGE=?", new String[]{message}, null, null, null);
        return cursor;
    }

    public Cursor getAllMessages(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT ID,MESSAGE,REPLY FROM MESSAGES",null);
        return  cursor;
    }

    public int updateMessage(int id, String message){
        System.out.println("Id : " + id +"\nMessage : " + message);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("MESSAGE",message);
        int res = sqLiteDatabase.update("MESSAGES", contentValues, "ID=?",new String[]{Integer.toString(id)});
        return res;
    }

    public int updateReply(int id, String reply){
        System.out.println("Id : " + id +"\nReply : " + reply);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("REPLY", reply);
        int res = sqLiteDatabase.update("MESSAGES", contentValues, "ID=?",new String[]{Integer.toString(id)});
        return  res;
    }
}
