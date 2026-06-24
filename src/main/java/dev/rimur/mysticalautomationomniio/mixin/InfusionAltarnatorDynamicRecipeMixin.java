package dev.rimur.mysticalautomationomniio.mixin;

import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.mysticalagriculture.api.crafting.IInfusionRecipe;
import dev.rimur.mysticalautomationomniio.support.DynamicRecipeSupport;
import net.minecraft.world.item.crafting.CraftingInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.blakebr0.mysticalautomation.tileentity.InfusionAltarnatorTileEntity", remap = false)
public abstract class InfusionAltarnatorDynamicRecipeMixin {
    @Inject(
        method = "getActiveRecipe()Lcom/blakebr0/mysticalagriculture/api/crafting/IInfusionRecipe;",
        at = @At("HEAD"),
        cancellable = true,
        remap = false,
        require = 1
    )
    private void mysticalautomationOmniIo$getActiveRecipeFromRealInventory(
        CallbackInfoReturnable<IInfusionRecipe> cir
    ) {
        cir.setReturnValue(DynamicRecipeSupport.findInfusionRecipe((BaseInventoryTileEntity) (Object) this));
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
