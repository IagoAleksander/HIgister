package com.iaz.higister.ui.main;

import java.util.List;

import com.iaz.higister.data.model.Ribot;
import com.iaz.higister.ui.base.MvpView;

public interface MainMvpView extends MvpView {

    void showRibots(List<Ribot> ribots);

    void showRibotsEmpty();

    void showError();

}
