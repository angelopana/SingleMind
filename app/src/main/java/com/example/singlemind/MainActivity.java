package com.example.singlemind;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity {

    //vars
    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton floatingActionButton;
    private BottomNavigationView bottomNavigationView;
    private Drawer result;
    private static String sURL = "https://cc.csusm.edu/calendar/view.php?view=month&time=";
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String mName, mEmail;
    private Uri mPhotoUrl;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomAppBar = findViewById(R.id.bottom_app_bar);
        floatingActionButton = findViewById(R.id.fab);

        init();
        initNavMenu();

    }

    private void init() {
        HomeFragment fragment = new HomeFragment();
        doNormalFragmentTransaction(fragment, getString(R.string.fragmentHome), false);


        AsyncCallWS test = new AsyncCallWS();
        test.execute();
        mAuth = FirebaseAuth.getInstance();

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            mName = mUser.getDisplayName();
            mEmail = mUser.getEmail();
        }

        setSupportActionBar(bottomAppBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bottom_bar, menu);
        return true;
    }


    private void initNavMenu() {
        new DrawerBuilder().withActivity(this).build();

        PrimaryDrawerItem home = new PrimaryDrawerItem().withIdentifier(1).withIcon(FontAwesome.Icon.faw_calendar).withName(R.string.drawer_home);

        SecondaryDrawerItem settings = new SecondaryDrawerItem().withIdentifier(7).withIcon(GoogleMaterial.Icon.gmd_settings).withName(R.string.drawer_settings);
        SecondaryDrawerItem privacy = new SecondaryDrawerItem().withIdentifier(9).withIcon(GoogleMaterial.Icon.gmd_security).withName(R.string.drawer_privacy);
        SecondaryDrawerItem contact = new SecondaryDrawerItem().withIdentifier(10).withIcon(GoogleMaterial.Icon.gmd_contact_mail).withName(R.string.drawer_contact);
        SecondaryDrawerItem rate = new SecondaryDrawerItem().withIdentifier(11).withIcon(GoogleMaterial.Icon.gmd_star).withName(R.string.drawer_rate);


        //create account header
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
//                 .withHeaderBackground(R.drawable.wood_header)
                .addProfiles(
                        new ProfileDrawerItem().withName(mEmail).withEmail(mEmail).withIcon(getResources().getDrawable(R.drawable.duck))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        //create the drawer and remember the `Drawer` result object
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(bottomAppBar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        home,
                        new DividerDrawerItem(),
                        settings, privacy, contact, rate

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (position == 1) {
//                            result.closeDrawer();

                        }
                        return true;
                    }
                })
                .withOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
                    @Override
                    public boolean onNavigationClickListener(View clickedView) {
                        onBackPressed();
                        return true;
                    }
                })
                .build();

    }

    //set back arrow for appbar
    public void setActionBarArrow() {
        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setHamburgerIcon() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item ) {
        switch (item.getItemId()) {

            case R.id.navigation_search:
//                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }


    public void doNormalFragmentTransaction(Fragment fragment, String tag, boolean addToBackStack){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, fragment, tag);

        if(addToBackStack){
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    private void signOut() {
        mAuth.signOut();
        //updateUI(null);
    }

    private class AsyncCallWS extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            //Call Web Method

            CougarCourses test = new CougarCourses();

            //test CC method
            try {
                test.download("smith635", "yrcOEHT2580", sURL);
            }
            catch (IOException e) {
                Log.i("IOException:", "IO Exception thrown");
            }

            return null;
        }

        @Override
        //Once WebService returns response
        protected void onPostExecute(Void result) {
            //do something
        }

        @Override

        protected void onPreExecute() {
            //do something
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //do something
        }
    }
}

