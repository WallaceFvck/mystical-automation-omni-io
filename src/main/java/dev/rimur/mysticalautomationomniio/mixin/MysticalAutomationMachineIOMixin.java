package dev.rimur.mysticalautomationomniio.mixin;

import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import dev.rimur.mysticalautomationomniio.support.MysticalAutomationItemHandlers;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Substitui o handler lateral original por um handler proprio:
 * input de processo por qualquer lado, output por qualquer lado e combustivel
 * ignorado pela automacao externa.
 */
@Mixin(
    targets = {
        "com.blakebr0.mysticalautomation.tileentity.InfuserTileEntity",
        "com.blakebr0.mysticalautomation.tileentity.CrafterTileEntity",
        "com.blakebr0.mysticalautomation.tileentity.FarmerTileEntity",
        "com.blakebr0.mysticalautomation.tileentity.FertilizerTileEntity",
        "com.blakebr0.mysticalautomation.tileentity.InfusionAltarnatorTileEntity",
        "com.blakebr0.mysticalautomation.tileentity.AwakeningAltarnatorTileEntity",
        "com.blakebr0.mysticalautomation.tileentity.EnchanternatorTileEntity"
    },
    remap = false
)
public abstract class MysticalAutomationMachineIOMixin {
    @Inject(
        method =
            "getSidedInventory(Lnet/minecraft/core/Direction;)" +
            "Lnet/neoforged/neoforge/transfer/item/ItemStacksResourceHandler;",
        at = @At("HEAD"),
        cancellable = true,
        remap = false,
        require = 1
    )
    private void mysticalautomationOmniIo$exposeInventoryOnEverySide(
        Direction ignoredSide,
        CallbackInfoReturnable<ItemStacksResourceHandler> cir
    ) {
        var handler = MysticalAutomationItemHandlers.create((BaseInventoryTileEntity) (Object) this);

        if (handler != null) {
            cir.setReturnValue(handler);
        }
    }
}
