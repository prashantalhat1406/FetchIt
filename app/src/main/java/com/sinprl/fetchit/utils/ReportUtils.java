package com.sinprl.fetchit.utils;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.sinprl.fetchit.R;
import com.sinprl.fetchit.data.Profile;
import com.sinprl.fetchit.data.ReportData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReportUtils {


    public static String getCurrentWeek(String baseDate){
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

    public static boolean isDateInCurrentWeek(Date profile_date, String baseDate) {
        Calendar currentCalendar = Calendar.getInstance();
        try {
            currentCalendar.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(baseDate));
        }catch (Exception e){
            Log.d("ReportsUtils", "isDateInCurrentWeek: " + e.getMessage());
        }
        Date min, max;
        min = currentCalendar.getTime();
        currentCalendar.add(Calendar.DAY_OF_MONTH, 6);
        max = currentCalendar.getTime();
        return profile_date.compareTo(min) >= 0 && profile_date.compareTo(max) <= 0;
    }

    public static String get_formatted_date(String bDate){
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

    public static ReportData getReportDataForDay(List<Profile> all_profiles, String reportDate){
        ReportData reportData = new ReportData();
        String _temp_basedate = ReportUtils.get_formatted_date(reportDate);
        for (Profile profile : all_profiles) {
            if (profile.entry_date.equals(_temp_basedate)){
                reportData.filtered_profiles.add(profile);
                switch (profile.status) {
                    case "NEW": reportData.setStatus_new(reportData.getStatus_new() + 1); break;
                    case "KYC": reportData.setStatus_kyc(reportData.getStatus_kyc() + 1); break;
                    case "LOG": reportData.setStatus_log(reportData.getStatus_log() + 1); break;
                    case "SAN": reportData.setStatus_san(reportData.getStatus_san() + 1); break;
                    case "DIS": reportData.setStatus_dis(reportData.getStatus_dis() + 1); break;
                }
            }
        }
        return reportData;
    }

    public static ReportData getReportDataForMonth(List<Profile> all_profiles, String reportDate){
        ReportData reportData = new ReportData();

        String _temp_basedate = ReportUtils.get_formatted_date(reportDate);
        String _temp_currentmonth = _temp_basedate.split("/")[1];
        String _temp_currentyear = _temp_basedate.split("/")[2];

        for (Profile profile : all_profiles) {
            if (profile.entry_date.split("/")[1].equals(_temp_currentmonth))
            {
                if (profile.entry_date.split("/")[2].equals(_temp_currentyear)) {
                    reportData.filtered_profiles.add(profile);
                    switch (profile.status)
                    {
                        case "NEW":
                            reportData.setStatus_new(reportData.getStatus_new() + 1);
                            break;
                        case "KYC":
                            reportData.setStatus_kyc(reportData.getStatus_kyc() + 1);
                            break;
                        case "LOG":
                            reportData.setStatus_log(reportData.getStatus_log() + 1);
                            break;
                        case "SAN":
                            reportData.setStatus_san(reportData.getStatus_san() + 1);
                            break;
                        case "DIS":
                            reportData.setStatus_dis(reportData.getStatus_dis() + 1);
                            break;
                    }
                }
            }
        }
        return reportData;
    }

    public static ReportData getReportDataForWeek(List<Profile> all_profiles, String reportDate){
        ReportData reportData = new ReportData();

        String _temp_basedate = ReportUtils.get_formatted_date(reportDate);
        try{
            for (Profile profile : all_profiles) {
                if (isDateInCurrentWeek(new SimpleDateFormat("dd/MM/yyyy").parse(profile.entry_date),_temp_basedate)) {
                    reportData.filtered_profiles.add(profile);
                    switch (profile.status) {
                        case "NEW": reportData.setStatus_new(reportData.getStatus_new() + 1); break;
                        case "KYC": reportData.setStatus_kyc(reportData.getStatus_kyc() + 1); break;
                        case "LOG": reportData.setStatus_log(reportData.getStatus_log() + 1); break;
                        case "SAN": reportData.setStatus_san(reportData.getStatus_san() + 1); break;
                        case "DIS": reportData.setStatus_dis(reportData.getStatus_dis() + 1); break;
                    }
                }
            }
        }catch (Exception e){
            Log.d("WEEKLY", e.getMessage());
        }
        return reportData;
    }

    public static void setBackgroundForTextView(Context mContext, TextView fill_view, TextView border_view_1, TextView border_view_2){
        fill_view.setBackground(mContext.getDrawable(R.drawable.button_blue_fill));
        fill_view.setTextColor(mContext.getColor(R.color.white));
        border_view_1.setBackground(mContext.getDrawable(R.drawable.button_blue_border));
        border_view_1.setTextColor(mContext.getColor(R.color.black));
        border_view_2.setBackground(mContext.getDrawable(R.drawable.button_blue_border));
        border_view_2.setTextColor(mContext.getColor(R.color.black));
    }

}
