package fukie.afterall;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.provider.MediaStore;
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
    DBHelper dbHelper = new DBHelper(this);
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
                //To show current date in the datepicker
                Calendar mcurrentDate = Calendar.getInstance();
               int mYear=mcurrentDate.get(Calendar.YEAR);
               int mMonth=mcurrentDate.get(Calendar.MONTH);
              int  mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker=new DatePickerDialog(AddEvent.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                    }
                },mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();  }
        });
    }

    public void submitAddEvent(View target) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        if(txtName.getText().toString().length() > 0
                && txtDate.getText().toString().length() >0
                && txtType.getText().toString().length() >0)
        dbHelper.insertContact(txtName.getText().toString(),
                txtType.getText().toString(),
                txtDate.getText().toString(),
                chckLoop.isChecked(),
                "100days");
        Intent intent = new Intent(AddEvent.this, MainActivity.class);
        startActivity(intent);
    }
}
