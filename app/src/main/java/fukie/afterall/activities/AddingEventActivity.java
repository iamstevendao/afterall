package fukie.afterall.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import fukie.afterall.DatabaseProcess;
import fukie.afterall.R;
import fukie.afterall.items.SpinnerAdapter;

public class AddingEventActivity extends AppCompatActivity {
    DatabaseProcess databaseProcess;
    EditText txtName;
    EditText txtDate;
    Spinner spinner;
    RadioGroup rdMemory;
    RadioButton rd100;
    RadioButton rdYear;
    RadioButton rdMonth;
    CheckBox chckLoop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        databaseProcess = new DatabaseProcess(MainActivity.context);
        txtName = (EditText) findViewById(R.id.txtName);
        txtDate = (EditText) findViewById(R.id.txtDate);
        spinner = (Spinner) findViewById(R.id.spinnerKind);
        rdMemory = (RadioGroup) findViewById(R.id.rdMemory);
        rd100 = (RadioButton) findViewById(R.id.rd100);
        rdYear = (RadioButton) findViewById(R.id.rdYear);
        rdMonth = (RadioButton) findViewById(R.id.rdMonth);
        chckLoop = (CheckBox) findViewById(R.id.chckLoop);

        Cursor cur = databaseProcess.query("select * from kind order by kind_id");
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(MainActivity.context, cur);
        spinner.setAdapter(spinnerAdapter);

        txtDate.setOnClickListener(new View.OnClickListener() {

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
                        txtDate.setText(sdf.format(newDate.getTime()));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });
    }

    public void submitAddEvent(View target) throws Exception {
        if (txtName.getText().toString().length() > 0
                && txtDate.getText().toString().length() > 0) {
            int loop = 0;
            if (chckLoop.isChecked()) {
                loop = 1;
            }
            databaseProcess.insertEvent(txtName.getText().toString(),
                    spinner.getSelectedItemPosition() + 1,
                    txtDate.getText().toString(),
                    loop,
                    0,
                    2);
            Intent intent = new Intent(AddingEventActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
