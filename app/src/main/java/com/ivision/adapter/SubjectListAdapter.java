package com.ivision.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.ivision.R;
import com.ivision.databinding.ProgressbarBinding;
import com.ivision.databinding.RecyclerviewStudentsListItemBinding;
import com.ivision.model.Subject;
import com.ivision.utils.ClickListener;
import com.ivision.utils.Session;

import java.util.List;

public class SubjectListAdapter extends RecyclerView.Adapter {

    final int VIEW_TYPE_ITEM = 0;
    final int VIEW_TYPE_LOADING = 1;
    private ClickListener listener;
    private Context context;
    private Session session;
    private List<Subject> list;
    private List<Subject> filterList;
    private String id = "0";
    private int type = 0;

    public SubjectListAdapter(Context context, List<Subject> list, String id, ClickListener listener) {
        this.context = context;
        this.session = new Session(context);
        this.list = list;
        this.filterList = list;
        this.id = id;
        this.listener = listener;
    }

    public SubjectListAdapter(Context context, List<Subject> list, int type, String id, ClickListener listener) {
        this.context = context;
        this.session = new Session(context);
        this.list = list;
        this.filterList = list;
        this.type = type;
        this.id = id;
        this.listener = listener;
    }

    public void addLoadingView() {
        new Handler().post(() -> {
            filterList.add(null);
            notifyItemInserted(filterList.size() - 1);
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
            listHolder = new MyViewHolder(RecyclerviewStudentsListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == VIEW_TYPE_LOADING) {
            listHolder = new MyViewHolder(ProgressbarBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            return listHolder;
        }
        MyViewHolder finalListHolder = listHolder;
        listHolder.itemView.setOnClickListener(view -> { });
        return listHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final Subject model = filterList.get(position);

        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            if (holder instanceof MyViewHolder) {

                final MyViewHolder mainHolder = (MyViewHolder) holder;

                if (type == 1)
                    mainHolder.binding.cvMain.getLayoutParams().width = (int) context.getResources().getDimension(com.intuit.sdp.R.dimen._125sdp);

                mainHolder.binding.ivImage.setVisibility(View.GONE);

                if (model.getSubject() != null) {
                    if (!model.getSubject().isEmpty()) {
                        mainHolder.binding.tvTitle.setText(model.getSubject());
                    }
                }

                mainHolder.binding.cvMain.setOnClickListener(view -> {
                    id = String.valueOf(model.getId());
                    listener.onItemSelected(position);
                    notifyDataSetChanged();
                });

                if (id.equals(String.valueOf(model.getId()))) {
                    mainHolder.binding.cvMain.setCardBackgroundColor(context.getResources().getColor(R.color.viewBackground));
                } else {
                    mainHolder.binding.cvMain.setCardBackgroundColor(context.getResources().getColor(R.color.white));
                }
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

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RecyclerviewStudentsListItemBinding binding;
        private ProgressbarBinding progressbarBinding;

        MyViewHolder(RecyclerviewStudentsListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        MyViewHolder(ProgressbarBinding progressbarBinding) {
            super(progressbarBinding.getRoot());
            this.progressbarBinding = progressbarBinding;
        }
    }
}