package fukie.afterall;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
   // TextView txtHello;
    DBHelper dbHelper;
    ListView lstEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     //   txtHello = (TextView) findViewById(R.id.txtHello);
        lstEvent = (ListView) findViewById(R.id.lstEvent);
//        dbHelper.dropTable();
        dbHelper = new DBHelper(this);
        try {
            dbHelper.addExample();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String s = "";
        List<Events> events = dbHelper.getAllEvents();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        for (Events event : events) {
            s += "Event: " + event.name +
                    " Date: " + sdf.format(event.date) +
                    " Loop: " + Boolean.toString(event.loop) +
                    " Memory: " + event.memory;
        }
    //    txtHello.setText(s);

        List<ListViewItem> listViewItems = dbHelper.getAllEvent();
        ListViewItem[] listViewItems1 = new ListViewItem[listViewItems.size()];
        listViewItems.toArray(listViewItems1);
        CustomAdapter customAdapter = new CustomAdapter(this, R.id.text, listViewItems1);
        lstEvent.setAdapter(customAdapter);
    }
//
//    @Override
//    public void onResume(){
//        super.onResume();
//        String s = "";
//        List<Events> events = dbHelper.getAllEvents();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//        for (Events event : events) {
//            s += "Event: " + event.name +
//                    " Date: " + sdf.format(event.date) +
//                    " Loop: " + Boolean.toString(event.loop) +
//                    " Memory: " + event.memory;
//        }
//        txtHello.setEventName(s);
//    }

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

    public class CustomAdapter extends ArrayAdapter {

        private ListViewItem[] objects;

        @Override
        public int getViewTypeCount() {
            return 4;
        }

        @Override
        public int getItemViewType(int position) {
            return objects[position].getType();
        }

        public CustomAdapter(Context context, int resource, ListViewItem[] objects) {
            super(context, resource, objects);
            this.objects = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ListViewItem listViewItem = objects[position];
            int listViewItemType = getItemViewType(position);

            if (convertView == null) {

                if (listViewItemType == Constant.EVENT_ANNIVERSARY) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_anniversary, parent, false);
                } else if (listViewItemType == Constant.EVENT_EDUCATION) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_education, parent, false);
                } else if (listViewItemType == Constant.EVENT_JOB) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_job, parent, false);
                } else if (listViewItemType == Constant.EVENT_LIFE) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_life, parent, false);
                } else if (listViewItemType == Constant.EVENT_TRIP) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_trip, parent, false);
                } else {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_other, parent, false);
                }
            }
            TextView txtName = (TextView) convertView.findViewById(R.id.txtEventName);
            TextView txtDate = (TextView) convertView.findViewById(R.id.txtEventDateCount);
            txtName.setText(listViewItem.eventName);
            txtDate.setText(Integer.toString(listViewItem.dateCount));
            return convertView;
        }

    }
}
