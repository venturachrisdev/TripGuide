package com.blancgrupo.apps.tripguide.presentation.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.view.MenuItem;

import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.presentation.ui.custom.NoSwipePager;
import com.blancgrupo.apps.tripguide.presentation.ui.fragment.CityDetailFragment;
import com.blancgrupo.apps.tripguide.presentation.ui.fragment.FavoritesFragment;
import com.blancgrupo.apps.tripguide.presentation.ui.fragment.ProfileFragment;
import com.blancgrupo.apps.tripguide.presentation.ui.fragment.SignInFragment;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.blancgrupo.apps.tripguide.utils.LocationUtils;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
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

    PagerAdapter pagerAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        String cityId = getIntent().getStringExtra(Constants.EXTRA_CITY_ID);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), cityId, true);
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
    }



    class PagerAdapter extends FragmentStatePagerAdapter {
        String cityId;
        boolean logged;

        public PagerAdapter(FragmentManager fm, String cityId, boolean logged) {
            super(fm);
            this.cityId = cityId;
            this.logged = logged;
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
                    FavoritesFragment favoritesFragment = new FavoritesFragment();
                    return favoritesFragment;
                case 2:
                    if (this.logged) {
                        ProfileFragment profileFragment = new ProfileFragment();
                        return profileFragment;
                    } else {
                        SignInFragment signInFragment = new SignInFragment();
                        return signInFragment;
                    }
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

        public void setLogged(boolean logged) {
            this.logged = logged;
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
            case R.id.nav_favorite:
                break;
            case R.id.nav_share:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.action_logout:
                pagerAdapter.setLogged(false);
                break;

        }
        return super.onOptionsItemSelected(item);
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
        if (data != null) {
            String newCityId = data.getStringExtra(Constants.EXTRA_CITY_ID);
            pagerAdapter.setCityId(newCityId);
        }
//        statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_NORMAL);
//        if (requestCode == Constants.CHOOSE_LOCATION_RC) {
//            if (resultCode == RESULT_OK) {
//                recyclerView.showShimmerAdapter();
//                cityViewModel.loadSingleCity(id);
//            }
//        }
    }

}
