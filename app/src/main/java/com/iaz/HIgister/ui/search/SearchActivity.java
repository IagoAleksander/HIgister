package com.iaz.HIgister.ui.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iaz.HIgister.data.model.BaseItem;
import com.iaz.HIgister.data.model.ListItem;
import com.iaz.HIgister.data.model.UserList;
import com.iaz.HIgister.ui.createItem.CreateItemActivity;
import com.iaz.Higister.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import devlight.io.library.ntb.NavigationTabBar;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.container)
    ViewPager mViewPager;

//    @BindView(R.id.logo_text_view)
//    public TextView textView;

//    @BindView(R.id.logo_text_layout)
//    public LinearLayout textLayout;

    @BindView(R.id.search_bar)
    EditText editText;

    @BindView(R.id.button)
    Button button;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ntb_vertical)
    public NavigationTabBar navigationTabBar;

    @BindView(R.id.add_new_item_button)
    TextView nextButton;

    public ArrayList<BaseItem> itens = new ArrayList<>();

    public UserList list;
    public boolean isFromTutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle("");
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        if (getIntent() != null) {
            list = getIntent().getParcelableExtra("list");
            isFromTutorial = getIntent().getBooleanExtra("isFromTutorial", false);
        }

        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return RecyclerViewFragment2.newInstance(itens, -1);
            }

            @Override
            public int getCount() {

                return 1;
            }
        });

        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_local_movies),
                        ContextCompat.getColor(getApplicationContext(), R.color.accent_dark))
                        .title("ic_first")
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_local_movies))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_action_tv),
                        ContextCompat.getColor(getApplicationContext(), R.color.pink))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_action_tv))
                        .title("ic_second")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.anime_icon),
                        ContextCompat.getColor(getApplicationContext(), R.color.green))
                        .selectedIcon(getResources().getDrawable(R.drawable.anime_icon))
                        .title("ic_third")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.manga_icon),
                        ContextCompat.getColor(getApplicationContext(), R.color.sienna))
                        .selectedIcon(getResources().getDrawable(R.drawable.manga_icon))
                        .title("ic_fourth")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_action_book),
                        ContextCompat.getColor(getApplicationContext(), R.color.orange))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_action_book))
                        .title("ic_fifth")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_action_music_2),
                        ContextCompat.getColor(getApplicationContext(), R.color.saffron))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_action_music_2))
                        .title("ic_sixth")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_action_flash),
                        ContextCompat.getColor(getApplicationContext(), R.color.purple))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_action_flash))
                        .title("ic_seventh")
                        .build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(mViewPager, 4);

        navigationTabBar.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

//                setFab(position);
            }
        });

        if (list != null && list.getType() != 0)
            navigationTabBar.setVisibility(View.VISIBLE);
        else
            navigationTabBar.setVisibility(View.GONE);

        nextButton.setOnClickListener(view -> {
            ListItem listItem = new ListItem();

            if (isFromTutorial) {
                Intent intent = new Intent();
                intent.putExtra("listItem", listItem);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
            else {
                Intent intent = new Intent(SearchActivity.this, CreateItemActivity.class);
                intent.putExtra("list", list);
                intent.putExtra("position", -1);
                intent.putExtra("listItem", listItem);
                startActivity(intent);
            }
        });
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

    @Override
    public void onBackPressed() {
        finish();

    }

}