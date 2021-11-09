package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class Notes_Tabs extends AppCompatActivity {


        private Toolbar toolbar;
        private TabLayout tabLayout;
        TabItem mynotes,deleted;
        private ViewPager pager;
        public DrawerLayout drawerLayout;
        public NavigationView navigationView;
        ActionBarDrawerToggle toggle;
        PagerAdapter adapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_notes__tabs);

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            pager =findViewById(R.id.viewpager);
            tabLayout = findViewById(R.id.tabs);
            mynotes = findViewById(R.id.notes);
            deleted = findViewById(R.id.deleted);

            drawerLayout=findViewById(R.id.drawer);
            navigationView=findViewById(R.id.nav_view);
            toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
            drawerLayout.addDrawerListener(toggle);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
                {
                    switch (menuItem.getItemId())
                    {
                        case R.id.home :
                            Toast.makeText(getApplicationContext(),"Home Panel is Open",Toast.LENGTH_LONG).show();
                            drawerLayout.closeDrawer(GravityCompat.START);
                            break;

                        case R.id.settings :
                            Toast.makeText(getApplicationContext(),"Setting Panel is Open",Toast.LENGTH_LONG).show();
                            drawerLayout.closeDrawer(GravityCompat.START);
                            break;

                        case R.id.bin :
                            Toast.makeText(getApplicationContext(),"Trash bin is Open",Toast.LENGTH_LONG).show();
                            drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                    }

                    return true;
                }
            });


            adapter = new PagerAdapter(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,tabLayout.getTabCount());
            pager.setAdapter(adapter);

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    pager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            pager.addOnAdapterChangeListener((ViewPager.OnAdapterChangeListener) new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//            tabLayout = findViewById(R.id.tabs);
//            tabLayout.setupWithViewPager(Pager);


        }

        private void setupViewPager(ViewPager viewPager) {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

            adapter.addFragment(new MainActivity(), "My notes");
            adapter.addFragment(new Deleted_Notes(), "Deleted Notes");

            viewPager.setAdapter(adapter);
        }



    class ViewPagerAdapter extends FragmentPagerAdapter {
            private final List<Fragment> mFragmentList = new ArrayList<>();
            private final List<String> mFragmentTitleList = new ArrayList<>();

            public ViewPagerAdapter(FragmentManager manager) {
                super(manager);
            }

            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }

            public void addFragment(Fragment fragment, String title) {
                mFragmentList.add(fragment);
                mFragmentTitleList.add(title);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentTitleList.get(position);
            }
        }
        @Override
       public boolean onCreateOptionsMenu(Menu menu){
            getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
        }
        @Override
        public boolean onOptionsItemSelected(MenuItem item){
            int id = item.getItemId();
            if(id == R.id.profile){
                //start activity for profile
                startActivity(new Intent(Notes_Tabs.this,Profile_Data.class));
            }
            return true;
        }
    }

