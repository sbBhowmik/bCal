package rupik.com.bengali_calendar;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appodeal.ads.Appodeal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    static  int clickCount = 0;

    HashMap <String,ArrayList<HashMap>>monthMap = new HashMap<String, ArrayList<HashMap>>();

    CustomAdapter adapter;
    HashMap <String,String>monthNames;
    int bengaliCurrentMonth;
    int currentDisplayedMonth;
    boolean oldDataDisplayed = true;

    int currentDisplayedMonthIndex = 0;

    ArrayList<MonthInfo> monthsDetails;


    @Override
    public void onResume() {
        super.onResume();
        Appodeal.onResume(this, Appodeal.BANNER);
        Appodeal.show(this, Appodeal.BANNER_BOTTOM);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String appKey = "1eb7a7faa8271da4f94b711cad962f864a64615a68b2027c";
        Appodeal.initialize(this, appKey, Appodeal.INTERSTITIAL | Appodeal.BANNER);

        Appodeal.show(this, Appodeal.BANNER_BOTTOM);

        this.fillData();
        populate1424Calendar();

        monthNames = new HashMap<String, String>();
        monthNames.put("1","Baisakh");
        monthNames.put("2","Jaistha");
        monthNames.put("3","Asharh");
        monthNames.put("4","Shrabon");
        monthNames.put("5","Bhadro");
        monthNames.put("6","Ashwin");
        monthNames.put("7","Kartik");
        monthNames.put("8","Agrahayan");
        monthNames.put("9","Poush");
        monthNames.put("10","Magh");
        monthNames.put("11","Falgun");
        monthNames.put("12","Choitro");

        this.findCurrentBengaliMonth();
        currentDisplayedMonth = bengaliCurrentMonth;
        //By default display the current month
        String monthName = this.keyOfBongMonth(bengaliCurrentMonth);
        this.displayDataOfBongMonth(monthName);


        Button prevMonthBtn = (Button) findViewById(R.id.previousMonthBtn);

        prevMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(clickCount == 5)
                {
                    Appodeal.show(MainActivity.this, Appodeal.INTERSTITIAL);
                    clickCount = 0;
                }
                else {
                    clickCount ++;
                }

                if(currentDisplayedMonth > 1 && oldDataDisplayed==true)
                {
                    currentDisplayedMonth --;
                    String monthName = keyOfBongMonth(currentDisplayedMonth);
                    displayDataOfBongMonth(monthName);
                }
                else {
                    if(currentDisplayedMonthIndex == 0)
                    {
                        currentDisplayedMonth --;
                        String monthName = keyOfBongMonth(currentDisplayedMonth);
                        displayDataOfBongMonth(monthName);
                    }
                    else {
                        if (currentDisplayedMonthIndex > 0){
                            currentDisplayedMonthIndex--;
                            displayCal();
                        }
                    }
                }
                MainActivity.this.checkButtonVisibility();
            }
        });

        final Button nextMonthBtn = (Button)findViewById(R.id.nextMonthBtn);
        assert nextMonthBtn != null;
        nextMonthBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(currentDisplayedMonth < 12 && oldDataDisplayed)
                {
                    if(clickCount == 5)
                    {
                        Appodeal.show(MainActivity.this, Appodeal.INTERSTITIAL);
                        clickCount = 0;
                    }
                    else {
                        clickCount ++;
                    }

                    currentDisplayedMonth ++;
                    String monthName = keyOfBongMonth(currentDisplayedMonth);
                    displayDataOfBongMonth(monthName);
                }
                else  {
                    if(currentDisplayedMonthIndex < monthsDetails.size()-1)
                    {
                        currentDisplayedMonthIndex++;
                        displayCal();
                    }
                }

                MainActivity.this.checkButtonVisibility();
