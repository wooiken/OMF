package omf.v2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class NoteActivity extends AppCompatActivity {

    MyNoteAdapter myNoteAdapter;
    RecyclerView displayNoteRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Realm.init(Realm.getApplicationContext());
        Realm realm = Realm.getDefaultInstance();

        RealmResults<Note> notesList = realm.where
                (Note.class).findAll().sort("createdTime", Sort.DESCENDING);

        displayNoteRV = findViewById(R.id.displayNoteRV);
        displayNoteRV.setLayoutManager(new LinearLayoutManager
                (NoteActivity.this,RecyclerView.VERTICAL,false));
        MyNoteAdapter myNoteAdapter = new MyNoteAdapter(getApplicationContext(), notesList);
        displayNoteRV.setAdapter(myNoteAdapter);

        notesList.addChangeListener(new RealmChangeListener<RealmResults<Note>>() {
            @Override
            public void onChange(RealmResults<Note> notes) {
                myNoteAdapter.notifyDataSetChanged();
            }
        });

    }
}