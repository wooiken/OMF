package omf.v2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //create drawer menu
    DrawerLayout drawerLayout;
    NavigationView nav_menu;
    ActionBarDrawerToggle drawerToggle;

    //create recycler view for item that added by user
    RecyclerView recyclerView;
    ImageView empty_image;
    TextView no_data;

    //floating button
    FloatingActionButton add_button, add_food_button, add_note_button;
    Animation fabOpen, fabClose, rotateOpen, rotateClose;
    boolean isOpen = false;

    //displaying data from SQLite database
    MyDatabaseHelper myDB;
    ArrayList<String>  item_id, item_title, item_quantity, item_unit, item_date;
    CustomAdapater customAdapater;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //give object for each resource file
        drawerLayout = findViewById(R.id.drawer_layout);
        nav_menu = findViewById(R.id.navigation_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.menu_Open, R.string.menu_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nav_menu.setNavigationItemSelectedListener(this);

        recyclerView = findViewById(R.id.item_recyclerView);
        empty_image = findViewById(R.id.emptyImage);
        no_data = findViewById(R.id.noData_txt);

        add_button = (FloatingActionButton) findViewById(R.id.add_item_button);
        add_note_button = (FloatingActionButton) findViewById(R.id.add_note_button);
        add_food_button = (FloatingActionButton) findViewById(R.id.add_food_button);

        //set floating button with rotate animation
        fabOpen = AnimationUtils.loadAnimation(this, R.anim.from_btm_anim);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.to_btm_anim);
        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);

        //set click listener for main floating button
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFeb();
            }
        });

        add_food_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFeb();
                Intent intent = new Intent(MainActivity.this, AddItem.class);
                startActivity(intent);
            }
        });

        add_note_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                animateFeb();
                Intent intent = new Intent(MainActivity.this, addNote.class);
                startActivity(intent);
            }
        });

        myDB = new MyDatabaseHelper(MainActivity.this);
        item_id = new ArrayList<>();
        item_title = new ArrayList<>();
        item_quantity = new ArrayList<>();
        item_unit = new ArrayList<>();
        item_date = new ArrayList<>();

        storeDataInArrays();

        customAdapater = new CustomAdapater(MainActivity.this, this,
                item_id, item_title, item_quantity, item_unit, item_date);
        recyclerView.setAdapter(customAdapater);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    void storeDataInArrays(){
        Cursor cursor = myDB.readAllItemData();
        if (cursor.getCount() == 0 ){
            empty_image.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        } else {
            while (cursor.moveToNext()){
                item_id.add(cursor.getString(0));
                item_title.add(cursor.getString(1));
                item_quantity.add(cursor.getString(2));
                item_unit.add(cursor.getString(3));
                item_date.add(cursor.getString(4));
            }
            empty_image.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_home:
                recreate();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menu_search:
                Intent intent = new Intent(MainActivity.this, addNote.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.menu_note:
                Intent intent2 = new Intent(MainActivity.this, NoteActivity.class);
                startActivity(intent2);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;

        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    //function for floating action button
    private void animateFeb(){
        if (isOpen){
            add_button.startAnimation(rotateClose);
            add_food_button.startAnimation(fabClose);
            add_note_button.startAnimation(fabClose);
            add_food_button.setClickable(false);
            add_note_button.setClickable(false);
            isOpen = false;
        } else {
            add_button.startAnimation(rotateOpen);
            add_food_button.startAnimation(fabOpen);
            add_note_button.startAnimation(fabOpen);
            add_food_button.setClickable(true);
            add_note_button.setClickable(true);
            isOpen = true;
        }
    }

}