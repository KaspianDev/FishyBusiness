package com.github.kaspiandev.fishybusiness.area;

public class FishyArea implements Area {

    private final double minX;
    private final double maxX;

    private final double minY;
    private final double maxY;

    private final double minZ;
    private final double maxZ;

    public FishyArea(double minX, double maxX, double minY, double maxY, double minZ, double maxZ) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }


}
