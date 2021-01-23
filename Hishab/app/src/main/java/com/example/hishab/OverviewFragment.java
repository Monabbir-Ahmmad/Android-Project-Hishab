package com.example.hishab;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class OverviewFragment extends Fragment implements FilterDialog.FilterDialogListener {

    private TextView tv_expense;
    private ExtendedFloatingActionButton btn_filter;
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

        databaseHelper = new DatabaseHelper(getActivity());
        dataSet = new ArrayList<>(databaseHelper.getAllData());

        tv_expense = view.findViewById(R.id.textView_expense);

        //This calculates the top panel values on startup
        topPanelCalculation();

        btn_filter = view.findViewById(R.id.button_filter);
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //This opens the filter dialog
                FilterDialog filterDialog = new FilterDialog();
                filterDialog.setTargetFragment(OverviewFragment.this, 1);
                filterDialog.show(getActivity().getSupportFragmentManager(), "FilterDialog");
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);

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
    private void deleteEntry(int position) {
        DataItem dataItem = dataSet.get(position);
        dataSet.remove(position);
        recyclerViewAdapter.notifyItemRemoved(position);
        topPanelCalculation();

        //SnackBar for Undoing item delete
        Snackbar.make(recyclerView, "Item deleted", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataSet.add(position, dataItem);
                recyclerViewAdapter.notifyItemInserted(position);
                topPanelCalculation();
            }
        }).addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                    // SnackBar closed on without pressing Undo
                    databaseHelper.deleteData(dataItem.getId());
                }
            }
        }).show();
    }


    //This calculates the top panel values on startup
    private void topPanelCalculation() {
        float totalExpense = 0;
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

        for (int i = 0; i < dataSet.size(); i++) {
            totalExpense += dataSet.get(i).getMoney();
        }

        //This will set the current total expense
        tv_expense.setText(decimalFormat.format(totalExpense) + " BDT");
    }


    //This creates the RecyclerView
    private void createRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewAdapter = new RecyclerViewAdapter(dataSet);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //This opens a bottom sheet with details from recyclerView item
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(dataSet.get(position));
                bottomSheetDialog.setTargetFragment(OverviewFragment.this, 2);
                bottomSheetDialog.show(getActivity().getSupportFragmentManager(), "BottomDialog");
            }
        });

    }


    ////RecyclerView swipe gesture
    private void createRecyclerViewSwipe() {

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            //Swipe gesture listener
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                deleteEntry(viewHolder.getAdapterPosition());
            }

            //Draw background with icon for recyclerView swipe gesture
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                Drawable icon = ContextCompat.getDrawable(getActivity(), R.drawable.ic_delete);
                icon.setTint(Color.WHITE);
                ColorDrawable background = new ColorDrawable(Color.RED);

                View itemView = viewHolder.itemView;
                int backgroundCornerOffset = 20;
                int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                int iconBottom = iconTop + icon.getIntrinsicHeight();

                if (dX > 0) { // Swiping to the right
                    int iconLeft = itemView.getLeft() + iconMargin;
                    int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                    background.setBounds(itemView.getLeft(), itemView.getTop(),
                            itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
                } else if (dX < 0) { // Swiping to the left
                    int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                    int iconRight = itemView.getRight() - iconMargin;
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                    background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                            itemView.getTop(), itemView.getRight(), itemView.getBottom());
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