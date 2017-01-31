package rupik.com.bengali_calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;
import android.view.LayoutInflater;
import android.widget.TimePicker;

/**
 * Created by macmin5 on 08/08/16.
 */
public class CustomAdapter extends BaseAdapter {

    ArrayList<HashMap> mMonthItems;
    Set <String>mKeys;
    private static LayoutInflater inflater=null;
    Context context;

    TimePickerDialog timePickerDialog;

    String reminderDate;
    String occasionName;

    public CustomAdapter(ArrayList<HashMap> monthList, Context cntxt)
    {
        this.context = cntxt;
        mMonthItems = monthList;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        return mMonthItems.size();
    }

    @Override
    public Object getItem(int position) {
        HashMap dayDetails = mMonthItems.get(position);

        String date = (String) dayDetails.get("date");

        return date;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView dateTv;
        TextView engDateTv;
        TextView occasionTV;
        TextView timeTV;
        TextView dayTV;
        ImageView mrgImageView;
        LinearLayout eventLayout;
        LinearLayout eventDetailsLayout;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.date_cell, null);

        holder.dateTv=(TextView) rowView.findViewById(R.id.dateTV);
        holder.engDateTv=(TextView) rowView.findViewById(R.id.engDateTV);
        holder.occasionTV=(TextView) rowView.findViewById(R.id.occasionTV);
        holder.timeTV=(TextView) rowView.findViewById(R.id.timeTV);
        holder.dayTV=(TextView) rowView.findViewById(R.id.dayTV);
        holder.mrgImageView = (ImageView) rowView.findViewById(R.id.mrgImageIV);
        holder.eventLayout = (LinearLayout)rowView.findViewById(R.id.eventLayout);
        holder.eventDetailsLayout = (LinearLayout)rowView.findViewById(R.id.eventDetailsLayout);

        final HashMap dayDetails = mMonthItems.get(position);

        String day = (String)dayDetails.get("engDay");
        String redMArk = (String)dayDetails.get("redMark");
        if(redMArk == null) redMArk = "0";
        if(day.toLowerCase().startsWith("sunday"))
        {
            holder.dateTv.setTextColor(Color.parseColor("#592c01"));
        }
        else  if(redMArk.contains("1"))
        {
            holder.dateTv.setTextColor(Color.parseColor("#592c01"));
        }
        else {
            holder.dateTv.setTextColor(Color.parseColor("#01280f"));
        }

        holder.dateTv.setText((String)dayDetails.get("date"));
        holder.engDateTv.setText((String)dayDetails.get("engDate"));
        String occnText = (String)dayDetails.get("occasion");
        holder.occasionTV.setText(occnText);

        if(occnText.contains("Subho Bibaho Din"))
        {
            holder.eventLayout.setVisibility(View.GONE);
        }
        else {
            holder.eventLayout.setVisibility(View.VISIBLE);
        }

        if(redMArk.contains("1"))
        {
            holder.occasionTV.setTextColor(Color.parseColor("#592c01"));
        }
        else {
            holder.occasionTV.setTextColor(Color.parseColor("#01280f"));
        }
        String timeStr = (String)dayDetails.get("time");
        if(timeStr == null)
        {
            holder.eventDetailsLayout.setVisibility(View.GONE);
        }
        else {
            holder.eventDetailsLayout.setVisibility(View.VISIBLE);
            timeStr = "Subho Bibaho Muhurto : " + timeStr;
            holder.timeTV.setText(timeStr);
        }

        holder.dayTV.setText(day);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                HashMap <String, String> pokeDetails = (HashMap<String, String>) newResults.get(position);
////                Toast.makeText(context, "You Clicked "+pokeDetails.get("name"), Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(context, PokeDetailsActivity.class);
//                intent.putExtra("pokeDetailsData", pokeDetails);
//                context.startActivity(intent);
            }
        });
        return rowView;
    }

}
