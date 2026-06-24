package dev.rimur.mysticalautomationomniio.support;

import com.blakebr0.cucumber.inventory.CachedRecipe;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.mysticalagriculture.api.crafting.IAwakeningRecipe;
import com.blakebr0.mysticalagriculture.api.crafting.IEnchanterRecipe;
import com.blakebr0.mysticalagriculture.api.crafting.IInfusionRecipe;
import com.blakebr0.mysticalautomation.compat.MysticalCompat;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

public final class DynamicRecipeSupport {
    private DynamicRecipeSupport() {
    }

    public static CraftingInput gridInput(BaseInventoryTileEntity tile) {
        return tile.getInventory().toCraftingInput(3, 3, 0, 9);
    }

    public static CraftingInput enchanterInput(BaseInventoryTileEntity tile) {
        return tile.getInventory().toShapelessCraftingInput(0, 3);
    }

    public static boolean hasActiveRecipe(BaseInventoryTileEntity tile, MachineKind kind) {
        return switch (kind) {
            case CRAFTER -> findCraftingRecipe(tile) != null;
            case INFUSION_ALTARNATOR -> findInfusionRecipe(tile) != null;
            case AWAKENING_ALTARNATOR -> findAwakeningRecipe(tile) != null;
            case ENCHANTERNATOR -> findEnchanterRecipe(tile) != null;
            default -> false;
        };
    }

    @Nullable
    public static CraftingRecipe findCraftingRecipe(BaseInventoryTileEntity tile) {
        var level = tile.getLevel();

        if (level == null) {
            return null;
        }

        return new CachedRecipe<CraftingInput, CraftingRecipe>(RecipeType.CRAFTING)
            .checkAndGet(gridInput(tile), level);
    }

    @Nullable
    public static IInfusionRecipe findInfusionRecipe(BaseInventoryTileEntity tile) {
        var level = tile.getLevel();

        if (level == null) {
            return null;
        }

        return new CachedRecipe<CraftingInput, IInfusionRecipe>(MysticalCompat.RecipeTypes.INFUSION.get())
            .checkAndGet(gridInput(tile), level);
    }

    @Nullable
    public static IAwakeningRecipe findAwakeningRecipe(BaseInventoryTileEntity tile) {
        var level = tile.getLevel();

        if (level == null) {
            return null;
        }

        return new CachedRecipe<CraftingInput, IAwakeningRecipe>(MysticalCompat.RecipeTypes.AWAKENING.get())
            .checkAndGet(gridInput(tile), level);
    }

    @Nullable
    public static IEnchanterRecipe findEnchanterRecipe(BaseInventoryTileEntity tile) {
        var level = tile.getLevel();

        if (level == null) {
            return null;
        }

        return new CachedRecipe<CraftingInput, IEnchanterRecipe>(MysticalCompat.RecipeTypes.ENCHANTER.get())
            .checkAndGet(enchanterInput(tile), level);
    }
}
