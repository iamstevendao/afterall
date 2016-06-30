package fukie.afterall.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Fukie on 23/05/2016.
 */
public class Events {
    private int id;
    private String name;
    private int kind;
    private int color;
    private String date;
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
            long diff = c.getTime().getTime() - today.getTime();
            diffDays = (int) (diff / (60 * 60 * 1000 * 24));
        } else {
            long diff = dateOfEvent.getTime() - today.getTime();
            diffDays = (int) (diff / (60 * 60 * 1000 * 24));
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

    public int getImg() { return img;}

    public int getLoop() {
        return loop;
    }

    public int getId() {return id;}
    public String getDiffString() throws Exception {
        String string = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar now = Calendar.getInstance();
        Calendar event = Calendar.getInstance();
        now.setTime(new Date());
        event.setTime(sdf.parse(date));
        int diffYear = now.get(Calendar.YEAR) - event.get(Calendar.YEAR);
        if (event.get(Calendar.MONTH) > now.get(Calendar.MONTH) ||
                (event.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                        && event.get(Calendar.DATE) > now.get(Calendar.DATE))) {
            diffYear--;
        }
        string += String.valueOf(diffYear) + "years ";
        int diffMonth = now.get(Calendar.MONTH) - event.get(Calendar.MONTH);
        if (event.get(Calendar.MONTH) > now.get(Calendar.MONTH) ||
                (event.get(Calendar.DATE) == now.get(Calendar.DATE))) {
            diffMonth--;
        }
        string += String.valueOf(diffMonth) + "months ";
        int diffDay = now.get(Calendar.DATE) - event.get(Calendar.DATE);
        string += String.valueOf(diffDay) + "days ";
        return string;
    }
}
