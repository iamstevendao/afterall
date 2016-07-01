package fukie.afterall.utils;

import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.RelativeSizeSpan;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import fukie.afterall.R;
import fukie.afterall.activities.MainActivity;

/**
 * Created by Fukie on 23/05/2016.
 */
public class Events {
    private int id;
    private String name;
    private int kind;
    private int color;
    private String date;
    private String dateOfLoop;
    private int diff;
    private int loop;
    private int img;

    public Events(int id, String name, int kind, int color, String date
            , int loop, int img) {
        this.id = id;
        this.name = name;
        this.kind = kind;
        this.color = color;
        this.date = date;
        this.loop = loop;
        this.img = img;
        try {
            this.diff = getDiffDate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getDiffDate() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        int diffDays;
        Date dateOfEvent = sdf.parse(date);
        Date today = new Date();
        if (loop == 1) {
            Calendar c = Calendar.getInstance();
            c.setTime(dateOfEvent);
            while (today.after(c.getTime())) {
                c.add(Calendar.YEAR, 1);
            }
            this.dateOfLoop = sdf.format(c.getTime());
            long diff = c.getTime().getTime() - today.getTime();
            diffDays = (int) (diff / (60 * 60 * 1000 * 24));
        } else {
            this.dateOfLoop = sdf.format(dateOfEvent);
            long diff = dateOfEvent.getTime() - today.getTime();
            diffDays = (int) (diff / (60 * 60 * 1000 * 24));
        }
        if ((diffDays == 0 && !dateOfLoop.equals(sdf.format(today))) || (diffDays > 0)) {
            diffDays++;
        }
        return diffDays;
    }

    public int getDiff() {
        return diff;
    }

    public String getName() {
        return name;
    }

    public int getKind() {
        return kind;
    }

    public int getColor() {
        return color;
    }

    public String getDate() {
        return date;
    }

    public int getImg() {
        return img;
    }

    public int getLoop() {
        return loop;
    }

    public int getId() {
        return id;
    }
//    public String getDiffString() throws Exception {
//        String string = "";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//        Calendar now = Calendar.getInstance();
//        Calendar event = Calendar.getInstance();
//        now.setTime(new Date());
//        event.setTime(sdf.parse(date));
//        int diffYear = now.get(Calendar.YEAR) - event.get(Calendar.YEAR);
//        if (event.get(Calendar.MONTH) > now.get(Calendar.MONTH) ||
//                (event.get(Calendar.MONTH) == now.get(Calendar.MONTH)
//                        && event.get(Calendar.DATE) > now.get(Calendar.DATE))) {
//            diffYear--;
//        }
//        string += String.valueOf(diffYear) + "years ";
//        int diffMonth = now.get(Calendar.MONTH) - event.get(Calendar.MONTH);
//        if (event.get(Calendar.MONTH) > now.get(Calendar.MONTH) ||
//                (event.get(Calendar.DATE) == now.get(Calendar.DATE))) {
//            diffMonth--;
//        }
//        string += String.valueOf(diffMonth) + "months ";
//        int diffDay = now.get(Calendar.DATE) - event.get(Calendar.DATE);
//        string += String.valueOf(diffDay) + "days ";
//        return string;
//    }

    public SpannableString getDiffString() throws Exception {
        SpannableString spannableString;
        String string = "";
        int sizeDate = MainActivity.context.getResources()
                .getDimensionPixelSize(R.dimen.size_date_diff);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        Date event;
        if (loop == 0)
            event = sdf.parse(date);
        else
            event = sdf.parse(dateOfLoop);
        DateTime then = new DateTime(event);
        DateTime now = new DateTime();
        Period period = new Period(then, now);
        int year = period.getYears();
        int month = period.getMonths();
        int day = period.getDays() + period.getWeeks() * 7;
        if (year < 0 || month < 0 || day < 0) {
            year = -year;
            month = -month;
            day = -day;
            day++;//fix bug 160701
            string += "> ";
        } else if (year == 0 && month == 0 && day == 0) {
            if (sdf.format(event).equals(now.toString(fmt))) {
                string = "TODAY";
                spannableString = new SpannableString(string);
                spannableString.setSpan(new AbsoluteSizeSpan(sizeDate), 0, string.length(), 0);
                return spannableString;
            } else {
                string += "> ";
                day++;
            }
        } else string += "> ";

        String yearx = String.valueOf(year);
        String monthx = String.valueOf(month);
        String dayx = String.valueOf(day);
        boolean haveYear = false, haveMonth = false, haveDay = false;
        int startYear = 2, startMonth = 2, startDay = 2;
        if (year != 0) {
            haveYear = true;
            string += yearx + " years ";
            startMonth = string.length();
        }
        if (month != 0) {
            haveMonth = true;
            string += monthx + " months ";
            startDay = string.length();
        }
        if (day != 0) {
            haveDay = true;
            string += dayx + " days ";
        }
        spannableString = new SpannableString(string);
        if (haveYear)
            spannableString.setSpan(new AbsoluteSizeSpan(sizeDate), startYear, startYear + yearx.length(), 0);
        if (haveMonth)
            spannableString.setSpan(new AbsoluteSizeSpan(sizeDate), startMonth, startMonth + monthx.length(), 0);
        if (haveDay)
            spannableString.setSpan(new AbsoluteSizeSpan(sizeDate), startDay, startDay + dayx.length(), 0);
        return spannableString;
    }
}
