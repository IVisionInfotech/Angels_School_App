package com.ivision.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.ivision.databinding.RecycleNewResulItemListBinding;
import com.ivision.databinding.RecycleResulItemListBinding;
import com.ivision.model.Result;
import com.ivision.utils.ClickListener;
import com.ivision.utils.Session;

import java.util.List;

public class NewResultAdapter extends RecyclerView.Adapter {

    private ClickListener listener;
    private Context context;
    private Session session;
    private List<String> subjectList, mark, subtotal, subrank;


    public NewResultAdapter(Context context, List<String> listSubject, List<String> mark, List<String> subrank, List<String> subtotal, ClickListener listener) {
        this.context = context;
        this.session = new Session(context);
        this.mark = mark;
        this.subtotal = subtotal;
        this.subrank = subrank;
        this.subjectList = listSubject;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NewResultAdapter.MyViewHolder listHolder = null;
        listHolder = new NewResultAdapter.MyViewHolder(RecycleNewResulItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        NewResultAdapter.MyViewHolder finalListHolder = listHolder;
        listHolder.itemView.setOnClickListener(view -> listener.onItemSelected(finalListHolder.getBindingAdapterPosition()));
        return listHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof NewResultAdapter.MyViewHolder) {

            final NewResultAdapter.MyViewHolder mainHolder = (NewResultAdapter.MyViewHolder) holder;

                mainHolder.binding.tvTotalMarks.setText(subjectList.get(position));
                mainHolder.binding.tvObtainedMarks.setText(mark.get(position) + "/" + subtotal.get(position));
                mainHolder.binding.tvRank.setText(subrank.get(position));
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
        return (null != subjectList ? subjectList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RecycleNewResulItemListBinding binding;

        MyViewHolder(RecycleNewResulItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
