package fukie.afterall;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEvent extends AppCompatActivity {
    DatabaseProcess databaseProcess;
    EditText txtName;
    EditText txtDate;
    EditText txtType;
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
        txtType = (EditText) findViewById(R.id.txtType);
        rdMemory = (RadioGroup) findViewById(R.id.rdMemory);
        rd100 = (RadioButton) findViewById(R.id.rd100);
        rdYear = (RadioButton) findViewById(R.id.rdYear);
        rdMonth = (RadioButton) findViewById(R.id.rdMonth);
        chckLoop = (CheckBox) findViewById(R.id.chckLoop);

        txtDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mCurrentDate = Calendar.getInstance();
                int mYear = mCurrentDate.get(Calendar.YEAR);
                int mMonth = mCurrentDate.get(Calendar.MONTH);
                int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(AddEvent.this
                        , new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedYear
                            , int selectedMonth, int selectedDay) {
                        // TODO Auto-generated method stub
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"
                                , Locale.getDefault());
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(selectedYear, selectedMonth, selectedMonth);
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
                && txtType.getText().toString().length() > 0
                && txtDate.getText().toString().length() > 0) {
            int loop = 0;
            if (chckLoop.isChecked()) {
                loop = 1;
            }
            databaseProcess.insertEvent(txtName.getText().toString(),
                    1,
                    txtDate.getText().toString(),
                    loop,
                    "100days");
            Intent intent = new Intent(AddEvent.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
