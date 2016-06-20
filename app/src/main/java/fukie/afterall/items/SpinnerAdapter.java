package fukie.afterall.items;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import fukie.afterall.utils.Constant;
import fukie.afterall.R;

/**
 * Created by Fukie on 13/06/2016.
 */
public class SpinnerAdapter extends BaseAdapter {
    Context context;
    Cursor cursor;
    public SpinnerAdapter(Context context, Cursor cur) {
        super();
        this.context=context;
        this.cursor = cur;
    }

    private class ViewHolder {
        TextView txtName;
        ImageView imgEvent;
        LinearLayout layoutHolder;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        cursor.moveToPosition(position);
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, null);
            viewHolder = new ViewHolder();
            viewHolder.imgEvent = (ImageView) convertView.findViewById(R.id.imgSpinnerItem);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txtSpinnerItem);
            viewHolder.layoutHolder = (LinearLayout)
                    convertView.findViewById(R.id.layoutSpinnerHolder);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        switch (cursor.getInt(cursor.getColumnIndex(Constant.KIND_COLUMN_ID))){
            case Constant.EVENT_ANNIVERSARY:
                viewHolder.imgEvent.setImageResource(R.drawable.anniversary);
                break;
            case Constant.EVENT_EDUCATION:
                viewHolder.imgEvent.setImageResource(R.drawable.education);
                break;
            case Constant.EVENT_JOB:
                viewHolder.imgEvent.setImageResource(R.drawable.job);
                break;
            case Constant.EVENT_LIFE:
                viewHolder.imgEvent.setImageResource(R.drawable.life);
                break;
            case Constant.EVENT_TRIP:
                viewHolder.imgEvent.setImageResource(R.drawable.trip);
                break;
            default:
                viewHolder.imgEvent.setImageResource(R.drawable.other);
                break;
        }

        switch (cursor.getInt(cursor.getColumnIndex(Constant.KIND_COLUMN_COLOR))) {
            case Constant.COLOR_PINK:
                viewHolder.layoutHolder.setBackgroundColor(ContextCompat.getColor(context
                        , R.color.pink_transparent));
                break;
            case Constant.COLOR_RED:
                viewHolder.layoutHolder.setBackgroundColor(ContextCompat.getColor(context
                        , R.color.red_transparent));
                break;
            case Constant.COLOR_BLUE:
                viewHolder.layoutHolder.setBackgroundColor(ContextCompat.getColor(context
                        , R.color.blue_transparent));
                break;
            case Constant.COLOR_GREEN:
                viewHolder.layoutHolder.setBackgroundColor(ContextCompat.getColor(context
                        , R.color.green_transparent));
                break;
            case Constant.COLOR_YELLOW:
                viewHolder.layoutHolder.setBackgroundColor(ContextCompat.getColor(context
                        , R.color.yellow_transparent));
                break;
            case Constant.COLOR_GRAY:
                viewHolder.layoutHolder.setBackgroundColor(ContextCompat.getColor(context
                        , R.color.gray_transparent));
                break;
            case Constant.COLOR_BLACK:
                viewHolder.layoutHolder.setBackgroundColor(ContextCompat.getColor(context
                        , R.color.black_transparent));
                break;
        }
        viewHolder.txtName.setText(cursor.getString(
                cursor.getColumnIndex(Constant.KIND_COLUMN_NAME)));
        return convertView;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
