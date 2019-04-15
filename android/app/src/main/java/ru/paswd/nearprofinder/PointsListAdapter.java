package ru.paswd.nearprofinder;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PointsListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<PointsItem> objects;

    PointsListAdapter(Context ctx, ArrayList<PointsItem> items) {
        context = ctx;
        objects = items;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_points_list_view, parent, false);
        }

        PointsItem item = (PointsItem) getItem(position);

        ((TextView) view.findViewById(R.id.itemPointsListViewTitle)).setText(item.Title);
        ((TextView) view.findViewById(R.id.itemPointsListViewAddress)).setText(item.Address);
        ((TextView) view.findViewById(R.id.itemPointsListViewCoordinate)).setText(item.Coordinates);

        return view;
    }
}
