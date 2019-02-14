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

public class PropertyListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<PropertyItem> objects;

    PropertyListAdapter(Context ctx, ArrayList<PropertyItem> items) {
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
            view = layoutInflater.inflate(R.layout.item_property_list_view, parent, false);
        }

        PropertyItem item = (PropertyItem) getItem(position);

        ((TextView) view.findViewById(R.id.item_property_list_view_title)).setText(item.Title);
        ((TextView) view.findViewById(R.id.item_property_list_view_address)).setText(item.Address);
        ((TextView) view.findViewById(R.id.item_property_list_view_price)).setText(Utils.setPriceConvenientFormat(item.Price) + " \u20BD");

        return view;
    }
}
