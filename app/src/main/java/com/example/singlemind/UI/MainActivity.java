package com.example.singlemind.UI;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.singlemind.R;
import com.example.singlemind.Utility.DateFormatterUtil;
import com.google.android.material.bottomappbar.BottomAppBar;
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
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity implements IMainActivity{

    //widgets
    private Toolbar toolbar;
    private BottomAppBar bottomAppBar;
    private Drawer result;
    private FloatingActionButton fab;

    //vars
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String mName, mEmail;
    private Uri mPhotoUrl;
    private ProfileDrawerItem mProfileHeader;
    private Calendar mCalendar;

    //const
    private static final String TAG = "MainActivity";

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
        fab = findViewById(R.id.fab);

        init();
        initNavMenu();

    }

    private void init() {
        HomeFragment fragment = new HomeFragment();
        doNormalFragmentTransaction(fragment, getString(R.string.fragmentHome), false);

        mAuth = FirebaseAuth.getInstance();

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if (mUser != null) {
            mName = mUser.getDisplayName();
            mEmail = mUser.getEmail();
            if (mUser.getPhotoUrl() != null) {
                mPhotoUrl = mUser.getPhotoUrl();
                Log.i(TAG, mPhotoUrl.toString());
            }
        }

        addProfileHeaders();
        overrideDrawerImageLoaderPicasso();
        setSupportActionBar(bottomAppBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bottom_bar, menu);
        return true;
    }

    private void addProfileHeaders() {
        if (mPhotoUrl != null) {
            mProfileHeader = new ProfileDrawerItem().withName(mName).withEmail(mEmail).withIcon(mPhotoUrl.toString());
        }
        else {
            mProfileHeader = new ProfileDrawerItem().withName(mName).withEmail(mEmail).withIcon(R.drawable.ic_user);
        }
    }
    private void overrideDrawerImageLoaderPicasso(){
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.get().load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.get().cancelRequest(imageView);
            }

        });
    }


    private void initNavMenu() {
        new DrawerBuilder().withActivity(this).build();

        PrimaryDrawerItem home = new PrimaryDrawerItem().withIdentifier(1).withIcon(FontAwesome.Icon.faw_calendar).withName(R.string.drawer_home);
        PrimaryDrawerItem importICS = new PrimaryDrawerItem().withIdentifier(2).withIcon(FontAwesome.Icon.faw_file_import).withName(R.string.drawer_import);

        SecondaryDrawerItem settings = new SecondaryDrawerItem().withIdentifier(3).withIcon(GoogleMaterial.Icon.gmd_settings).withName(R.string.drawer_settings);
        SecondaryDrawerItem privacy = new SecondaryDrawerItem().withIdentifier(4).withIcon(GoogleMaterial.Icon.gmd_security).withName(R.string.drawer_privacy);
        SecondaryDrawerItem contact = new SecondaryDrawerItem().withIdentifier(5).withIcon(GoogleMaterial.Icon.gmd_contact_mail).withName(R.string.drawer_contact);
        SecondaryDrawerItem rate = new SecondaryDrawerItem().withIdentifier(6).withIcon(GoogleMaterial.Icon.gmd_star).withName(R.string.drawer_rate);

        SecondaryDrawerItem logout = new SecondaryDrawerItem().withIdentifier(7).withIcon(FontAwesome.Icon.faw_sign_out_alt).withName(R.string.sign_out);

        //create account header
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
//                 .withHeaderBackground(R.drawable.wood_header)
                .addProfiles(
                        mProfileHeader
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
                        home, importICS,
                        new DividerDrawerItem(),
                        settings, privacy, contact, rate,
                        new DividerDrawerItem(),
                        logout
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position) {
                            case 1:
                                HomeFragment homeFragment = new HomeFragment();
                                doNormalFragmentTransaction(homeFragment, getString(R.string.fragmentHome), true);
                                result.closeDrawer();
                                break;
                            case 2:
                                ImportFragment importFragment = new ImportFragment();
                                doNormalFragmentTransaction(importFragment, getString(R.string.fragmentImport), true);
                                result.closeDrawer();
                                break;
                            case 9:
                                mAuth.getInstance().signOut();
                                Log.i("LOG", "Signed out");
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                result.closeDrawer();
                                break;
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

    public void addEvent(View view){

        AddEventDialog dialog = AddEventDialog.newInstance();
        dialog.show(getSupportFragmentManager(), "MyDialogFragment");
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

    public void doDateFragmentTransaction(Fragment fragment, String tag, boolean addToBackStack, Calendar cal){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();

        if(cal != null){
            String calendar = new DateFormatterUtil().getFullDate(cal);
            bundle.putString("calendar", calendar);
        }

        fragment.setArguments(bundle);
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

    @Override
    public void setCalendarDate(Calendar calendar) {
            mCalendar = calendar;
    }
}

