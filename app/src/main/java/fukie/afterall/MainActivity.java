package fukie.afterall;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    // TextView txtHello;
    DatabaseProcess databaseProcess;
    ListView lstEvent;

    static Context context;

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
        databaseProcess = new DatabaseProcess(this);
        // databaseProcess.dropTable();
        switch (checkAppStart()) {
            case NORMAL:
                //databaseProcess.deleteAllItems();
//                try {
//                    databaseProcess.addExample();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                break;
            case FIRST_TIME_VERSION:
                sharedPreferences.edit().putInt(LAST_APP_VERSION, currentVersionCode).apply();
                break;
            case FIRST_TIME:
                try {
                    databaseProcess.initializeFirstTime();
                    databaseProcess.addExample();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sharedPreferences.edit().putInt(LAST_APP_VERSION, currentVersionCode).apply();
                break;
            default:
                break;
        }

        List<Events> listViewItems = databaseProcess.getAllEvent();
        CustomAdapter customAdapter = new CustomAdapter(this, listViewItems);
        lstEvent.setAdapter(customAdapter);

        //Toast.makeText(context, databaseProcess.xxx(), Toast.LENGTH_LONG).show();
    }


    public void addEvent(View target) {
        Intent intent = new Intent(MainActivity.this, AddEvent.class);
        startActivity(intent);
    }

    public class CustomAdapter extends BaseAdapter {

        private List<Events> objects;

        private Context mContext;

        public CustomAdapter(Context context, List<Events> cur) {
            super();
            mContext = context;
            objects = cur;
        }

        public int getCount() {
            return objects.size();
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Events listViewItem = objects.get(position);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, null);

            TextView lstItemName = (TextView) convertView.findViewById(R.id.lstItemName);
            TextView lstItemCount = (TextView) convertView.findViewById(R.id.lstItemCount);
            ImageView lstItemImage = (ImageView) convertView.findViewById(R.id.lstItemImage);
            RelativeLayout lstItemHolder = (RelativeLayout) convertView.findViewById(R.id.lstItemHolder);
            switch (listViewItem.getColor()) {
                case Constant.COLOR_PINK:
                    lstItemHolder.setBackgroundColor(ContextCompat.getColor(context, R.color.pink_transparent));
                    break;
                case Constant.COLOR_RED:
                    lstItemHolder.setBackgroundColor(ContextCompat.getColor(context, R.color.red_transparent));
                    break;
                case Constant.COLOR_BLUE:
                    lstItemHolder.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_transparent));
                    break;
                case Constant.COLOR_GREEN:
                    lstItemHolder.setBackgroundColor(ContextCompat.getColor(context, R.color.green_transparent));
                    break;
                case Constant.COLOR_YELLOW:
                    lstItemHolder.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow_transparent));
                    break;
                case Constant.COLOR_BROWN:
                    lstItemHolder.setBackgroundColor(ContextCompat.getColor(context, R.color.brown_transparent));
                    break;
                case Constant.COLOR_GRAY:
                    lstItemHolder.setBackgroundColor(ContextCompat.getColor(context, R.color.gray_transparent));
                    break;
                case Constant.COLOR_BLACK:
                    lstItemHolder.setBackgroundColor(ContextCompat.getColor(context, R.color.black_transparent));
                    break;
            }
            lstItemName.setText(listViewItem.getName());
            lstItemCount.setText(String.valueOf(listViewItem.getDate()));
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
