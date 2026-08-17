package com.mahem.furnace_mod.menus;

import com.mahem.furnace_mod.mod_types.ModMenuType;
import com.mahem.furnace_mod.mod_types.ModRecipeType;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;

public class ForgeFurnaceMenu extends AbstractFurnaceMenu {
    public ForgeFurnaceMenu(int containerId, Inventory playerInv) {
        this(containerId, playerInv, new SimpleContainer(3), new SimpleContainerData(4));
    }

    // Constructor for server-side (actual usage)
    public ForgeFurnaceMenu(int containerId, Inventory playerInv, Container container, ContainerData data) {
        super(
                ModMenuType.FORGE_FURNACE_MENU.get(), // MenuType
                ModRecipeType.FORGE_FURNACE_TYPE.get(),
                RecipeBookType.FURNACE,               // RecipeBookType (Enum)
                containerId,
                playerInv,
                container,
                data
        );
    }
}
