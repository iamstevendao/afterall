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
        databaseProcess = new DatabaseProcess(context);
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


        private class ViewHolder {
            TextView txtName;
            TextView txtCount;
            ImageView imgEvent;
            RelativeLayout layoutBackground;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Events listViewItem = objects.get(position);
            ViewHolder viewHolder;
            if(convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.listview_item, null);
                viewHolder = new ViewHolder();
                viewHolder.txtName = (TextView) convertView.findViewById(R.id.lstItemName);
                viewHolder.txtCount = (TextView) convertView.findViewById(R.id.lstItemCount);
                viewHolder.imgEvent = (ImageView) convertView.findViewById(R.id.lstItemImage);
                viewHolder.layoutBackground = (RelativeLayout)
                        convertView.findViewById(R.id.lstItemHolder);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            switch (listViewItem.getColor()) {
                case Constant.COLOR_PINK:
                    viewHolder.layoutBackground.setBackgroundColor(ContextCompat.getColor(context
                            , R.color.pink_transparent));
                    break;
                case Constant.COLOR_RED:
                    viewHolder.layoutBackground.setBackgroundColor(ContextCompat.getColor(context
                            , R.color.red_transparent));
                    break;
                case Constant.COLOR_BLUE:
                    viewHolder.layoutBackground.setBackgroundColor(ContextCompat.getColor(context
                            , R.color.blue_transparent));
                    break;
                case Constant.COLOR_GREEN:
                    viewHolder.layoutBackground.setBackgroundColor(ContextCompat.getColor(context
                            , R.color.green_transparent));
                    break;
                case Constant.COLOR_YELLOW:
                    viewHolder.layoutBackground.setBackgroundColor(ContextCompat.getColor(context
                            , R.color.yellow_transparent));
                    break;
                case Constant.COLOR_BROWN:
                    viewHolder.layoutBackground.setBackgroundColor(ContextCompat.getColor(context
                            , R.color.brown_transparent));
                    break;
                case Constant.COLOR_GRAY:
                    viewHolder.layoutBackground.setBackgroundColor(ContextCompat.getColor(context
                            , R.color.gray_transparent));
                    break;
                case Constant.COLOR_BLACK:
                    viewHolder.layoutBackground.setBackgroundColor(ContextCompat.getColor(context
                            , R.color.black_transparent));
                    break;
            }

            switch (listViewItem.getKind()){
                case Constant.EVENT_ANNIVERSARY:
                    viewHolder.imgEvent.setImageResource(R.drawable.anniversary);
                    break;
                case Constant.EVENT_EDUCATION:
                    viewHolder.imgEvent.setImageResource(R.drawable.education);
                    break;
                case Constant.EVENT_JOB:
                    viewHolder.imgEvent.setImageResource(R.drawable.job);
                    break;
                case Constant.EVENT_LIFE:
                    viewHolder.imgEvent.setImageResource(R.drawable.life);
                    break;
                case Constant.EVENT_TRIP:
                    viewHolder.imgEvent.setImageResource(R.drawable.trip);
                    break;
                default:
                    viewHolder.imgEvent.setImageResource(R.drawable.other);
                    break;
            }
            viewHolder.txtName.setText(listViewItem.getName());
            viewHolder.txtCount.setText(String.valueOf(listViewItem.getDiff()));

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
