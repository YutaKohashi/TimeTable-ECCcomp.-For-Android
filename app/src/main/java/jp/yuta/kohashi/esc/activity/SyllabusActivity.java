package jp.yuta.kohashi.esc.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

import jp.yuta.kohashi.esc.R;
import jp.yuta.kohashi.esc.adapter.ViewPagerAdapter;
import jp.yuta.kohashi.esc.fragments.AprilFragment;
import jp.yuta.kohashi.esc.fragments.AugustFragment;
import jp.yuta.kohashi.esc.fragments.DecemberFragment;
import jp.yuta.kohashi.esc.fragments.FebruaryFragment;
import jp.yuta.kohashi.esc.fragments.JanuaryFragment;
import jp.yuta.kohashi.esc.fragments.JulyFragment;
import jp.yuta.kohashi.esc.fragments.JuneFragment;
import jp.yuta.kohashi.esc.fragments.MarchFragment;
import jp.yuta.kohashi.esc.fragments.MayFragment;
import jp.yuta.kohashi.esc.fragments.NovemberFragment;
import jp.yuta.kohashi.esc.fragments.OctoberFragment;
import jp.yuta.kohashi.esc.fragments.SeptemberFragment;

/**
 * Created by Yuta on 2016/04/04.
 */
public class SyllabusActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);

        toolbar= (Toolbar)findViewById(R.id.callender_toolbar);
//        toolbar.setTitle("シラバス");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        viewPager = (ViewPager)findViewById(R.id.callender_viewpager);
        setUpViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tab_syllabus);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new AprilFragment(),"4月");
        adapter.addFrag(new MayFragment(),"5月");
        adapter.addFrag(new JuneFragment(),"6月");
        adapter.addFrag(new JulyFragment(),"7月");
        adapter.addFrag(new AugustFragment(),"8月");
        adapter.addFrag(new SeptemberFragment(),"9月");
        adapter.addFrag(new OctoberFragment(),"10月");
        adapter.addFrag(new NovemberFragment(),"11月");
        adapter.addFrag(new DecemberFragment(),"12月");
        adapter.addFrag(new JanuaryFragment(),"1月");
        adapter.addFrag(new FebruaryFragment(),"2月");
        adapter.addFrag(new MarchFragment(),"3月");
        viewPager.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
