package com.ivision.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.ivision.databinding.RecycleResulItemListBinding;
import com.ivision.model.Result;
import com.ivision.utils.ClickListener;
import com.ivision.utils.Common;
import com.ivision.utils.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResultAdapter extends RecyclerView.Adapter {

    private ClickListener listener;
    private Context context;
    private Session session;
    private List<Result> list;
    private List<Result> filterList;
    private List<String> subjectList = new ArrayList<>();
    private List<String> mark = new ArrayList<>();
    private List<String> subtotal = new ArrayList<>();
    private List<String> subrank = new ArrayList<>();


    public ResultAdapter(Context context, List<Result> list, ClickListener listener) {
        this.context = context;
        this.session = new Session(context);
        this.list = list;
        this.filterList = list;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ResultAdapter.MyViewHolder listHolder = null;
        listHolder = new ResultAdapter.MyViewHolder(RecycleResulItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        ResultAdapter.MyViewHolder finalListHolder = listHolder;
        listHolder.itemView.setOnClickListener(view -> listener.onItemSelected(finalListHolder.getBindingAdapterPosition()));
        return listHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Result model = filterList.get(position);

        int sum = 0;
        if (holder instanceof ResultAdapter.MyViewHolder) {

            final ResultAdapter.MyViewHolder mainHolder = (ResultAdapter.MyViewHolder) holder;

            for (int i = 0; i < model.getMark().size(); i++) {
                sum += Integer.parseInt(model.getMark().get(i));
            }

            mainHolder.binding.tvExamDate.setText(Common.changeDateFormat(model.getDate(),"dd-MM-yyyy"));
            mainHolder.binding.tvTotalMarks.setText(model.getTestname());
            mainHolder.binding.tvObtainedMarks.setText(sum + "/" + model.getTotal());
            mainHolder.binding.tvObtainedMarks1.setText(model.getTrank());


            NewResultAdapter adapter = new NewResultAdapter(context, model.getListSubject(), model.getMark(), model.getSubrank(), model.getSubtotal(), new ClickListener() {
                @Override
                public void onItemSelected(int position) {
                }
            });

            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            mainHolder.binding.recyclerView.setLayoutManager(layoutManager);
            mainHolder.binding.recyclerView.setAdapter(adapter);


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

        private RecycleResulItemListBinding binding;

        MyViewHolder(RecycleResulItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
