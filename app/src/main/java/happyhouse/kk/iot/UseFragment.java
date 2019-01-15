package happyhouse.kk.iot;


import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class UseFragment extends Fragment {

    private ListView listView;
    private String data;
    private JSONObject jsonObject = null;
    private Vector<String> listv = new Vector<>();
    private String[] products = {"礦泉水", "泡麵", "麵包", "飲料", "零食"};
    private TextView total_txt;
    private TextView remain_txt;
    private ProgressBar mBar;
    private LinearLayout ll;
    private int remain=0;
    private final int total = 666;

    public UseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        setLatest(getView());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Timer timer = new Timer();
        timer.schedule(new MyTimerTask(), 1);
        View root = inflater.inflate(R.layout.fragment_use, container, false);
        listView = root.findViewById(R.id.list_view);
        total_txt = root.findViewById(R.id.total);
        remain_txt = root.findViewById(R.id.remains);
        ll = root.findViewById(R.id.linear_layout_use);
        mBar = root.findViewById(R.id.progress_bar_use);
        //new MyThread(root).start();
        Button date_pick = root.findViewById(R.id.date_pick);
        final GregorianCalendar gc = new GregorianCalendar();
        date_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                DatePickerDialog dpd =  new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day_of_month) {
                        Vector<String> select_list = new Vector<>();
                        for(int i=0; i<listv.size(); i++) {
                            String date = listv.elementAt(i);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN);
                            try {
                                Date d = sdf.parse(date);
                                Calendar calendar = Calendar.getInstance(Locale.TAIWAN);
                                calendar.setTime(d);
                                int y = calendar.get(Calendar.YEAR);
                                int m = calendar.get(Calendar.MONTH);
                                int dom = calendar.get(Calendar.DAY_OF_MONTH);
                                if(year==y && month==m && day_of_month==dom) {
                                    select_list.add(listv.elementAt(i));
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), R.layout.support_simple_spinner_dropdown_item, select_list);
                        listView.setAdapter(arrayAdapter);
                    }
                }, gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });

        return root;
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
    private void setLatest(final View view) {
        getLatest();
        //System.out.println(data);
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String msg = "商品總數: " + total;
                    total_txt.setText(msg);
                    msg = "剩餘數量: " + remain;
                    remain_txt.setText(msg);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, listv);
                    listView.setAdapter(arrayAdapter);
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    private void getLatest() {
        try {
            if (jsonObject != null) {
                JSONObject jsonObjects = jsonObject;
                JSONArray data = jsonObjects.getJSONArray("raw_data");
                System.out.println(data);
                remain = total - data.length();
                for(int i=0; i<data.length(); i++) {
                    int random = (int)(products.length*Math.random());
                    String prod = "                     " + products[random];
                    listv.add(i, data.getString(i)+prod);
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
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
            setLatest(getView());
        }
    }
    private class GetData extends AsyncTask<String, Integer, StringBuilder> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //ll.setVisibility(View.INVISIBLE);
            //mBar.setVisibility(View.VISIBLE);
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
            //mBar.setVisibility(View.INVISIBLE);
            //ll.setVisibility(View.VISIBLE);
        }
    }
    private class MyThread extends Thread {
        View view;
        MyThread(View view) {
            this.view = view;
        }

        @Override
        public void run() {
            try {
                JSONObject json_object = null;
                /*while (HomeFragment.jsonObject==null) {
                    System.out.print("hi");
                }
                json_object = HomeFragment.jsonObject;*/
                //System.out.println(json_object);
                JSONArray data = json_object.getJSONArray("raw_data");
                System.out.println(data);
                remain = total - data.length();
                for(int i=0; i<data.length(); i++) {
                    int random = (int)(products.length*Math.random());
                    String prod = "                     " + products[random];
                    listv.add(i, data.getString(i)+prod);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), R.layout.support_simple_spinner_dropdown_item, listv);
                        listView.setAdapter(arrayAdapter);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String msg = "商品總數: " + total;
            total_txt.setText(msg);
            msg = "剩餘數量: " + remain;
            remain_txt.setText(msg);
        }
    }
}
