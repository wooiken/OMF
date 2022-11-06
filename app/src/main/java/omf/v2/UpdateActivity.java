package omf.v2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity {

    EditText title_input, quantity_input, unit_input, date_input;
    Button update_button, del_button;
    AutoCompleteTextView autoCompleteUnit2;
    String [] quantityUnit = {"gram", "kg", "ml", "litre", "pack(s)", "can(s)", "bottle(s)", "box(s)",
            "carton", "serving(s)", "pot(s)"};
    ArrayAdapter<String> adapterUnit2;
    String id, title, quantity, unit, date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        title_input = findViewById(R.id.textEditTitle2);
        quantity_input = findViewById(R.id.textEditQuantity2);
        unit_input = findViewById(R.id.autoCompleteUnit2);
        date_input = findViewById(R.id.textEditDate2);
        update_button = findViewById(R.id.updateItemButton);
        del_button = findViewById((R.id.deleteButton));

        //call this method for display existing data and update new data
        getAndSetIntentData();

        autoCompleteUnit2 = findViewById(R.id.autoCompleteUnit2);
        adapterUnit2 = new ArrayAdapter<String>(this, R.layout.unit_list, quantityUnit);
        autoCompleteUnit2.setAdapter(adapterUnit2);

        //set calender picker for expiry date
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        date_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        UpdateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;

                        String date =  day+"/"+month+"/"+year;
                        date_input.setText(date);
                    }
                }
                        ,year,month,day);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        ActionBar ab = getSupportActionBar();
        if (ab !=null){
            ab.setTitle(title);
        }


        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                title = title_input.getText().toString().trim();
                quantity = quantity_input.getText().toString().trim();
                unit = unit_input.getText().toString().trim();
                date = date_input.getText().toString().trim();
                myDB.updateData(id, title, quantity, unit, date);
                finish();
            }
        });

        del_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });

    }

    void getAndSetIntentData(){
        if (getIntent().hasExtra("id") && getIntent().hasExtra("title") && getIntent().hasExtra("quantity") &&
                getIntent().hasExtra("unit") && getIntent().hasExtra("date")){

            //getting data from intent
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            quantity = getIntent().getStringExtra("quantity");
            unit = getIntent().getStringExtra("unit");
            date = getIntent().getStringExtra("date");

            //setting intent data
            title_input.setText(title);
            quantity_input.setText(quantity);
            unit_input.setText(unit);
            date_input.setText(date);

        } else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + title + " ?");
        builder.setMessage("Are you sure you want to delete " + title + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                myDB.delData(id);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.create().show();
    }
}