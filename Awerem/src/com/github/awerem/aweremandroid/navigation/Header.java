package com.github.awerem.aweremandroid.navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.awerem.aweremandroid.R;
import com.github.awerem.aweremandroid.navigation.NavigationArrayAdapter.RowType;

public class Header implements Item
{
    private String name;

    public Header(String name)
    {
        this.name = name;
    }

    @Override
    public int getViewType()
    {
        return RowType.HEADER_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView)
    {
        View view;
        if (convertView == null)
        {
            view = (View) inflater.inflate(R.layout.drawer_list_header, null);
        }
        else
        {
            view = convertView;
        }

        TextView text = (TextView) view.findViewById(R.id.navheader);
        text.setText(name);

        return view;
    }

}
