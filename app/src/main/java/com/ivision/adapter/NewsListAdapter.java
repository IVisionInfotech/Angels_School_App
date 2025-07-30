package com.ivision.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import com.ivision.R;
import com.ivision.databinding.ProgressbarBinding;
import com.ivision.databinding.RecyclerviewNewsListItemBinding;
import com.ivision.model.News;
import com.ivision.utils.ClickListener;
import com.ivision.utils.Common;
import com.ivision.utils.Session;

import java.util.ArrayList;
import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter implements Filterable {

    final int VIEW_TYPE_ITEM = 0;
    final int VIEW_TYPE_LOADING = 1;
    private ClickListener listener, listener2;
    private Context context;
    private Session session;
    private List<News> list;
    private List<News> filterList;
    private int type = 0; //0 vertical, 1 horizontal

    public NewsListAdapter(Context context, List<News> list, ClickListener listener, ClickListener listener2) {
        this.context = context;
        this.session = new Session(context);
        this.list = list;
        this.filterList = list;
        this.listener = listener;
        this.listener2 = listener2;
    }

    public NewsListAdapter(Context context, List<News> list, int type, ClickListener listener) {
        this.context = context;
        this.session = new Session(context);
        this.list = list;
        this.filterList = list;
        this.type = type;
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
            listHolder = new MyViewHolder(RecyclerviewNewsListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final News model = filterList.get(position);

        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            if (holder instanceof MyViewHolder) {

                final MyViewHolder mainHolder = (MyViewHolder) holder;

                if (type == 1)
                    mainHolder.binding.cvMain.getLayoutParams().width = (int) context.getResources().getDimension(com.intuit.sdp.R.dimen._200sdp);

                mainHolder.binding.tvTitle.setText(model.getTitle());
                mainHolder.binding.tvDetails.setText(model.getMsg());
                mainHolder.binding.tvDateTime.setText(Common.changeDateFormat(model.getDate(), "dd-MM-yyyy", "dd MMM, yyyy"));

                mainHolder.binding.ivDownload.setVisibility((model.getFile() == null || model.getFile().isEmpty()) ? View.GONE : View.VISIBLE);
                mainHolder.binding.ivDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener2.onItemSelected(position);
                    }
                });
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
                    List<News> newFilteredList = new ArrayList<>();
                    for (News row : list) {
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
                filterList = (ArrayList<News>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RecyclerviewNewsListItemBinding binding;
        private ProgressbarBinding progressbarBinding;

        MyViewHolder(RecyclerviewNewsListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        MyViewHolder(ProgressbarBinding progressbarBinding) {
            super(progressbarBinding.getRoot());
            this.progressbarBinding = progressbarBinding;
        }
    }
}