package fukie.afterall.items;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import fukie.afterall.Constant;
import fukie.afterall.Events;
import fukie.afterall.R;

/**
 * Created by Fukie on 13/06/2016.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<Events> objects;
    private Context mContext;

    public EventAdapter(Context context, List<Events> cur) {
        mContext = context;
        objects = cur;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtName;
        TextView txtCount;
        ImageView imgEvent;
        RelativeLayout layoutBackground;

        public ViewHolder(View v){
            super(v);
            this.txtName = (TextView) v.findViewById(R.id.lstItemName);
            this.txtCount = (TextView) v.findViewById(R.id.lstItemCount);
            this.imgEvent = (ImageView) v.findViewById(R.id.lstItemImage);
            this.layoutBackground = (RelativeLayout) v.findViewById(R.id.lstItemHolder);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Events listViewItem = objects.get(position);
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
    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        Events listViewItem = objects.get(position);
//        ViewHolder viewHolder;
//        if(convertView == null) {
//            convertView = LayoutInflater.from(mContext).inflate
//                    (R.layout.listview_item, parent, false);
//            viewHolder = new ViewHolder(convertView);
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//
//
//        return convertView;
//    }

    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

}
