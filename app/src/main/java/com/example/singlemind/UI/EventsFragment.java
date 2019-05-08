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

import com.applandeo.materialcalendarview.EventDay;
import com.example.singlemind.Adapter.CalendarAdapter;
import com.example.singlemind.Controllers.DBManager;
import com.example.singlemind.Model.Event;
import com.example.singlemind.Model.FlagEvent;
import com.example.singlemind.R;
import com.example.singlemind.Utility.DateFormatterUtil;
import com.example.singlemind.Utility.EventBuilderUtil;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.GONE;

public class EventsFragment extends Fragment implements View.OnClickListener {

    private List<Event> mRecyclerEvents = new ArrayList<>();
    private RecyclerView mCalendarRecycler;
    private CalendarAdapter mCalendarAdapter;
    private Calendar mCalendar;

    private static final String TAG = "EventsFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_events, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String calStr = bundle.getString("calendar");
            Log.i(TAG, calStr);
            mCalendar = new DateFormatterUtil().getCalDateFromString(calStr);
        }

        init(v);
        initEvents(v);
        initSwipe();

        return v;
    }

    public void init(View view) {

        mCalendarRecycler = view.findViewById(R.id.recycler_calendar);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        mCalendarRecycler.setLayoutManager(llm);

    }

    private void initEvents(View view) {
        DBManager.getInstance().getEvents(new IFiretoreObjectListener() {
            @Override
            public void onRetrievalSuccess(Object object) {

                mRecyclerEvents = new EventBuilderUtil().getEventsByDay((List<Event>) object, mCalendar);
                mCalendarAdapter = new CalendarAdapter(mRecyclerEvents);
                mCalendarRecycler.setAdapter(mCalendarAdapter);

                if (mCalendarAdapter.getItemCount() == 0 ) {
                    mCalendarRecycler.setVisibility(GONE);
                }
            }

            @Override
            public void onRetrievalFailure() {
                Snackbar.make(view, R.string.event_error_on_storage, Snackbar.LENGTH_SHORT).show();

            }
        });
    }

    private void initSwipe() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                mRecyclerEvents.remove(viewHolder.getAdapterPosition());
                mCalendarAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                if (mCalendarAdapter.getItemCount() == 0 ) {
                    mCalendarRecycler.setVisibility(GONE);
                }
            }

            @Override
            public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mCalendarRecycler);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FlagEvent event){
        //retrieve info from dialog
        Log.i(TAG, "Subscriber heard you loud and clear");
        if (mCalendarAdapter.getItemCount() == 0 ) {
            mCalendarRecycler.setVisibility(GONE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
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
