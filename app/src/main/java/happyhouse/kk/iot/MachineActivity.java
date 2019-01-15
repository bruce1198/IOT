package happyhouse.kk.iot;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

public class MachineActivity extends AppCompatActivity {
    static String MAC_ADDRESS = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine);
        getXML();
    }
    private void getXML() {
        Intent i = getIntent();
        Bundle b = i.getExtras();
        try {
            String title = b.getString("title");
            Toolbar toolbar = findViewById(R.id.machine_toolbar);
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
            MAC_ADDRESS = b.getString("mac_address");
            //System.out.println(MAC_ADDRESS);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        ViewPager pager = findViewById(R.id.pager);
        pager.setAdapter(new FixedTabsPagerAdapter(getSupportFragmentManager()));
        TabLayout layout = findViewById(R.id.tab_layout);
        pager.setCurrentItem(1);
        layout.setupWithViewPager(pager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    class FixedTabsPagerAdapter extends FragmentPagerAdapter {

        FixedTabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new HomeFragment();
                case 1:
                    return new UseFragment();
                case 2:
                    return new MapFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.home);
                case 1:
                    return getString(R.string.occupy);
                case 2:
                    return getString(R.string.map);
                default:
                    return null;
            }
        }
    }
}