//                Toast.makeText(getApplicationContext(), "Btn Clkd", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void displayCal()
    {
        MonthInfo monthInfo = monthsDetails.get(currentDisplayedMonthIndex);
        CustomAdapter adapter = new CustomAdapter(monthInfo, this);
        ListView lv = (ListView)findViewById(R.id.calListView);
        lv.setAdapter(adapter);
    }

    void checkButtonVisibility()
    {
        Button prevMonthBtn = (Button)findViewById(R.id.previousMonthBtn);
        Button nextMonthBtn = (Button)findViewById(R.id.nextMonthBtn);
        if(currentDisplayedMonth == 1)
        {
            prevMonthBtn.setVisibility(View.INVISIBLE);
        }
        else {
            prevMonthBtn.setVisibility(View.VISIBLE);
        }
        if(currentDisplayedMonth == 12)
        {
            oldDataDisplayed = false;
//            nextMonthBtn.setVisibility(View.INVISIBLE);
        }
        else {
            nextMonthBtn.setVisibility(View.VISIBLE);
        }
    }

    void displayDataOfBongMonth(String monthName){
        ListView lv = (ListView)findViewById(R.id.calListView);
//        ArrayList<HashMap> filteredArray = monthMap.get("Baisakh"); //test
        ArrayList<HashMap> filteredArray = monthMap.get(monthName);
        adapter = new CustomAdapter(filteredArray,this);
        lv.setAdapter(adapter);

        TextView monthTitleTV = (TextView) findViewById(R.id.monthNameTV);
        monthTitleTV.setText(monthName.toUpperCase());
        if(currentDisplayedMonth == bengaliCurrentMonth)
        {
            monthTitleTV.setTextColor(Color.parseColor("#00004d"));
        }
        else {
            monthTitleTV.setTextColor(Color.parseColor("#33334d"));
        }
    }

    String keyOfBongMonth(int monthNumber) {
        String key = Integer.toString(monthNumber);
        String currentBongMonthKey = monthNames.get(key);
        return currentBongMonthKey;
    }

    void findCurrentBengaliMonth()
    {
        //New Logic for 1424
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date strDate = sdf.parse("15/04/2017");
            if (new Date().after(strDate)) {
                //old Logic
//                oldDataDisplayed = true;
            } else {
                //new Logic
//                oldDataDisplayed = false;
            }
        }
        catch (ParseException e)
        {
            Toast.makeText(this,"ParseException !!!", Toast.LENGTH_LONG).show();
        }
        //


        //find current bengali month
        int currentYear = Integer.parseInt((String)  new SimpleDateFormat("yyyy").format(new Date()));
        int currentDate = Integer.parseInt((String)  new SimpleDateFormat("dd").format(new Date()));
        int currentMonth = Integer.parseInt( (String) new SimpleDateFormat("MM").format(new Date()));
        if(currentYear != 2016 || currentYear != 2017)
        {
            //logic fails
            Toast.makeText(this,"Logic Will Fail! Please update this app !!!", Toast.LENGTH_LONG);
        }
        if(currentMonth == 4)
        {
            if(currentDate >=14 )
            {
                bengaliCurrentMonth = 1;
            }
        }
        else if(currentMonth == 5)
        {
            if(currentDate <= 14)
            {
                bengaliCurrentMonth = 1;
            }
            else {
                bengaliCurrentMonth = 2;
            }
        }
        else if(currentMonth == 6)
        {
            if(currentDate <= 15)
            {
                bengaliCurrentMonth = 2;
            }
            else {
                bengaliCurrentMonth = 3;
            }
        }
        else if(currentMonth == 7){
            if(currentDate <= 16)
            {
                bengaliCurrentMonth = 3;
            }
            else {
                bengaliCurrentMonth = 4;
            }
        }
        else if(currentMonth == 8){
            if(currentDate <= 17)
            {
                bengaliCurrentMonth = 4;
            }
            else {
                bengaliCurrentMonth = 5;
            }
        }
        else if(currentMonth == 9){
            if(currentDate <= 17)
            {
                bengaliCurrentMonth = 5;
            }
            else {
                bengaliCurrentMonth = 6;
            }
        }
        else if(currentMonth == 10){
            if(currentDate <= 17)
            {
                bengaliCurrentMonth = 6;
            }
            else {
                bengaliCurrentMonth = 7;
            }
        }
        else if(currentMonth == 11){
            if(currentDate <= 16)
            {
                bengaliCurrentMonth = 7;
            }
            else {
                bengaliCurrentMonth = 8;
            }
        }
        else if(currentMonth == 12){
            if(currentDate <= 16)
            {
                bengaliCurrentMonth = 8;
            }
            else {
                bengaliCurrentMonth = 9;
            }
        }
        else if(currentMonth == 1){
            if(currentDate <= 14)
            {
                bengaliCurrentMonth = 9;
            }
            else {
                bengaliCurrentMonth = 10;
            }
        }
        else if(currentMonth == 2){
            if(currentDate <= 12)
            {
                bengaliCurrentMonth = 10;
            }
            else {
                bengaliCurrentMonth = 11;
            }
        }
        else if(currentMonth == 3){
            if(currentDate <= 14)
            {
                bengaliCurrentMonth = 11;
            }
            else {
                bengaliCurrentMonth = 12;
            }
        }
        else if(currentMonth == 4){
            if(currentDate <= 14)
            {
                bengaliCurrentMonth = 12;
            }
        }

    }

    void fillData()
    {

        HashMap <String, String>dayDetails = new HashMap<String, String>();

        ArrayList<HashMap> monthArrList = new ArrayList<HashMap>();


        //-----------------BAISAKH-------


        dayDetails.put("occasion","Nabo Barsho");

        dayDetails.put("engDate","(14-04-2016)");
        dayDetails.put("engDay","Thursday");
        dayDetails.put("url","https://en.wikipedia.org/wiki/Pohela_Boishakh");
        dayDetails.put("date","01");

        monthArrList.add(dayDetails);

        //------
        dayDetails = new HashMap<String, String>();

        dayDetails.put("occasion","Annapurna Puja");

        dayDetails.put("engDate","(14-04-2016)");
        dayDetails.put("engDay","Thursday");
        dayDetails.put("url","https://en.wikipedia.org/wiki/Annapoorna_devi");
        dayDetails.put("date","01");
        dayDetails.put("redMark","1");

        monthArrList.add(dayDetails);
        //--------

        dayDetails = new HashMap<String, String>();

        dayDetails.put("occasion","Bashanti Puja");

        dayDetails.put("engDate","(15-04-2016)");
        dayDetails.put("engDay","Wednesday");
        dayDetails.put("url","http://www.hindu-blog.com/2010/03/basanti-puja-2010-date-chaitra-durga.html");
        dayDetails.put("date","02");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);
//-----------------



//-----------------

        dayDetails = new HashMap<String, String>();

        dayDetails.put("occasion","Ekadashi");

        dayDetails.put("engDate","(17-04-2016)");
        dayDetails.put("engDay","Sunday");
        dayDetails.put("url","https://en.wikipedia.org/wiki/Ekadashi");
        dayDetails.put("date","04");
        monthArrList.add(dayDetails);
//-----------------

        dayDetails = new HashMap<String, String>();

        dayDetails.put("occasion","Mahabir Jayanti ");

        dayDetails.put("engDate","(19-04-2016)");
        dayDetails.put("engDay","Thursday");
        dayDetails.put("url","https://en.wikipedia.org/wiki/Mahavir_Jayanti");
        dayDetails.put("date","06");
        dayDetails.put("redMark","1");
        dayDetails.put("time","From 03:28 am to 05:15 am");
        monthArrList.add(dayDetails);
//-----------------

        dayDetails = new HashMap<String, String>();

        dayDetails.put("occasion","Purnima");

        dayDetails.put("engDate","(22-04-2016)");
        dayDetails.put("engDay","Friday");
        dayDetails.put("url","https://en.wikipedia.org/wiki/Purnima");
        dayDetails.put("date","09");
        dayDetails.put("time","From 07:34 pm to 08:46 pm, From 10:10 pm to 11:56 pm, From 03:16 am to 05:13 am");
        monthArrList.add(dayDetails);


        //-----------------

        dayDetails = new HashMap<String, String>();

        dayDetails.put("occasion","Subho Bibaho Din");

        dayDetails.put("engDate","(26-04-2016)");
        dayDetails.put("engDay","Tuesday");
        dayDetails.put("date","13");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 09:38 pm to 01:27 am, From 04:31 am to 05:10 am");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();

        dayDetails.put("occasion","Subho Bibaho Din");

        dayDetails.put("engDate","(26-04-2016)");
        dayDetails.put("engDay","Tuesday");
        dayDetails.put("date","13");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 09:38 pm to 01:27 am, From 04:31 am to 05:10 am");
        monthArrList.add(dayDetails);






        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");

        dayDetails.put("engDate","(28-04-2016)");
        dayDetails.put("engDay","Thursday");
        dayDetails.put("date","15");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 04:41 pm to 05:09 pm");
        monthArrList.add(dayDetails);


