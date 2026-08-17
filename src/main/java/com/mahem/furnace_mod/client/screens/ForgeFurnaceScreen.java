package com.mahem.furnace_mod.client.screens;

import com.mahem.furnace_mod.menus.ForgeFurnaceMenu;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.SearchRecipeBookCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ForgeFurnaceScreen extends AbstractFurnaceScreen<ForgeFurnaceMenu> {
    private static final Identifier LIT_PROGRESS_SPRITE = Identifier.withDefaultNamespace("container/blast_furnace/lit_progress");
    private static final Identifier BURN_PROGRESS_SPRITE = Identifier.withDefaultNamespace("container/blast_furnace/burn_progress");
    private static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/gui/container/blast_furnace.png");
    private static final Component FILTER_NAME = Component.translatable("gui.recipebook.toggleRecipes.blastable");
    private static final List<RecipeBookComponent.TabInfo> TABS = List.of(
            new RecipeBookComponent.TabInfo(SearchRecipeBookCategory.BLAST_FURNACE),
            new RecipeBookComponent.TabInfo(Items.REDSTONE_ORE, RecipeBookCategories.BLAST_FURNACE_BLOCKS),
            new RecipeBookComponent.TabInfo(Items.IRON_SHOVEL, Items.GOLDEN_LEGGINGS, RecipeBookCategories.BLAST_FURNACE_MISC)
    );

    public ForgeFurnaceScreen(ForgeFurnaceMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, FILTER_NAME, TEXTURE, LIT_PROGRESS_SPRITE, BURN_PROGRESS_SPRITE, TABS);
    }
}
