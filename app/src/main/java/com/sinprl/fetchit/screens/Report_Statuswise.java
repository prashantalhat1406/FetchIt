package com.sinprl.fetchit.screens;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sinprl.fetchit.R;
import com.sinprl.fetchit.data.Profile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Report_Statuswise extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private FirebaseDatabase database;
    private List<Profile> all_profiles;
    Integer count_NEW=0,count_KYC=0,count_LOG=0,count_SAN=0,count_DIS=0;
    TextView txtNEW, txtKYC, txtLOG,txtSAN, txtDIS, txtCurrentSelection;
    boolean dailyFlag = false, weeklyFlag = false, monthlyFlag = false;
    Button back, next;
    String baseDate="";
    TextView txtDaily,txtMonthly,txtWeekly;
    DatePickerDialog datePickerDialog;
    Calendar calendar;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,dayOfMonth);
        txtCurrentSelection.setText(new SimpleDateFormat("dd-MMM-yyyy").format(calendar.getTime()));
        baseDate = new SimpleDateFormat("dd-MMM-yyyy").format(calendar.getTime());
        if (dailyFlag)
            txtDaily.callOnClick();
        if (monthlyFlag)
            txtMonthly.callOnClick();
        if (weeklyFlag)
            txtWeekly.callOnClick();


    }

    public String get_formatted_date(String bDate){
        SimpleDateFormat spf = new SimpleDateFormat("dd-MMM-yyyy");
        Date fromatedDate = null;
        try {
            fromatedDate = spf.parse(bDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        spf= new SimpleDateFormat("dd/MM/yyyy");
        String _temp_basedate = spf.format(fromatedDate);
        return _temp_basedate;
    }

    public boolean isDateInCurrentWeek(Date date, String baseDate) {
        Calendar currentCalendar = Calendar.getInstance();
        try {
            currentCalendar.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(baseDate));
        }catch (Exception e){
            Log.d("REPORT_STATUSWISE", "isDateInCurrentWeek: " + e.getMessage());
        }
        Date min, max;
        min = currentCalendar.getTime();
        currentCalendar.add(Calendar.DAY_OF_MONTH, 6);
        max = currentCalendar.getTime();
        return date.compareTo(min) >= 0 && date.compareTo(max) <= 0;
    }

    public String getCurrentWeek(String baseDate){
        String currWeek = "";
        Calendar currentCalendar = Calendar.getInstance();
        try {
            currentCalendar.setTime(new SimpleDateFormat("dd-MMM-yyyy").parse(baseDate));
        }catch (Exception e){}
        currWeek = "" + new SimpleDateFormat("dd-MMM").format(currentCalendar.getTime());
        currentCalendar.add(Calendar.DAY_OF_MONTH, 6);
        currWeek = currWeek +" to "+ new SimpleDateFormat("dd-MMM").format(currentCalendar.getTime());
        return  currWeek;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report_statuswise);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        all_profiles = new ArrayList<>();
        txtNEW = findViewById(R.id.txt_report_status_new);
        txtKYC = findViewById(R.id.txt_report_status_kyc);
        txtLOG = findViewById(R.id.txt_report_status_log);
        txtSAN = findViewById(R.id.txt_report_status_san);
        txtDIS = findViewById(R.id.txt_report_status_dis);
        txtCurrentSelection = findViewById(R.id.txt_report_currentselection);

        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(
                Report_Statuswise.this, Report_Statuswise.this, year, month, day);

        baseDate = new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());
        dailyFlag = true;
        weeklyFlag = false;
        monthlyFlag = false;
        txtCurrentSelection.setText(baseDate);
        txtCurrentSelection.setOnClickListener(v -> {
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        txtDaily = findViewById(R.id.txt_report_orderwise_daily);
        txtMonthly = findViewById(R.id.txt_report_orderwise_monthly);
        txtWeekly = findViewById(R.id.txt_report_orderwise_weekly);


        database = FirebaseDatabase.getInstance("https://fetchit-a4181-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference profile_ref = database.getReference("Profiles/");
        Query query = profile_ref.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                all_profiles = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                    Profile profile = postSnapshot.getValue(Profile.class);
                    all_profiles.add(profile);
                }
                txtDaily.callOnClick();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        txtMonthly.setOnClickListener(v -> {
            txtMonthly.setBackground(getDrawable(R.drawable.button_blue_fill));
            txtMonthly.setTextColor(getColor(R.color.white));
            txtWeekly.setBackground(getDrawable(R.drawable.button_blue_border));
            txtWeekly.setTextColor(getColor(R.color.black));
            txtDaily.setBackground(getDrawable(R.drawable.button_blue_border));
            txtDaily.setTextColor(getColor(R.color.black));
            dailyFlag = false;
            weeklyFlag = false;
            monthlyFlag = true;



            String currentMonth = baseDate.split("-")[1];
            count_NEW=0;
            count_KYC =0;
            count_LOG =0 ;
            count_SAN =0;
            count_DIS =0;
            txtCurrentSelection.setText("" + currentMonth);

            String _temp_basedate = get_formatted_date(baseDate);
            String _temp_currentmonth = _temp_basedate.split("/")[1];

            for (Profile profile : all_profiles) {
                if (profile.entry_date.split("/")[1].equals(_temp_currentmonth)) {
                    switch (profile.status) {
                        case "NEW": count_NEW = count_NEW + 1; break;
                        case "KYC": count_KYC = count_KYC + 1; break;
                        case "LOG": count_LOG = count_LOG + 1; break;
                        case "SAN": count_SAN = count_SAN + 1; break;
                        case "DIS": count_DIS = count_DIS + 1; break;
                    }
                }
            }
            txtNEW.setText("" + count_NEW);
            txtKYC.setText("" + count_KYC);
            txtLOG.setText("" + count_LOG);
            txtSAN.setText("" + count_SAN);
            txtDIS.setText("" + count_DIS);
        });

        txtWeekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtWeekly.setBackground(getDrawable(R.drawable.button_blue_fill));
                txtWeekly.setTextColor(getColor(R.color.white));
                txtMonthly.setBackground(getDrawable(R.drawable.button_blue_border));
                txtMonthly.setTextColor(getColor(R.color.black));
                txtDaily.setBackground(getDrawable(R.drawable.button_blue_border));
                txtDaily.setTextColor(getColor(R.color.black));

                dailyFlag = false;
                weeklyFlag = true;
                monthlyFlag = false;
                count_NEW=0;
                count_KYC =0;
                count_LOG =0 ;
                count_SAN =0;
                count_DIS =0;

                txtCurrentSelection.setText(getCurrentWeek(baseDate));
                String _temp_basedate = get_formatted_date(baseDate);

                try{
                    for (Profile profile : all_profiles) {
                        if (isDateInCurrentWeek(new SimpleDateFormat("dd/MM/yyyy").parse(profile.entry_date),_temp_basedate)) {
                            switch (profile.status) {
                                case "NEW": count_NEW = count_NEW + 1; break;
                                case "KYC": count_KYC = count_KYC + 1; break;
                                case "LOG": count_LOG = count_LOG + 1; break;
                                case "SAN": count_SAN = count_SAN + 1; break;
                                case "DIS": count_DIS = count_DIS + 1; break;
                            }
                        }
                    }}catch (Exception e){
                    Log.d("WEEKLY", e.getMessage());
                }
                txtNEW.setText("" + count_NEW);
                txtKYC.setText("" + count_KYC);
                txtLOG.setText("" + count_LOG);
                txtSAN.setText("" + count_SAN);
                txtDIS.setText("" + count_DIS);
            }
        });

        txtDaily.setOnClickListener(v -> {

            txtDaily.setBackground(getDrawable(R.drawable.button_blue_fill));
            txtDaily.setTextColor(getColor(R.color.white));
            txtWeekly.setBackground(getDrawable(R.drawable.button_blue_border));
            txtWeekly.setTextColor(getColor(R.color.black));
            txtMonthly.setBackground(getDrawable(R.drawable.button_blue_border));
            txtMonthly.setTextColor(getColor(R.color.black));
            dailyFlag = true;
            weeklyFlag = false;
            monthlyFlag = false;

            count_NEW=0;
            count_KYC =0;
            count_LOG =0 ;
            count_SAN =0;
            count_DIS =0;

            txtCurrentSelection.setText("" + baseDate);


            String _temp_basedate = get_formatted_date(baseDate);
            for (Profile profile : all_profiles) {
                if (profile.entry_date.equals(_temp_basedate)){
                    switch (profile.status) {
                        case "NEW": count_NEW = count_NEW + 1; break;
                        case "KYC": count_KYC = count_KYC + 1; break;
                        case "LOG": count_LOG = count_LOG + 1; break;
                        case "SAN": count_SAN = count_SAN + 1; break;
                        case "DIS": count_DIS = count_DIS + 1; break;
                    }
                }
            }

            txtNEW.setText("" + count_NEW);
            txtKYC.setText("" + count_KYC);
            txtLOG.setText("" + count_LOG);
            txtSAN.setText("" + count_SAN);
            txtDIS.setText("" + count_DIS);

        });

        back = findViewById(R.id.but_reportorderwise_back);
        back.setOnClickListener(v -> {
            Calendar currentCalendar = Calendar.getInstance();
            try {
                currentCalendar.setTime(new SimpleDateFormat("dd-MMM-yyyy").parse(baseDate));
            }catch (Exception e){}
            if (dailyFlag)
            {
                currentCalendar.add(Calendar.DAY_OF_MONTH,-1);
                baseDate = new SimpleDateFormat("dd-MMM-yyyy").format(currentCalendar.getTime());
                txtDaily.callOnClick();
            }
            if (weeklyFlag)
            {
                currentCalendar.add(Calendar.DAY_OF_MONTH,-7);
                baseDate = new SimpleDateFormat("dd-MMM-yyyy").format(currentCalendar.getTime());
                txtWeekly.callOnClick();
            }
            if (monthlyFlag)
            {
                currentCalendar.add(Calendar.MONTH,-1);
                baseDate = new SimpleDateFormat("dd-MMM-yyyy").format(currentCalendar.getTime());
                txtMonthly.callOnClick();
            }
        });

        next = findViewById(R.id.but_reportorderwise_next);
        next.setOnClickListener(v -> {
            Calendar currentCalendar = Calendar.getInstance();
            try {
                currentCalendar.setTime(new SimpleDateFormat("dd-MMM-yyyy").parse(baseDate));
            }catch (Exception e){}
            if (dailyFlag)
            {
                currentCalendar.add(Calendar.DAY_OF_MONTH,1);
                baseDate = new SimpleDateFormat("dd-MMM-yyyy").format(currentCalendar.getTime());
                txtDaily.callOnClick();
            }
            if (weeklyFlag)
            {
                currentCalendar.add(Calendar.DAY_OF_MONTH,7);
                baseDate = new SimpleDateFormat("dd-MMM-yyyy").format(currentCalendar.getTime());
                txtWeekly.callOnClick();
            }
            if (monthlyFlag)
            {
                currentCalendar.add(Calendar.MONTH,1);
                baseDate = new SimpleDateFormat("dd-MMM-yyyy").format(currentCalendar.getTime());
                txtMonthly.callOnClick();
            }
        });



    }
}