//-----------------

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");

        dayDetails.put("engDate","(29-04-2016)");
        dayDetails.put("engDay","Friday");
        dayDetails.put("date","16");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 07:07 pm to 08:46 pm, From 10:10 pm to 01:15 am, From 04:19 am to 05:08 am");
        monthArrList.add(dayDetails);


//-----------------

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");

        dayDetails.put("engDate","(30-04-2016)");
        dayDetails.put("engDay","Saturday");
        dayDetails.put("date","17");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 07:23 pm to 01:11 am");
        monthArrList.add(dayDetails);


//-----------------
//-----------------

        dayDetails = new HashMap<String, String>();

        dayDetails.put("occasion","May Day");

        dayDetails.put("engDate","(01-05-2016)");
        dayDetails.put("engDay","Sunday");
        dayDetails.put("url","https://en.wikipedia.org/wiki/May_Day");
        dayDetails.put("date","18");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);



//-----------------

        dayDetails = new HashMap<String, String>();

        dayDetails.put("occasion","Ekadashi ");

        dayDetails.put("engDate","(03-05-2016)");
        dayDetails.put("engDay","Thursday");
        dayDetails.put("url","https://en.wikipedia.org/wiki/Ekadashi");
        dayDetails.put("date","20");
        monthArrList.add(dayDetails);
//-----------------

        dayDetails = new HashMap<String, String>();

        dayDetails.put("occasion","Shoby-mi-raaz");

        dayDetails.put("engDate","(05-05-2016)");
        dayDetails.put("engDay","Thursday");
        dayDetails.put("url","https://en.wikipedia.org/wiki/Isra_and_Mi%27raj");
        dayDetails.put("date","22");
        monthArrList.add(dayDetails);
//-----------------


        dayDetails = new HashMap<String, String>();

        dayDetails.put("occasion","Amavashya");

        dayDetails.put("engDate","(06-05-2016)");
        dayDetails.put("engDay","Friday");
        dayDetails.put("url","https://en.wikipedia.org/wiki/Amavasya");
        dayDetails.put("date","23");
        monthArrList.add(dayDetails);
//-----------------

        dayDetails = new HashMap<String, String>();

        dayDetails.put("occasion","Rabindra Jayanti");

        dayDetails.put("engDate","(08-05-2016)");
        dayDetails.put("engDay","Sunday");
        dayDetails.put("url","https://en.wikipedia.org/wiki/Rabindra_Jayanti");
        dayDetails.put("date","25");
        dayDetails.put("redMark","1");
        dayDetails.put("time", "From 06:32 pm to 12:40 am, From 03:44 am to 05:02 am");
        monthArrList.add(dayDetails);
//-----------------

        dayDetails = new HashMap<String, String>();

        dayDetails.put("occasion","Akshaya Tritiya");

        dayDetails.put("engDate","(09-05-2016)");
        dayDetails.put("engDay","Monday");
        dayDetails.put("url","https://en.wikipedia.org/wiki/Akshaya_Tritiya");
        dayDetails.put("date","26");
        monthArrList.add(dayDetails);
//-----------------

        monthMap.put("Baisakh", monthArrList);
