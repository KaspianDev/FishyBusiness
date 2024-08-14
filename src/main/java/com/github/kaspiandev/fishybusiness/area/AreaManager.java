package com.github.kaspiandev.fishybusiness.area;

import com.github.kaspiandev.fishybusiness.FishyBusiness;

import java.util.ArrayList;
import java.util.List;

public class AreaManager {

    private final FishyBusiness plugin;
    private final List<Area> areas;

    public AreaManager(FishyBusiness plugin) {
        this.plugin = plugin;
        this.areas = new ArrayList<>();
    }

}
