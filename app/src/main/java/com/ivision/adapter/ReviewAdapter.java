package com.ivision.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.ivision.databinding.RecycleResulItemListBinding;
import com.ivision.databinding.RecycleReviewListBinding;
import com.ivision.model.Result;
import com.ivision.model.Review;
import com.ivision.utils.ClickListener;
import com.ivision.utils.Session;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter {

    private ClickListener listener;
    private Context context;
    private Session session;
    private List<Review> list;
    private List<Review> filterList;

    public ReviewAdapter(Context context, List<Review> list, ClickListener listener) {
        this.context = context;
        this.session = new Session(context);
        this.list = list;
        this.filterList = list;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ReviewAdapter.MyViewHolder listHolder = null;
        listHolder = new ReviewAdapter.MyViewHolder(RecycleReviewListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        ReviewAdapter.MyViewHolder finalListHolder = listHolder;
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

        final Review model = filterList.get(position);

        if (holder instanceof ReviewAdapter.MyViewHolder) {

            final ReviewAdapter.MyViewHolder mainHolder = (ReviewAdapter.MyViewHolder) holder;

            mainHolder.binding.tvReview.setText(model.getReview());
            mainHolder.binding.tvDate.setText(model.getDate());

        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (null != filterList ? filterList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RecycleReviewListBinding binding;

        MyViewHolder(RecycleReviewListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
