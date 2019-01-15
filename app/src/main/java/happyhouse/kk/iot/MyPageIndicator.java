package happyhouse.kk.iot;

import android.support.v4.view.ViewPager;
import android.widget.TextView;

public class MyPageIndicator implements ViewPager.OnPageChangeListener {

    ViewPager mPager;
    TextView tv;

    public MyPageIndicator(ViewPager mPager, TextView tv) {
        this.mPager = mPager;
        this.tv = tv;
    }
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
        System.out.println(i);
    }
}
