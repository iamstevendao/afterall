package fukie.afterall;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Fukie on 23/05/2016.
 */
public class Events {
    private String name;
    private int kind;
    private int color;
    private String date;
    private int diff;
    private boolean loop;
    private boolean notification;
    private int img;

    public Events(String name, int kind, int color, String date
            , boolean loop, boolean notification, int img) {
        this.name = name;
        this.kind = kind;
        this.color = color;
        this.date = date;
        this.loop = loop;
        this.notification = notification;
        this.img = img;
        try {
            this.diff = getDiffDate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getDiffDate() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        int diffDays = 0;
        Date dateOfEvent = sdf.parse(date);
        Date today = new Date();
        if (loop) {
            Calendar c= Calendar.getInstance();
            c.setTime(dateOfEvent);
            while(today.after(c.getTime())){
               // c.setTime(dateOfEvent);
                c.add(Calendar.YEAR, 1);
            }
            date = c.getTime().toString();
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

    public boolean isLoop() {
        return loop;
    }

    public String getDiffString() throws Exception {
        String string = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar now = Calendar.getInstance();
        Calendar event = Calendar.getInstance();
        now.setTime(new Date());
        event.setTime(sdf.parse(date));
        int diffYear = now.get(Calendar.YEAR) - event.get(Calendar.YEAR);
        if (now.get(Calendar.MONTH) > event.get(Calendar.MONTH) ||
                (now.get(Calendar.MONTH) == event.get(Calendar.MONTH)
                        && now.get(Calendar.DATE) > event.get(Calendar.DATE))) {
            diffYear--;
        }
        string += String.valueOf(diffYear) + "years ";
        int diffMonth = now.get(Calendar.MONTH) - event.get(Calendar.MONTH);
        if (now.get(Calendar.MONTH) > event.get(Calendar.MONTH) ||
                (now.get(Calendar.DATE) == event.get(Calendar.DATE))) {
            diffMonth--;
        }
        string += String.valueOf(diffMonth) + "months ";
        int diffDay = now.get(Calendar.DATE) - event.get(Calendar.DATE);
        string += String.valueOf(diffDay) + "days ";
        return string;
    }

    public boolean isNotification() {
        return notification;
    }

    public String getImageUri() {
        return "@drawable/bg" + img;
    }
}
