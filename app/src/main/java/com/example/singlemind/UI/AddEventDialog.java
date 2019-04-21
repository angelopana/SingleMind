package com.example.singlemind.UI;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.singlemind.Controllers.DBManager;
import com.example.singlemind.FlagEvent;
import com.example.singlemind.Model.Event;
import com.example.singlemind.R;
import com.example.singlemind.Utility.DateFormatterUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import static android.app.Activity.RESULT_OK;

public class AddEventDialog extends DialogFragment {

    private EditText mETTitle, mETDescription;
    private Button mBtnDatePicker, mBtnTimePicker, mSubmit;
    private TimePickerDialog mStartTimePicker;

    private ConstraintLayout mRootView;
    private static Calendar sCal;
    private static Event sEvent;
    private static DateFormatterUtil sFormatter = new DateFormatterUtil();
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    int mStartHour, mStartMin;

    private static final String TAG = "AddEventDialog";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;

    public static AddEventDialog newInstance() {
        AddEventDialog fragment = new AddEventDialog();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.add_event_fragment, container, false);

        mBtnDatePicker = view.findViewById(R.id.date_picker_btn);
        mBtnTimePicker = view.findViewById(R.id.time_picker_btn);
        mSubmit = view.findViewById(R.id.button_submit_reg);
        mETTitle = view.findViewById(R.id.edit_name);
        mETDescription = view.findViewById(R.id.edit_event_description);
        mRootView = view.findViewById(android.R.id.content);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        sEvent = new Event();
        final Calendar calendar = Calendar.getInstance();

        mStartHour = calendar.get(Calendar.HOUR_OF_DAY);
        mStartMin = calendar.get(Calendar.MINUTE);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        toolbar.setTitle("Set your event");

        mBtnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(new Date());
                dialog.setTargetFragment(AddEventDialog.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mBtnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartTimePicker = new TimePickerDialog(getContext(), R.style.TimePickerTheme,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                mStartHour = hourOfDay;
                                mStartMin = minute;

                                sCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                sCal.set(Calendar.MINUTE, minute);
                                sCal.set(Calendar.SECOND, 0);
                                sCal.set(Calendar.MILLISECOND, 0);

                                mBtnTimePicker.setText(sFormatter.getTimeDate(sCal));
                                sEvent.setmEventTime(sFormatter.getFullDate(sCal));

                            }
                        }, mStartHour, mStartMin, false);
                mStartTimePicker.show();
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sEvent.setmEventName(mETTitle.getText().toString());
                sEvent.setmEventDescription(mETDescription.getText().toString());
                sEvent.setmEventUID(System.currentTimeMillis());

                DBManager.getInstance().setEvent(sEvent, new IUpdatable() {
                    @Override
                    public void onUpdateSuccess() {
                        Log.i(TAG, "Event stored to Firestore");
                        EventBus.getDefault().post(new FlagEvent(true));
                        dismiss();
                    }

                    @Override
                    public void onUpdateFailed() {
                        //notify the user
                        Log.i(TAG, "Event failed to store to Firestore");
                        Snackbar.make(v, R.string.event_error_on_storage, Snackbar.LENGTH_SHORT)
                                .show();
                    }
                });
            }
        });


        return view;
    }

    //This method returns the date from the calendar
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK){
            return;
        }

        if (requestCode == REQUEST_DATE) {
            sCal = (Calendar) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);

            mBtnDatePicker.setText(sFormatter.getDayDate(sCal));
            sEvent.setmEventTime(sFormatter.getFullDate(sCal));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}
