package omf.v2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "OhMyFood.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "my_food";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "item_title";
    private static final String COLUMN_QUANTITY = "item_quantity";
    private static final String COLUMN_UNIT = "item_unit";
    private static final String COLUMN_EXPDATE = "item_exp_date";


    MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_TITLE + " TEXT, " +
                        COLUMN_QUANTITY + " REAL, " +
                        COLUMN_UNIT + " TEXT, " +
                        COLUMN_EXPDATE + " TEXT);" ;
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    void addItem(String title, double quantity, String itemUnit, String itemDate){
        SQLiteDatabase db = this.getWritableDatabase();
        //store all data in database
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_QUANTITY, quantity);
        cv.put(COLUMN_UNIT, itemUnit);
        cv.put(COLUMN_EXPDATE, itemDate);
        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1){
            Toast.makeText(context, "Failed adding item", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Item added", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllItemData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateData(String row_id, String title, String quantity, String unit, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_QUANTITY, quantity);
        cv.put(COLUMN_UNIT, unit);
        cv.put(COLUMN_EXPDATE, date);

        long result =  db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if (result == -1 ){
            Toast.makeText(context, "Opss! Failed to update", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Item updated", Toast.LENGTH_SHORT).show();
        }

    }

    void delData(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();

        long result =  db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if (result == -1 ){
            Toast.makeText(context, "Opss! Failed to delete", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Item delete", Toast.LENGTH_SHORT).show();
        }

    }
}
