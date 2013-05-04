package gov.nih.nlm.ncbi.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ncbi.sqlite";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Contract.Summary.TABLE_NAME + " ("
                + Contract.Summary._ID + " INTEGER PRIMARY KEY,"
                + Contract.Summary.COLUMN_NAME_DATA + " TEXT NOT NULL,"
                + Contract.Summary.COLUMN_NAME_OFFLINE + " INTEGER NOT NULL"
                + ");");

        db.execSQL("CREATE TABLE " + Contract.Content.TABLE_NAME + " ("
                + Contract.Content._ID + " INTEGER UNIQUE,"
                + Contract.Content.COLUMN_NAME_DATA + " TEXT NOT NULL,"
                + "FOREIGN KEY (" + Contract.Content._ID + ") REFERENCES "
                + Contract.Summary.TABLE_NAME + " (" + Contract.Summary._ID
                + "));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.Summary.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.Content.TABLE_NAME);

        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}