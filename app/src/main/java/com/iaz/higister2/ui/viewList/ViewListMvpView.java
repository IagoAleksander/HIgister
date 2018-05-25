package com.iaz.higister2.ui.viewList;

import com.iaz.higister2.data.model.ListItem;
import com.iaz.higister2.ui.base.MvpView;

import java.util.ArrayList;

public interface ViewListMvpView extends MvpView {

    void updateData(ArrayList<ListItem> listItems);
}
