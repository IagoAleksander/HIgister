package com.alks_ander.higister.ui.search;

import java.util.ArrayList;

import com.alks_ander.higister.data.model.BaseItem;
import com.alks_ander.higister.ui.base.MvpView;

public interface SearchMvpView extends MvpView {

    void showItems(ArrayList<BaseItem> itens);
}
