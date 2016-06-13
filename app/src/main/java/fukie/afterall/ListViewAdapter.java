package fukie.afterall;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Fukie on 13/06/2016.
 */
public class ListViewAdapter extends BaseAdapter {

    private List<Events> objects;

    private Context mContext;

    public ListViewAdapter(Context context, List<Events> cur) {
        super();
        mContext = context;
        objects = cur;
    }

    public int getCount() {
        return objects.size();
    }


    private class ViewHolder {
        TextView txtName;
        TextView txtCount;
        ImageView imgEvent;
        RelativeLayout layoutBackground;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Events listViewItem = objects.get(position);
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.lstItemName);
            viewHolder.txtCount = (TextView) convertView.findViewById(R.id.lstItemCount);
            viewHolder.imgEvent = (ImageView) convertView.findViewById(R.id.lstItemImage);
            viewHolder.layoutBackground = (RelativeLayout)
                    convertView.findViewById(R.id.lstItemHolder);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        switch (listViewItem.getColor()) {
            case Constant.COLOR_PINK:
                viewHolder.layoutBackground.setBackgroundColor(ContextCompat.getColor(mContext
                        , R.color.pink_transparent));
                break;
            case Constant.COLOR_RED:
                viewHolder.layoutBackground.setBackgroundColor(ContextCompat.getColor(mContext
                        , R.color.red_transparent));
                break;
            case Constant.COLOR_BLUE:
                viewHolder.layoutBackground.setBackgroundColor(ContextCompat.getColor(mContext
                        , R.color.blue_transparent));
                break;
            case Constant.COLOR_GREEN:
                viewHolder.layoutBackground.setBackgroundColor(ContextCompat.getColor(mContext
                        , R.color.green_transparent));
                break;
            case Constant.COLOR_YELLOW:
                viewHolder.layoutBackground.setBackgroundColor(ContextCompat.getColor(mContext
                        , R.color.yellow_transparent));
                break;
            case Constant.COLOR_BROWN:
                viewHolder.layoutBackground.setBackgroundColor(ContextCompat.getColor(mContext
                        , R.color.brown_transparent));
                break;
            case Constant.COLOR_GRAY:
                viewHolder.layoutBackground.setBackgroundColor(ContextCompat.getColor(mContext
                        , R.color.gray_transparent));
                break;
            case Constant.COLOR_BLACK:
                viewHolder.layoutBackground.setBackgroundColor(ContextCompat.getColor(mContext
                        , R.color.black_transparent));
                break;
        }

        switch (listViewItem.getKind()){
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
        viewHolder.txtName.setText(listViewItem.getName());
        viewHolder.txtCount.setText(String.valueOf(listViewItem.getDiff()));

        return convertView;
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

}
