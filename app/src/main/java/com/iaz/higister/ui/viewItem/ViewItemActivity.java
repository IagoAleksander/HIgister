package com.iaz.higister.ui.viewItem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.iaz.higister.R;
import com.iaz.higister.data.model.ListItem;
import com.iaz.higister.ui.base.BaseActivity;
import com.iaz.higister.util.CustomPhotoPickerDialog;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Iago Aleksander on 14/04/18.
 */

public class ViewItemActivity extends BaseActivity {

    @Inject
    ViewItemPresenter mViewItemPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.indicator)
    CircleIndicator circleIndicator;
    @BindView(R.id.previous_button)
    TextView previousButton;
    @BindView(R.id.next_button)
    TextView nextButton;


    ArrayList<ListItem> listItems = new ArrayList<>();
    int position;

    private CustomPhotoPickerDialog photoDialog;
    FragmentPagerAdapter adapterViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        mViewItemPresenter.setActivity(this);

        setContentView(R.layout.activity_view_item);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            listItems = getIntent().getExtras().getParcelableArrayList("listItems");
            position = getIntent().getIntExtra("position", 0);
        }

        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(), listItems);
        viewPager.setAdapter(adapterViewPager);
        circleIndicator.setViewPager(viewPager);

        viewPager.setCurrentItem(position);

        setSupportActionBar(mToolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Create List Item");

        previousButton.setOnClickListener(v -> {

            position--;
            viewPager.setCurrentItem(position);
        });

        nextButton.setOnClickListener(v -> {

            position++;
            viewPager.setCurrentItem(position);
        });

        previousButton.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.VISIBLE);

        if (position == 0) {
            previousButton.setVisibility(View.GONE);
        }
        if (position == listItems.size() - 1) {
            nextButton.setVisibility(View.GONE);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                previousButton.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);

                if (position == 0) {
                    previousButton.setVisibility(View.GONE);
                }
                if (position == listItems.size() - 1) {
                    nextButton.setVisibility(View.GONE);
                }



            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
//                overridePendingTransition(R.anim.slide_in_backward, R.anim.slide_out_backward);
                break;
//            case R.id.action_next:
//                goToNextSection();
//                break;
            default:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {

        ArrayList<ListItem> listItems = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fragmentManager, ArrayList<ListItem> listItems) {
            super(fragmentManager);
            this.listItems = listItems;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return listItems.size();
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            return ViewItemFragment.newInstance(listItems.get(position));
        }

    }

}
