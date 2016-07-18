package com.cf.testdemo.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class CFUtil {

	public static double getRoundUp(double d) {
		long l = (int) Math.round(d * 100);
		d = l / 100.0;
		return d;
	}

	public static String getAmountFormate(double amount) {
		amount = getRoundUp(amount);
		DecimalFormat formatter = new DecimalFormat("##,##,##,##,##,##,##0.00");
		return formatter.format(amount);
	}

	public static String getFormatedDate(String dataTimeValue, String pattern,
			String applyPattern) {
		if (dataTimeValue != null && !dataTimeValue.trim().equalsIgnoreCase("")) {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			try {
				Date date = sdf.parse(dataTimeValue);
				// dataTimeValue =
				// DateFormat.getDateTimeInstance().format(date);
				sdf.applyPattern(applyPattern);
				dataTimeValue = sdf.format(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return dataTimeValue;
	}

	public static Date getDate(String dataTimeValue, String pattern,
							   String applyPattern) {
		Date date = null;
		if (dataTimeValue != null && !dataTimeValue.trim().equalsIgnoreCase("")) {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);// yyyy-MM-dd'T'HH:mm:ss
			try {
				date = sdf.parse(dataTimeValue);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}

	public static Date getDate(String dataTimeValue, String pattern) {
		Date date = null;
		if (dataTimeValue != null && !dataTimeValue.trim().equalsIgnoreCase("")) {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);// yyyy-MM-dd'T'HH:mm:ss
			try {
				date = sdf.parse(dataTimeValue);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}

	public static String getMonthName(int month){
		switch(month){
			case 1:
				return "Jan";
			case 2:
				return "Feb";
			case 3:
				return "Mar";
			case 4:
				return "Apr";
			case 5:
				return "May";
			case 6:
				return "Jun";
			case 7:
				return "Jul";
			case 8:
				return "Aug";
			case 9:
				return "Sep";
			case 10:
				return "Oct";
			case 11:
				return "Nov";
			case 12:
				return "Dec";
		}
		return "";
	}

}