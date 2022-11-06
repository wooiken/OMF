package omf.v2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import io.realm.Realm;

public class addNote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        EditText noteTitleInput = findViewById(R.id.titleinputNote);
        EditText noteDescpInput = findViewById(R.id.descpinputNote);
        MaterialButton saveNoteBtn = findViewById(R.id.saveNoteBtn);

        //set Realm database
        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();

        saveNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = noteTitleInput.getText().toString();
                String description = noteDescpInput.getText().toString();
                long createdTime = System.currentTimeMillis();

                realm.beginTransaction();
                Note note = realm.createObject(Note.class);
                note.setTitle(title);
                note.setDescription(description);
                note.setCreatedTime(createdTime);
                realm.commitTransaction();
                Toast.makeText(addNote.this, "Note saved", Toast.LENGTH_SHORT).show();
                finish();

            }
        });

    }
}