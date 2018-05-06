package com.iaz.higister.ui.search;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iaz.higister.R;
import com.iaz.higister.data.model.BaseItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.iaz.higister.util.Constants;
import com.yalantis.flipviewpager.utils.FlipSettings;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.iaz.higister.util.Constants.*;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class RecyclerViewFragment2 extends Fragment implements SearchMvpView {

    private static final boolean GRID_LAYOUT = false;
    private static final int ITEM_COUNT = 100;

    SearchPresenter mSearchPresenter = new SearchPresenter();

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.lists_header_text)
    TextView listsHeaderText;

    @BindView(R.id.insert_text_layout)
    CardView insertTextLayout;

    @BindView(R.id.show_results_layout)
    LinearLayout showResultsLayout;

    SearchActivity activity;

    ResultsAdapter adapter;
    ArrayList<BaseItem> items = new ArrayList<>();
    int type;
    HashMap<Integer, ArrayList<BaseItem>> itemsCollection = new HashMap<>();
    HashMap<Integer, Integer> counterList = new HashMap<>();

    public static RecyclerViewFragment2 newInstance(ArrayList<BaseItem> items, int type) {

        RecyclerViewFragment2 f = new RecyclerViewFragment2();
        Bundle args = new Bundle();
        args.putParcelableArrayList("searchData", items);
        args.putInt("type", type);
        f.setArguments(args);
        return f;
    }

    int i = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
//
        activity = (SearchActivity) getActivity();
        Bundle args = getArguments();
        if (args != null && args.getParcelableArrayList("searchData") != null) {
            items = args.getParcelableArrayList("searchData");
            type = args.getInt("type", 0);
        }

        if (type == -1) {
            insertTextLayout.setVisibility(View.VISIBLE);
            showResultsLayout.setVisibility(View.GONE);
            activity.navigationTabBar.setVisibility(View.GONE);
        } else {
            showResultsLayout.setVisibility(View.VISIBLE);
            insertTextLayout.setVisibility(View.GONE);

            if (activity.list.getType() == 0)
                activity.navigationTabBar.setVisibility(View.VISIBLE);
        }

        float logicalDensity = Resources.getSystem().getDisplayMetrics().density;
        mSearchPresenter.attachView(this);


        FlipSettings settings = new FlipSettings.Builder().defaultPage(1).build();


        adapter = new ResultsAdapter((SearchActivity) getActivity(), items, settings);
        //Use this now
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


//                Log.d ("height", ""+dx);
//                if (activity.textLayout.getVisibility() == View.VISIBLE) {
//                activity.textLayout.setVisibility(View.GONE);
//                activity.textView.setVisibility(View.VISIBLE);

//                    activity.canChange = false;
//                }
//                else {
//                    activity.canChange = true;
//                }
            }
        });

        activity.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counterList.clear();
                i = 0;
