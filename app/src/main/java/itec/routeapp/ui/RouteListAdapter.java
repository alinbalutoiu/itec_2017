package itec.routeapp.ui;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import itec.routeapp.R;
import itec.routeapp.model.Route;
import itec.routeapp.utils.DateUtils;

/**
 * Created by Mihaela Ilin on 4/9/2017.
 */

public class RouteListAdapter extends BaseAdapter{

    private Context mContext;
    private int layoutResourceId;
    private List<Route> items;

    public RouteListAdapter(Context c, int layoutResourceId,
                              @NonNull List<Route> items) {
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

    public View getView(int position, View view, ViewGroup parent) {
        ItemHolder itemHolder;
        if (view == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            itemHolder = new ItemHolder();

            view = inflater.inflate(layoutResourceId, parent, false);
            itemHolder.timestamp = (TextView) view.findViewById(R.id.tTime);
            itemHolder.distance = (TextView) view.findViewById(R.id.distValue);
            itemHolder.speed = (TextView) view.findViewById(R.id.speedValue);

            view.setTag(itemHolder);

        } else {
            itemHolder = (ItemHolder) view.getTag();
        }

        final Route route = items.get(position);

        itemHolder.timestamp.setText(DateUtils.getFormattedStringFromMillis(route.getTimeMillis()));
        itemHolder.distance.setText(String.valueOf(route.getTotalDistance()/1000) + " km");
        itemHolder.speed.setText(String.valueOf(route.getAverageSpeed()) + " mps");

        return view;
    }

    private static class ItemHolder {
        TextView timestamp;
        TextView distance;
        TextView speed;
    }

}