//-----------------

        monthArrList = new ArrayList<HashMap>();


        dayDetails = new HashMap<String, String>();

        dayDetails.put("occasion","Subho Bibaho Din");

        dayDetails.put("engDate","(16-05-2016)");
        dayDetails.put("engDay","Monday");
        dayDetails.put("date","02");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 06:07 pm to 07:40 pm, From 03:14 am to 04:58 am");
        monthArrList.add(dayDetails);



        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ekadashi");
        dayDetails.put("engDate","(17-05-2016)");
        dayDetails.put("engDay","Thursday");
        dayDetails.put("date","03");
        dayDetails.put("time","From 06:06 pm to 07:29 pm, From 08:50 pm to 12:05 am, From 03:10 am to 04:58 am");
        monthArrList.add(dayDetails);


        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Bd: Purnima");
        dayDetails.put("engDate","(21-05-2016)");
        dayDetails.put("engDay","Saturday");
        dayDetails.put("date","07");
        monthArrList.add(dayDetails);


        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Shab-E-Barat");
        dayDetails.put("engDate","(23-05-2016)");
        dayDetails.put("engDay","Monday");
        dayDetails.put("date","09");
        monthArrList.add(dayDetails);


        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");

        dayDetails.put("engDate","(27-05-2016)");
        dayDetails.put("engDay","Friday");
        dayDetails.put("date","13");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 06:12 pm to 08:52 pm, From 10:13 am to 01:00 am");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");

        dayDetails.put("engDate","(30-05-2016)");
        dayDetails.put("engDay","Monday");
        dayDetails.put("date","16");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 11:34 pm to 12:47 am, From 02:18 am to 04:55 am");
        monthArrList.add(dayDetails);


        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");

        dayDetails.put("engDate","(31-05-2016)");
        dayDetails.put("engDay","Tuesday");
        dayDetails.put("date","17");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 08:53 pm to 12:43 am, From 02:14 am to 04:55 am");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ekadashi");
        dayDetails.put("engDate","(01-06-2016)");
        dayDetails.put("engDay","Wednesday");
        dayDetails.put("date","18");
        monthArrList.add(dayDetails);


        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Lokenath Baba Ti:");
        dayDetails.put("engDate","(02-06-2016)");
        dayDetails.put("engDay","Thursday");
        dayDetails.put("date","19");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Amavashya ");
        dayDetails.put("engDate","(05-06-2016)");
        dayDetails.put("engDay","Sunday");
        dayDetails.put("date","22");
        dayDetails.put("time","From 08:23 pm to 12:23 am, From 02:15 am to 04:54 am");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Jamai Shashti ");
        dayDetails.put("engDate","(10-06-2016)");
        dayDetails.put("engDay","Friday");
        dayDetails.put("date","27");
        dayDetails.put("redMark","1");
        dayDetails.put("time","From 06:17 pm to 08:56 pm, From 10:16 pm to 12:04 am, From 01:35 am to 04:54 am");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(12-06-2016)");
        dayDetails.put("engDay","Sunday");
        dayDetails.put("date","29");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 06:18 pm to 08:35 pm, From 10:22 pm to 12:56 am, From 02:15 am to 04:25 am");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ganga Puja ");
        dayDetails.put("engDate","(15-06-2016) ");
        dayDetails.put("engDay","Wednesday");
        dayDetails.put("date","32");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);


        monthArrList.add(dayDetails);

        monthMap.put("Jaistha",monthArrList);
        //---------------------


        monthArrList = new ArrayList<HashMap>();

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ekadashi ");
        dayDetails.put("engDate","(16-06-2016) ");
        dayDetails.put("engDay","Thursday");
        dayDetails.put("date","01");
        monthArrList.add(dayDetails);



        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Purnima ");
        dayDetails.put("engDate","(20-06-2016)");
        dayDetails.put("engDay","Monday");
        dayDetails.put("date","05");
        dayDetails.put("time","From 06:21 pm to 08:06 pm, From 09:53 pm to 10:18 pm, From 11:32 pm to 04:54 am");
        monthArrList.add(dayDetails);



        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ambubachi Pr");
        dayDetails.put("engDate","(22-06-2016)");
        dayDetails.put("engDay","Wednesday");
        dayDetails.put("date","07");
        monthArrList.add(dayDetails);



        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ambubachi Ni");
        dayDetails.put("engDate","(25-06-2016) ");
        dayDetails.put("engDay","Saturday");
        dayDetails.put("date","10");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(27-06-2016)");
        dayDetails.put("engDay","Monday");
        dayDetails.put("date","12");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 06:22 pm to 07:40 pm, From 09:27 pm to 10:20 pm, From 11:39 pm to 04:56 am");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ekadashi ");
        dayDetails.put("engDate","(30-06-2016)");
        dayDetails.put("engDay","Thursday");
        dayDetails.put("date","15");
        monthArrList.add(dayDetails);



        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Jumath-ul-bidha ");
        dayDetails.put("engDate","(01-07-2016)");
        dayDetails.put("engDay","Friday");
        dayDetails.put("date","16");
        dayDetails.put("time","From 11:07 pm to 04:45 am");
        monthArrList.add(dayDetails);



        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Amavashya ");
        dayDetails.put("engDate","(04-07-2016) ");
        dayDetails.put("engDay","Monday");
        dayDetails.put("date","19");

        monthArrList.add(dayDetails);


        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ratha Jatra ");
        dayDetails.put("engDate","(06-07-2016) ");
        dayDetails.put("engDay","Wednesday");
        dayDetails.put("redMark","1");
        dayDetails.put("date","21");

        monthArrList.add(dayDetails);


        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Eid ul Fiter");
        dayDetails.put("engDate","(06-07-2016) ");
        dayDetails.put("engDay","Wednesday");
        dayDetails.put("date","21");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);



        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Bipodtarini Puja");
        dayDetails.put("engDate","(09-07-2016) ");
        dayDetails.put("engDay","Saturday");
        dayDetails.put("date","24");
        dayDetails.put("redMark","1");
        dayDetails.put("time","From 10:13 pm to 03:23 am");

        monthArrList.add(dayDetails);


        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Bipodtarini Puja");
        dayDetails.put("engDate","(12-07-2016) ");
        dayDetails.put("engDay","Tuesday");
        dayDetails.put("date","27");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);


        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(13-07-2016)");
        dayDetails.put("engDay","Wednesday");
        dayDetails.put("date","28");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 09:57 pm to 02:22 am");
        monthArrList.add(dayDetails);


        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ekadashi ");
        dayDetails.put("engDate","(15-07-2016) ");
        dayDetails.put("engDay","Friday");
        dayDetails.put("date","30");
        monthArrList.add(dayDetails);

        monthMap.put("Asharh",monthArrList);

        //------------------------


        monthArrList = new ArrayList<HashMap>();

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Guru Purnima");
        dayDetails.put("engDate","(19-07-2016)");
        dayDetails.put("engDay","Tuesday");
        dayDetails.put("date","03");
        dayDetails.put("redMark","1");
        dayDetails.put("time","From 10:45 pm to 02:44 am");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(20-07-2016)");
        dayDetails.put("engDay","Wednesday");
        dayDetails.put("date","04");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 09:28 pm to 02:24 am");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Naag Panchami");
        dayDetails.put("engDate","(24-07-2016)");
        dayDetails.put("engDay","Sunday");
        dayDetails.put("date","08");
        dayDetails.put("redMark","1");
        dayDetails.put("time","From 09:16 pm to 10:29 pm");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Purnojatra");
        dayDetails.put("engDate","(26-07-2016)");
        dayDetails.put("engDay","Sunday");
        dayDetails.put("date","09");
        dayDetails.put("redMark","1");

        monthArrList.add(dayDetails);

