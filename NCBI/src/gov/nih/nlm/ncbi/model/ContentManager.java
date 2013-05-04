package gov.nih.nlm.ncbi.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ContentManager {

    private Context context = null;

    public ContentManager(Context context) {
        this.context = context;
    }

    public Content select(String selection) {
        DataBaseHelper helper = new DataBaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        String[] projection = { Contract.Content._ID,
                Contract.Content.COLUMN_NAME_DATA };

        Cursor cursor = database.query(Contract.Content.TABLE_NAME, projection,
                selection, null, null, null, null);
        cursor.moveToFirst();

        Content content = null;
        if (cursor.getCount() > 0) {
            content = new Content();
            content.setId((long) cursor.getInt(cursor
                    .getColumnIndex(Contract.Content._ID)));
            content.setData(cursor.getString(cursor
                    .getColumnIndex(Contract.Content.COLUMN_NAME_DATA)));
        }
        cursor.close();
        database.close();

        return content;
    }

    public void insertOrUpdate(Content content) {
        DataBaseHelper helper = new DataBaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Contract.Content._ID, content.getId());
        values.put(Contract.Content.COLUMN_NAME_DATA, content.getData());

        try {
            database.insertOrThrow(Contract.Content.TABLE_NAME, null, values);
        } catch (SQLException e) {
            String[] whereArgs = { String.valueOf(content.getId()) };

            database.update(Contract.Content.TABLE_NAME, values,
                    Contract.Content._ID + "=?", whereArgs);
        }
        database.close();
    }

    public void delete(Content content) {
        DataBaseHelper helper = new DataBaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        database.delete(Contract.Content.TABLE_NAME, Contract.Content._ID
                + "=?", new String[] { String.valueOf(content.getId()) });
        database.close();
    }
}