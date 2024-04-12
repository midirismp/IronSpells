package io.redspace.ironsspellbooks.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.capabilities.spell.SpellData;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import io.redspace.ironsspellbooks.util.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ArcaneAnvilRecipeCategory implements IRecipeCategory<ArcaneAnvilRecipe> {
    public static final RecipeType<ArcaneAnvilRecipe> ARCANE_ANVIL_RECIPE_RECIPE_TYPE = RecipeType.create(IronsSpellbooks.MODID, "arcane_anvil", ArcaneAnvilRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final String leftSlotName = "leftSlot";
    private final String rightSlotName = "rightSlot";
    private final String outputSlotName = "outputSlot";
    private final int paddingBottom = 15;

    public ArcaneAnvilRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.drawableBuilder(JeiPlugin.RECIPE_GUI_VANILLA, 0, 168, 125, 18)
                .addPadding(0, paddingBottom, 0, 0)
                .build();
        icon = guiHelper.createDrawableItemStack(new ItemStack(BlockRegistry.ARCANE_ANVIL_BLOCK.get()));
    }

    @Override
    public RecipeType<ArcaneAnvilRecipe> getRecipeType() {
        return ARCANE_ANVIL_RECIPE_RECIPE_TYPE;
    }

    @Override
    public net.minecraft.network.chat.Component getTitle() {
        return BlockRegistry.ARCANE_ANVIL_BLOCK.get().getName();
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ArcaneAnvilRecipe recipe, IFocusGroup focuses) {
        List<ItemStack> leftInputs = recipe.leftInputs();
        List<ItemStack> rightInputs = recipe.rightInputs();
        List<ItemStack> outputs = recipe.outputs();

        IRecipeSlotBuilder leftInputSlot = builder.addSlot(RecipeIngredientRole.INPUT, 1, 1)
                .addItemStacks(leftInputs)
                .setSlotName(leftSlotName);

        IRecipeSlotBuilder rightInputSlot = builder.addSlot(RecipeIngredientRole.INPUT, 50, 1)
                .addItemStacks(rightInputs)
                .setSlotName(rightSlotName);

        IRecipeSlotBuilder outputSlot = builder.addSlot(RecipeIngredientRole.OUTPUT, 108, 1)
                .addItemStacks(outputs)
                .setSlotName(outputSlotName);

        if (leftInputs.size() == rightInputs.size()) {
            if (leftInputs.size() == outputs.size()) {
                builder.createFocusLink(leftInputSlot, rightInputSlot, outputSlot);
            }
        } else if (leftInputs.size() == outputs.size() && rightInputs.size() == 1) {
            builder.createFocusLink(leftInputSlot, outputSlot);
        } else if (rightInputs.size() == outputs.size() && leftInputs.size() == 1) {
            builder.createFocusLink(rightInputSlot, outputSlot);
        }
    }

    @Override
    public void draw(@NotNull ArcaneAnvilRecipe recipe, IRecipeSlotsView recipeSlotsView, @NotNull PoseStack poseStack, double mouseX, double mouseY) {
        Optional<ItemStack> leftStack = recipeSlotsView.findSlotByName(leftSlotName)
                .flatMap(IRecipeSlotView::getDisplayedItemStack);

        Optional<ItemStack> rightStack = recipeSlotsView.findSlotByName(rightSlotName)
                .flatMap(IRecipeSlotView::getDisplayedItemStack);

        Optional<ItemStack> outputStack = recipeSlotsView.findSlotByName(outputSlotName)
                .flatMap(IRecipeSlotView::getDisplayedItemStack);

        if (leftStack.isEmpty() || rightStack.isEmpty() || outputStack.isEmpty()) {
            return;
        }

        if (leftStack.get().getItem() instanceof Scroll
                && rightStack.get().getItem() instanceof Scroll
                && outputStack.get().getItem() instanceof Scroll) {
            var minecraft = Minecraft.getInstance();
            drawScrollInfo(minecraft, poseStack, leftStack.get(), outputStack.get());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ARCANE_ANVIL_RECIPE_RECIPE_TYPE.getUid();
    }

    @Override
    public Class<? extends ArcaneAnvilRecipe> getRecipeClass() {
        return ArcaneAnvilRecipe.class;
    }

    private void drawScrollInfo(Minecraft minecraft, PoseStack poseStack, ItemStack leftStack, ItemStack outputStack) {
        var inputSpellData = SpellData.getSpellData(leftStack);
        var inputText = String.format("L%d", inputSpellData.getLevel());
        var inputColor = inputSpellData.getSpell().getRarity().getChatFormatting().getColor().intValue();

        var outputSpellData = SpellData.getSpellData(outputStack);
        var outputText = String.format("L%d", outputSpellData.getLevel());
        var outputColor = outputSpellData.getSpell().getRarity().getChatFormatting().getColor().intValue();

        int y = (getBackground().getHeight() / 2) + (paddingBottom / 2) + (minecraft.font.lineHeight / 2) - 4;

        //Left Item
        int x = 3;
        minecraft.font.drawShadow(poseStack, inputText, x, y, inputColor);

        //Right Item
        x += 50;
        minecraft.font.drawShadow(poseStack, inputText, x, y, inputColor);

        //Output Item
        int outputWidth = minecraft.font.width(outputText);
        minecraft.font.drawShadow(poseStack, outputText, getBackground().getWidth() - (outputWidth + 3), y, outputColor);
    }
}
