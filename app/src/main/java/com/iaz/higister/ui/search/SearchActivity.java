package com.iaz.higister.ui.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper;
import com.sdsmdg.harjot.rotatingtext.models.Rotatable;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yalantis.flipviewpager.utils.FlipSettings;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;
import com.iaz.higister.R;
import com.iaz.higister.data.SyncService;
import com.iaz.higister.data.model.BaseItem;
import com.iaz.higister.data.model.ComicVine.Results;
import com.iaz.higister.data.model.GoodReads.BestBook;
import com.iaz.higister.data.model.LastFM.Track;
import com.iaz.higister.data.model.MyAnimeList.Result;
import com.iaz.higister.data.model.Omdb.Search;
import com.iaz.higister.ui.base.BaseActivity;

public class SearchActivity extends BaseActivity implements SearchMvpView {

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "uk.co.ribot.androidboilerplate.ui.main.MainActivity.EXTRA_TRIGGER_SYNC_FLAG";

    @Inject
    SearchPresenter mSearchPresenter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.search_bar)
    EditText searchText;

    @BindView(R.id.type_spinner)
    Spinner typeSpinner;

    @BindView(R.id.button)
    Button button;

    @BindView(R.id.switcher_layout)
    LinearLayout rotatingTextLayout;

    @BindView(R.id.switcher_header)
    TextView rotatingTextHeader;

    @BindView(R.id.custom_switcher)
    RotatingTextWrapper rotatingTextWrapper;

    ResultsAdapter mResultsAdapter;

    int counter, i = 0;
    private ArrayList<Target> targets = new ArrayList<>();

    /**
     * Return an Intent to start this Activity.
     * triggerDataSyncOnCreate allows disabling the background sync service onCreate. Should
     * only be set to false during testing.
     */
    public static Intent getStartIntent(Context context, boolean triggerDataSyncOnCreate) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_TRIGGER_SYNC_FLAG, triggerDataSyncOnCreate);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_search2);
        ButterKnife.bind(this);

        mSearchPresenter.attachView(this);


        if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
            startService(SyncService.getStartIntent(this));
        }

        ArrayList<String> types = new ArrayList<String>() {{
            add("movie");
            add("series");
            add("anime");
            add("manga");
            add("book");
            add("music");
            add("comics");
            add("person");
        }};



        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Reckoner_Bold.ttf");

        rotatingTextHeader.setTypeface(typeface);

        final Rotatable rotatable = new Rotatable(getResources().getColor(R.color.accent_dark), 2000, "","your movies", "your books", "your musics", "YOU...", "", "");
        rotatable.setSize(35);
        rotatable.setAnimationDuration(500);
        rotatable.setTypeface(typeface);
        rotatable.setCenter(true);

        rotatingTextWrapper.setSize(35);
        rotatingTextWrapper.setContent("?", rotatable);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                rotatingTextWrapper.pause(0);
//                rotatingTextLayout.setVisibility(View.GONE);
            }
        }, 9500);   //1.5 seconds


        final ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, types) {
        };

        typeSpinner.setAdapter(typeAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String type = typeSpinner.getSelectedItem().toString();

                if (type.equals("person")) {
                    FlipSettings settings = new FlipSettings.Builder().defaultPage(1).build();
                    mRecyclerView.setAdapter(new FriendsAdapter(SearchActivity.this, Utils.friends, settings));
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                }
                else {

                    FlipSettings settings = new FlipSettings.Builder().defaultPage(1).build();

                    if (mResultsAdapter == null) {
                        mResultsAdapter = new ResultsAdapter(SearchActivity.this, new ArrayList<BaseItem>(), settings);
                        mRecyclerView.setAdapter(mResultsAdapter);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                    }
                }

                mSearchPresenter.loadResults(type, searchText.getText().toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSearchPresenter.detachView();
    }

    /***** MVP View methods implementation *****/

    public void logThis() {
        Timber.d("sucesso");
    }

    @Override
    public void showItems(final ArrayList<BaseItem> itens) {

        counter = 0;
        i = 0;
        targets.clear();

        for (final BaseItem item : itens) {


            Timber.d("counter: " +Integer.toString(counter));
            targets.add (new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                    Log.i(TAG, "The image was obtained correctly, now you can do your canvas operation!");
                    counter++;
                    Timber.d("counter: " +Integer.toString(counter));

                    item.setBit(bitmap);

                    if (counter == itens.size()) {
                        Timber.d("sucesso");
                        mResultsAdapter.setRibots(itens);
                        mResultsAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
//                    Log.e(TAG, "The image was not obtained");
                    counter++;
                    Timber.d("counter: " +Integer.toString(counter));

                    InputStream is = getResources().openRawResource(R.drawable.large_movie_poster);
                    Bitmap pisc = BitmapFactory.decodeStream(new BufferedInputStream(is));

                    item.setBit(pisc);

                    if (counter == itens.size()) {
                        Timber.d("sucesso");
                        mResultsAdapter.setRibots(itens);
                        mResultsAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
//                    Log.(TAG, "Getting ready to get the image");
                    //Here you should place a loading gif in the ImageView to
                    //while image is being obtained.
                }
            });

            item.setBackgroundColor(getBackgroundColor(i));

            if (item instanceof Result) {
                Picasso.with(this).load(((Result) item).getImage_url()).into(targets.get(i++));
            }
            else if (item instanceof Results) {
                Picasso.with(this).load(((Results) item).getImage().getSmall_url()).into(targets.get(i++));
            }
            else if (item instanceof BestBook) {
                Picasso.with(this).load(((BestBook) item).getSmall_image_url()).into(targets.get(i++));
            }
            else if (item instanceof Search) {
                Picasso.with(this).load(((Search) item).getPoster()).into(targets.get(i++));
            }
            else if (item instanceof Track) {
                mResultsAdapter.setRibots(itens);
                mResultsAdapter.notifyDataSetChanged();
                break;
            }

        }

    }

    public int getBackgroundColor (int position) {
        switch (position) {
            case 0:
            case 1:
                return getResources().getColor(R.color.sienna);
            case 2:
            case 3:
                return getResources().getColor(R.color.green);
            case 4:
            case 5:
                return getResources().getColor(R.color.orange);
            case 6:
            case 7:
                return getResources().getColor(R.color.purple);
            default:
                return getResources().getColor(R.color.saffron);

        }

    }

}
