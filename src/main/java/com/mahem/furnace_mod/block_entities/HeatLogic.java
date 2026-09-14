package com.mahem.furnace_mod.block_entities;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.UnknownNullability;

public class HeatLogic {
    protected int totalHeat;
    protected int addHeat;
    protected int deductHeat;
    protected boolean isLit;

    public boolean conduction(boolean isLit) {
        if (isLit) {
            totalHeat += addHeat;
        } else {
            totalHeat -= deductHeat;
            System.out.println("Coolin down'");
        }
        if (totalHeat <= 0) {
            totalHeat = 0;
        }
        return isLit;
    }

    public void setAddHeat(int value) {
        this.addHeat = value;
        System.out.println(this.totalHeat);
    }

    public int getTotalHeat() {return this.totalHeat;}
    public int getAddHeat() {return this.addHeat;}
    public int getDeductHeat() {return this.deductHeat;}
    public boolean getIsLit() {return this.isLit;}
}