//                counter = 0;
                itemsCollection.clear();

                if (activity.list.getType() == -1 || activity.list.getType() == 0) {
                    mSearchPresenter.loadResults(Constants.MOVIES, activity.editText.getText().toString());
                    mSearchPresenter.loadResults(Constants.TV_SERIES, activity.editText.getText().toString());
                    mSearchPresenter.loadResults(Constants.ANIMES, activity.editText.getText().toString());
                    mSearchPresenter.loadResults(Constants.MANGAS, activity.editText.getText().toString());
                    mSearchPresenter.loadResults(Constants.BOOKS, activity.editText.getText().toString());
                    mSearchPresenter.loadResults(Constants.MUSICS, activity.editText.getText().toString());
                    mSearchPresenter.loadResults(Constants.COMICS, activity.editText.getText().toString());
                } else {
                    mSearchPresenter.loadResults(activity.list.getType(), activity.editText.getText().toString());
                }
            }
        });

        populateLabel();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSearchPresenter.detachView();
    }

    /***** MVP View methods implementation *****/

    @Override
    public void showItems(final ArrayList<BaseItem> itens, final int type) {

//        final int counter = this.counter++;

        if (itens != null) {
            counterList.put(type, 0);
            if (type == Constants.MUSICS || itens.isEmpty()) {
                itemsCollection.put(type, itens);
                preupdateAdapter();
            } else {
                for (final BaseItem item : itens) {

                    String bitmapAddress = item.imageUrl;

                    RequestOptions myOptions = new RequestOptions()
                            .override(200, 300)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

                    Glide.with(this)
                            .asBitmap()
                            .load(bitmapAddress)
                            .apply(myOptions)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                    counterList.put(item.getMyType(), counterList.get(item.getMyType()) + 1);
//
                                    item.setBit(resource);
                                    if (counterList.get(item.getMyType()) == itens.size()) {
                                        Timber.d("sucesso");
                                        itemsCollection.put(item.getMyType(), itens);
                                        preupdateAdapter();
                                    }
                                }

                                @Override
                                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                    counterList.put(item.getMyType(), counterList.get(item.getMyType()) + 1);

                                    InputStream is = getResources().openRawResource(R.drawable.large_movie_poster);
                                    Bitmap pisc = BitmapFactory.decodeStream(new BufferedInputStream(is));

                                    item.setBit(pisc);

                                    if (counterList.get(item.getMyType()) == itens.size()) {

                                        if (item.getMyType() == Constants.BOOKS) {
                                            Timber.d("sucesso");
                                        }
                                        itemsCollection.put(item.getMyType(), itens);
                                        preupdateAdapter();
                                    }
                                }
                            });

                }
            }
        }
    }

    public void preupdateAdapter() {

        if (activity.list.getType() != 0 || (counterList.size() == 7
                && itemsCollection.containsKey(Constants.MOVIES)
                && itemsCollection.containsKey(Constants.TV_SERIES)
                && itemsCollection.containsKey(Constants.ANIMES)
                && itemsCollection.containsKey(Constants.MANGAS)
                && itemsCollection.containsKey(Constants.BOOKS)
                && itemsCollection.containsKey(Constants.MUSICS)
                && itemsCollection.containsKey(Constants.COMICS))) {
            updateAdapter();
        }
    }

    public void updateAdapter() {

        for (Fragment frag : activity.getSupportFragmentManager().getFragments()) {
            if (frag instanceof RecyclerViewFragment2) {
                activity.getSupportFragmentManager().beginTransaction().remove(frag).commit();
            }
        }

        Runtime.getRuntime().gc();
        activity.mViewPager.setAdapter(new FragmentStatePagerAdapter(activity.getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {

                if (activity.list.getType() != 0) {
                    return RecyclerViewFragment2.newInstance(itemsCollection.get(activity.list.getType()), activity.list.getType());
                } else {
                    switch (position % 8) {
                        case 0:
                            return RecyclerViewFragment2.newInstance(itemsCollection.get(Constants.MOVIES), MOVIES);
                        case 1:
                            return RecyclerViewFragment2.newInstance(itemsCollection.get(Constants.TV_SERIES), TV_SERIES);
                        case 2:
                            return RecyclerViewFragment2.newInstance(itemsCollection.get(Constants.ANIMES), ANIMES);
                        case 3:
                            return RecyclerViewFragment2.newInstance(itemsCollection.get(Constants.MANGAS), MANGAS);
                        case 4:
                            return RecyclerViewFragment2.newInstance(itemsCollection.get(Constants.BOOKS), BOOKS);
                        case 5:
                            return RecyclerViewFragment2.newInstance(itemsCollection.get(Constants.MUSICS), MUSICS);
                        case 6:
                            return RecyclerViewFragment2.newInstance(itemsCollection.get(Constants.COMICS), COMICS);
                        default:
                            return RecyclerViewFragment2.newInstance(itemsCollection.get(Constants.MOVIES), MOVIES);
                    }
                }
            }

            @Override
            public int getCount() {
                if (activity.list.getType() != 0) {
                    return 1;
                } else {
                    return 8;
                }
            }

        });
        activity.mViewPager.getAdapter().notifyDataSetChanged();
//        activity.mViewPager.getPagerTitleStrip().setViewPager(activity.mViewPager.getViewPager());
    }

    public void populateLabel() {
        switch (type) {
            case MOVIES:
                listsHeaderText.setText("MOVIES");
                break;
            case TV_SERIES:
                listsHeaderText.setText("TV SERIES");
                break;
            case ANIMES:
                listsHeaderText.setText("ANIMES");
                break;
            case MANGAS:
                listsHeaderText.setText("MANGAS");
                break;
            case BOOKS:
                listsHeaderText.setText("BOOKS");
                break;
            case MUSICS:
                listsHeaderText.setText("MUSICS");
                break;
            case COMICS:
                listsHeaderText.setText("COMICS");
                break;

            default:
                listsHeaderText.setText("MISC");

        }
    }


}
