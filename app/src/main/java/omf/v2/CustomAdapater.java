package omf.v2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.text.Transliterator;
import android.os.Build;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class CustomAdapater extends RecyclerView.Adapter<CustomAdapater.MyViewHolder> {

    private Context context;
    private ArrayList item_id, item_title, item_quantity, item_unit, item_date;
    Activity activity;


    CustomAdapater(Activity activity, Context context, ArrayList item_id, ArrayList item_title,
                   ArrayList item_quantity, ArrayList item_unit, ArrayList item_date){
        this.activity = activity;
        this.item_id = item_id;
        this.context = context;
        this.item_title = item_title;
        this.item_quantity = item_quantity;
        this.item_unit = item_unit;
        this.item_date = item_date;

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_row, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,final int position) {
        holder.item_title_txt.setText(String.valueOf(item_title.get(position)));
        holder.item_quantity_txt.setText(String.valueOf(item_quantity.get(position)));
        holder.item_unit_txt.setText(String.valueOf(item_unit.get(position)));
        holder.item_date_txt.setText(String.valueOf(item_date.get(position)).concat(" EXP"));

        //calculate countdown day for expiring food
        Calendar today_date = Calendar.getInstance();
        today_date.set(Calendar.DAY_OF_MONTH,0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String mtoday = dateFormat.format(Calendar.getInstance().getTime());

        String dateStr = holder.item_date_txt.getText().toString();

        try {
            Date date1 = dateFormat.parse(dateStr);
            Date date2 = dateFormat.parse(mtoday);

            long expDate = date1.getTime();
            long currentDate = date2.getTime();

            Period period = new Period(currentDate,expDate, PeriodType.yearMonthDay());
            int days = period.getDays();

            if (days <= 0){
                holder.countDownDay.setText("EXPIRED");
                holder.countDownDay.setTextColor(Color.RED);
                holder.countDownDay.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else if (days <= 1){
                holder.countDownDay.setText("EXPIRED IN: "  + days + " day");
                holder.countDownDay.setTextColor(Color.RED);
                holder.countDownDay.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else if (days <= 5){
                holder.countDownDay.setText("EXPIRED IN: "  + days + " days");
                holder.countDownDay.setTextColor(Color.RED);
                holder.countDownDay.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
             else {
                holder.countDownDay.setText("Expired in: "  + days + " days");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.mainLayout.setOnClickListener( view ->  {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(item_id.get(position)));
                intent.putExtra("title", String.valueOf(item_title.get(position)));
                intent.putExtra("quantity", String.valueOf(item_quantity.get(position)));
                intent.putExtra("unit", String.valueOf(item_unit.get(position)));
                intent.putExtra("date", String.valueOf(item_date.get(position)));
                activity.startActivityForResult(intent,1);
        });

    }

    @Override
    public int getItemCount() {

        return item_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView item_title_txt;
        TextView item_quantity_txt;
        TextView item_unit_txt;
        TextView item_date_txt;
        TextView countDownDay;
        LinearLayout mainLayout;

        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.N)
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_title_txt = itemView.findViewById(R.id.item_title_txt);
            item_quantity_txt = itemView.findViewById(R.id.item_quantity_txt);
            item_unit_txt = itemView.findViewById(R.id.item_unit_txt);
            item_date_txt = itemView.findViewById(R.id.item_date_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            countDownDay = itemView.findViewById(R.id.countDownDay);





        }
    }

}
