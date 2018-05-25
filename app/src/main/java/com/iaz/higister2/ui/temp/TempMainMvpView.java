package com.iaz.higister2.ui.temp;

import com.iaz.higister2.data.model.Ribot;
import com.iaz.higister2.ui.base.MvpView;

import java.util.List;

public interface TempMainMvpView extends MvpView {

    void showRibots(List<Ribot> ribots);

    void showRibotsEmpty();

    void showError();

}
