package happyhouse.kk.iot;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private final static String MAC_ADDRESS = "12345612";
    private final static String MAC_ADDRESS2 = "12345613";
    private final static String MAC_ADDRESS3 = "12345614";
    private final static String MAC_ADDRESS4 = "12345619";
    private DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getXML();
        //System.out.println(data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void getXML() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        //mDrawerLayout = findViewById(R.id.drawer_layout);
        //NavigationView navigationView = findViewById(R.id.nav_view);
        /*navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        return true;
                    }
                });*/
        Button m1 = findViewById(R.id.m1);
        Button m2 = findViewById(R.id.m2);
        Button m3 = findViewById(R.id.m3);
        Button m4 = findViewById(R.id.m4);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MachineActivity.class);
                Bundle b = new Bundle();
                switch (view.getId()) {
                    case R.id.m1:
                        b.putString("title", getString(R.string.m1));
                        b.putString("mac_address", MAC_ADDRESS);
                        i.putExtras(b);
                        startActivity(i);
                        break;
                    case R.id.m2:
                        b.putString("title", getString(R.string.m2));
                        b.putString("mac_address", MAC_ADDRESS2);
                        i.putExtras(b);
                        startActivity(i);
                        break;
                    case R.id.m3:
                        b.putString("title", getString(R.string.m3));
                        b.putString("mac_address", MAC_ADDRESS3);
                        i.putExtras(b);
                        startActivity(i);
                        break;
                    case R.id.m4:
                        b.putString("title", getString(R.string.m4));
                        b.putString("mac_address", MAC_ADDRESS4);
                        i.putExtras(b);
                        startActivity(i);
                        break;
                }
            }
        };
        m1.setOnClickListener(listener);
        m2.setOnClickListener(listener);
        m3.setOnClickListener(listener);
        m4.setOnClickListener(listener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
