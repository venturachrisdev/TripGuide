package com.blancgrupo.apps.tripguide.presentation.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.ReviewAdapter;
import com.blancgrupo.apps.tripguide.presentation.ui.custom.NoSwipePager;
import com.blancgrupo.apps.tripguide.presentation.ui.fragment.CityDetailFragment;
import com.blancgrupo.apps.tripguide.presentation.ui.fragment.FavoritesFragment;
import com.blancgrupo.apps.tripguide.presentation.ui.fragment.ProfileFragment;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.blancgrupo.apps.tripguide.utils.LocationUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener,
        ReviewAdapter.ReviewProfileListener,
        ProfileFragment.AuthListener {
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewpager)
    NoSwipePager viewPager;
    @BindView(R.id.bottom_nav)
    BottomNavigationViewEx bottomNavigationViewEx;

    GoogleApiClient mGoogleApiClient;
    PagerAdapter pagerAdapter;
    ApiUtils.AuthFragment authFragment;

    private static final int RC_SIGN_IN = 1001;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout for this fragment
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        String cityId = getIntent().getStringExtra(Constants.EXTRA_CITY_ID);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), cityId);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPagingEnabled(false);
        bottomNavigationViewEx.setupWithViewPager(viewPager);
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.action_favorites:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.action_profile:
                        viewPager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (getSupportActionBar() != null) {
                    switch (position) {
                        case 1:
                            getSupportActionBar().setTitle(R.string.favorites);
                            break;
                        case 2:
                            getSupportActionBar().setTitle(R.string.profile);
                            break;
                        default:
                            getSupportActionBar().setTitle(R.string.app_name);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Intent original = getIntent();
        Intent intent = new Intent(this, PlaceDetailActivity.class);
        if (original.getAction() != null && original.getAction().equals(Intent.ACTION_VIEW)) {
            intent.setAction(original.getAction());
            Uri path = original.getData();
            if (path != null) {
                List<String> params = path.getPathSegments();
                if (params.get(0).equals("place")) {
                    String placeId = params.get(1);
                    intent.putExtra(Constants.EXTRA_PLACE_ID, placeId);
                    startActivity(intent);
                    this.setIntent(new Intent(Intent.ACTION_DEFAULT));
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

//    @Override
//    public void loadAccount() {
////        final ProgressDialog dialog = new ProgressDialog(this);
////        dialog.setMessage("Signing in...");
//        Toast.makeText(this, "Loading Account!!!!", Toast.LENGTH_SHORT).show();
//        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
//            Toast.makeText(this, "Not logged", Toast.LENGTH_SHORT).show();
//            if (authFragment.isUserSaved()) {
//                OptionalPendingResult<GoogleSignInResult> pendingResult =
//                        Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//                if (pendingResult.isDone()) {
//                    // There's immediate result available.
//                    //authFragment.initializeProfileLayout(pendingResult.get().getSignInAccount());
//                } else {
//                    // There's no immediate result ready, displays some progress indicator and waits for the
//                    // async callback.
////                    dialog.show();
////                    pendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
////                        @Override
////                        public void onResult(@NonNull GoogleSignInResult result) {
////                            //authFragment.initializeProfileLayout(result.getSignInAccount());
////                            dialog.hide();
////                        }
////                    });
//                }
//            } else {
//                Toast.makeText(this, "User not saved", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(this, "Theres a session.", Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public void onReviewProfileClick(String placeId) {
        Intent i = new Intent(this, PlaceDetailActivity.class);
        i.putExtra(Constants.EXTRA_PLACE_ID, placeId);
        startActivity(i);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.options_btn) {
            MenuItem remove =  menu.add(0, v.getId(), 0, "Remove");
            remove.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    Toast.makeText(HomeActivity.this, "Removing ", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals("Remove")) {
            Toast.makeText(this, "Removed!!", Toast.LENGTH_SHORT).show();
        }
        return super.onContextItemSelected(item);
    }

    class PagerAdapter extends FragmentStatePagerAdapter {
        String cityId;

        public PagerAdapter(FragmentManager fm, String cityId) {
            super(fm);
            this.cityId = cityId;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    CityDetailFragment cityDetailFragment = new CityDetailFragment();
                    Bundle args = new Bundle();
                    args.putString(Constants.EXTRA_CITY_ID, cityId);
                    cityDetailFragment.setArguments(args);
                    return cityDetailFragment;
                case 1:
                    return new FavoritesFragment();
                case 2:
                    ProfileFragment profileFragment = new ProfileFragment();
                    authFragment = profileFragment;
                    return profileFragment;

            }
            return new Fragment();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return 3;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LocationUtils.PERMISSION_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if (LocationUtils.gpsEnabledOrShowDialog(HomeActivity.this)) {
                    startActivity(new Intent(HomeActivity.this, MapActivity.class));
                }

            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                LocationUtils.showAreYouSureLocation(HomeActivity.this);
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_location:
                Intent intent = new Intent(this, ChooseLocationActivity.class);
                startActivityForResult(intent, Constants.CHOOSE_LOCATION_RC);
                break;
            case R.id.nav_share:
                shareAppLink();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private void shareAppLink() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String title = getString(R.string.download) + " " + getString(R.string.app_name);
        String googlePlayUrl = String.format("https://play.google.com/store/apps/details?id=%s", MyApplication.packageName);
        String text = String.format(getString(R.string.download_app), getString(R.string.app_name), googlePlayUrl);
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        Intent chooser = Intent.createChooser(intent, getString(R.string.share_app));
        startActivity(chooser);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.action_logout:
                signOutWithGoogle();

        }
        return super.onOptionsItemSelected(item);
    }

    private void signOutWithGoogle() {
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if (authFragment != null) {
                        authFragment.handleSignOut(status);
                    } else {
                        Toast.makeText(HomeActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            pagerAdapter.notifyDataSetChanged();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        if (requestCode == Constants.CHOOSE_LOCATION_RC) {
            if (data != null) {
                String newCityId = data.getStringExtra(Constants.EXTRA_CITY_ID);
                pagerAdapter.setCityId(newCityId);
            }
        }
//        statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_NORMAL);
//        if (requestCode == Constants.CHOOSE_LOCATION_RC) {
//            if (resultCode == RESULT_OK) {
//                recyclerView.showShimmerAdapter();
//                cityViewModel.loadSingleCity(id);
//            }
//        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (authFragment != null) {
            authFragment.handleSignInResult(result);
        } else {
            Toast.makeText(this, "Dude got null", Toast.LENGTH_LONG).show();
        }
    }

}
