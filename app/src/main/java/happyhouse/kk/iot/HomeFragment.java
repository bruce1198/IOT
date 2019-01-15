package happyhouse.kk.iot;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.Mac;

public class HomeFragment extends Fragment {

    private String data;
    private JSONObject jsonObject = null;
    private String id = "no data";
    private String mac_address = "no data";
    private String create_at = "no data";
    private String power = "no data";
    private float[] weekday = new float[7];
    private float[] morn = new float[7];
    private float[] noon = new float[7];
    private float[] night = new float[7];
    private TextView for_id;
    private TextView for_macaddr;
    private TextView for_power;
    private TextView for_create_at;
    private ProgressBar mBar;
    private BarChart mBarChart;
    private LinearLayout ll;
    private SwipeRefreshLayout refreshLayout;
    public HomeFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        setLatest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        getXML(root);
        Timer timer = new Timer();
        timer.schedule(new MyTimerTask(), 1);
        return root;
    }
    private void getXML(View view) {
        try {
            for_id = view.findViewById(R.id.for_id);
            for_macaddr = view.findViewById(R.id.for_macaddr);
            for_power = view.findViewById(R.id.for_power);
            for_create_at = view.findViewById(R.id.for_create_at);
            ll = view.findViewById(R.id.linear_layout);
            refreshLayout = view.findViewById(R.id.refresh_layout);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //getData();
                }
            });
            mBar = view.findViewById(R.id.progress_bar);
            mBarChart = view.findViewById(R.id.bar_chart);
            Button hot_time = view.findViewById(R.id.hot_time);
            hot_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), DayActivity.class);
                    Bundle b = new Bundle();
                    for(Integer i=0; i<7; i++) {
                        b.putFloat(i.toString(), morn[i]);
                    }
                    for(Integer i=7; i<14; i++) {
                        b.putFloat(i.toString(), noon[i-7]);
                    }
                    for(Integer i=14; i<21; i++) {
                        b.putFloat(i.toString(), night[i-14]);
                    }
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }
    private void getData() {
        GetData myTask = new GetData();
        try {
            data = myTask.execute(getResources().getString(R.string.api)+"api.php?mac_address="+MachineActivity.MAC_ADDRESS).get().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        parseJson(data);
    }
    private void setLatest() {
        getLatest();
        //System.out.println(data);
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String msg = "Id: "+id;
                    for_id.setText(msg);
                    msg = "Mac Addr: "+mac_address;
                    for_macaddr.setText(msg);
                    msg = "Power: " + power +"V";
                    for_power.setText(msg);
                    msg = "Latest Time: " + create_at;
                    for_create_at.setText(msg);
                }
            });
            //don't put in runOnUiThread!!!
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        drawBarChart(weekday);
    }
    private void getLatest() {
        try {
            if(jsonObject!=null) {
                JSONObject jsonObjects = jsonObject;
                id = jsonObjects.getString("id");
                mac_address = jsonObjects.getString("macaddr");
                power = jsonObjects.getString("power");
                create_at = jsonObjects.getString("created_at");
                JSONObject week = jsonObjects.getJSONObject("weekday");
                JSONObject jmorn = jsonObjects.getJSONObject("morn");
                JSONObject jnoon = jsonObjects.getJSONObject("noon");
                JSONObject jnight = jsonObjects.getJSONObject("night");
                for(Integer i=1; i<=6; i++) {
                    try {
                        weekday[i-1] = Float.valueOf(week.getString(i.toString()));
                    } catch (Exception e) {
                        weekday[i-1] = 0f;
                    }
                    try {
                        morn[i-1] = Float.valueOf(jmorn.getString(i.toString()));
                    } catch (Exception e) {
                        morn[i-1] = 0f;
                    }
                    try {
                        noon[i-1] = Float.valueOf(jnoon.getString(i.toString()));
                    } catch (Exception e) {
                        noon[i-1] = 0f;
                    }
                    try {
                        night[i-1] = Float.valueOf(jnight.getString(i.toString()));
                    } catch (Exception e) {
                        night[i-1] = 0f;
                    }
                }
                try {
                    weekday[6] = Float.valueOf(week.getString("0"));
                } catch (Exception e) {
                    weekday[6] = 0f;
                }
                try {
                    morn[6] = Float.valueOf(jmorn.getString("0"));
                } catch (Exception e) {
                    morn[6] = 0f;
                }
                try {
                    noon[6] = Float.valueOf(jnoon.getString("0"));
                } catch (Exception e) {
                    noon[6] = 0f;
                }
                try {
                    night[6] = Float.valueOf(jnight.getString("0"));
                } catch (Exception e) {
                    night[6] = 0f;
                }
            }
            else {
                id = "no data";
                mac_address = "no data";
                create_at = "no data";
                for(Integer i=0; i<7; i++) {
                    morn[i] = 0f;
                    noon[i] = 0f;
                    night[i] = 0f;
                    weekday[i] = 0f;
                }
            }
        } catch (NullPointerException|JSONException e) {
            id = "no data";
            mac_address = "no data";
            create_at = "no data";
            for(Integer i=0; i<7; i++) {
                morn[i] = 0f;
                noon[i] = 0f;
                night[i] = 0f;
                weekday[i] = 0f;
            }
            e.printStackTrace();
        }
    }
    private void drawBarChart(float[] week) {
        for(float w : week) {
            System.out.println(w);
        }
        BarChart barChart = mBarChart;
        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);
        barChart.setTouchEnabled(false);

        XAxis xl = barChart.getXAxis();
        xl.setDrawGridLines(false);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        String[] labels = {"一", "二", "三", "四", "五", "六", "日"};
        xl.setValueFormatter(new LabelFormatter(labels));

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f);
        leftAxis.setSpaceBottom(0f);
        barChart.getAxisRight().setEnabled(false);

        //data
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i=0; i<7; i++) {
            try {
                entries.set(i, new BarEntry(i, week[i]));
            } catch (IndexOutOfBoundsException e) {
                entries.add(i, new BarEntry(i, week[i]));
            }
        }

        BarDataSet set1;
        set1 = new BarDataSet(entries, "次數");
        set1.setColors(ColorTemplate.rgb("7bb3ff"));
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        barChart.setData(data);
    }
    private void parseJson(String data) {
        try {
            System.out.println(data);
            jsonObject = new JSONObject(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            getData();
            setLatest();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class GetData extends AsyncTask<String, Integer, StringBuilder> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ll.setVisibility(View.INVISIBLE);
            mBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected StringBuilder doInBackground(String... urls) {
            StringBuilder response = new StringBuilder();
            HttpURLConnection conn = null;
            try {
                URL url = new URL(urls[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(50000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setUseCaches(false);

                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                reader.close();

            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                if(conn!=null)
                    conn.disconnect();
            }
            return response;
        }

        @Override
        protected void onProgressUpdate(Integer...progress) {
            mBar.setProgress(progress[0]);
        }

        protected void onPostExecute(StringBuilder sb) {
            mBar.setVisibility(View.INVISIBLE);
            ll.setVisibility(View.VISIBLE);
            refreshLayout.setRefreshing(false);
            setLatest();
        }
    }
}
