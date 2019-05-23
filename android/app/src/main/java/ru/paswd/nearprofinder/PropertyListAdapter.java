package ru.paswd.nearprofinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.paswd.nearprofinder.model.Utils;

public class PropertyListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<PropertyItem> objects;

    PropertyListAdapter(Context ctx, List<PropertyItem> items) {
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

        ((TextView) view.findViewById(R.id.itemPropertyListViewTitle)).setText(item.Title);
        ((TextView) view.findViewById(R.id.itemPropertyListViewAddress)).setText(item.Address);
        ((TextView) view.findViewById(R.id.itemPropertyListViewPrice)).setText(Utils.setPriceConvenientFormat(item.Price) + " \u20BD");

        return view;
    }
}
