package dev.rimur.mysticalautomationomniio.mixin;

import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import dev.rimur.mysticalautomationomniio.support.DynamicRecipeSupport;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.blakebr0.mysticalautomation.tileentity.CrafterTileEntity", remap = false)
public abstract class CrafterDynamicRecipeMixin {
    @Inject(
        method = "getActiveRecipe()Lnet/minecraft/world/item/crafting/CraftingRecipe;",
        at = @At("HEAD"),
        cancellable = true,
        remap = false,
        require = 1
    )
    private void mysticalautomationOmniIo$getActiveRecipeFromRealInventory(
        CallbackInfoReturnable<CraftingRecipe> cir
    ) {
        cir.setReturnValue(DynamicRecipeSupport.findCraftingRecipe((BaseInventoryTileEntity) (Object) this));
    }

    @Inject(
        method = "toCraftingInput()Lnet/minecraft/world/item/crafting/CraftingInput;",
        at = @At("HEAD"),
        cancellable = true,
        remap = false,
        require = 1
    )
    private void mysticalautomationOmniIo$toCraftingInputFromRealInventory(
        CallbackInfoReturnable<CraftingInput> cir
    ) {
        cir.setReturnValue(DynamicRecipeSupport.gridInput((BaseInventoryTileEntity) (Object) this));
    }
}
