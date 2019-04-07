package com.example.singlemind;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EventsFragment extends Fragment implements View.OnClickListener {

    private List<Event> recyclerEvents = new ArrayList<>();
    private RecyclerView calendarRecycler;
    private CalendarAdapter calendarAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_events, container, false);

        init(v);
        initEvents();

        calendarAdapter = new CalendarAdapter(recyclerEvents);
        calendarRecycler.setAdapter(calendarAdapter);

        return v;
    }

    public void init(View view) {

        calendarRecycler = view.findViewById(R.id.recycler_calendar);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        calendarRecycler.setLayoutManager(llm);

    }

    private void initEvents() {
        recyclerEvents.add(new Event("Homework", "8:00am", "Homework2a", "long string"));
        recyclerEvents.add(new Event("Homework", "9:00am", "Homework2b", "long string"));
        recyclerEvents.add(new Event("Homework", "10:00am", "Homework2c", "long string"));
        recyclerEvents.add(new Event("Homework", "11:00am", "Homework2d", "long string"));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.bottom_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            //case R.id.navigation_share:

            //return true;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
