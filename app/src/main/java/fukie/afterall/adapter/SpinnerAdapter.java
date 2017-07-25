package fukie.afterall.adapter;

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

import fukie.afterall.utils.Constants;
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
        LinearLayout layoutHolder;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        cursor.moveToPosition(position);
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, null);
            viewHolder = new ViewHolder();
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txtSpinnerItem);
            viewHolder.layoutHolder = (LinearLayout)
                    convertView.findViewById(R.id.layoutSpinnerHolder);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        switch (cursor.getInt(cursor.getColumnIndex(Constants.KIND_COLUMN_COLOR))) {
            case Constants.COLOR_PINK:
                viewHolder.layoutHolder.setBackgroundColor(ContextCompat.getColor(context
                        , R.color.pink_reduced));
                break;
            case Constants.COLOR_RED:
                viewHolder.layoutHolder.setBackgroundColor(ContextCompat.getColor(context
                        , R.color.red_transparent));
                break;
            case Constants.COLOR_BLUE:
                viewHolder.layoutHolder.setBackgroundColor(ContextCompat.getColor(context
                        , R.color.blue_transparent));
                break;
            case Constants.COLOR_GREEN:
                viewHolder.layoutHolder.setBackgroundColor(ContextCompat.getColor(context
                        , R.color.green_transparent));
                break;
            case Constants.COLOR_YELLOW:
                viewHolder.layoutHolder.setBackgroundColor(ContextCompat.getColor(context
                        , R.color.yellow_transparent));
                break;
            case Constants.COLOR_GRAY:
                viewHolder.layoutHolder.setBackgroundColor(ContextCompat.getColor(context
                        , R.color.gray_transparent));
                break;
            case Constants.COLOR_BLACK:
                viewHolder.layoutHolder.setBackgroundColor(ContextCompat.getColor(context
                        , R.color.black_transparent));
                break;
        }
        viewHolder.txtName.setText(cursor.getString(
                cursor.getColumnIndex(Constants.KIND_COLUMN_NAME)));
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
