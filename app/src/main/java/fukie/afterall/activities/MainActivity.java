package fukie.afterall.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;


import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import fukie.afterall.utils.DatabaseProcess;
import fukie.afterall.utils.Events;
import fukie.afterall.R;
import fukie.afterall.items.DividerItemDecoration;
import fukie.afterall.items.RecyclerAdapter;
import fukie.afterall.items.RecyclerItemClickListener;

public class MainActivity extends AppCompatActivity {
    // TextView txtHello;
    DatabaseProcess databaseProcess;
    RecyclerView lstEvent;

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
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        lstEvent = (RecyclerView) findViewById(R.id.lstEvent);

        databaseProcess = new DatabaseProcess(context);

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
        final RecyclerAdapter recyclerAdapter =
                new RecyclerAdapter(this, rearrangeList(listViewItems));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        lstEvent.setLayoutManager(mLayoutManager);
        lstEvent.setItemAnimator(new DefaultItemAnimator());
        lstEvent.addItemDecoration(
                new DividerItemDecoration(ContextCompat.getDrawable(this, R.drawable.divider)));
        lstEvent.setAdapter(recyclerAdapter);
        lstEvent.addOnItemTouchListener(
                new RecyclerItemClickListener(context
                        , new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ((FoldingCell) view).toggle(false);
                        recyclerAdapter.registerToggle(position);
                    }
                })
        );

    }


    public void addEvent(View target) {
        Intent intent = new Intent(MainActivity.this, AddingEventActivity.class);
        startActivity(intent);
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
            Toast.makeText(context, "Unable to determine current app version from pacakge manager."
                    + " Defenisvely assuming normal app start.", Toast.LENGTH_SHORT).show();
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

    private List<Events> rearrangeList(List<Events> listViewItems) {
        int countNegative = 0;
        int countZero = 0;
        for (int i = 0; i < listViewItems.size() - 1; i++) {
            for (int j = i + 1; j < listViewItems.size(); j++) {
                Events e1 = listViewItems.get(i);
                Events e2 = listViewItems.get(j);
                if (e1.getDiff() < e2.getDiff()) {
                    Collections.swap(listViewItems, i, j);
                }
            }
        }
        for(int i = 0; i< listViewItems.size(); i++){
            if (listViewItems.get(i).getDiff() < 0)
                countNegative++;
            if (listViewItems.get(i).getDiff() == 0)
                countZero++;
        }
        List<Events> items = new ArrayList<>(listViewItems.size());
        for (int i = countNegative; i < countNegative + countZero; i++) {
            items.add(listViewItems.get(i));
        }
        for (int i = countNegative - 1; i > -1; i--) {
            items.add(listViewItems.get(i));
        }
        for (int i = countNegative + countZero; i < listViewItems.size(); i++) {
            items.add(listViewItems.get(i));
        }
        return items;
    }
}
