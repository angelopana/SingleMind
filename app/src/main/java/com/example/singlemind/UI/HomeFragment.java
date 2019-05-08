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
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.singlemind.Controllers.DBManager;
import com.example.singlemind.Model.FlagEvent;
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

    private TextView mHomework, mAppointment, mGathering, mParty, mBirthday, mOther, mTotal;
    private int mHomeworkCounter, mAppointmentCounter, mGatheringCounter, mPartyCounter, mBirthdayCounter, mOtherCounter;


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

        mTotal = v.findViewById(R.id.text_event_count_num);

        mHomework = v.findViewById(R.id.text_event_homework_count_num);
        mAppointment = v.findViewById(R.id.text_event_appointment_count_num);
        mGathering = v.findViewById(R.id.text_event_gathering_count_num);
        mParty = v.findViewById(R.id.text_event_party_count_num);
        mBirthday = v.findViewById(R.id.text_event_birthday_count_num);
        mOther = v.findViewById(R.id.text_event_other_count_num);

        mHomeworkCounter = 0;
        mAppointmentCounter = 0;
        mGatheringCounter = 0;
        mPartyCounter = 0;
        mBirthdayCounter = 0;
        mOtherCounter = 0;

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

                DBManager.getInstance().getEvents(new IFiretoreObjectListener() {
                    @Override
                    public void onRetrievalSuccess(Object object) {
                        List<Event> events = (List<Event>) object;
                        for (Event e: events) {
                            switch (e.getmEventType()){
                                case 0:
                                    mHomeworkCounter++;
                                    break;
                                case 1:
                                    mAppointmentCounter++;
                                    break;
                                case 2:
                                    mGatheringCounter++;
                                    break;
                                case 3:
                                    mPartyCounter++;
                                    break;
                                case 4:
                                    mBirthdayCounter++;
                                    break;
                                case 5:
                                    mOtherCounter++;
                                    break;
                            }
                        }

                        //set counter totals
                        mHomework.setText(String.valueOf(mHomeworkCounter));
                        mAppointment.setText(String.valueOf(mAppointmentCounter));
                        mGathering.setText(String.valueOf(mGatheringCounter));
                        mParty.setText(String.valueOf(mPartyCounter));
                        mBirthday.setText(String.valueOf(mBirthdayCounter));
                        mOther.setText(String.valueOf(mOtherCounter));

                        int total = mAppointmentCounter + mHomeworkCounter + mGatheringCounter + mPartyCounter
                                        + mBirthdayCounter + mOtherCounter;

                        mTotal.setText(String.valueOf(total));
                    }

                    @Override
                    public void onRetrievalFailure() {

                    }
                });
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
                    Log.i(TAG, "refreshed counters onEvent");
                    //refreshCounters();
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