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
    private String memory;

    public Events(String name, int kind, int color, String date, boolean loop, String memory) {
        this.name = name;
        this.kind = kind;
        this.color = color;
        this.date = date;
        this.loop = loop;
        this.memory = memory;
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
                c.setTime(dateOfEvent);
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

    public boolean isLoop() {
        return loop;
    }

    public String getMemory() {
        return memory;
    }
}
