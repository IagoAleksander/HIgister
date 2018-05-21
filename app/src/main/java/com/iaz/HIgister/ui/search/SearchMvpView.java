package com.iaz.HIgister.ui.search;


import com.iaz.HIgister.data.model.BaseItem;
import com.iaz.HIgister.ui.base.MvpView;

import java.util.ArrayList;

public interface SearchMvpView extends MvpView {

    void showItems(ArrayList<BaseItem> itens, int type);
}
