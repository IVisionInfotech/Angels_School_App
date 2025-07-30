package com.ivision.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import com.ivision.databinding.ProgressbarBinding;
import com.ivision.databinding.RecyclerviewNotificationListItemBinding;
import com.ivision.model.Notification;
import com.ivision.utils.ClickListener;
import com.ivision.utils.Session;

import java.util.ArrayList;
import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter implements Filterable {

    final int VIEW_TYPE_ITEM = 0;
    final int VIEW_TYPE_LOADING = 1;
    private ClickListener listener;
    private Context context;
    private Session session;
    private List<Notification> list;
    private List<Notification> filterList;

    public NotificationListAdapter(Context context, List<Notification> list, ClickListener listener) {
        this.context = context;
        this.session = new Session(context);
        this.list = list;
        this.filterList = list;
        this.listener = listener;
    }

    public void addLoadingView() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                filterList.add(null);
                notifyItemInserted(filterList.size() - 1);
            }
        });
    }

    public void removeLoadingView() {
        if (filterList.size() > 0) {
            filterList.remove(filterList.size() - 1);
            notifyItemRemoved(filterList.size());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder listHolder = null;
        if (viewType == VIEW_TYPE_ITEM) {
            listHolder = new MyViewHolder(RecyclerviewNotificationListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == VIEW_TYPE_LOADING) {
            listHolder = new MyViewHolder(ProgressbarBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            return listHolder;
        }
        MyViewHolder finalListHolder = listHolder;
        listHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemSelected(finalListHolder.getBindingAdapterPosition());
            }
        });
        return listHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final Notification model = filterList.get(position);

        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            if (holder instanceof MyViewHolder) {

                final MyViewHolder mainHolder = (MyViewHolder) holder;

                mainHolder.binding.tvTitle.setText(model.getTitle());
                mainHolder.binding.tvDateTime.setText(model.getDesc());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return filterList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (null != filterList ? filterList.size() : 0);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filterList = list;
                } else {
                    List<Notification> newFilteredList = new ArrayList<>();
                    for (Notification row : list) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            newFilteredList.add(row);
                        }
                    }
                    filterList = newFilteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filterList = (ArrayList<Notification>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RecyclerviewNotificationListItemBinding binding;
        private ProgressbarBinding progressbarBinding;

        MyViewHolder(RecyclerviewNotificationListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        MyViewHolder(ProgressbarBinding progressbarBinding) {
            super(progressbarBinding.getRoot());
            this.progressbarBinding = progressbarBinding;
        }
    }
}