//----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(29-07-2016)");
        dayDetails.put("engDay","Friday");
        dayDetails.put("date","13");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 10:21 pm to 02:6 am, 04:19 am to 05:09 am");
        monthArrList.add(dayDetails);
        //----------

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ekadashi");
        dayDetails.put("engDate","(30-07-2016)");
        dayDetails.put("engDay","Saturday");
        dayDetails.put("date","14");
        dayDetails.put("time","From 08:52 pm to 02:02 am");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Amavashya");
        dayDetails.put("engDate","(02-08-2016)");
        dayDetails.put("engDay","Thursday");
        dayDetails.put("date","17");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Monosha Puja");
        dayDetails.put("engDate","(07-08-2016)");
        dayDetails.put("engDay","Sunday");
        dayDetails.put("date","22");
        dayDetails.put("redMark","1");
        dayDetails.put("time","From 06:12 pm to 06:46 pm, From 09:51 pm to 01: 05 am, From 02:27 am to 03:43 am");
        monthArrList.add(dayDetails);

        //----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(09-08-2016)");
        dayDetails.put("engDay","Tuesday");
        dayDetails.put("date","24");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 06:11 pm to 06:38 pm, From 09:43 pm to 03:35 am");
        monthArrList.add(dayDetails);
        //----------


        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Jhulon Jatra Aram");
        dayDetails.put("engDate","(13-08-2016)");
        dayDetails.put("engDay","Saturday");
        dayDetails.put("date","28");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ekadashi");
        dayDetails.put("engDate","(14-08-2016)");
        dayDetails.put("engDay","Sunday");
        dayDetails.put("date","29");
        dayDetails.put("time","From 09:22 pm to 11:02 pm");
        monthArrList.add(dayDetails);

        //----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(15-08-2016)");
        dayDetails.put("engDay","Monday");
        dayDetails.put("date","30");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 12:15 am to 03:10 am");
        monthArrList.add(dayDetails);
        //----------


        monthMap.put("Shrabon",monthArrList);

        //------------------------

        monthArrList = new ArrayList<HashMap>();

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Purnima ");
                dayDetails.put("date","01");
                        dayDetails.put("engDate","(18-08-2016)");
                                dayDetails.put("engDay","Thursday");
        monthArrList.add(dayDetails);

                dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Janmashtami");
                dayDetails.put("date","08");
                        dayDetails.put("engDate","(25-08-2016)");
                                dayDetails.put("engDay","Thursday");
        monthArrList.add(dayDetails);

        //----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(26-08-2016)");
        dayDetails.put("engDay","Friday");
        dayDetails.put("date","09");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 01:22 am to 02:28 am");
        monthArrList.add(dayDetails);
        //----------

                dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ekadashi");
                dayDetails.put("date","11");
                        dayDetails.put("engDate","(28-08-2016)");
                                dayDetails.put("engDay","Sunday");
        monthArrList.add(dayDetails);

                dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Amavashya");
                dayDetails.put("date","15");
                        dayDetails.put("engDate","(01-09-2016)");
                                dayDetails.put("engDay","Thursday");

        //----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(05-09-2016)");
        dayDetails.put("engDay","Monday");
        dayDetails.put("date","19");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 09:38 pm to 10:08 pm, From 11:35 pm to 01:49 am.");
        monthArrList.add(dayDetails);
        //----------

        //----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(10-09-2016)");
        dayDetails.put("engDay","Saturday");
        dayDetails.put("date","24");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 09:18 pm to 01:29 am.");
        monthArrList.add(dayDetails);
        //----------

                dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Eid Ul Azha");
                dayDetails.put("date","26");
                        dayDetails.put("engDate","(12-09-2016)");
                                dayDetails.put("engDay","Monday");
        monthArrList.add(dayDetails);

                dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ekadashi");
                dayDetails.put("date","26");
                        dayDetails.put("engDate","(12-09-2016) ");
                                dayDetails.put("engDay","Monday");
        monthArrList.add(dayDetails);


        //----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(13-09-2016)");
        dayDetails.put("engDay","Tuesday");
        dayDetails.put("date","27");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 09:06 pm to 01:18 am.");
        monthArrList.add(dayDetails);
        //----------

                dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Purnima ");
                dayDetails.put("date","30");
                        dayDetails.put("engDate","(16-09-2016)");
                                dayDetails.put("engDay","Friday");
        monthArrList.add(dayDetails);

                dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Vishwakarma Puja");
                dayDetails.put("date","31");
                        dayDetails.put("engDate","(17-09-2016)");
                                dayDetails.put("engDay","Saturday");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);


        monthMap.put("Bhadro",monthArrList);

        //------------------------
        monthArrList = new ArrayList<HashMap>();


        //----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(21-09-2016)");
        dayDetails.put("engDay","Wednesday");
        dayDetails.put("date","04");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 11:35 pm to 02:28 am.");
        monthArrList.add(dayDetails);
        //----------


        //----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(22-09-2016)");
        dayDetails.put("engDay","Thursday");
        dayDetails.put("date","05");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 10:10 pm to 11:29 pm, From 12:59 pm to 02:55 am.");
        monthArrList.add(dayDetails);
        //----------


        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ekadashi");
        dayDetails.put("date","09 ");
        dayDetails.put("engDate","(26-09-2016)");
        dayDetails.put("engDay","Monday");
        monthArrList.add(dayDetails);

        //----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(27-09-2016)");
        dayDetails.put("engDay","Tuesday");
        dayDetails.put("date","10");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 12:35 am to 02:35 am, From 04:46 am to 05:29 am.");
        monthArrList.add(dayDetails);
        //----------

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Amavashya");
        dayDetails.put("date","13");
        dayDetails.put("engDate","(30-09-2016)");
        dayDetails.put("engDay","Friday");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Mahalaya");
        dayDetails.put("date","13");
        dayDetails.put("engDate","(30-09-2016)");
        dayDetails.put("engDay","Friday");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);

        //----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(01-10-2016)");
        dayDetails.put("engDay","Saturday");
        dayDetails.put("date","14");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 11:33 pm to 02:19 am.");
        monthArrList.add(dayDetails);
        //----------

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Durga Shashthi");
        dayDetails.put("date","20");
        dayDetails.put("engDate","(07-10-2016)");
        dayDetails.put("engDay","Friday");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Durga Puja Saptami");
        dayDetails.put("date","21");
        dayDetails.put("engDate","(08-10-2016)");
        dayDetails.put("engDay","Saturday");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Durga Puja Ashtami");
        dayDetails.put("date","22");
        dayDetails.put("engDate","(09-10-2016)");
        dayDetails.put("engDay","Sunday");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Durga Puja Navami");
        dayDetails.put("date","23");
        dayDetails.put("engDate","(10-10-2016)");
        dayDetails.put("engDay","Monday");
        dayDetails.put("redMark","1");
        dayDetails.put("time","From 07:17 pm to 09:51 pm, From 11:24 pm to 01:44 am, From 03:55 am to 05:34 am.");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Dashami");
        dayDetails.put("date","24");
        dayDetails.put("engDate","(11-10-2016)");
        dayDetails.put("engDay","Tuesday");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Muhurram");
        dayDetails.put("date","25");
        dayDetails.put("engDate","(12-10-2016)");
        dayDetails.put("engDay","Wednesday");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ekadashi");
        dayDetails.put("date","25");
        dayDetails.put("engDate","(12-10-2016)");
        dayDetails.put("engDay","Wednesday");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Lakshmi Puja");
        dayDetails.put("date","28");
        dayDetails.put("engDate","(15-10-2016)");
        dayDetails.put("engDay","Saturday");
        dayDetails.put("redMark","1");
        dayDetails.put("time","From 12:18 pm to 10:11 am next day.");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Purnima");
        dayDetails.put("date","29");
        dayDetails.put("engDate","(16-10-2016)");
        dayDetails.put("engDay","Sunday");
        monthArrList.add(dayDetails);


        monthMap.put("Ashwin",monthArrList);

        //------------------------

        monthArrList = new ArrayList<HashMap>();


