package itec.routeapp.ui;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import itec.routeapp.R;
import itec.routeapp.model.Month;
import itec.routeapp.model.MonthlyAchievement;

/**
 * Created by Mihaela Ilin on 4/8/2017.
 */

public class AchievementAdapter extends BaseAdapter{

    private Context mContext;
    private int layoutResourceId;
    private List<MonthlyAchievement> items;

    public AchievementAdapter(Context c, int layoutResourceId,
                              @NonNull List<MonthlyAchievement> items) {
        mContext = c;
        this.items = items;
        this.layoutResourceId = layoutResourceId;
    }

    public int getCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return items.get(position);
    }

    public long getItemId(int position) {
        return items.get(position).hashCode();
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View view, ViewGroup parent) {
        ItemHolder itemHolder;
        if (view == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            itemHolder = new ItemHolder();

            view = inflater.inflate(layoutResourceId, parent, false);
            itemHolder.month = (TextView) view.findViewById(R.id.tMonth);
            itemHolder.achievementTitle = (TextView) view.findViewById(R.id.tPrize);
            itemHolder.counter = (TextView) view.findViewById(R.id.tCounter);

            view.setTag(itemHolder);

        } else {
            itemHolder = (ItemHolder) view.getTag();
        }

        final MonthlyAchievement ach = items.get(position);

        itemHolder.month.setText(Month.values()[ach.getMonth()].getTitle());
        //this should be parametrizable (if we had more types of achievements)
        itemHolder.achievementTitle.setText("Eco friendly: ");
        itemHolder.counter.setText(ach.getTotalAchievements());

        return view;
    }

    private static class ItemHolder {
        TextView month;
        TextView achievementTitle;
        TextView counter;
    }

}
