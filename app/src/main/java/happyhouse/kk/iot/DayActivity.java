package happyhouse.kk.iot;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nshmura.recyclertablayout.RecyclerTabLayout;

import java.util.ArrayList;
import java.util.List;

public class DayActivity extends AppCompatActivity {

    TabsPagerAdapter mAdapter;
    ViewPager mPager;
    RecyclerTabLayout tabLayout;
    public static float[] morn;
    public static float[] noon;
    public static float[] night;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        morn = new float[7];
        noon = new float[7];
        night = new float[7];
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        try {
            for(Integer i=0; i<7; i++) {
                morn[i] = b.getFloat(i.toString());
            }
            for(Integer i=7; i<14; i++) {
                noon[i-7] = b.getFloat(i.toString());
            }
            for(Integer i=14; i<21; i++) {
                night[i-14] = b.getFloat(i.toString());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        getXML();
    }

    private void getXML() {
        try {
            Toolbar toolbar = findViewById(R.id.day_toolbar);
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getResources().getString(R.string.hot_time));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        mPager = findViewById(R.id.day_pager);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(i==0) {
                    mPager.setCurrentItem(7000);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(DayFragment.newInstance("MON", 0));
        fragments.add(DayFragment.newInstance("TUE", 1));
        fragments.add(DayFragment.newInstance("WED", 2));
        fragments.add(DayFragment.newInstance("THU", 3));
        fragments.add(DayFragment.newInstance("FRI", 4));
        fragments.add(DayFragment.newInstance("SAT", 5));
        fragments.add(DayFragment.newInstance("SUN", 6));
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), fragments);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(7000);
        tabLayout = findViewById(R.id.cycle_tab_layout);
        tabLayout.setUpWithViewPager(mPager);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    class TabsPagerAdapter extends FragmentStatePagerAdapter {

        List<Fragment> mFrags;
        String[] title = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};

        TabsPagerAdapter(FragmentManager fm, List<Fragment> frags) {
            super(fm);
            mFrags = frags;
        }
        @Override
        public Fragment getItem(int position) {
            int index = position % mFrags.size();
            return mFrags.get(index);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            int index = position % mFrags.size();
            return title[index];
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }
    }
}
