package com.iaz.higister2.ui.search;

import com.iaz.higister2.data.model.BaseItem;
import com.iaz.higister2.ui.base.MvpView;

import java.util.ArrayList;

public interface SearchMvpView extends MvpView {

    void showItems(ArrayList<BaseItem> itens, int type);
}
