package com.github.kaspiandev.fishybusiness.area.adapter;

import com.github.kaspiandev.fishybusiness.area.Area;
import com.github.kaspiandev.fishybusiness.gson.ListAdapter;

public class AreaListAdapter extends ListAdapter<Area> {

    @Override
    public Class<Area> getItemClass() {
        return Area.class;
    }

}
