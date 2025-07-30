package com.ivision.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ivision.R;
import com.ivision.adapter.StudentsListAdapter;
import com.ivision.model.User;
import com.ivision.utils.ClickListener;
import com.ivision.utils.Common;
import com.ivision.utils.RealmController;

import java.util.ArrayList;

public class StudentBottomSheetDialog extends BottomSheetDialogFragment {

    private Context context;
    private TextView tvDialogTitle;
    private RecyclerView recyclerView;
    private StudentsListAdapter adapter;
    private ArrayList<User> list = new ArrayList<>();
    private ItemClickListener itemClickListener;
    private String id = "";

    public static StudentBottomSheetDialog newInstance() {
        return new StudentBottomSheetDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_recyclerview_list, container, false);
        this.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        context = getActivity();

        id = Common.getStudentId(context);

        init(view);

        bindRecyclerView();

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
        return new BottomSheetDialog(requireContext(), R.style.BottomSheetDialog); // To have transparent dialog window background.
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemClickListener) {
            itemClickListener = (ItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        itemClickListener = null;
    }

    private void init(View view) {

        tvDialogTitle = view.findViewById(R.id.tvDialogTitle);
        tvDialogTitle.setText("Select Student");

        recyclerView = view.findViewById(R.id.recyclerView);
    }

    private void bindRecyclerView() {

        list = new ArrayList<>(RealmController.with(context).getAllUser());

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);

        adapter = new StudentsListAdapter(context, list, id, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
                id = String.valueOf(list.get(position).getId());
                itemClickListener.onItemClick(id);
                dismiss();
            }
        });

        recyclerView.setAdapter(adapter);
    }

    public interface ItemClickListener {
        void onItemClick(String model);
    }
}
