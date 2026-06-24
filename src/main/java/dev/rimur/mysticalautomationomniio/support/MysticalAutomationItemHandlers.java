package dev.rimur.mysticalautomationomniio.support;

import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jetbrains.annotations.Nullable;

public final class MysticalAutomationItemHandlers {
    private MysticalAutomationItemHandlers() {
    }

    @Nullable
    public static ItemStacksResourceHandler create(BaseInventoryTileEntity tile) {
        var policy = MachineSlotPolicy.fromClassName(tile.getClass().getName());

        if (policy == null) {
            return null;
        }

        return new OmniItemStacksResourceHandler(tile, policy);
    }
}
