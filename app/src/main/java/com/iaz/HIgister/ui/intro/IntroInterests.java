package com.iaz.HIgister.ui.intro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.iaz.Higister.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alksander on 28/05/2018.
 */

public class IntroInterests extends Fragment {

    @BindView(R.id.checkbox_movies)
    CheckBox mCheckBoxMovies;

    @BindView(R.id.checkbox_series)
    CheckBox mCheckBoxSeries;

    @BindView(R.id.checkbox_books)
    CheckBox mCheckBoxBooks;

    @BindView(R.id.checkbox_music)
    CheckBox mCheckBoxMusic;

    @BindView(R.id.checkbox_animes)
    CheckBox mCheckBoxAnimes;

    @BindView(R.id.checkbox_mangas)
    CheckBox mCheckBoxMangas;

    @BindView(R.id.checkbox_comics)
    CheckBox mCheckBoxComics;

    IntroActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (IntroActivity) getActivity();
//        activity.fragmentInterests = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.intro_interests, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mCheckBoxMovies.setChecked(false);
        mCheckBoxSeries.setChecked(false);
        mCheckBoxBooks.setChecked(false);
        mCheckBoxMusic.setChecked(false);
        mCheckBoxAnimes.setChecked(false);
        mCheckBoxMangas.setChecked(false);
        mCheckBoxComics.setChecked(false);

        if (activity.user.getInterests() != null && !activity.user.getInterests().isEmpty())
            for (String interest : activity.user.getInterests()) {
                switch (interest) {
                    case "Movies":
                        mCheckBoxMovies.setChecked(true);
                        mCheckBoxMovies.setEnabled(true);
                        break;
                    case "TV Series":
                        mCheckBoxSeries.setChecked(true);
                        break;
                    case "Books":
                        mCheckBoxBooks.setChecked(true);
                        break;
                    case "Music":
                        mCheckBoxMusic.setChecked(true);
                        break;
                    case "Animes":
                        mCheckBoxAnimes.setChecked(true);
                        break;
                    case "Mangas":
                        mCheckBoxMangas.setChecked(true);
                        break;
                    case "Comics":
                        mCheckBoxComics.setChecked(true);
                        break;
                }
            }
    }


}
