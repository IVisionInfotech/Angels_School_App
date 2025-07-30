package com.ivision.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.ivision.databinding.RecyclerviewManagementListItemBinding;
import com.ivision.model.Management;
import com.ivision.utils.ClickListener;
import com.ivision.utils.Common;
import com.ivision.utils.Session;

import java.util.List;

public class ManagementListAdapter extends RecyclerView.Adapter {

    private ClickListener listener;
    private Context context;
    private Session session;
    private List<Management> list;
    private List<Management> filterList;
    private int type = 0; //0 vertical, 1 horizontal

    public ManagementListAdapter(Context context, List<Management> list, ClickListener listener) {
        this.context = context;
        this.session = new Session(context);
        this.list = list;
        this.filterList = list;
        this.listener = listener;
    }

    public ManagementListAdapter(Context context, List<Management> list, int type, ClickListener listener) {
        this.context = context;
        this.session = new Session(context);
        this.list = list;
        this.filterList = list;
        this.type = type;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder listHolder = null;
        listHolder = new MyViewHolder(RecyclerviewManagementListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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

        final Management model = filterList.get(position);

        if (holder instanceof MyViewHolder) {

            final MyViewHolder mainHolder = (MyViewHolder) holder;

            if (type == 1)
                mainHolder.binding.cvMain.getLayoutParams().width = (int) context.getResources().getDimension(com.intuit.sdp.R.dimen._175sdp);

            Common.loadImage(context, mainHolder.binding.ivImage, model.getImg());
            mainHolder.binding.tvTitle.setText(model.getName());
            mainHolder.binding.tvMainTitle.setText(model.getDesignation());
            mainHolder.binding.tvDescription.setText(model.getDescription());
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

        private RecyclerviewManagementListItemBinding binding;

        MyViewHolder(RecyclerviewManagementListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}