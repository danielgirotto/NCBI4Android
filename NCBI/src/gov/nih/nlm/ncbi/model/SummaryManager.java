package gov.nih.nlm.ncbi.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SummaryManager {

    private Context context = null;

    public SummaryManager(Context context) {
        this.context = context;
    }

    public Summary select(String selection) {
        DataBaseHelper helper = new DataBaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        String[] projection = { Contract.Summary._ID,
                Contract.Summary.COLUMN_NAME_DATA };

        Cursor cursor = database.query(Contract.Summary.TABLE_NAME, projection,
                selection, null, null, null, null);
        cursor.moveToFirst();

        Summary summary = null;
        if (cursor.getCount() > 0) {
            summary = new Summary();
            summary.setId((long) cursor.getInt(cursor
                    .getColumnIndex(Contract.Summary._ID)));
            summary.setData(cursor.getString(cursor
                    .getColumnIndex(Contract.Summary.COLUMN_NAME_DATA)));
        }
        cursor.close();
        database.close();

        return summary;
    }

    public List<Summary> selectAll() {
        DataBaseHelper helper = new DataBaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        String[] projection = { Contract.Summary._ID,
                Contract.Summary.COLUMN_NAME_DATA };

        Cursor cursor = database.query(Contract.Summary.TABLE_NAME, projection,
                Contract.Summary.COLUMN_NAME_OFFLINE + " = 1", null, null,
                null, null);
        cursor.moveToFirst();

        List<Summary> summaryList = new ArrayList<Summary>();
        if (cursor.getCount() > 0) {
            do {
                Summary summary = new Summary();
                summary.setId((long) cursor.getInt(cursor
                        .getColumnIndex(Contract.Summary._ID)));
                summary.setData(cursor.getString(cursor
                        .getColumnIndex(Contract.Summary.COLUMN_NAME_DATA)));
                summaryList.add(summary);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();

        return summaryList;
    }

    public void insertOrUpdate(Summary summary, int offline) {
        DataBaseHelper helper = new DataBaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Contract.Summary._ID, summary.getId());
        values.put(Contract.Summary.COLUMN_NAME_DATA, summary.getData());
        values.put(Contract.Summary.COLUMN_NAME_OFFLINE, offline);

        try {
            database.insertOrThrow(Contract.Summary.TABLE_NAME, null, values);
        } catch (SQLException e) {
            String[] whereArgs = { String.valueOf(summary.getId()) };

            database.update(Contract.Summary.TABLE_NAME, values,
                    Contract.Summary._ID + "=?", whereArgs);
        }
        database.close();
    }

    public void delete(Summary summary) {
        DataBaseHelper helper = new DataBaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        database.delete(Contract.Summary.TABLE_NAME, Contract.Summary._ID
                + "=?", new String[] { String.valueOf(summary.getId()) });
        database.close();
    }
}