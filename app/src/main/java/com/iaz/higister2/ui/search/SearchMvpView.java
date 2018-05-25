package com.iaz.higister.ui.search;

import com.iaz.higister.data.model.BaseItem;
import com.iaz.higister.ui.base.MvpView;

import java.util.ArrayList;

public interface SearchMvpView extends MvpView {

    void showItems(ArrayList<BaseItem> itens, int type);
}
