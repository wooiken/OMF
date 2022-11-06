package omf.v2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

public class AddItem extends AppCompatActivity {

    EditText quantity_input, exp_date_input;
    DatePickerDialog.OnDateSetListener setListener;
    AutoCompleteTextView title_input, autoCompleteUnit;
    String [] quantityUnit = {"gram", "kg", "ml", "litre", "pack(s)", "can(s)", "bottle(s)", "box(s)",
            "carton", "serving(s)", "pot(s)"};

    String [] item_title = {"Apple", "Almond", "Avocado", "Abalone", "Almond milk","Apricot",
                            "Almond flake", "Aloe ver", "Ale", "Ash gourd", "Agar-agar", "Azuki beans",
                            "Almond oil", "Acai powder"} ;

    ArrayAdapter<String> adapterItem, adapterUnit;
    Button add_item_toDB_button , clear_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        title_input = findViewById(R.id.textEditTitle);
        //set autocomplete text for item titel
        adapterItem = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, item_title);
        title_input.setThreshold(1);
        title_input.setAdapter(adapterItem);

        quantity_input = findViewById(R.id.textEditQuantity);
        exp_date_input = findViewById(R.id.textEditDate);

        //set dropdown menu for quantity unit selection
        autoCompleteUnit = findViewById(R.id.autoCompleteUnit);
        adapterUnit = new ArrayAdapter<String>(this, R.layout.unit_list, quantityUnit);
        autoCompleteUnit.setAdapter(adapterUnit);

        //set calender picker for expiry date
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        exp_date_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddItem.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;

                        String date =  day+"/"+month+"/"+year;
                        exp_date_input.setText(date);
                    }
                }
                        ,year,month,day);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        //save food item data to database
        add_item_toDB_button = findViewById(R.id.addItemButton);
        add_item_toDB_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyDatabaseHelper myDB = new MyDatabaseHelper(AddItem.this);
                myDB.addItem(title_input.getText().toString().trim(),
                        Double.parseDouble(quantity_input.getText().toString().trim()),
                        autoCompleteUnit.getText().toString().trim(),
                        exp_date_input.getText().toString().trim());
                startActivity(new Intent(AddItem.this, MainActivity.class));

            }
        });

        clear_button = findViewById(R.id.clearButton);
        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title_input.getText().clear();
                quantity_input.getText().clear();
                exp_date_input.getText().clear();
            }
        });


    }
}