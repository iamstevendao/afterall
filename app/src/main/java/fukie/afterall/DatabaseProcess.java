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

    Context context;
    SQLiteDatabase db;

    public DatabaseProcess(Context context) {
        this.context = context;
        this.db = context.openOrCreateDatabase(Constant.DATABASE_NAME, Context.MODE_PRIVATE, null);
        db.execSQL(
                "create table if not exists event" +
                        "(event_id integer primary key autoincrement, event_name text, " +
                        "kind_id integer, event_date text, event_loop integer" +
                        ", event_notification integer, event_image integer);"
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

    public void insertEvent(String name, int kind, String date, int loop, int memory, int img) {
        db.execSQL("INSERT INTO event(event_name, kind_id, event_date"
                + ", event_loop, event_notification, event_image) "
                + "VALUES('" + name + "', " + kind + ", '" + date + "', "
                + loop + ", " + memory + ", " + img + ");");
    }

    public void addExample() throws Exception {
        insertEvent("yeu", 1, "2016-5-23", 0, 0, 7);
        insertEvent("hoc", 2, "2016-5-24", 1, 0, 8);
        insertEvent("an", 5, "2016-5-25", 0, 1, 3);
        insertEvent("ngu", 3, "2016-5-26", 1, 1, 4);
        insertEvent("choi", 4, "2016-5-26", 0, 0, 5);
    }

    public Cursor query(String query) {
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();
        return res;
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
                        res.getString(res.getColumnIndex(Constant.EVENT_COLUMN_NAME))
                        , res.getInt(res.getColumnIndex(Constant.KIND_COLUMN_ID))
                        , res.getInt(res.getColumnIndex(Constant.KIND_COLUMN_COLOR))
                        , res.getString(res.getColumnIndex(Constant.EVENT_COLUMN_DATE))
                        , res.getInt(res.getColumnIndex(Constant.EVENT_COLUMN_LOOP)) == 1
                        , res.getInt(res.getColumnIndex(Constant.EVENT_COLUMN_NOTIFICATION)) == 1
                        , res.getInt(res.getColumnIndex(Constant.EVENT_COLUMN_IMAGE)));
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
