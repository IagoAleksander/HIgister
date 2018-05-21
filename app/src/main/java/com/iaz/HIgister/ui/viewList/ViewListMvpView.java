package com.iaz.HIgister.ui.viewList;

import com.iaz.HIgister.data.model.ListItem;
import com.iaz.HIgister.ui.base.MvpView;

import java.util.ArrayList;

public interface ViewListMvpView extends MvpView {

    void updateData(ArrayList<ListItem> listItems);
}
