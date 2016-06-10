package fukie.afterall;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    // TextView txtHello;
    DBHelper dbHelper;
    ListView lstEvent;

    static Context context;

    final String PREFS_NAME = "MyPrefsFile";
    SharedPreferences sharedPreferences;

    public enum AppStart {
        FIRST_TIME, FIRST_TIME_VERSION, NORMAL
    }

    private static final String LAST_APP_VERSION = "last_app_version";
    int currentVersionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplication().getApplicationContext();

        lstEvent = (ListView) findViewById(R.id.lstEvent);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        dbHelper = new DBHelper(this);
        // dbHelper.dropTable();
        switch (checkAppStart()) {
            case NORMAL:
//                dbHelper.deleteAllItems();
//                try {
//                    dbHelper.addExample();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                break;
            case FIRST_TIME_VERSION:
                sharedPreferences.edit().putInt(LAST_APP_VERSION, currentVersionCode).apply();
                break;
            case FIRST_TIME:
                try {
                    dbHelper.addExample();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sharedPreferences.edit().putInt(LAST_APP_VERSION, currentVersionCode).apply();
                break;
            default:
                break;
        }

        List<ListViewItem> listViewItems = dbHelper.getAllEvent();
        ListViewItem[] listViewItems1 = new ListViewItem[listViewItems.size()];
        listViewItems.toArray(listViewItems1);
        CustomAdapter customAdapter = new CustomAdapter(this, listViewItems1);
        lstEvent.setAdapter(customAdapter);
    }


    public void addEvent(View target) {
        Intent intent = new Intent(MainActivity.this, AddEvent.class);
        startActivity(intent);
    }

    public static class ListViewItem {
        private String eventName;
        private int dateCount;
        private int type;

        public ListViewItem(String eventName, int dateCount, int type) {
            this.eventName = eventName;
            this.dateCount = dateCount;
            this.type = type;
        }

        public String getEventName() {
            return eventName;
        }

        public int getType() {
            return type;
        }

        public int getDateCount() {
            return dateCount;
        }
    }

    public class CustomAdapter extends BaseAdapter {

        private ListViewItem[] objects;

        private Context mContext;

        public CustomAdapter(Context context, ListViewItem[] cur) {
            super();
            mContext = context;
            objects = cur;
        }

        public int getCount() {
            return objects.length;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListViewItem listViewItem = objects[position];
            int listViewItemType = getItemViewType(position);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, null);

            TextView lstItemName = (TextView) convertView.findViewById(R.id.lstItemName);
            TextView lstItemCount = (TextView) convertView.findViewById(R.id.lstItemCount);
            ImageView lstItemImage = (ImageView) convertView.findViewById(R.id.lstItemImage);
            RelativeLayout lstItemHolder = (RelativeLayout) convertView.findViewById(R.id.lstItemHolder);
            //   if (convertView == null) {

            switch (listViewItemType) {
                case Constant.EVENT_ANNIVERSARY:
                    lstItemHolder.setBackgroundColor(ContextCompat.getColor(mContext, R.color.pink_transparent));
                    break;
                case Constant.EVENT_EDUCATION:
                    lstItemHolder.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue_transparent));
                    break;
                case Constant.EVENT_JOB:
                    lstItemHolder.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red_transparent));
                    break;
                case Constant.EVENT_LIFE:
                    lstItemHolder.setBackgroundColor(ContextCompat.getColor(mContext, R.color.green_transparent));
                    break;
                case Constant.EVENT_TRIP:
                    lstItemHolder.setBackgroundColor(ContextCompat.getColor(mContext, R.color.yellow_transparent));
                    break;
                case Constant.EVENT_OTHER:
                    lstItemHolder.setBackgroundColor(ContextCompat.getColor(mContext, R.color.gray_transparent));
                    break;
            }
            //      }
            lstItemName.setText(listViewItem.eventName);
            lstItemCount.setText(String.valueOf(listViewItem.dateCount));
            return convertView;
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

    }

    public AppStart checkAppStart() {
        PackageInfo pInfo;

        AppStart appStart = AppStart.NORMAL;
        try {
            int lastVersionCode = sharedPreferences.getInt(LAST_APP_VERSION, -1);
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            currentVersionCode = pInfo.versionCode;
            appStart = checkAppStart(currentVersionCode, lastVersionCode);

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(context, "Unable to determine current app version from pacakge manager. Defenisvely assuming normal app start.", Toast.LENGTH_SHORT).show();
        }
        return appStart;
    }

    public AppStart checkAppStart(int currentVersionCode, int lastVersionCode) {
        if (lastVersionCode == -1) {
            return AppStart.FIRST_TIME;
        } else if (lastVersionCode < currentVersionCode) {
            return AppStart.FIRST_TIME_VERSION;
        } else {
            return AppStart.NORMAL;
        }
    }
}
