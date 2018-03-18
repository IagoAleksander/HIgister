package com.iaz.higister.ui.search;

import java.util.ArrayList;

import com.iaz.higister.data.model.BaseItem;
import com.iaz.higister.ui.base.MvpView;

public interface SearchMvpView extends MvpView {

    void showItems(ArrayList<BaseItem> itens);
}
