package dev.rimur.mysticalautomationomniio.mixin;

import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.mysticalagriculture.api.crafting.IEnchanterRecipe;
import dev.rimur.mysticalautomationomniio.support.DynamicRecipeSupport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.blakebr0.mysticalautomation.tileentity.EnchanternatorTileEntity", remap = false)
public abstract class EnchanternatorDynamicRecipeMixin {
    @Shadow(remap = false)
    private int selectedLevel;

    @Inject(
        method = "getActiveRecipe()Lcom/blakebr0/mysticalagriculture/api/crafting/IEnchanterRecipe;",
        at = @At("HEAD"),
        cancellable = true,
        remap = false,
        require = 1
    )
    private void mysticalautomationOmniIo$getActiveRecipeFromRealInventory(
        CallbackInfoReturnable<IEnchanterRecipe> cir
    ) {
        var tile = (BaseInventoryTileEntity) (Object) this;
        var recipe = DynamicRecipeSupport.findEnchanterRecipe(tile);

        if (recipe == null) {
            this.mysticalautomationOmniIo$setSelectedLevel(tile, 0);
            cir.setReturnValue(null);
            return;
        }

        var maxLevel = recipe.getMaxResultEnchantmentLevel(DynamicRecipeSupport.enchanterInput(tile));
        this.mysticalautomationOmniIo$setSelectedLevel(tile, maxLevel);

        cir.setReturnValue(recipe);
    }

    private void mysticalautomationOmniIo$setSelectedLevel(BaseInventoryTileEntity tile, int level) {
        var clampedLevel = Math.clamp(level, 0, 5);

        if (this.selectedLevel != clampedLevel) {
            this.selectedLevel = clampedLevel;
            tile.setChangedFast();
        }
    }
}
