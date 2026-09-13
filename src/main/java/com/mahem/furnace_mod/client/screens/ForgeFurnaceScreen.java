package com.mahem.furnace_mod.client.screens;

import com.mahem.furnace_mod.menus.ForgeFurnaceMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class ForgeFurnaceScreen extends AbstractContainerScreen<ForgeFurnaceMenu> {
    private static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/gui/container/blast_furnace.png");
    private static final Identifier LIT_PROGRESS_SPRITE = Identifier.withDefaultNamespace("container/blast_furnace/lit_progress");
    private static final Identifier BURN_PROGRESS_SPRITE = Identifier.withDefaultNamespace("container/blast_furnace/burn_progress");
    //private static final Component FILTER_NAME = Component.translatable("gui.recipebook.toggleRecipes.blastable");

    public ForgeFurnaceScreen(ForgeFurnaceMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

        renderBurnProgress(graphics, x, y);
        renderLitProgress(graphics, x, y);
    }

    private void renderBurnProgress(GuiGraphicsExtractor guiGraphics, int x, int y) {
        if(menu.isCrafting()) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BURN_PROGRESS_SPRITE,x + 73, y + 35, (float) 0, (float) 0,
                    (int) menu.getBurnProgress(), 16, 24, 16);
        }
    }
    private void renderLitProgress(GuiGraphicsExtractor guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, LIT_PROGRESS_SPRITE,
                    x + 104, (int) (y + 13 + 16 - menu.getLitProgress()),
                    (float) 0, 16 - menu.getLitProgress(),
                    16, (int) menu.getLitProgress(),
                    16, 16);
        }
    }

}
