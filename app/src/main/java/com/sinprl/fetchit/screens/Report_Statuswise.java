package com.sinprl.fetchit.screens;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.sinprl.fetchit.data.ReportData;
import com.sinprl.fetchit.utils.ReportUtils;

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

    public void showErrorMessage(String message){
        Toast error = Toast.makeText(Report_Statuswise.this, message,Toast.LENGTH_SHORT);
        error.setGravity(Gravity.TOP, 0, 0);
        error.show();
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

    public void showProfilesStatusWise(String status){
        Intent profileIntent = new Intent(Report_Statuswise.this, Profile_Display.class);
        Bundle extras = new Bundle();
        extras.putString("report_status", status);
        extras.putString("base_date", baseDate);

        if (dailyFlag)
            extras.putInt("period", 0);
        if (weeklyFlag)
            extras.putInt("period", 1);
        if (monthlyFlag)
            extras.putInt("period", 2);

        profileIntent.putExtras(extras);
        startActivity(profileIntent);
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
                    profile.setId(postSnapshot.getKey());
                    all_profiles.add(profile);
                }
                txtDaily.callOnClick();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        txtMonthly.setOnClickListener(v -> {
            ReportUtils.setBackgroundForTextView(this, txtMonthly,txtWeekly,txtDaily);
            dailyFlag = false;
            weeklyFlag = false;
            monthlyFlag = true;
            txtCurrentSelection.setText("" + baseDate.split("-")[1]);
            ReportData rd = ReportUtils.getReportDataForMonth(all_profiles, baseDate);
            txtNEW.setText("" + rd.getStatus_new());
            txtKYC.setText("" + rd.getStatus_kyc());
            txtLOG.setText("" + rd.getStatus_log());
            txtSAN.setText("" + rd.getStatus_san());
            txtDIS.setText("" + rd.getStatus_dis());
        });

        txtWeekly.setOnClickListener(v -> {
            ReportUtils.setBackgroundForTextView(this, txtWeekly, txtMonthly,txtDaily);
            dailyFlag = false;
            weeklyFlag = true;
            monthlyFlag = false;
            txtCurrentSelection.setText(ReportUtils.getCurrentWeek(baseDate));
            ReportData rd = ReportUtils.getReportDataForWeek(all_profiles, baseDate);
            txtNEW.setText("" + rd.getStatus_new());
            txtKYC.setText("" + rd.getStatus_kyc());
            txtLOG.setText("" + rd.getStatus_log());
            txtSAN.setText("" + rd.getStatus_san());
            txtDIS.setText("" + rd.getStatus_dis());
        });

        txtDaily.setOnClickListener(v -> {
            ReportUtils.setBackgroundForTextView(this,txtDaily, txtWeekly, txtMonthly);
            dailyFlag = true;
            weeklyFlag = false;
            monthlyFlag = false;
            txtCurrentSelection.setText("" + baseDate);
            ReportData rd = ReportUtils.getReportDataForDay(all_profiles, baseDate);
            txtNEW.setText("" + rd.getStatus_new());
            txtKYC.setText("" + rd.getStatus_kyc());
            txtLOG.setText("" + rd.getStatus_log());
            txtSAN.setText("" + rd.getStatus_san());
            txtDIS.setText("" + rd.getStatus_dis());
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

        LinearLayout llnew = findViewById(R.id.layout_report_new);
        llnew.setOnClickListener(v -> {
            if (Integer.parseInt( txtNEW.getText().toString()) > 0)
                showProfilesStatusWise("NEW");
            else
                showErrorMessage("No Profiles to show !");
        });

        LinearLayout llkyc = findViewById(R.id.layout_report_kyc);
        llkyc.setOnClickListener(v -> {
            if (Integer.parseInt( txtKYC.getText().toString()) > 0)
                showProfilesStatusWise("KYC");
            else
                showErrorMessage("No Profiles to show !");
        });

        LinearLayout lllog = findViewById(R.id.layout_report_log);
        lllog.setOnClickListener(v -> {
            if (Integer.parseInt( txtLOG.getText().toString()) > 0)
                showProfilesStatusWise("LOG");
            else
                showErrorMessage("No Profiles to show !");
        });

        LinearLayout llsan = findViewById(R.id.layout_report_san);
        llsan.setOnClickListener(v -> {
            if (Integer.parseInt( txtSAN.getText().toString()) > 0)
                showProfilesStatusWise("SAN");
            else
                showErrorMessage("No Profiles to show !");
        });

        LinearLayout lldis = findViewById(R.id.layout_report_dis);
        lldis.setOnClickListener(v -> {
            if (Integer.parseInt( txtDIS.getText().toString()) > 0)
                showProfilesStatusWise("DIS");
            else
                showErrorMessage("No Profiles to show !");
        });



    }
}