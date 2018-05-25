package com.iaz.higister.ui.temp;

import com.iaz.higister.data.model.Ribot;
import com.iaz.higister.ui.base.MvpView;

import java.util.List;

public interface TempMainMvpView extends MvpView {

    void showRibots(List<Ribot> ribots);

    void showRibotsEmpty();

    void showError();

}
