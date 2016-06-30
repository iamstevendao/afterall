package fukie.afterall.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fukie on 23/05/2016.
 */
public class DatabaseProcess {

    Context context;
    SQLiteDatabase db;

    public DatabaseProcess(Context context) {
        this.context = context;
        this.db = context.openOrCreateDatabase(Constants.DATABASE_NAME, Context.MODE_PRIVATE, null);
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

    public void insertEvent(String name, int kind, String date, int loop, int img) {
        db.execSQL("INSERT INTO event(event_name, kind_id, event_date"
                + ", event_loop, event_image) "
                + "VALUES('" + name + "', " + kind + ", '" + date + "', "
                + loop + ", " + img + ");");
    }

    public void modifyEvent(int id, String name, int kind, String date, int loop, int img) {
        db.execSQL("UPDATE event SET event_name ='" + name + "', kind_id="+kind
                +", event_date='" + date +"', event_loop=" + loop + ", event_image=" + img +
                " WHERE event_id=" + id);
    }

    public void deleteEvent(int id){
        db.execSQL("DELETE FROM event WHERE event_id=" + id);
    }

    public void addExample() throws Exception {
        insertEvent("Yêu xa nhé!", 1, "2016-5-14", 0, 2);
        insertEvent("Có!", 1, "2012-01-05", 0, 1);
        insertEvent("Sinh Nhật Lợn", 5, "1995-02-05", 1, 3);
        insertEvent("Sinh Nhật Chó", 3, "1995-9-16", 1, 4);
        insertEvent("choi", 4, "2016-6-20", 0, 1);
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
            while (!res.isAfterLast()) {
                Events event = new Events(
                        res.getInt(res.getColumnIndex(Constants.EVENT_COLUMN_ID))
                        , res.getString(res.getColumnIndex(Constants.EVENT_COLUMN_NAME))
                        , res.getInt(res.getColumnIndex(Constants.KIND_COLUMN_ID))
                        , res.getInt(res.getColumnIndex(Constants.KIND_COLUMN_COLOR))
                        , res.getString(res.getColumnIndex(Constants.EVENT_COLUMN_DATE))
                        , res.getInt(res.getColumnIndex(Constants.EVENT_COLUMN_LOOP))
                        , res.getInt(res.getColumnIndex(Constants.EVENT_COLUMN_IMAGE)));
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
