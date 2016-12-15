package edu.wpi.goalify.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.wpi.goalify.models.TeamLocation;

/**
 * Created by tupac on 12/13/2016.
 */

public class DBHelper  extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SQLiteData.db";
    private static final int DATABASE_VERSION = 6;
    public static final String TABLE_NAME = "followed_teams";
    public static final String TEAM_ID = "_id";
    public static final String TEAM_NAME = "team_name";
    public static final String TEAM_LOCATION_LAT = "team_location_lat";
    public static final String TEAM_LOCATION_LON = "team_location_lon";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                TEAM_ID + " INTEGER PRIMARY KEY, " +
                TEAM_NAME + " TEXT, " +
                TEAM_LOCATION_LAT + " INTEGER, " +
                TEAM_LOCATION_LON + " INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertTeam(int id, String name, double lat, double lon) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TEAM_ID, id);
        contentValues.put(TEAM_NAME, name);
        contentValues.put(TEAM_LOCATION_LAT, lat);
        contentValues.put(TEAM_LOCATION_LON, lat);
        db.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        return true;
    }

    public Cursor getAllTeams() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + TABLE_NAME, null );
        return res;
    }

    public Integer deleteTeam(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                TEAM_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }

}