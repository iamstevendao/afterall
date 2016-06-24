package fukie.afterall.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
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
                final View popUpView = getLayoutInflater().inflate(R.layout.about, null,false);

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
