package com.example.singlemind.UI;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.singlemind.Controllers.ICSManager;
import com.example.singlemind.Model.Event;
import com.example.singlemind.Utility.ImportUtil;
import com.example.singlemind.R;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class ImportFragment extends Fragment implements View.OnClickListener {

    private ImageButton mImportButton, mGoogleImport;
    private List<Event> mEvents;
    private static final int READ_REQUEST_CODE = 42;
    private static final String TAG = "Import Fragment";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_import, container, false);

        init(v);

        return v;
    }

    public void init(View view) {
        // init components
        mImportButton = view.findViewById(R.id.image_ics_import);
        mGoogleImport = view.findViewById(R.id.image_google_import);

        mImportButton.setOnClickListener(this);
        mGoogleImport.setOnClickListener(this);
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
            case R.id.image_ics_import:
                performFileSearch();
                break;

            case R.id.image_google_import:
                //performFileSearch();
                break;

        }
    }

    public void performFileSearch() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("*/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        if (requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                try {
                    mEvents = new ImportUtil().readTextFromUri(getContext(), uri);
                    ICSManager.getInstance().saveEvents(mEvents);
                }
                catch (IOException e) {
                    e.getStackTrace();
                }
            }
        }
    }


}
