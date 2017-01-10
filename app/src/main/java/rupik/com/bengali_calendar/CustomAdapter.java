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
        ImageButton reminderButton;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.cal_listview, null);



        holder.dateTv=(TextView) rowView.findViewById(R.id.dateTV);
        holder.engDateTv=(TextView) rowView.findViewById(R.id.engDateTV);
        holder.occasionTV=(TextView) rowView.findViewById(R.id.occasionTV);
        holder.timeTV=(TextView) rowView.findViewById(R.id.timeTV);
        holder.dayTV=(TextView) rowView.findViewById(R.id.dayTV);
        holder.mrgImageView = (ImageView) rowView.findViewById(R.id.mrgImageIV);
        holder.reminderButton = (ImageButton) rowView.findViewById(R.id.reminderButton);

        final HashMap dayDetails = mMonthItems.get(position);

        String day = (String)dayDetails.get("engDay");
        String redMArk = (String)dayDetails.get("redMark");
        if(redMArk == null) redMArk = "0";
        if(day.toLowerCase().startsWith("sunday"))
        {
            holder.dateTv.setTextColor(Color.parseColor("#ff0000"));
        }
        else  if(redMArk.contains("1"))
        {
            holder.dateTv.setTextColor(Color.parseColor("#800000"));
        }
        else {
            holder.dateTv.setTextColor(Color.parseColor("#33334d"));
        }

        holder.dateTv.setText((String)dayDetails.get("date"));
        holder.engDateTv.setText((String)dayDetails.get("engDate"));
        String occnText = (String)dayDetails.get("occasion");
        holder.occasionTV.setText(occnText);

        if(redMArk.contains("1"))
        {
            holder.occasionTV.setTextColor(Color.parseColor("#800000"));
        }
        else {
            holder.occasionTV.setTextColor(Color.parseColor("#33334d"));
        }
        String timeStr = (String)dayDetails.get("time");
        if(timeStr == null)
        {
            holder.timeTV.setVisibility(View.GONE);
            holder.mrgImageView.setVisibility(View.GONE);
        }
        else {
            timeStr = "Subho Bibaho Muhurto : " + timeStr;
            holder.mrgImageView.setVisibility(View.VISIBLE);
            holder.timeTV.setVisibility(View.VISIBLE);
            holder.timeTV.setText(timeStr);
        }

        holder.dayTV.setText(day);

        holder.reminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();

                reminderDate = (String)dayDetails.get("engDay");
                occasionName = (String)dayDetails.get("occasion");

                timePickerDialog = new TimePickerDialog(context, onTimeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });



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

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();
            calSet.set(Calendar.HOUR_OF_DAY, i);
            calSet.set(Calendar.MINUTE, i1);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);
            if(calSet.compareTo(calNow) <= 0){
                calSet.add(Calendar.DATE, 1);
            }
            setAlarm(calSet);
        }
    };

    private void setAlarm(Calendar targetCal){

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("engDate", reminderDate);
        intent.putExtra("occasionName", occasionName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
    }
}
