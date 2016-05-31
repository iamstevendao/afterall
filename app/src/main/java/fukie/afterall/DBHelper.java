package fukie.afterall;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Fukie on 23/05/2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "afterall.db";
    public static final String EVENTS_TABLE_NAME = "events";
    public static final String EVENTS_COLUMN_ID = "_id";
    public static final String EVENTS_COLUMN_NAME = "name";
    public static final String EVENTS_COLUMN_TYPE = "type";
    public static final String EVENTS_COLUMN_DATE = "date";
    public static final String EVENTS_COLUMN_LOOP = "loop";
    public static final String EVENTS_COLUMN_MEMORY = "memory";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
        //context.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table events " +
                        "(_id integer primary key autoincrement, name text, " +
                        "type text, date date, loop boolean, memory text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS events");
        onCreate(db);
    }

    public void insertContact(String name, String type, String date, boolean loop, String memory)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("type", type);
        contentValues.put("date", date);
        contentValues.put("loop", loop);
        contentValues.put("memory", memory);
        db.insert("events", null, contentValues);
    }

    public void addExample() throws Exception{
        insertContact("yeu", "ANNIVERSARY", "2016-5-23", false, "100 days");
        insertContact("hoc", "EDUCATION", "2016-5-24", true, "200 days");
        insertContact("an", "OTHER", "2016-5-25", true, "300 days");
        insertContact("ngu", "LIFE", "2016-5-26", false, "100 days");
        insertContact("choi", "LIFE", "2016-5-26", false, "100 days");
    }

    public int getNumberOfEvents(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, EVENTS_TABLE_NAME);
    }

    public List<Events> getAllEvents() {
        List<Events> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from events", null);
        res.moveToFirst();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            while (!res.isAfterLast()) {
                String type = res.getString(res.getColumnIndex(EVENTS_COLUMN_TYPE));
                Events event = new Events(
                        res.getString(res.getColumnIndex(EVENTS_COLUMN_NAME)),
                        res.getString(res.getColumnIndex(EVENTS_COLUMN_TYPE)),
                        sdf.parse(res.getString(res.getColumnIndex(EVENTS_COLUMN_DATE))),
                        res.getInt(res.getColumnIndex(EVENTS_COLUMN_LOOP))==1,
                        res.getString(res.getColumnIndex(EVENTS_COLUMN_MEMORY)));
                events.add(event);
                res.moveToNext();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        res.close();
        return events;
    }

    public List<MainActivity.ListViewItem> getAllEvent() {
        List<MainActivity.ListViewItem> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from events", null);
        res.moveToFirst();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            while (!res.isAfterLast()) {
                Date today = new Date();
                long diff =  sdf.parse(res.getString(
                        res.getColumnIndex(EVENTS_COLUMN_DATE))).getTime() - today.getTime();
                int diffDays = (int)(diff / (60 * 60 * 1000 * 24));
                String type = res.getString(res.getColumnIndex(EVENTS_COLUMN_TYPE));
                MainActivity.ListViewItem event = new MainActivity.ListViewItem(
                        res.getString(res.getColumnIndex(EVENTS_COLUMN_NAME)),
                        diffDays,
                        Constant.getEventTypeId(type));
                events.add(event);
                res.moveToNext();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        res.close();
        return events;
    }
}
