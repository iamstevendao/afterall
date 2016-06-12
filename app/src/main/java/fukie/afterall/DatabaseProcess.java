package fukie.afterall;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Fukie on 23/05/2016.
 */
public class DatabaseProcess {
    public static final String DATABASE_NAME = "afterAll.db";
    public static final String EVENT_TABLE_NAME = "event";
    public static final String KIND_TABLE_NAME = "kind";
    public static final String EVENT_COLUMN_ID = "event_id";
    public static final String EVENT_COLUMN_NAME = "event_name";
    public static final String KIND_COLUMN_ID = "kind_id";
    public static final String KIND_COLUMN_NAME = "kind_name";
    public static final String KIND_COLUMN_COLOR = "kind_color";
    public static final String EVENT_COLUMN_DATE = "event_date";
    public static final String EVENT_COLUMN_LOOP = "event_loop";
    public static final String EVENT_COLUMN_MEMORY = "event_memory";
    Context context;
    SQLiteDatabase db;

    public DatabaseProcess(Context context) {
        this.context = context;
        this.db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        db.execSQL(
                "create table if not exists event" +
                        "(event_id integer primary key autoincrement, event_name text, " +
                        "kind_id integer, event_date text, event_loop integer, event_memory text);"
        );
        db.execSQL(
                "create table if not exists kind" +
                        "(kind_id integer primary key autoincrement, kind_name text" +
                        ", kind_color integer);"
        );
    }

    public void initializeFirstTime() {
        insertKind("anniversary", 1);
        insertKind("education", 2);
        insertKind("job", 3);
        insertKind("life", 4);
        insertKind("trip", 5);
        insertKind("other", 6);
    }

    public void dropAllTable() {
        db.execSQL("DROP TABLE IF EXISTS event");
        db.execSQL("DROP TABLE IF EXISTS kind");
    }

    public void insertKind(String name, int color) {
        db.execSQL("INSERT INTO kind(kind_name, kind_color) " +
                "VALUES('" + name + "', " + color + ");");
    }

    public void insertEvent(String name, int kind, String date, int loop, String memory) {
        db.execSQL("INSERT INTO event(event_name, kind_id, event_date, event_loop, event_memory) " +
                "VALUES('" + name + "', " + kind + ", '" + date + "', "
                + loop + ", '" + memory + "');");
    }

    public void addExample() throws Exception {
        insertEvent("yeu", 1, "2016-5-23", 0, "100 days");
        insertEvent("hoc", 2, "2016-5-24", 1, "200 days");
        insertEvent("an", 5, "2016-5-25", 0, "300 days");
        insertEvent("ngu", 3, "2016-5-26", 1, "100 days");
        insertEvent("choi", 4, "2016-5-26", 0, "100 days");
    }

    public List<Events> getAllEvent() {
        List<Events> events = new ArrayList<>();
        Cursor res = db.rawQuery("select * from event natural join kind", null);
        res.moveToFirst();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            while (!res.isAfterLast()) {
                //  Date today = new Date();
                //long diff =  sdf.parse(res.getString(
                //      res.getColumnIndex(EVENT_COLUMN_DATE))).getTime() - today.getTime();
                // int diffDays = (int)(diff / (60 * 60 * 1000 * 24));
                // int kind = res.getInt(res.getColumnIndex(KIND_COLUMN_ID));

                Events event = new Events(
                        res.getString(res.getColumnIndex(EVENT_COLUMN_NAME))
                        , res.getInt(res.getColumnIndex(KIND_COLUMN_ID))
                        , res.getInt(res.getColumnIndex(KIND_COLUMN_COLOR))
                        , res.getString(res.getColumnIndex(EVENT_COLUMN_DATE))
                        , res.getInt(res.getColumnIndex(EVENT_COLUMN_LOOP)) == 1
                        , "default");
                events.add(event);
                res.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        res.close();
        return events;
    }
}
