package com.mahem.furnace_mod.menus;

import com.mahem.furnace_mod.mod_types.ModMenuType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.crafting.RecipePropertySet;

public class ForgeFurnaceMenu extends AbstractFurnaceMenu {

    public ForgeFurnaceMenu(int containerId, Inventory playerInv, FriendlyByteBuf extraData) {
        this(containerId, playerInv, new SimpleContainer(3), new SimpleContainerData(4));
    }

    // Constructor for server-side (actual usage)
    public ForgeFurnaceMenu(int containerId, Inventory playerInv, Container container, ContainerData data) {
        super(
                ModMenuType.FORGE_FURNACE_MENU.get(), // MenuType
                RecipePropertySet.FURNACE_INPUT,
                RecipeBookType.FURNACE,               // RecipeBookType (Enum)
                containerId,
                playerInv,
                container,
                data
        );
    }
}