package com.alks_ander.higister.ui.main;

import java.util.List;

import com.alks_ander.higister.data.model.Ribot;
import com.alks_ander.higister.ui.base.MvpView;

public interface MainMvpView extends MvpView {

    void showRibots(List<Ribot> ribots);

    void showRibotsEmpty();

    void showError();

}