//----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(19-10-2016)");
        dayDetails.put("engDay","Wednesday");
        dayDetails.put("date","02");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 12:44 am to 02:30 am, From 04:04 am to 05:30 am.");
        monthArrList.add(dayDetails);
        //----------

        //----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(20-10-2016)");
        dayDetails.put("engDay","Thursday");
        dayDetails.put("date","03");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 12:56 am to 04:37 am.");
        monthArrList.add(dayDetails);
        //----------


        //----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(25-10-2016)");
        dayDetails.put("engDay","Tuesday");
        dayDetails.put("date","08");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 08:10 pm to 02:29 am.");
        monthArrList.add(dayDetails);
        //----------


        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ekadashi ");
        dayDetails.put("date","09");
        dayDetails.put("engDate","(26-10-2016)");
        dayDetails.put("engDay","Wednesday");
        dayDetails.put("time","From 04:31 am to 05:09 pm next day.");
        monthArrList.add(dayDetails);

        //----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(27-10-2016)");
        dayDetails.put("engDay","Thursday");
        dayDetails.put("date","10");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 06:08 pm to 08:37 pm.");
        monthArrList.add(dayDetails);
        //----------

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Dhantaras");
        dayDetails.put("date","11");
        dayDetails.put("engDate","(28-10-2016)");
        dayDetails.put("engDay","Friday");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Kali Puja ");
        dayDetails.put("date","12");
        dayDetails.put("engDate","(29-10-2016) ");
        dayDetails.put("engDay","Saturday");
        dayDetails.put("redMark","1");
        dayDetails.put("time","From 7.50pm to 9.43pm next day.");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Amavashya ");
        dayDetails.put("date","13");
        dayDetails.put("engDate","(30-10-2016)");
        dayDetails.put("engDay","Sunday");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Dipabali ");
        dayDetails.put("date","13");
        dayDetails.put("engDate","(30-10-2016)");
        dayDetails.put("engDay","Sunday");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Bhai phota ");
        dayDetails.put("date","15");
        dayDetails.put("engDate","(01-11-2016)");
        dayDetails.put("engDay","Tuesday");
        dayDetails.put("redMark","1");
        dayDetails.put("time","From  1.55am to 3.53 am next day.");
        monthArrList.add(dayDetails);

        //----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(04-11-2016)");
        dayDetails.put("engDay","Friday");
        dayDetails.put("date","18");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 05:37 pm to 08:07 pm.");
        monthArrList.add(dayDetails);
        //----------

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Chhat puja");
        dayDetails.put("date","20");
        dayDetails.put("engDate","(06-11-2016) ");
        dayDetails.put("engDay","Sunday");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Jagaddhatri Puja ");
        dayDetails.put("date","23");
        dayDetails.put("engDate","(09-11-2016) ");
        dayDetails.put("engDay","Wednesday");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);

        //----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(11-11-2016)");
        dayDetails.put("engDay","Friday");
        dayDetails.put("date","25");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 02:29 am to 03:58 am.");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ekadasi");
        dayDetails.put("engDate","(11-11-2016)");
        dayDetails.put("engDay","Friday");
        dayDetails.put("date","25");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 4.54 am to 2.30 am next day.");
        monthArrList.add(dayDetails);
        //----------

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Rash Jatra ");
        dayDetails.put("date","27");
        dayDetails.put("engDate","(13-11-2016) ");
        dayDetails.put("engDay","Sunday");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Purnima ");
        dayDetails.put("date","28");
        dayDetails.put("engDate","(14-11-2016) ");
        dayDetails.put("engDay","Monday");
        dayDetails.put("time","From 7.47 pm to 5.25 pm next day.");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Kartik Puja ");
        dayDetails.put("date","30");
        dayDetails.put("engDate","(17-11-2015) ");
        dayDetails.put("engDay","Tuesday");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);


        monthMap.put("Kartik",monthArrList);

        //------------------------

        monthArrList = new ArrayList<HashMap>();

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ekadashi");
                dayDetails.put("date","09");
                        dayDetails.put("engDate","(24-11-2016)");
                                dayDetails.put("engDay","Friday");
        dayDetails.put("time","9.07 am to 10.44am next day.");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Amavashya");
                dayDetails.put("date","13");
                        dayDetails.put("engDate","(29-11-2016)");
                                dayDetails.put("engDay","Tuesday");
        dayDetails.put("time","From 4.47 pm to 6.56 pm next day.");
        monthArrList.add(dayDetails);


        dayDetails = new HashMap<String, String>();

        dayDetails.put("occasion","Ackri Chahar");
                dayDetails.put("date","14");
                        dayDetails.put("engDate","(30-11-2016)");
                                dayDetails.put("engDay","Wednesday");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ekadashi");
                dayDetails.put("date","24");
                        dayDetails.put("engDate","(10-12-2016)");
                                dayDetails.put("engDay","Saturday");
        dayDetails.put("time","From 3.36 pm to 1.12 pm");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Purnima");
                dayDetails.put("date","27");
                        dayDetails.put("engDate","(13-12-2016)");
                                dayDetails.put("engDay","Tuesday");
        dayDetails.put("time","From 8.31 am to 6.15 am");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Fateha Duaz Daham");
                dayDetails.put("date","27");
                        dayDetails.put("engDate","(13-12-2016)");
                                dayDetails.put("engDay","Tuesday");
        dayDetails.put("time","From 11:40 pm to 04:04 am.");
        monthArrList.add(dayDetails);

        monthMap.put("Agrahayan",monthArrList);

        //-------

        monthArrList = new ArrayList<HashMap>();


        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ekadashi ");
        dayDetails.put("date","08");
        dayDetails.put("engDate","(24-12-2016) ");
        dayDetails.put("engDay","Saturday");
        dayDetails.put("time","From 4.21am to let night 4.39 am next day.");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Baro Din ");
        dayDetails.put("date","09");
        dayDetails.put("engDate","(25-12-2016) ");
        dayDetails.put("engDay","Sunday");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Amavashya ");
        dayDetails.put("date","13");
        dayDetails.put("engDate","(29-12-2016) ");
        dayDetails.put("engDay","Thursday");
        dayDetails.put("time","From 12.19 pm to 1.35 pm next day.");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ekadashi ");
        dayDetails.put("date","23");
        dayDetails.put("engDate","(08-01-2017) ");
        dayDetails.put("engDay","Sunday");
        dayDetails.put("time","From 2.17 am to 11.57 pm next day.");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Fateha Eaz Daham ");
        dayDetails.put("date","25");
        dayDetails.put("engDate","(10-01-2017) ");
        dayDetails.put("engDay","Tuesday");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Purnima ");
        dayDetails.put("date","27");
        dayDetails.put("engDate","(12-01-2017) ");
        dayDetails.put("engDay","Thursday");
        dayDetails.put("time","From 5.46 pm to 4 13 pm next day.");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Poush Parbon ");
        dayDetails.put("date","29");
        dayDetails.put("engDate","(14-01-2017) ");
        dayDetails.put("engDay","Saturday");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);



        monthMap.put("Poush",monthArrList);

        //------------------------

        monthArrList = new ArrayList<HashMap>();


        //----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(16-01-2017)");
        dayDetails.put("engDay","Monday");
        dayDetails.put("date","02");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 02:17 am to 04:04 am.");
        monthArrList.add(dayDetails);
        //----------

        //----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(17-01-2017)");
        dayDetails.put("engDay","Tuesday");
        dayDetails.put("date","03");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 11:30 pm to 04:00 am.");
        monthArrList.add(dayDetails);
        //----------

        //----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(22-01-2017)");
        dayDetails.put("engDay","Sunday");
        dayDetails.put("date","08");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 11:11 pm to 01:27 am, From 03:06 am to 03:40 am.");
        monthArrList.add(dayDetails);
        //----------

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ekadashi ");
        dayDetails.put("date","09");
        dayDetails.put("engDate","(23-01-2017)");
        dayDetails.put("engDay","Monday");
        dayDetails.put("time","From 12.26 am to 2.23 am next day.");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Netaji's birthday ");
        dayDetails.put("date","09");
        dayDetails.put("engDate","(23-01-2017)");
        dayDetails.put("engDay","Monday");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);

        //----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(24-01-2017)");
        dayDetails.put("engDay","Tuesday");
        dayDetails.put("date","10");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 02:22 am to 03:33 am, From 05:38am to 06:24 am.");
        monthArrList.add(dayDetails);
        //----------

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Republic Day India ");
        dayDetails.put("date","12");
        dayDetails.put("engDate","(26-01-2017)");
        dayDetails.put("engDay","Thursday");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Amavashya");
        dayDetails.put("date","13");
        dayDetails.put("engDate","(27-01-2017)");
        dayDetails.put("engDay","Friday");
        dayDetails.put("time","From 5.56 am to 6.08 am next day.");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Saraswati Puja ");
        dayDetails.put("date","18");
        dayDetails.put("engDate","(01-02-2017) ");
        dayDetails.put("engDay","Wednesday");
        dayDetails.put("redMark","1");
        dayDetails.put("time","From 10:33 pm to 03:06 am, 04:43 am to 06:21 am.");
        monthArrList.add(dayDetails);

        //----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(02-02-2017)");
        dayDetails.put("engDay","Thursday");
        dayDetails.put("date","19");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From (midnight) 02:48 am to 06:20 am.");
        monthArrList.add(dayDetails);
        //----------

        //----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(03-02-2017)");
        dayDetails.put("engDay","Friday");
        dayDetails.put("date","20");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 05:20 pm to 06:04 pm.");
        monthArrList.add(dayDetails);
        //----------

        //----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(05-02-2017)");
        dayDetails.put("engDay","Sunday");
        dayDetails.put("date","22");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 05:29 pm to 05:56 pm, From 10:17 pm to 01:28 am, From 03:05 am to 06:19 am.");
        monthArrList.add(dayDetails);
        //----------

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ekadashi ");
        dayDetails.put("date","24");
        dayDetails.put("engDate","(07-02-2017) ");
        dayDetails.put("engDay","Thursday");
        dayDetails.put("time","From 12.53 pm to 20.48 am next day.");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Purnima ");
        dayDetails.put("date","27");
        dayDetails.put("engDate","(10-02-2017) ");
        dayDetails.put("engDay","Friday");
        dayDetails.put("time","From 7.25 am to 5 34 am next day");
        monthArrList.add(dayDetails);


        monthMap.put("Magh",monthArrList);

        //------------------------

        monthArrList = new ArrayList<HashMap>();


        //----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(18-02-2017)");
        dayDetails.put("engDay","Saturday");
        dayDetails.put("date","06");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 10:53 pm to 04:36 am.");
        monthArrList.add(dayDetails);
        //----------


        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ekadashi ");
        dayDetails.put("date","10");
        dayDetails.put("engDate","(22-02-2017)");
        dayDetails.put("engDay","Wednesday");
        dayDetails.put("time","From 7.08 pm to 8.17 pm next day.");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Maha Shivratri ");
        dayDetails.put("date","12");
        dayDetails.put("engDate","(28-02-2017) ");
        dayDetails.put("engDay","Friday");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Amavashya ");
        dayDetails.put("date","14");
        dayDetails.put("engDate","(26-02-2017) ");
        dayDetails.put("engDay","Sunday");
        dayDetails.put("time","From 8.46 pm to 7.51 pm next day");
        monthArrList.add(dayDetails);

        //----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(02-03-2017)");
        dayDetails.put("engDay","Thursday");
        dayDetails.put("date","18");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 08:40 pm to 11:49 pm, From 01:22 am to 03:46 am.");
        monthArrList.add(dayDetails);
        //----------

        //----------
        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Subho Bibaho Din");
        dayDetails.put("engDate","(04-03-2017)");
        dayDetails.put("engDay","Saturday");
        dayDetails.put("date","20");
        dayDetails.put("redMark","0");
        dayDetails.put("time","From 02:14 am to 04:26 am.");
        monthArrList.add(dayDetails);
        //----------

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ekadashi ");
        dayDetails.put("date","24");
        dayDetails.put("engDate","(08-03-2017) ");
        dayDetails.put("engDay","Wednesday");
        dayDetails.put("time","From 11.29 pm to 9.56 pm next day");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Dol Purnima ");
        dayDetails.put("date","28");
        dayDetails.put("engDate","(12-03-2017)");
        dayDetails.put("engDay","Sunday");
        dayDetails.put("time","From 05:51 pm to 08:01 pm, From 10:15 pm to 11:35 pm.");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Holi  ");
        dayDetails.put("date","29");
        dayDetails.put("engDate","(13-03-2017)");
        dayDetails.put("engDay","Monday");
        dayDetails.put("redMark","1");
        dayDetails.put("time","From 05:47 pm to 06:14 pm.");
        monthArrList.add(dayDetails);


        monthMap.put("Falgun",monthArrList);

        //------------------------

        monthArrList = new ArrayList<HashMap>();


        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ekadashi ");
        dayDetails.put("date","10");
        dayDetails.put("engDate","(24-03-2017)");
        dayDetails.put("engDay","Friday");
        dayDetails.put("time","From 11.6 am to 11 .10 am next day.");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Amavashya ");
        dayDetails.put("date","14");
        dayDetails.put("engDate","(28-03-2017) ");
        dayDetails.put("engDay","Tuesday");
        dayDetails.put("time","From 8.31 am to 6.49 am next day.");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Basanti Puja(Sapatami)");
        dayDetails.put("date","20");
        dayDetails.put("engDate","(03-04-2017) ");
        dayDetails.put("engDay","Monday");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Annapurna Puja ");
        dayDetails.put("date","21");
        dayDetails.put("engDate","(07-04-2017) ");
        dayDetails.put("engDay","Tuesday");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Ekadashi ");
        dayDetails.put("date","24");
        dayDetails.put("engDate","(07-04-2017) ");
        dayDetails.put("engDay","Friday");
        dayDetails.put("time","From 10.25 am to 9.41 am next day.");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Purnima ");
        dayDetails.put("date","28");
        dayDetails.put("engDate","(11-04-2017)");
        dayDetails.put("engDay","Tuesday");
        dayDetails.put("time","From 10.29 am to 11.44 am next day.");
        monthArrList.add(dayDetails);

        dayDetails = new HashMap<String, String>();
        dayDetails.put("occasion","Charak Puja ");
        dayDetails.put("date","31");
        dayDetails.put("engDate","(14-04-2017)");
        dayDetails.put("engDay","Friday");
        dayDetails.put("redMark","1");
        monthArrList.add(dayDetails);


        monthMap.put("Choitro",monthArrList);

        //------------------------

        monthArrList = new ArrayList<HashMap>();


    }

    void populate1424Calendar()
    {
        monthsDetails = new ArrayList<>();

        MonthInfo monthInfo = new MonthInfo();

        monthInfo.setMonthName("Baisakh");
        monthInfo.setNumberOfDaysInMonth(31);

        Date date;
        Calendar cal = Calendar.getInstance();
        cal.set(2017,4, 15);
        date = cal.getTime();
        monthInfo.setStartDate(date);

        cal.set(2017,5, 15);
        date = cal.getTime();
        monthInfo.setEndDate(date);

        ArrayList<DateInfo> specialDates = new ArrayList<>();

        DateInfo dateInfo = new DateInfo();
        dateInfo.setDate(1);
        dateInfo.setOccasionName("Nabo barsho \nAnnapurna Puja");
        specialDates.add(dateInfo);

        dateInfo = new DateInfo();
        dateInfo.setDate(2);
        dateInfo.setOccasionName("Bashanti Puja");
        specialDates.add(dateInfo);

        dateInfo = new DateInfo();
        dateInfo.setDate(5);
        dateInfo.setOccasionName("Ekadashi");
        specialDates.add(dateInfo);

        dateInfo = new DateInfo();
        dateInfo.setDate(6);
        dateInfo.setOccasionName("Mahabir Joynti");
        specialDates.add(dateInfo);

        dateInfo = new DateInfo();
        dateInfo.setDate(9);
        dateInfo.setOccasionName("Purnima");
        dateInfo.setOccasionDetails("From 00.00pm to 00.00 pm for testing purpose");
        specialDates.add(dateInfo);

        dateInfo = new DateInfo();
        dateInfo.setDate(18);
        dateInfo.setOccasionName("May Day");
        specialDates.add(dateInfo);

        dateInfo = new DateInfo();
        dateInfo.setDate(20);
        dateInfo.setOccasionName("Ekadashi");
        specialDates.add(dateInfo);

        dateInfo = new DateInfo();
        dateInfo.setDate(22);
        dateInfo.setOccasionName("Shoby-mi-raaz ");
        specialDates.add(dateInfo);

        dateInfo = new DateInfo();
        dateInfo.setDate(23);
        dateInfo.setOccasionName("Amavasha");
        specialDates.add(dateInfo);

        dateInfo = new DateInfo();
        dateInfo.setDate(25);
        dateInfo.setOccasionName("Rabindra Joynti");
        specialDates.add(dateInfo);

        dateInfo = new DateInfo();
        dateInfo.setDate(26);
        dateInfo.setOccasionName("Akhoy Tretiya");
        specialDates.add(dateInfo);

        monthInfo.setSpecialDates(specialDates);

        monthsDetails.add(monthInfo);


        //===


        monthInfo = new MonthInfo();

        monthInfo.setMonthName("Jaistha");
        monthInfo.setNumberOfDaysInMonth(30);

        cal = Calendar.getInstance();
        cal.set(2017,5, 16);
        date = cal.getTime();
        monthInfo.setStartDate(date);

        cal.set(2017,6, 15);
        date = cal.getTime();
        monthInfo.setEndDate(date);

        specialDates = new ArrayList<>();

        dateInfo = new DateInfo();
        dateInfo.setDate(1);
        dateInfo.setOccasionName("Nabo barsho \nAnnapurna Puja");
        specialDates.add(dateInfo);

        dateInfo = new DateInfo();
        dateInfo.setDate(2);
        dateInfo.setOccasionName("Bashanti Puja");
        specialDates.add(dateInfo);

        dateInfo = new DateInfo();
        dateInfo.setDate(5);
        dateInfo.setOccasionName("Ekadashi");
        specialDates.add(dateInfo);

        dateInfo = new DateInfo();
        dateInfo.setDate(6);
        dateInfo.setOccasionName("Mahabir Joynti");
        specialDates.add(dateInfo);

        dateInfo = new DateInfo();
        dateInfo.setDate(9);
        dateInfo.setOccasionName("Purnima");
        dateInfo.setOccasionDetails("From 00.00pm to 00.00 pm for testing purpose");
        specialDates.add(dateInfo);

        dateInfo = new DateInfo();
        dateInfo.setDate(18);
        dateInfo.setOccasionName("May Day");
        specialDates.add(dateInfo);

        dateInfo = new DateInfo();
        dateInfo.setDate(20);
        dateInfo.setOccasionName("Ekadashi");
        specialDates.add(dateInfo);

        dateInfo = new DateInfo();
        dateInfo.setDate(22);
        dateInfo.setOccasionName("Shoby-mi-raaz ");
        specialDates.add(dateInfo);

        dateInfo = new DateInfo();
        dateInfo.setDate(23);
        dateInfo.setOccasionName("Amavasha");
        specialDates.add(dateInfo);

        dateInfo = new DateInfo();
        dateInfo.setDate(25);
        dateInfo.setOccasionName("Rabindra Joynti");
        specialDates.add(dateInfo);

        dateInfo = new DateInfo();
        dateInfo.setDate(26);
        dateInfo.setOccasionName("Akhoy Tretiya");
        specialDates.add(dateInfo);

        monthInfo.setSpecialDates(specialDates);

        monthsDetails.add(monthInfo);



    }
}
