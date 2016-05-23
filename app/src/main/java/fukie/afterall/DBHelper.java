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
    public static final String EVENTS_COLUMN_ID = "id";
    public static final String EVENTS_COLUMN_NAME = "name";
    public static final String EVENTS_COLUMN_TYPE = "type";
    public static final String EVENTS_COLUMN_DATE = "date";
    public static final String EVENTS_COLUMN_LOOP = "loop";
    public static final String EVENTS_COLUMN_MEMORY = "memory";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS events");
        db.execSQL(
                "create table events " +
                        "(id integer primary key, name text, " +
                        "type text,date date, loop boolean, memory text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS events");
        onCreate(db);
    }

    public void insertContact(String name, String type, Date date, boolean loop, String memory)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("type", type);
        contentValues.put("date", sdf.format(date));
        contentValues.put("loop", loop);
        contentValues.put("memory", memory);
        db.insert("events", null, contentValues);
    }

    public void addExample() throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        insertContact("yeu", "YEU", sdf.parse("2016-5-23"), false, "100 days");
        insertContact("hoc", "HOC", sdf.parse("2016-5-24"), true, "200 days");
        insertContact("an", "AN", sdf.parse("2016-5-25"), true, "300 days");
        insertContact("ngu", "NGU", sdf.parse("2016-5-26"), false, "100 days");
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
}
