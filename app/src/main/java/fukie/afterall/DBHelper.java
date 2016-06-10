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
    public static final String DATABASE_NAME = "afterAll";
    public static final String EVENT_TABLE_NAME = "event";
    public static final String KIND_TABLE_NAME = "kind";
    public static final String EVENT_COLUMN_ID = "event_id";
    public static final String EVENT_COLUMN_NAME = "event_name";
    public static final String KIND_COLUMN_ID = "kind_id";
    public static final String KIND_COLUMN_NAME = "kind_name";
    public static final String EVENT_COLUMN_DATE = "event_date";
    public static final String EVENT_COLUMN_LOOP = "event_loop";
    public static final String EVENT_COLUMN_MEMORY = "event_memory";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
      //  context.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table event" +
                        "(event_id integer primary key autoincrement, event_name text, " +
                        "kind_id integer, event_date text, event_loop integer, event_memory text)"
        );
        db.execSQL(
                "create table kind" +
                        "(kind_id integer primary key autoincrement, kind_name text);" +
                        "INSERT INTO kind(kind_name) VALUES('anniversary');" +
                        "INSERT INTO kind(kind_name) VALUES('education');" +
                        "INSERT INTO kind(kind_name) VALUES('job');" +
                        "INSERT INTO kind(kind_name) VALUES('life');" +
                        "INSERT INTO kind(kind_name) VALUES('trip');" +
                        "INSERT INTO kind(kind_name) VALUES('other');"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS event");
        db.execSQL("DROP TABLE IF EXISTS kind");
        onCreate(db);
    }

    public void deleteAllItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EVENT_TABLE_NAME, null, null);
    }
    public void insertKind(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KIND_COLUMN_NAME, name);
        db.insert(KIND_TABLE_NAME, null, contentValues);
    }

    public void insertEvent(String name, int kind, String date, int loop, String memory)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENT_COLUMN_NAME, name);
        contentValues.put(KIND_COLUMN_ID, kind);
        contentValues.put(EVENT_COLUMN_DATE, date);
        contentValues.put(EVENT_COLUMN_LOOP, loop);
        contentValues.put(EVENT_COLUMN_MEMORY, memory);
        db.insert(EVENT_TABLE_NAME, null, contentValues);
    }

    public void addExample() throws Exception {
        insertEvent("yeu", 5, "2016-5-23", 0, "100 days");
        insertEvent("hoc", 1, "2016-5-24", 1, "200 days");
        insertEvent("an", 2, "2016-5-25", 0, "300 days");
        insertEvent("ngu", 3, "2016-5-26", 1, "100 days");
        insertEvent("choi", 4, "2016-5-26", 0, "100 days");
    }

    public int getNumberOfEvents(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, EVENT_TABLE_NAME);
    }

    public List<MainActivity.ListViewItem> getAllEvent() {
        List<MainActivity.ListViewItem> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select event_date, event_name, kind_name from event" +
                " natural join kind", null);
        res.moveToFirst();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            while (!res.isAfterLast()) {
                Date today = new Date();
                long diff =  sdf.parse(res.getString(
                        res.getColumnIndex(EVENT_COLUMN_DATE))).getTime() - today.getTime();
                int diffDays = (int)(diff / (60 * 60 * 1000 * 24));
                String type = res.getString(res.getColumnIndex(KIND_COLUMN_NAME));
                MainActivity.ListViewItem event = new MainActivity.ListViewItem(
                        res.getString(res.getColumnIndex(EVENT_COLUMN_NAME)),
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
