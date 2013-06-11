package com.github.awerem.aweremandroid.navigation;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/* Inspired from http://stackoverflow.com/questions/13590627/android-listview-headers */

public class NavigationArrayAdapter extends ArrayAdapter<Item>
{
    private List<Item> items;
    private LayoutInflater inflater;

    public enum RowType
    {
        LIST_ITEM, HEADER_ITEM
    }

    public NavigationArrayAdapter(Context context, ArrayList<Item> items)
    {
        super(context, 0, items);
        inflater = LayoutInflater.from(context);
        this.items = items;
    }

    @Override
    public int getViewTypeCount()
    {
        return RowType.values().length;

    }

    @Override
    public int getItemViewType(int position)
    {
        return items.get(position).getViewType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return items.get(position).getView(inflater, convertView);
    }
    
    @Override
    public boolean isEnabled(int position)
    {
        if (items.get(position).getViewType() == RowType.HEADER_ITEM.ordinal())
            return false;
        else
            return true;
    }
}
