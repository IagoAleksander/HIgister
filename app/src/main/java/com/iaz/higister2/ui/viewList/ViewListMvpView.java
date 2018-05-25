package com.iaz.higister.ui.viewList;

import com.iaz.higister.data.model.ListItem;
import com.iaz.higister.ui.base.MvpView;

import java.util.ArrayList;

public interface ViewListMvpView extends MvpView {

    void updateData(ArrayList<ListItem> listItems);
}
