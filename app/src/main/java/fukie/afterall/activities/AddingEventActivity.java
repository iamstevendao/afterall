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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

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
    int currentImage;
    LinearLayout categoryContainer;
    LinearLayout mainContainer;

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
        categoryContainer = (LinearLayout) findViewById(R.id.add_category_container);
        mainContainer = (LinearLayout) findViewById(R.id.add_main_container);

        final Rect displayRectangle = new Rect();
        Window window = AddingEventActivity.this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        Cursor cur = databaseProcess.query("select * from kind order by kind_id");
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(MainActivity.context, cur);
        spinnerCategory.setAdapter(spinnerAdapter);
        Random random = new Random();
        currentImage = random.nextInt(Constants.background.length);
        imageBackground.setImageResource(Constants.background[currentImage]);
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        textDate.setText(sdf.format(today));
        categoryContainer.setBackgroundColor(ContextCompat.getColor(
                context, Constants.eventColor[spinnerCategory.getSelectedItemPosition()]));

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView
                    , int position, long id) {
                categoryContainer.setBackgroundColor(ContextCompat.getColor(
                        context, Constants.eventColor[spinnerCategory.getSelectedItemPosition()]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        textName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupWindow mpopup;
                final View popUpView = getLayoutInflater()
                        .inflate(R.layout.add_name_layout, null, false);

                mpopup = new PopupWindow(popUpView,
                        (int) (displayRectangle.width() * 0.7f)
                        , (int) (displayRectangle.height() * 0.4f)
                        , true);
                mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
                mpopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0);
                final EditText editText = (EditText) popUpView.findViewById(R.id.add_name_edit);
                Button cancel = (Button) popUpView.findViewById(R.id.add_name_close);
                cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // to dismiss popup();
                        mainContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
                        mpopup.dismiss();

                    }
                });
                Button submit = (Button) popUpView.findViewById(R.id.add_name_submit);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        textName.setText(editText.getText());
                        mainContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
                        mpopup.dismiss();
                    }
                });
                mpopup.setOnDismissListener(new PopupWindow.OnDismissListener() {

                    @Override
                    public void onDismiss() {
                        mainContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
                        mpopup.dismiss();
                    }
                });
                mainContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.dim_color));
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

                mpopup = new PopupWindow(popUpView
                        , (int) (displayRectangle.width() * 0.9f)
                        , (int) (displayRectangle.height() * 0.7f)
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
                mainContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.dim_color));

                recyclerView.addOnItemTouchListener(
                        new RecyclerItemClickListener(context
                                , new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                imageBackground.setImageResource(Constants.background[position]);
                                currentImage = position;
                                mainContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
                                mpopup.dismiss();
                            }
                        })
                );
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
                    currentImage);
            Intent intent = new Intent(AddingEventActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void cancelAddEvent(View target) {
        Intent intent = new Intent(AddingEventActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
