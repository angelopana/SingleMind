package com.example.singlemind.UI;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.singlemind.Controllers.DBManager;
import com.example.singlemind.Controllers.FlagEvent;
import com.example.singlemind.Model.Event;
import com.example.singlemind.R;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private List<EventDay> mEvents = new ArrayList<>();
    private ArrayList<Event> e = new ArrayList<>();
    private CalendarView mCalendarView;
    private IMainActivity iMainActivity;

    private static final String TAG = "HomeFragment";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
        iMainActivity = (IMainActivity) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        initCalendar(v);

        mCalendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar cal = eventDay.getCalendar();

                EventsFragment fragment = new EventsFragment();
                ((MainActivity)getActivity()).doDateFragmentTransaction(fragment, getString(R.string.fragmentEvents), true, cal);
            }
        });
        return v;
    }


    public void initCalendar(View view) {

        mCalendarView = view.findViewById(R.id.calendarView);

        DBManager.getInstance().getEventDays(new IFiretoreObjectListener() {
            @Override
            public void onRetrievalSuccess(Object object) {
                mEvents = (List<EventDay>) object;
                //updateUI
                mCalendarView.setEvents(mEvents);
            }

            @Override
            public void onRetrievalFailure() {
                Snackbar.make(view, R.string.event_error_on_storage, Snackbar.LENGTH_SHORT).show();
            }
        });

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

    @Override
    public void onResume(){
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FlagEvent event){
        //retrieve info from dialog
        Log.i(TAG, "Subscriber heard you loud and clear");
        if (event.getFlag()) {
            DBManager.getInstance().getEventDays(new IFiretoreObjectListener() {
                @Override
                public void onRetrievalSuccess(Object object) {
                    mEvents = (List<EventDay>) object;
                    //updateUI
                    mCalendarView.setEvents(mEvents);
                }

                @Override
                public void onRetrievalFailure() {

                }
            });

        }
    }

    public void updateUI(){
        //
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }
}