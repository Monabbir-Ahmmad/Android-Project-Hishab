package com.example.hishab.overview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hishab.R;
import com.example.hishab.data.DataItem;
import com.example.hishab.database.DatabaseHelper;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class OverviewFragment extends Fragment implements FilterDialog.FilterDialogListener {

    private TextView tvExpense;
    private ExtendedFloatingActionButton btnFilter;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private DatabaseHelper databaseHelper;
    private ArrayList<DataItem> dataSet;


    public OverviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        //Find views
        tvExpense = view.findViewById(R.id.textView_expense);
        recyclerView = view.findViewById(R.id.recyclerView);
        btnFilter = view.findViewById(R.id.button_filter);

        databaseHelper = new DatabaseHelper(getActivity());
        dataSet = databaseHelper.getAllData();

        //This calculates the top panel values on startup
        topPanelCalculation();

        btnFilter.setOnClickListener(v -> {
            //This opens the filter dialog
            FilterDialog filterDialog = new FilterDialog();
            filterDialog.setTargetFragment(OverviewFragment.this, 1);
            filterDialog.show(getActivity().getSupportFragmentManager(), "FilterDialog");
        });

        //This creates the RecyclerView
        createRecyclerView();
        createRecyclerViewSwipe();

        return view;
    }


    //Applies the filters
    @Override
    public void applyFilter(String category, String sortBy, long startTimestamp, long endTimestamp) {
        dataSet = databaseHelper.getFilteredData(category, sortBy, startTimestamp, endTimestamp);
        createRecyclerView();
        topPanelCalculation();
    }


    //Delete data when delete gesture is used and show snackBar to undo
    private void deleteItem(int position) {
        DataItem dataItem = dataSet.get(position);
        dataSet.remove(position);
        recyclerViewAdapter.notifyItemRemoved(position);
        topPanelCalculation();
        databaseHelper.deleteData(dataItem.getId(), 1);

        //SnackBar for Undoing item delete
        Snackbar.make(recyclerView, "Item deleted", Snackbar.LENGTH_LONG).setAction("Undo", view -> {
            dataSet.add(position, dataItem);
            recyclerViewAdapter.notifyItemInserted(position);
            topPanelCalculation();
            databaseHelper.deleteData(dataItem.getId(), 0);
        }).show();
    }


    //This calculates the top panel values on startup
    private void topPanelCalculation() {
        double totalExpense = 0;
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

        for (int i = 0; i < dataSet.size(); i++) {
            totalExpense += dataSet.get(i).getAmount();
        }

        //This will set the current total expense
        tvExpense.setText(String.format("%s BDT", decimalFormat.format(totalExpense)));
    }


    //This creates the RecyclerView
    private void createRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewAdapter = new RecyclerViewAdapter(dataSet, getActivity());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setOnItemClickListener(position -> {
            //This opens a bottom sheet with details from recyclerView item
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(dataSet.get(position));
            bottomSheetDialog.setTargetFragment(OverviewFragment.this, 2);
            bottomSheetDialog.show(getActivity().getSupportFragmentManager(), "BottomDialog");
        });

    }


    ////RecyclerView swipe gesture
    private void createRecyclerViewSwipe() {

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            //Swipe gesture listener
            @Override
            public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                deleteItem(viewHolder.getAdapterPosition());
            }

            //Draw background with icon for recyclerView swipe gesture
            @Override
            public void onChildDraw(@NotNull Canvas c, @NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemView = viewHolder.itemView;

                Drawable background = ContextCompat.getDrawable(getActivity(), R.drawable.shape_rounded_rectangle);
                background.setTint(Color.parseColor("#FF432C"));
                background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getRight(), itemView.getBottom());

                Drawable icon = ContextCompat.getDrawable(getActivity(), R.drawable.ic_delete);
                icon.setTint(Color.WHITE);

                int iconMargin = itemView.getHeight() / 4;
                int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                int iconBottom = iconTop + icon.getIntrinsicHeight();
                int iconLeft, iconRight;

                if (dX > 0) { // Swiping to the right
                    iconLeft = itemView.getLeft() + iconMargin;
                    iconRight = iconLeft + icon.getIntrinsicWidth();
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                } else if (dX < 0) { // Swiping to the left
                    iconRight = itemView.getRight() - iconMargin;
                    iconLeft = iconRight - icon.getIntrinsicWidth();
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                } else { // view is unSwiped
                    background.setBounds(0, 0, 0, 0);
                }

                background.draw(c);
                icon.draw(c);
            }
        });

        //Attach itemTouchHelper to recyclerView
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }


}