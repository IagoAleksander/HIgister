package com.iaz.higister.ui.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iaz.higister.R;
import com.iaz.higister.data.model.BaseItem;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.iaz.higister.data.model.UserList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.materialViewPager)
    public MaterialViewPager mViewPager;


    @BindView(R.id.logo_text_view)
    public TextView textView;

    @BindView(R.id.logo_text_layout)
    public LinearLayout textLayout;

    @BindView(R.id.search_bar)
    EditText editText;

    @BindView(R.id.button)
    Button button;

    public String searchedItem = "";

    public ArrayList<BaseItem> itens = new ArrayList<>();

    public boolean canChange = true;

    public UserList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle("");
        ButterKnife.bind(this);

        final Toolbar toolbar = mViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        if (getIntent() != null)
            list = getIntent().getParcelableExtra("list");

        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
//                switch (position % 4) {
                    //case 0:
                    //    return RecyclerViewFragment.newInstance();
                    //case 1:
                    //    return RecyclerViewFragment.newInstance();
                    //case 2:
                    //    return WebViewFragment.newInstance();
//                    default:
                        return RecyclerViewFragment2.newInstance(itens);
//                }
            }

            @Override
            public int getCount() {
                return 8;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 8) {
                    case 0:
                        return "Movies";
                    case 1:
                        return "Series";
                    case 2:
                        return "Animes";
                    case 3:
                        return "Mangas";
                    case 4:
                        return "Books";
                    case 5:
                        return "Musics";
                    case 6:
                        return "Comics";
                    case 7:
                        return "People";
                }
                return "";
            }
        });

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page % 4) {
                    case 0:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.red,
                                getDrawable(R.drawable.movie));
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue,
                                "http://www.hdiphonewallpapers.us/phone-wallpapers/540x960-1/540x960-mobile-wallpapers-hd-2218x5ox3.jpg");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.cyan,
                                "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                    case 3:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });

        mViewPager.getPagerTitleStrip().setTabPaddingLeftRight(0);
        mViewPager.getPagerTitleStrip().setDividerPadding(20);
        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());


//        final View logo = findViewById(R.id.logo_white);
//        if (logo != null) {
//            logo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mViewPager.notifyHeaderChanged();
//                    Toast.makeText(getApplicationContext(), "Yes, the title is clickable", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }



        if (textView != null && textLayout != null) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (canChange) {
                        textView.setVisibility(View.GONE);
                        textLayout.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        if (button != null && editText != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (editText.getText().length() > 3) {
                        textLayout.setVisibility(View.GONE);
                        textView.setText(editText.getText());
                        textView.setVisibility(View.VISIBLE);
                    }

                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();



        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
            finish();

    }

}