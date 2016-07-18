package com.cf.testdemo.viewadapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cf.testdemo.R;
import com.cf.testdemo.model.data.CountriesData;
import com.cf.testdemo.view.CountriesActivity;
import com.cf.testdemo.viewholder.CountriesListViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by arunkt on 23/06/16.
 */
public class CountriesListAdapter extends ArrayAdapter<CountriesData> {

    private ArrayList<CountriesData> leadsArrayList = null;
    private CountriesActivity context = null;

    private ArrayList<CountriesData> searchArrayList = null;
    private AppsFilter filter = null;

    public CountriesListAdapter(CountriesActivity context, ArrayList<CountriesData> appsLists) {
        super(context, R.layout.countries_list_item, appsLists);
        this.context = context;
        leadsArrayList = new ArrayList<CountriesData>();
        searchArrayList = new ArrayList<CountriesData>();
        leadsArrayList.addAll(appsLists);
        searchArrayList.addAll(appsLists);

    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new AppsFilter();
        }
        return filter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CountriesData data = (CountriesData)getItem(position);
        CountriesListViewHolder viewHolder;
        if (convertView == null) {

            viewHolder = new CountriesListViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.countries_list_item, parent, false);

            viewHolder.nameTextView = (TextView)convertView.findViewById(R.id.name_textView);
            viewHolder.name_officialTextView = (TextView)convertView.findViewById(R.id.phone_textView);
            viewHolder.flafImageView = (ImageView) convertView.findViewById(R.id.imageView);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (CountriesListViewHolder) convertView.getTag();
        }

        viewHolder.nameTextView.setText(data.getName());
        viewHolder.name_officialTextView.setText(data.getName_official());

        Picasso.with(context)
                .load(data.getFlag_32())
                .resize(32,16)
                .into(viewHolder.flafImageView);

        return convertView;
    }


    private class AppsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (!TextUtils.isEmpty(constraint)) {
                ArrayList<CountriesData> filteredItems = new ArrayList<CountriesData>();
                for (CountriesData appsListsData : leadsArrayList) {
                    if (appsListsData.convertToString().toLowerCase().contains(constraint)) {
                        filteredItems.add(appsListsData);
                    }
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            } else {
                synchronized (this) {
                    result.values = leadsArrayList;
                    result.count = leadsArrayList.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,FilterResults results) {
            searchArrayList = (ArrayList<CountriesData>) results.values;
            notifyDataSetChanged();
            clear();
            if (searchArrayList.size() > 0) {
                for (CountriesData appsListsData : searchArrayList) {
                    add(appsListsData);
                }
            }
            notifyDataSetInvalidated();
        }

    }


}
