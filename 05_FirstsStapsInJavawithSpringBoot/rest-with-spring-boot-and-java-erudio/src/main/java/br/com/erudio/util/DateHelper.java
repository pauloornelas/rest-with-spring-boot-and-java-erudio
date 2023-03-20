package br.com.erudio.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {

	public static final String DATE_PATTERN_DEFAULT_API = "dd/MM/yyyy";
	public static final String DATE_WITH_TIME_PATTERN_DEFAULT_API = "dd/MM/yyyy HH:mm:ss";

	public static String PATTERN_DATE_YYYYMMDD = "yyyy-MM-dd";
	public static String PATTERN_DATE_HOUR_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";

	private DateHelper() {}

	public static Date getTodayDateWithoutTime() {
		Calendar cal = Calendar.getInstance(); // locale-specific
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Date(cal.getTimeInMillis());
	}

	public static Date getTodayDateWithTime() {
		Calendar cal = Calendar.getInstance(); // locale-specific
		cal.setTime(new Date());
		return new Date(cal.getTimeInMillis());
	}

	public static Date getDateWithoutTime(Date paramDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(paramDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date getDateWithAddedOneMonth(Date paramDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(paramDate);
		calendar.add(Calendar.MONTH, 1);
		return calendar.getTime();
	}

	public static boolean isDateInInterval(Date dateToCompare, Date dateStart, Date dateEnd) {
		boolean isDateInInterval = false;
		if ( (dateToCompare.equals(dateStart)) || dateToCompare.after(dateStart) && (dateToCompare.before(dateEnd) || dateToCompare.equals(dateEnd)) ) {
			isDateInInterval = true;
		}
		return isDateInInterval;
	}

	public static Integer getAttributeOfDate(Date dateParam, Integer calendarField) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateParam);
		return calendar.get(calendarField);
	}

	public static String formatDateToPattern(Date date, String pattern) {
		String result;
		if (date != null && pattern != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			result = sdf.format(date);
		} else {
			result = "";
		}
		return result;
	}

	public static Date parseStringToPattern(String date, String pattern) throws ParseException {
		Date result = null;
		if (date != null && pattern != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			result = sdf.parse(date);
		}
		return result;
	}

	public static boolean isDateEqualsOrAfterToday(Date dateToCompare) {
		boolean isDateEqualsOrAfterToday = false;
		Date today = getTodayDateWithoutTime();
		if ( dateToCompare.after(today) || dateToCompare.equals(today) ) {
			isDateEqualsOrAfterToday = true;
		}
		return isDateEqualsOrAfterToday;
	}

}