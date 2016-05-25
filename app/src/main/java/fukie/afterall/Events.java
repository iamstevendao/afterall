package fukie.afterall;

import java.util.Date;

/**
 * Created by Fukie on 23/05/2016.
 */
public class Events {
    public String name;
    public String type;
    public Date date;
    public boolean loop;
    public String memory;

    public Events(String name, String type, Date date, boolean loop, String memory){
        this.name = name;
        this.type = type;
        this.date = date;
        this.loop = loop;
        this.memory = memory;
    }
}
