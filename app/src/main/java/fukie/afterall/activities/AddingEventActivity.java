package fukie.afterall.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import fukie.afterall.items.DividerItemDecoration;
import fukie.afterall.items.ImageAdapter;
import fukie.afterall.items.RecyclerAdapter;
import fukie.afterall.items.RecyclerItemClickListener;
import fukie.afterall.utils.Constants;
import fukie.afterall.utils.DatabaseProcess;
import fukie.afterall.R;
import fukie.afterall.items.SpinnerAdapter;

public class AddingEventActivity extends AppCompatActivity {
    DatabaseProcess databaseProcess;
    TextView textName;
    TextView textDate;
    ToggleButton toggleButton;
    TextView textCategory;
    Spinner spinnerCategory;
    ImageView imageBackground;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        context = getApplication().getApplicationContext();
        databaseProcess = new DatabaseProcess(MainActivity.context);
        textName = (TextView) findViewById(R.id.add_name);
        textDate = (TextView) findViewById(R.id.add_date);
        textCategory = (TextView) findViewById(R.id.add_text_category);
        spinnerCategory = (Spinner) findViewById(R.id.add_spinner_category);
        toggleButton = (ToggleButton) findViewById(R.id.add_toggle);
        imageBackground = (ImageView) findViewById(R.id.add_background);

        Cursor cur = databaseProcess.query("select * from kind order by kind_id");
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(MainActivity.context, cur);
        spinnerCategory.setAdapter(spinnerAdapter);

        textName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupWindow mpopup;
                final View popUpView = getLayoutInflater()
                        .inflate(R.layout.add_name_layout, null,false);

                mpopup = new PopupWindow(popUpView, 400, 500, true);
                mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
                mpopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);

                Button cancel=(Button)popUpView.findViewById(R.id.close1);
                cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // to dismiss popup();
                        mpopup.dismiss();

                    }
                });
            }
        });

        textDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mCurrentDate = Calendar.getInstance();
                int mYear = mCurrentDate.get(Calendar.YEAR);
                int mMonth = mCurrentDate.get(Calendar.MONTH);
                int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(AddingEventActivity.this
                        , new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedYear
                            , int selectedMonth, int selectedDay) {
                        // TODO Auto-generated method stub
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"
                                , Locale.getDefault());
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(selectedYear, selectedMonth, selectedDay);
                        textDate.setText(sdf.format(newDate.getTime()));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });

        imageBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupWindow mpopup;
                final View popUpView = getLayoutInflater()
                        .inflate(R.layout.add_background_layout, null, false);
                Rect displayRectangle = new Rect();
                Window window = AddingEventActivity.this.getWindow();
                window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
                mpopup = new PopupWindow(popUpView
                        , (int)(displayRectangle.width() * 0.9f)
                        , (int)(displayRectangle.height() * 0.7f)
                        , true);

                mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
                mpopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);

                RecyclerView recyclerView = (RecyclerView) popUpView.findViewById(
                        R.id.grid_view);
                ImageAdapter recyclerAdapter = new ImageAdapter(context);
                recyclerView.addItemDecoration(new
                        DividerItemDecoration(ContextCompat.getDrawable(context, R.drawable.divider)));
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                recyclerView.setAdapter(recyclerAdapter);

//                re.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    public void onItemClick(AdapterView<?> parent, View v,
//                                            int position, long id) {
//                        Toast.makeText(AddingEventActivity.this, "" + position,
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });
    }

    public void submitAddEvent(View target) throws Exception {
        if (textName.getText().toString().length() > 0
                && textDate.getText().toString().length() > 0) {
            int loop = 0;
            if (toggleButton.isChecked()) {
                loop = 1;
            }
            databaseProcess.insertEvent(textName.getText().toString(),
                    spinnerCategory.getSelectedItemPosition() + 1,
                    textDate.getText().toString(),
                    loop,
                    2);
            Intent intent = new Intent(AddingEventActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
