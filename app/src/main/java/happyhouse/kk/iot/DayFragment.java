package happyhouse.kk.iot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

public class DayFragment extends Fragment {

    private PieChart mChart;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String day;
    private int d;
    private Float mornT;
    private Float noonT;
    private Float nightT;
    public DayFragment() {
        // Required empty public constructor
    }

    public static DayFragment newInstance(String day, int d) {

        DayFragment fragment = new DayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, day);
        args.putInt(ARG_PARAM2, d);
        fragment.setArguments(args);
        return fragment;
    }

    public String getDay() {
        return day;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            day = getArguments().getString(ARG_PARAM1);
            d = getArguments().getInt(ARG_PARAM2);
            System.out.println(d);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_day, container, false);
        mChart = root.findViewById(R.id.pie_chart);
        mornT = DayActivity.morn[d];
        noonT = DayActivity.noon[d];
        nightT = DayActivity.night[d];
        int total = (int)(mornT + noonT + nightT);
        TextView day = root.findViewById(R.id.day);
        String msg = "資料總數: " + total;
        day.setText(msg);
        drawBarChart();
        return root;
    }
    private void drawBarChart() {
        PieChart pieChart = mChart;
        pieChart.setCenterText(getResources().getString(R.string.hot_time));
        pieChart.setHoleRadius(0f);
        pieChart.setTransparentCircleRadius(0f);
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);
        pieChart.setTouchEnabled(false);

        //data
        Float total = mornT + noonT + nightT;
        List<PieEntry> entries = new ArrayList<>();
        if(mornT!=0)
            entries.add(new PieEntry(100*mornT/total, "早上"));
        if(noonT!=0)
            entries.add(new PieEntry(100*noonT/total, "下午"));
        if(nightT!=0)
            entries.add(new PieEntry(100*nightT/total, "晚上"));

        PieDataSet set1 = new PieDataSet(entries, "");

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(ResourcesCompat.getColor(getResources(), R.color.red, null));
        colors.add(ResourcesCompat.getColor(getResources(), R.color.m2, null));
        colors.add(ResourcesCompat.getColor(getResources(), R.color.light_blue, null));
        set1.setColors(colors);


        PieData pieData = new PieData(set1);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(20f);
        pieChart.setData(pieData);
    }

}
