package fukie.afterall.items;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ramotion.foldingcell.FoldingCell;

import java.util.HashSet;
import java.util.List;

import fukie.afterall.utils.BitmapBlur;
import fukie.afterall.utils.Constant;
import fukie.afterall.utils.Events;
import fukie.afterall.R;

/**
 * Created by Fukie on 13/06/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<Events> objects;
    private Context mContext;
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();

    public RecyclerAdapter(Context context, List<Events> cur) {
        mContext = context;
        objects = cur;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitleName;
        TextView txtTitleCount;
        ImageView imgTitleEvent;
        ImageView imgTitleArrow;
        View imgTitleBlur;

        TextView txtContentName;
        TextView txtContentDate;
        TextView txtContentDiffDate;
        TextView txtContentCategory;
        ImageView imgContentEvent;
        ImageView imgContentLoop;
        ImageView imgContentNoti;

        public ViewHolder(View v, Context context) {
            super(v);
            this.txtTitleName = (TextView) v.findViewById(R.id.title_txt_name);
            this.txtTitleCount = (TextView) v.findViewById(R.id.title_txt_count);
            this.imgTitleEvent = (ImageView) v.findViewById(R.id.title_image_event);
            this.imgTitleBlur = v.findViewById(R.id.title_view_blur);
            Typeface face=Typeface.createFromAsset(context.getAssets(), "fonts/MobileSans.ttf");
            this.txtTitleCount.setTypeface(face);
            this.imgTitleArrow = (ImageView) v.findViewById(R.id.title_image_arrow);

            this.txtContentName = (TextView) v.findViewById(R.id.content_txt_name);
            this.txtContentDate = (TextView) v.findViewById(R.id.content_txt_date);
            this.txtContentDiffDate = (TextView) v.findViewById(R.id.content_txt_diff_date);
            this.txtContentCategory = (TextView) v.findViewById(R.id.content_txt_category);
            this.imgContentEvent = (ImageView) v.findViewById(R.id.content_img_event);
            this.imgContentLoop = (ImageView) v.findViewById(R.id.content_img_loop);
            this.imgContentNoti = (ImageView) v.findViewById(R.id.content_img_noti);
        }
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Events listViewItem = objects.get(position);
        if (unfoldedIndexes.contains(position)) {
            ((FoldingCell) viewHolder.itemView).unfold(true);
        } else {
            ((FoldingCell) viewHolder.itemView).fold(true);
        }
        switch (listViewItem.getColor()) {
            case Constant.COLOR_PINK:
                ((FoldingCell) viewHolder.itemView).initialize(1200, ContextCompat.getColor(mContext
                        , R.color.pink_transparent), 1);
                viewHolder.imgTitleBlur.setBackgroundResource(R.drawable.blur_pink);
                break;
            case Constant.COLOR_RED:
                ((FoldingCell) viewHolder.itemView).initialize(1200, ContextCompat.getColor(mContext
                        , R.color.red_transparent), 1);
                viewHolder.imgTitleBlur.setBackgroundResource(R.drawable.blur_red);
                break;
            case Constant.COLOR_BLUE:
                ((FoldingCell) viewHolder.itemView).initialize(1200, ContextCompat.getColor(mContext
                        , R.color.blue_transparent), 1);
                viewHolder.imgTitleBlur.setBackgroundResource(R.drawable.blur_blue);
                break;
            case Constant.COLOR_GREEN:
                ((FoldingCell) viewHolder.itemView).initialize(1200, ContextCompat.getColor(mContext
                        , R.color.green_transparent), 1);
                viewHolder.imgTitleBlur.setBackgroundResource(R.drawable.blur_green);
                break;
            case Constant.COLOR_YELLOW:
                ((FoldingCell) viewHolder.itemView).initialize(1200, ContextCompat.getColor(mContext
                        , R.color.yellow_transparent), 1);
                viewHolder.imgTitleBlur.setBackgroundResource(R.drawable.blur_yellow);
                break;
            case Constant.COLOR_GRAY:
                ((FoldingCell) viewHolder.itemView).initialize(1200, ContextCompat.getColor(mContext
                        , R.color.gray_transparent), 1);
                viewHolder.imgTitleBlur.setBackgroundResource(R.drawable.blur_gray);
                break;
            case Constant.COLOR_BLACK:
                ((FoldingCell) viewHolder.itemView).initialize(1200, ContextCompat.getColor(mContext
                        , R.color.black_transparent), 1);
                viewHolder.imgTitleBlur.setBackgroundResource(R.drawable.blur_black);
                break;
        }

        switch (listViewItem.getKind()) {
            case Constant.EVENT_ANNIVERSARY:
               // viewHolder.imgTitleEvent.setImageResource(R.drawable.anniversary);
                viewHolder.txtContentCategory.setText("ANNIVERSARY");
                break;
            case Constant.EVENT_EDUCATION:
              //  viewHolder.imgTitleEvent.setImageResource(R.drawable.education);
                viewHolder.txtContentCategory.setText("EDUCATION");
                break;
            case Constant.EVENT_JOB:
              //  viewHolder.imgTitleEvent.setImageResource(R.drawable.job);
                viewHolder.txtContentCategory.setText("JOB");
                break;
            case Constant.EVENT_LIFE:
               // viewHolder.imgTitleEvent.setImageResource(R.drawable.life);
                viewHolder.txtContentCategory.setText("LIFE");
                break;
            case Constant.EVENT_TRIP:
              //  viewHolder.imgTitleEvent.setImageResource(R.drawable.trip);
                viewHolder.txtContentCategory.setText("TRIP");
                break;
            default:
              //  viewHolder.imgTitleEvent.setImageResource(R.drawable.other);
                viewHolder.txtContentCategory.setText("OTHERS");
                break;
        }
        viewHolder.txtTitleName.setText(listViewItem.getName());

        if(listViewItem.getDiff() > 0) {
            viewHolder.txtTitleCount.setText(String.valueOf(listViewItem.getDiff()));
            viewHolder.imgTitleArrow.setImageResource(R.drawable.arrow_right);
        } else if(listViewItem.getDiff() == 0){
            viewHolder.imgTitleArrow.setVisibility(View.GONE);
            viewHolder.txtTitleCount.setText(String.valueOf(0));
        } else
            viewHolder.txtTitleCount.setText(String.valueOf(-listViewItem.getDiff()));

        viewHolder.txtContentDate.setText(listViewItem.getDate());
        viewHolder.txtContentName.setText(listViewItem.getName());
        try {
            viewHolder.txtContentDiffDate.setText(listViewItem.getDiffString());
        }catch (Exception e){
            e.printStackTrace();
        }
        int imageResource = mContext.getResources()
                .getIdentifier(listViewItem.getImageUri(), null, mContext.getPackageName());
        viewHolder.imgTitleEvent.setImageResource(imageResource);
        viewHolder.imgContentEvent.setImageResource(imageResource);
        if(listViewItem.isLoop())
            viewHolder.imgContentLoop.setImageResource(R.drawable.img_true);
        if(listViewItem.isNotification())
            viewHolder.imgContentNoti.setImageResource(R.drawable.img_true);

    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell, parent, false);
        return new ViewHolder(v, mContext);
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }
}


