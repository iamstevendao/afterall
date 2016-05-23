package fukie.afterall;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.txtHello);
        Button bttnAddEvent = (Button) findViewById(R.id.bttnAddEvent);
        DBHelper dbHelper = new DBHelper(this);
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
        textView.setText(s);
    }

    public void addEvent(View target) {
        Intent intent = new Intent(MainActivity.this, AddEvent.class);
        startActivity(intent);
    }
}
