package fukie.afterall.utils;

import android.support.v4.content.ContextCompat;

import java.util.Map;

import fukie.afterall.R;
import fukie.afterall.activities.MainActivity;

/**
 * Created by Fukie on 27/05/2016.
 */
public class Constants {
    public static final int EVENT_ANNIVERSARY = 1;
    public static final int EVENT_EDUCATION = 2;
    public static final int EVENT_JOB = 3;
    public static final int EVENT_LIFE = 4;
    public static final int EVENT_TRIP = 5;
    public static final int EVENT_OTHER = 6;

    public static final int COLOR_PINK = 1;
    public static final int COLOR_RED = 2;
    public static final int COLOR_BLUE = 3;
    public static final int COLOR_GREEN = 4;
    public static final int COLOR_YELLOW = 5;
    public static final int COLOR_GRAY = 7;
    public static final int COLOR_BLACK = 6;

    public static final String DATABASE_NAME = "afterAll.db";
    public static final String EVENT_TABLE_NAME = "event";
    public static final String KIND_TABLE_NAME = "kind";
    public static final String EVENT_COLUMN_ID = "event_id";
    public static final String EVENT_COLUMN_NAME = "event_name";
    public static final String KIND_COLUMN_ID = "kind_id";
    public static final String KIND_COLUMN_NAME = "kind_name";
    public static final String KIND_COLUMN_COLOR = "kind_color";
    public static final String EVENT_COLUMN_DATE = "event_date";
    public static final String EVENT_COLUMN_LOOP = "event_loop";
    public static final String EVENT_COLUMN_NOTIFICATION = "event_notification";
    public static final String EVENT_COLUMN_IMAGE = "event_image";


    // references to our images
    public static Integer[] background = {
            R.drawable.bg2, R.drawable.bg1,
            R.drawable.bg3, R.drawable.bg4, R.drawable.bg1,
            R.drawable.bg3, R.drawable.bg4, R.drawable.bg1,
            R.drawable.bg3, R.drawable.bg4, R.drawable.bg1,
            R.drawable.bg3, R.drawable.bg4, R.drawable.bg1
    };

    public static Integer[] eventColor = {
            R.color.pink_reduced,
            R.color.red_transparent,
            R.color.blue_transparent,
            R.color.green_transparent,
            R.color.yellow_transparent,
            R.color.gray_transparent,
            R.color.black_transparent
    };
}
