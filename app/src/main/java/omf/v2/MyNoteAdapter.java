package omf.v2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyNoteAdapter extends RecyclerView.Adapter<MyNoteAdapter.MyViewHolder>{

    Context context;
    RealmResults<Note> notesList;
    Activity activity;

    public MyNoteAdapter(Context context, RealmResults<Note> notesList) {
        this.context = context;
        this.notesList = notesList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder
                (LayoutInflater.from(context).inflate(R.layout.note_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Note note = notesList.get(position);
        holder.noteTitleOutput.setText(note.getTitle());
        holder.noteDespOutput.setText(note.getDescription());

        String formatedTime = DateFormat.getDateTimeInstance().format(note.createdTime);
        holder.noteTimeOutput.setText(formatedTime);

        //long click the delete item
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                PopupMenu menu = new PopupMenu(context,v);
                menu.getMenu().add("DELETE");
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        note.deleteFromRealm();
                        realm.commitTransaction();
                        Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show();

                        return true;
                    }
                });
                menu.show();

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView noteTitleOutput, noteDespOutput, noteTimeOutput;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            noteTitleOutput = itemView.findViewById(R.id.noteTitleOutput);
            noteDespOutput = itemView.findViewById(R.id.noteDespOutput);
            noteTimeOutput = itemView.findViewById(R.id.noteTimeOutput);



        }
    }


}
