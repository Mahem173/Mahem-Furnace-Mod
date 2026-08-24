package com.mahem.furnace_mod.block_entities;

public class HeatLogic {
    protected int totalHeat;
    protected int addHeat;
    protected int deductHeat;
    protected boolean isLit;

    public void conduction() {
        if (this.isLit) {
            this.totalHeat += this.addHeat;
        } else {
            this.totalHeat -= this.deductHeat;
        }
        if (this.totalHeat <= 0) {
            this.totalHeat = 0;
        }
    }

    public void setAddHeat(int value) {
        this.addHeat = value;}

    public int getTotalHeat() {return this.totalHeat;}
    public int getAddHeat() {return this.addHeat;}
    public int getDeductHeat() {return this.deductHeat;}
    public boolean getIsLit() {return this.isLit;}
    public void setLit(boolean lit) {this.isLit = lit;}
}