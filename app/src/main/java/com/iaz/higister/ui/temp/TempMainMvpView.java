package com.iaz.higister.ui.temp;

import java.util.List;

import com.iaz.higister.data.model.Ribot;
import com.iaz.higister.ui.base.MvpView;

public interface TempMainMvpView extends MvpView {

    void showRibots(List<Ribot> ribots);

    void showRibotsEmpty();

    void showError();

}
