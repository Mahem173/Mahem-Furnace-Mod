package com.mahem.furnace_mod.block_entities;

public class HeatLogic {
    protected int totalHeat;
    protected int addHeat;
    protected int deductHeat;

    public void conduction(boolean isLit) {
        if (isLit) {
            totalHeat += addHeat;
            //System.out.println("Heatin Up");
        } else {
            totalHeat -= deductHeat;
            //System.out.println("Coolin Down");
        }
        if (totalHeat <= 0) {
            totalHeat = 0;
        }
    }

    public void setAddHeat(int value) {
        this.addHeat = value;
        System.out.println(this.totalHeat);
    }
}