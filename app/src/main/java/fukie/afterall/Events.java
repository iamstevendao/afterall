package fukie.afterall;

import java.util.Date;

/**
 * Created by Fukie on 23/05/2016.
 */
public class Events {
    private String name;
    private int kind;
    private int color;
    private String date;
    private boolean loop;
    private String memory;

    public Events(String name, int kind, int color, String date, boolean loop, String memory){
        this.name = name;
        this.kind = kind;
        this.color = color;
        this.date = date;
        this.loop = loop;
        this.memory = memory;
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
