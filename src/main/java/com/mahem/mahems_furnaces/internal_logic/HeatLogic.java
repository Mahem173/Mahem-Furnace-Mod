package com.mahem.mahems_furnaces.internal_logic;

public class HeatLogic {
    protected int totalHeat;
    protected int addHeat;
    protected int deductHeat;
    protected int ceilHeat;

    public void conduction(boolean isLit) {
        if (isLit && totalHeat <= ceilHeat) {
            totalHeat += addHeat;
            //System.out.println("Heatin Up");
        } else {
            totalHeat -= deductHeat;
            //System.out.println("Coolin Down");
        }
        if (totalHeat <= 0) {
            totalHeat = 0;
        }
        //System.out.println("Heat:" +  totalHeat);
    }

    public void setHeatValue(int value) {
        this.addHeat = value;
        this.deductHeat = value;
    }

    public void setCeilHeat(int value) {
        this.ceilHeat = value;
    }

    public int getTotalHeat() {
        return this.totalHeat;
    }
}
