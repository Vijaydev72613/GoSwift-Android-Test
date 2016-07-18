
package com.cf.testdemo.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class DateHndler {

    public static String[] DayOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    public static String getFormattedDate(String pDate,boolean pIsDateTime) {
        String lRetVal = pDate;
        if (null != pDate && pDate.length() > 0) {
            Calendar lCurCal = getCalendarFromISOString(pDate);
            if (null != lCurCal) {
                lRetVal = getFormattedDateTime(lCurCal.getTime().getTime(),pIsDateTime);
            }
        }
        return lRetVal;
    }
    
    
    
    public static String getFormattedDateTime(String pTime,boolean pIsDateTime) {
        String lRetVal = null,
                lYear = null,
                lMonth = null,
                lDay = null,
                lHr = null,
                lMin = null,
                lSec = null;
        
        
        long lCalVal = 0;
        int lIndex = -1;
        try {
            lIndex = pTime.indexOf('-');
            if (0 <= lIndex) {
                lYear = pTime.substring(0, lIndex);
                lYear = lYear.substring(2, lYear.length());
            }
            pTime = pTime.substring(lIndex + 1);
            lIndex = pTime.indexOf('-');
            if (0 <= lIndex) {
                lMonth = pTime.substring(0, lIndex);
            }
            pTime = pTime.substring(lIndex + 1);
            if(true == pIsDateTime){
	            lIndex = pTime.indexOf('T');
	            if (0 <= lIndex) {
	                lDay = pTime.substring(0, lIndex);
	                pTime = pTime.substring(lIndex + 1);  
	                lIndex = pTime.indexOf(':');
	                if (0 <= lIndex) {
	                    lHr = pTime.substring(0, lIndex);
	                }
	                pTime = pTime.substring(lIndex + 1);
	                lIndex = pTime.indexOf(':');
	                if (0 <= lIndex) {
	                    lMin = pTime.substring(0, lIndex);
	                }
	                pTime = pTime.substring(lIndex + 1);
	                lSec = pTime;
	                
	                lRetVal = lMonth + "/" + lDay + "/" + lYear + " " + lHr + ":" + lMin + ":" + lSec;
	                
	                Calendar lCalender = getCalendarFromString(lRetVal);
	                
	                lCalVal = lCalender.getTime().getTime();
	                
	                lCalVal = convertToCurTimeZone(lCalVal);
	                lRetVal = getFormattedDateTime(lCalVal,pIsDateTime);
	            } else {
	                lDay = pTime.substring(0);
	                lRetVal = lMonth + "/" + lDay + "/" + lYear;
	            }
            }
            else {
                lDay = pTime.substring(0,2);
                lRetVal = lDay + "/" + lMonth + "/" + lYear;
                
            }
        } catch (Exception ex) {
        
        }
        return lRetVal;
    }
    
    
    public static String recalculateTime(String pExtTime, String pTimeZone) {
        String lRetVal = null;
        long lCalVal = 0;
        try {
            if (null != pExtTime) {
                
                Calendar lCalendar = getCalendarFromString(pExtTime);
                if (null != pTimeZone) {
                    lCalendar.setTimeZone(TimeZone.getTimeZone(pTimeZone));
                }
                
                lCalVal = lCalendar.getTime().getTime();
                
                lCalVal = convertToCurTimeZone(lCalVal);
                lRetVal = getFormattedDateTime(lCalVal,true);
            }
        } catch (Exception ex) {
        
        }
        return lRetVal;
    }

    public static String getFormattedDateTime(long pTimeMillis,boolean pIsDateTime) {
        String lRetVal = null;
        SimpleDateFormat lDateFormat = null;
        
        if(true == pIsDateTime)
        	lDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
        else
        	lDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        lRetVal = lDateFormat.format(pTimeMillis);
        return lRetVal;
    }

    public static String getFormattedDateTimeISO(long pTimeMillis) {
        String lRetVal = null;
        int		lMonth = 0;
        
        SimpleDateFormat lDateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        
        lRetVal = lDateFormat.format(pTimeMillis);
        lRetVal = String.valueOf(lDateFormat.getCalendar().getTime().getYear() + 1900);
        if(lDateFormat.getCalendar().getTime().getMonth() > 9) {
        	lRetVal += "-"+ String.valueOf(lDateFormat.getCalendar().getTime().getMonth());
        }
        else {
        	lMonth = lDateFormat.getCalendar().getTime().getMonth();
        	if(0 == lMonth){
        		lMonth += 12;
        		lRetVal += "-"+ String.valueOf(lMonth);
        	}
        	else
        		lRetVal += "-0"+ String.valueOf(lMonth);
        }
        if(lDateFormat.getCalendar().getTime().getDate() > 9) {
        	lRetVal += "-"+ String.valueOf(lDateFormat.getCalendar().getTime().getDate());
        }
        else {
        	lRetVal += "-0"+ String.valueOf(lDateFormat.getCalendar().getTime().getDate());
        }
        lRetVal += "T";
        if(lDateFormat.getCalendar().getTime().getHours() > 9) {
        	lRetVal += String.valueOf(lDateFormat.getCalendar().getTime().getHours());
        }
        else {
        	lRetVal += "0"+String.valueOf(lDateFormat.getCalendar().getTime().getHours());
        }
        if(lDateFormat.getCalendar().getTime().getMinutes() > 9) {
        	lRetVal += ":" +String.valueOf(lDateFormat.getCalendar().getTime().getMinutes());
        }
        else {
        	lRetVal += ":0" +String.valueOf(lDateFormat.getCalendar().getTime().getMinutes());
        }
        if(lDateFormat.getCalendar().getTime().getSeconds() > 9) {
        	lRetVal += ":" +String.valueOf(lDateFormat.getCalendar().getTime().getSeconds());
        }
        else {
        	lRetVal += ":0" +String.valueOf(lDateFormat.getCalendar().getTime().getSeconds());
        }
        
        int lRawOffset = TimeZone.getDefault().getRawOffset();
        lRawOffset /= (1000 * 60); 
        lRetVal += (lRawOffset > 0) ? "+" : "-";
        if (Math.abs(lRawOffset / 60) < 9) {
            lRetVal += "0";
        }
        lRetVal += String.valueOf(Math.abs(lRawOffset / 60)) + ":";
        if (Math.abs(lRawOffset % 60) < 9) {
            lRetVal += "0";
        }
        lRetVal += String.valueOf(Math.abs(lRawOffset % 60));
        return lRetVal;
    }
   
    public static String getFormattedDate(long pTimeMillis) {
        String lRetVal = null;
        SimpleDateFormat lDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        lRetVal = lDateFormat.format(pTimeMillis);
        return lRetVal;
    }
    
    public static String getFormattedDateForFileName(long pTimeMillis) {
        String lRetVal = null;
        SimpleDateFormat lDateFormat = new SimpleDateFormat("ddmmyyyyhhmmss");
        lRetVal = lDateFormat.format(pTimeMillis);
        return lRetVal;
    }

    public static long convertToCurTimeZone(long pTime) {
        long lRetVal = 0;
        Calendar lCal = Calendar.getInstance();
        lCal.setTime(new Date(pTime));
        lCal.setTimeZone(TimeZone.getDefault());
        lRetVal = lCal.getTime().getTime();
        return lRetVal;
    }

    public static long convertToGMTTimeZone(long pTime) {
        long lRetVal = 0;
        Calendar lCal = Calendar.getInstance();
        lCal.setTimeZone(TimeZone.getTimeZone("GMT"));
        lCal.setTime(new Date(pTime));
        lRetVal = lCal.getTime().getTime();
        return lRetVal;
    }

    public static String getEndDate(String pDateString, int pDuration) {
        String lRetVal = null;
        Calendar lCalender = getCalendarFromString(pDateString);
        lRetVal = getFormattedDateTime(lCalender.getTime().getTime() + pDuration * 1000,true);
        return lRetVal;
    }

    public static String getCurrentTimezone() {
        String lRetVal = null;
        Calendar lCalendar = Calendar.getInstance();
        TimeZone lTimeZone = lCalendar.getTimeZone();
        lRetVal = lTimeZone.getID();
        return lRetVal;
    }

    
    public static String getNextAndPrevMonthDate(String pCurDate, boolean pNextMonth) {
        long OneMonthPeriod = 2592000000L;  
        String lRetVal = null;
        try {
            if (null != pCurDate) {
                
                Calendar lCalendar = getCalendarFromString(pCurDate);
                
                long lCalVal = lCalendar.getTime().getTime();
                
                if (true == pNextMonth) {
                    lCalVal += OneMonthPeriod;
                } else {
                    lCalVal -= OneMonthPeriod;
                }
                lCalendar = Calendar.getInstance();
                lCalendar.setTime(new Date(lCalVal));
                
                lCalendar.set(Calendar.HOUR_OF_DAY, 12);
                lCalendar.set(Calendar.MINUTE, 0);
                lCalendar.set(Calendar.SECOND, 0);
                lCalendar.set(Calendar.MILLISECOND, 0);
                lCalendar.set(Calendar.AM_PM, Calendar.AM);
                lCalVal = lCalendar.getTime().getTime();
                
                lRetVal = getFormattedDateTime(lCalVal,true);
            }
        } catch (Exception ex) {
        
        }
        return lRetVal;
    }

    public static String getGMTTimeInISOFormat(long pTime) {
        String lRetVal = null;

        Calendar lCal = Calendar.getInstance();
        lCal.setTimeZone(TimeZone.getTimeZone("GMT"));
        lCal.setTime(new Date(pTime));

        lRetVal = String.valueOf(lCal.get(Calendar.YEAR)) + "-";
        if ((lCal.get(Calendar.MONTH) + 1) < 10) {
            lRetVal += "0";
        }
        lRetVal += String.valueOf(lCal.get(Calendar.MONTH) + 1) + "-";
        if (lCal.get(Calendar.DAY_OF_MONTH) < 10) {
            lRetVal += "0";
        }
        lRetVal += String.valueOf(lCal.get(Calendar.DAY_OF_MONTH)) + "T";
        if (lCal.get(Calendar.HOUR_OF_DAY) < 10) {
            lRetVal += "0";
        }
        lRetVal += String.valueOf(lCal.get(Calendar.HOUR_OF_DAY)) + ":";
        if (lCal.get(Calendar.MINUTE) < 10) {
            lRetVal += "0";
        }
        lRetVal += String.valueOf(lCal.get(Calendar.MINUTE)) + ":";
        if (lCal.get(Calendar.SECOND) < 10) {
            lRetVal += "0";
        }
        lRetVal += String.valueOf(lCal.get(Calendar.SECOND)) + "Z";
        return lRetVal;
    }

    
    public static String convertToISOFormat(String pDateString, String pTZoneStr) {
        String lDateTime = pDateString;
        int lIndex = 0;
        SimpleDateFormat lDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        try {
            if (null != pDateString && 4 != pDateString.indexOf("-")) {
                
                Calendar lCalendar = getCalendarFromString(pDateString);
                if (null != lCalendar) {
                    lCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));
                    
                    long lCalVal = lCalendar.getTime().getTime();
                    pDateString = lDateFormat.format(lCalVal);
                    lIndex = pDateString.lastIndexOf('/');
                    lDateTime = pDateString.substring(lIndex + 1, lIndex + 5);
                    lDateTime += "-";
                    lDateTime += pDateString.substring(0, pDateString.indexOf('/'));
                    lDateTime += "-";
                    lDateTime += pDateString.substring(pDateString.indexOf('/') + 1, pDateString.lastIndexOf('/'));
                    if (0 <= pDateString.indexOf(' ')) {
                        String lSubString = null;
                        lDateTime += "T";
                        lSubString = pDateString.substring(pDateString.lastIndexOf('/') + 6, pDateString.indexOf(':'));
                        if (lSubString.length() < 2) {
                            lSubString = "0" + lSubString;
                        }
                        lDateTime += lSubString;
                        pDateString = pDateString.substring(pDateString.indexOf(':'));
                        lIndex = pDateString.lastIndexOf(' ');
                        if (-1 != lIndex) {
                            lDateTime += pDateString.substring(0, lIndex);
                        } else {
                            lDateTime += pDateString.substring(0);
                        }
                        if (null == pTZoneStr) {
                            lDateTime += "+00:00";
                        } else {
                            lDateTime += pTZoneStr;
                        }
                    }
                }
            }
        } catch (Exception ex) {
        
        }
        return lDateTime;
    }

    public static int dateDiffFromLastSun(String pCurDay) {
        int i, lCnt;
        lCnt = DayOfWeek.length;
        for (i = 0; i < lCnt; i++) {
            if (DayOfWeek[i].compareTo(pCurDay) == 0) {
                break;
            }
        }
        return i;
    }
    
    public static Calendar getCalendarFromString(String pEntitytime) {
        Calendar lCurCal = Calendar.getInstance();
        String lDateString = null;
        try {
            if (null != pEntitytime) {
                int lIndex = pEntitytime.indexOf('/');
                if (-1 != lIndex) {
                    lDateString = pEntitytime.substring(0, lIndex);
                    lCurCal.set(Calendar.MONTH, Integer.parseInt(lDateString) - 1);
                    pEntitytime = pEntitytime.substring(lIndex + 1);

                    lIndex = pEntitytime.indexOf('/');
                    lDateString = pEntitytime.substring(0, lIndex);
                    lCurCal.set(Calendar.DATE, Integer.parseInt(lDateString));
                    pEntitytime = pEntitytime.substring(lIndex + 1);

                    lIndex = pEntitytime.indexOf(' ');
                    if (-1 != lIndex) {
                        lDateString = pEntitytime.substring(0, lIndex);
                        lCurCal.set(Calendar.YEAR, Integer.parseInt(lDateString));
                        pEntitytime = pEntitytime.substring(lIndex + 1);

                        lIndex = pEntitytime.indexOf(':');
                        lDateString = pEntitytime.substring(0, lIndex);
                        if (pEntitytime.toUpperCase().indexOf("AM") != -1 ||
                                pEntitytime.toUpperCase().indexOf("PM") != -1) {
                            lCurCal.set(Calendar.HOUR, Integer.parseInt(lDateString));
                        } else {
                            lCurCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(lDateString));
                        }
                        pEntitytime = pEntitytime.substring(lIndex + 1);

                        lIndex = pEntitytime.indexOf(':');
                        lDateString = pEntitytime.substring(0, lIndex);
                        lCurCal.set(Calendar.MINUTE, Integer.parseInt(lDateString));
                        pEntitytime = pEntitytime.substring(lIndex + 1);

                        lDateString = pEntitytime.substring(0, 2);
                        if (lDateString.equals("00")) {
                            lDateString = "0";
                        }
                        lCurCal.set(Calendar.SECOND, Integer.parseInt(lDateString));
                        lCurCal.set(Calendar.MILLISECOND, 0);
                        pEntitytime = pEntitytime.substring(2);

                        lIndex = pEntitytime.indexOf('+');
                        if (-1 == lIndex) {
                            lIndex = pEntitytime.indexOf('-');
                        }
                        if (-1 != lIndex) {
                            lDateString = pEntitytime.substring(lIndex);
                            TimeZone lServerTZ = TimeZone.getTimeZone("GMT" + lDateString);
                            lCurCal.setTimeZone(lServerTZ);
                        } else {
                            if (pEntitytime.toUpperCase().indexOf("AM") != -1) {
                                lCurCal.set(Calendar.AM_PM, Calendar.AM);
                            } else if (pEntitytime.toUpperCase().indexOf("PM") != -1) {
                                lCurCal.set(Calendar.AM_PM, Calendar.PM);
                            }
                            if (pEntitytime.toUpperCase().indexOf("000Z") != -1) {
                                lCurCal.setTimeZone(TimeZone.getTimeZone("GMT" + "+00.00"));
                            } else {
                                lCurCal.setTimeZone(TimeZone.getDefault());
                            }
                        }
                    } else {
                        lCurCal.set(Calendar.YEAR, Integer.parseInt(pEntitytime));
                    }
                }
            }
        } catch (Exception ex) {
        
        }
        return lCurCal;
    }

    
    public static Calendar getCalendarFromISOString(String pEntitytime) {
        String lDateString = null;
        Calendar lCurCal = Calendar.getInstance();
        try {
            if (null != pEntitytime) {
                int lIndex = pEntitytime.indexOf('-');

                
                lDateString = pEntitytime.substring(0, lIndex);
                lCurCal.set(Calendar.YEAR, Integer.parseInt(lDateString));
                pEntitytime = pEntitytime.substring(lIndex + 1);

                
                lIndex = pEntitytime.indexOf('-');
                lDateString = pEntitytime.substring(0, lIndex);
                lCurCal.set(Calendar.MONTH, Integer.parseInt(lDateString) -1);
                pEntitytime = pEntitytime.substring(lIndex + 1);

                lIndex = pEntitytime.indexOf('T');
                if (-1 != lIndex) {
                    
                    lDateString = pEntitytime.substring(0, lIndex);
                    lCurCal.set(Calendar.DATE, Integer.parseInt(lDateString));
                    pEntitytime = pEntitytime.substring(lIndex + 1);

                    
                    lIndex = pEntitytime.indexOf(':');
                    lDateString = pEntitytime.substring(0, lIndex);
                    if (pEntitytime.toUpperCase().indexOf("AM") != -1 ||
                            pEntitytime.toUpperCase().indexOf("PM") != -1) {
                        lCurCal.set(Calendar.HOUR, Integer.parseInt(lDateString));
                    } else {
                        lCurCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(lDateString));
                    }
                    pEntitytime = pEntitytime.substring(lIndex + 1);

                    
                    lIndex = pEntitytime.indexOf(':');
                    lDateString = pEntitytime.substring(0, lIndex);
                    lCurCal.set(Calendar.MINUTE, Integer.parseInt(lDateString));
                    pEntitytime = pEntitytime.substring(lIndex + 1);

                    
                    lDateString = pEntitytime.substring(0, 2);
                    if (lDateString.equals("00")) {
                        lDateString = "0";
                    }
                    lCurCal.set(Calendar.SECOND, Integer.parseInt(lDateString));
                    lCurCal.set(Calendar.MILLISECOND, 0);
                    pEntitytime = pEntitytime.substring(2);

                    lIndex = pEntitytime.indexOf('+');
                    if (-1 == lIndex) {
                        lIndex = pEntitytime.indexOf('-');
                    }
                    if (-1 != lIndex) {
                        lDateString = pEntitytime.substring(lIndex);
                        TimeZone lServerTZ = TimeZone.getTimeZone("GMT" + lDateString);
                        lCurCal.setTimeZone(lServerTZ);
                    } else {
                        if (pEntitytime.toUpperCase().indexOf("AM") != -1) {
                            lCurCal.set(Calendar.AM_PM, Calendar.AM);
                        } else if (pEntitytime.toUpperCase().indexOf("PM") != -1) {
                            lCurCal.set(Calendar.AM_PM, Calendar.PM);
                        }
                        if (pEntitytime.toUpperCase().indexOf("000Z") != -1 ||
                                pEntitytime.toUpperCase().indexOf("Z") != -1) {
                            lCurCal.setTimeZone(TimeZone.getTimeZone("GMT" + "+00.00"));
                        } else {
                            lCurCal.setTimeZone(TimeZone.getDefault());
                        }
                    }
                } else {
                    lCurCal.set(Calendar.DATE, Integer.parseInt(pEntitytime));
                }
            }
        } catch (Exception ex) {
        
        }
        return lCurCal;
    }
    
    
    public static boolean compareTime(String pEntitytime, boolean pLastWeek) {
        Calendar lCurCal = Calendar.getInstance();
        Date lObjDate = new Date();
        long lGoBackTime = 0;
        long lLastWeekTime = 0;
        boolean lRetVal = false;
        try {
            if (null != pEntitytime) {
                String lDateString = lObjDate.toString();
                lDateString = lDateString.substring(0, 3);
                lGoBackTime = dateDiffFromLastSun(lDateString);
                lLastWeekTime = lGoBackTime + 7;
                lGoBackTime *= (24 * 3600 * 1000);  
                lObjDate.setTime(lObjDate.getTime() - lGoBackTime);
                lLastWeekTime *= (24 * 3600 * 1000);  
                lCurCal.setTime(lObjDate);
                lCurCal.set(Calendar.HOUR_OF_DAY, 23);
                lCurCal.set(Calendar.MINUTE, 59);
                lCurCal.set(Calendar.SECOND, 59);
                lCurCal.set(Calendar.MILLISECOND, 999);
                lGoBackTime = lCurCal.getTime().getTime();    
                lObjDate.setTime(lObjDate.getTime() - lLastWeekTime);
                lCurCal.setTime(lObjDate);
                lCurCal.set(Calendar.HOUR_OF_DAY, 23);
                lCurCal.set(Calendar.MINUTE, 59);
                lCurCal.set(Calendar.SECOND, 59);
                lCurCal.set(Calendar.MILLISECOND, 999);
                lLastWeekTime = lCurCal.getTime().getTime();    
                lCurCal = getCalendarFromString(pEntitytime);
                if (false == pLastWeek) {
                    lRetVal = (lGoBackTime < lCurCal.getTime().getTime());
                } else {
                    if (lGoBackTime >= lCurCal.getTime().getTime() && lLastWeekTime < lCurCal.getTime().getTime()) {
                        lRetVal = true;
                    }
                }
            }
        } catch (Exception ex) {
        
        }
        return lRetVal;
    }

    public static long getDate(String pDate) {
        long lRetVal = 0;
        try {
            if (null != pDate) {
                Calendar lCal = getCalendarFromISOString(pDate);
                lRetVal = lCal.getTime().getTime();
            }
        } catch (Exception ex) {
        
        }
        return lRetVal;
    }

    public static long getNextXDate(String pDate,int pNumDays) {
        long lRetVal = 0;
        try {
            if (null != pDate) {
                Calendar lCal = getCalendarFromISOString(pDate);
                lCal.set(Calendar.DAY_OF_MONTH, lCal.get(Calendar.DAY_OF_MONTH)+pNumDays);
                lRetVal = lCal.getTime().getTime();
            }
        } catch (Exception ex) {
        
        }
        return lRetVal;
    }
}
