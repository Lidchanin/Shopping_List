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
        return new DogsFilter(this, products);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item from filtered list.
        Product dog = resultList.get(position);

        // Inflate your custom row layout as usual.
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.item_auto_complete_name_and_cost, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.auto_complete_name);
        TextView tvCost = (TextView) convertView.findViewById(R.id.auto_complete_cost);
        tvName.setText(dog.getName());
        tvCost.setText(String.valueOf(dog.getCost()));
        return convertView;
    }

    private class DogsFilter extends Filter {

        AutoCompleteProductNamesAndCostsAdapter adapter;
        List<Product> originalList;
        List<Product> filteredList;

        public DogsFilter(AutoCompleteProductNamesAndCostsAdapter adapter, List<Product> originalList) {
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
    /*private List<Product> products, suggestions;

    public AutoCompleteProductNamesAndCostsAdapter(Context context, List<Product> products) {
        super(context, android.R.layout.simple_list_item_1, products);
        this.products = products;
        this.suggestions = new ArrayList<>(products);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Product product = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_auto_complete_name_and_cost, parent, false);
        }
        TextView textViewName = (TextView) convertView.findViewById(R.id.auto_complete_name);
        TextView textViewCost = (TextView) convertView.findViewById(R.id.auto_complete_cost);

        Log.d("MY_LOG", "____________________________");
        if (textViewName != null) {
            if (product != null) {
                textViewName.setText(product.getName());
                Log.d("MY_LOG", "name = " + product.getName());
            } else
                textViewName.setText(null);
        }

        if (textViewCost != null) {
            if (product != null) {
                textViewCost.setText(String.valueOf(product.getCost()));
                Log.d("MY_LOG", "cost = " + product.getCost());
            } else
                textViewCost.setText(null);
        }

        if (textViewName != null) {
            textViewName.setText(product != null ? product.getName() : null);
        }
        if (textViewCost != null) {
            textViewCost.setText(product != null ? String.valueOf(product.getCost()) : null);
        }
        // TODO: 23.05.2017 need this part?
//        if (position % 2 == 0) {
//            convertView.setBackgroundColor(Integer.parseInt("#eeeeee"));
//        } else {
//            convertView.setBackgroundColor(Integer.parseInt("#ffffff"));
//        }
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return nameAndCostFilter;
    }

    private Filter nameAndCostFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                final String filterPattern = constraint.toString().toLowerCase();
                for (Product product : products) {
                    if (product.getName().toLowerCase().contains(filterPattern)) {
                        suggestions.add(product);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Product> p = (ArrayList<Product>) results.values;
            if (results.count > 0) {
                clear();
                for (Product product : p) {
                    add(product);
                    notifyDataSetChanged();
                }
            }
        }
    };*/

}
