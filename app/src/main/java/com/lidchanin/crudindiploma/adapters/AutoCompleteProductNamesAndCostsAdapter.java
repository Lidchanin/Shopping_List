package com.lidchanin.crudindiploma.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.data.models.Product;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteProductNamesAndCostsAdapter extends ArrayAdapter<Product> {

    private final List<Product> products;
    private List<Product> resultList = new ArrayList<>();

    public AutoCompleteProductNamesAndCostsAdapter(Context context, List<Product> products) {
        super(context, 0, products);
        this.products = products;
    }

    @Nullable
    @Override
    public Product getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new NamesAndCostFilter(this, products);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Product product = resultList.get(position);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.item_auto_complete_name_and_cost, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.auto_complete_name);
        TextView tvCost = (TextView) convertView.findViewById(R.id.auto_complete_cost);
        tvName.setText(product.getName());
        tvCost.setText(String.valueOf(product.getCost()));
        return convertView;
    }

    private class NamesAndCostFilter extends Filter {

        AutoCompleteProductNamesAndCostsAdapter adapter;
        List<Product> originalList;
        List<Product> filteredList;

        NamesAndCostFilter(AutoCompleteProductNamesAndCostsAdapter adapter, List<Product> originalList) {
            super();
            this.adapter = adapter;
            this.originalList = originalList;
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                // Your filtering logic goes in here
                for (final Product dog : originalList) {
                    if (dog.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(dog);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.resultList.clear();
            adapter.resultList.addAll((List) results.values);
            adapter.notifyDataSetChanged();
        }
    }
}
