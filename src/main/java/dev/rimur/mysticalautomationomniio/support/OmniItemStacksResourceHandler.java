package dev.rimur.mysticalautomationomniio.support;

import com.blakebr0.cucumber.inventory.CItemStacksHandler;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.Arrays;

public final class OmniItemStacksResourceHandler extends ItemStacksResourceHandler {
    private final BaseInventoryTileEntity tile;
    private final CItemStacksHandler inventory;
    private final MachineSlotPolicy policy;

    public OmniItemStacksResourceHandler(BaseInventoryTileEntity tile, MachineSlotPolicy policy) {
        super(tile.getInventory().getStacks());

        this.tile = tile;
        this.inventory = tile.getInventory();
        this.policy = policy;
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public ItemResource getResource(int slot) {
        return this.inventory.getResource(slot);
    }

    @Override
    public long getAmountAsLong(int slot) {
        return this.inventory.getAmountAsLong(slot);
    }

    @Override
    public long getCapacityAsLong(int slot, ItemResource resource) {
        if (!this.canInsertInto(slot, resource)) {
            return 0;
        }

        return this.inventory.getCapacityAsLong(slot, resource);
    }

    @Override
    public boolean isValid(int slot, ItemResource resource) {
        return this.canInsertInto(slot, resource);
    }

    @Override
    public int insert(ItemResource resource, int amount, TransactionContext transaction) {
        if (resource == null || resource.isEmpty() || amount <= 0) {
            return 0;
        }

        if (this.policy.dynamicRecipe()) {
            return this.insertDistributed(resource, amount, transaction);
        }

        return this.insertSequential(resource, amount, transaction);
    }

    @Override
    public int insert(int slot, ItemResource resource, int amount, TransactionContext transaction) {
        if (resource == null || resource.isEmpty() || amount <= 0) {
            return 0;
        }

        if (!this.canInsertInto(slot, resource) || !this.canStackInto(slot, resource)) {
            return 0;
        }

        if (this.policy.bypassRecipeSlotValidation()) {
            return this.inventory.insert(slot, resource, amount, transaction, true);
        }

        return this.inventory.insert(slot, resource, amount, transaction);
    }

    @Override
    public int extract(int slot, ItemResource resource, int amount, TransactionContext transaction) {
        if (resource == null || resource.isEmpty() || amount <= 0) {
            return 0;
        }

        if (!this.isOutputSlot(slot)) {
            return 0;
        }

        return this.inventory.extract(slot, resource, amount, transaction, true);
    }

    private int insertSequential(ItemResource resource, int amount, TransactionContext transaction) {
        var inserted = 0;

        for (var slot : this.policy.fillOrder()) {
            if (inserted >= amount) {
                break;
            }

            inserted += this.insert(slot, resource, amount - inserted, transaction);
        }

        return inserted;
    }

    private int insertDistributed(ItemResource resource, int amount, TransactionContext transaction) {
        var inserted = 0;

        while (inserted < amount) {
            var slot = this.chooseDistributedSlot(resource);

            if (slot < 0) {
                break;
            }

            var moved = this.insert(slot, resource, 1, transaction);

            if (moved <= 0) {
                break;
            }

            inserted += moved;
        }

        return inserted;
    }

    private int chooseDistributedSlot(ItemResource resource) {
        if (this.policy.kind() == MachineKind.AWAKENING_ALTARNATOR) {
            return this.chooseAwakeningAltarnatorSlot(resource);
        }

        if (this.policy.kind() == MachineKind.ENCHANTERNATOR) {
            return this.chooseEnchanternatorSlot(resource);
        }

        if (DynamicRecipeSupport.hasActiveRecipe(this.tile, this.policy.kind())) {
            return this.chooseLowestOccupiedCompatibleSlot(resource);
        }

        for (var slot : this.policy.fillOrder()) {
            if (this.isEmptySlot(slot) && this.canInsertInto(slot, resource)) {
                return slot;
            }
        }

        return this.chooseLowestCompatibleSlot(resource);
    }

    private int chooseAwakeningAltarnatorSlot(ItemResource resource) {
        if (this.isAwakeningEssence(resource)) {
            var existingEssenceSlot = this.chooseLowestOccupiedCompatibleSlot(new int[] {1, 3, 5, 7}, resource);

            if (existingEssenceSlot >= 0) {
                return existingEssenceSlot;
            }

            for (var slot : new int[] {1, 3, 5, 7}) {
                if (this.isEmptySlot(slot) && this.canInsertInto(slot, resource) && this.canStackInto(slot, resource)) {
                    return slot;
                }
            }

            return -1;
        }

        if (this.isEmptySlot(0) && this.canInsertInto(0, resource) && this.canStackInto(0, resource)) {
            return 0;
        }

        for (var slot : new int[] {2, 4, 6, 8}) {
            if (this.isEmptySlot(slot) && this.canInsertInto(slot, resource) && this.canStackInto(slot, resource)) {
                return slot;
            }
        }

        var existingPedestalSlot = this.chooseLowestOccupiedCompatibleSlot(new int[] {2, 4, 6, 8}, resource);

        if (existingPedestalSlot >= 0) {
            return existingPedestalSlot;
        }

        if (this.canStackInto(0, resource)) {
            return 0;
        }

        return -1;
    }

    private int chooseEnchanternatorSlot(ItemResource resource) {
        if (this.isEnchanterTarget(resource)) {
            if (this.canInsertInto(2, resource) && this.canStackInto(2, resource)) {
                return 2;
            }

            return -1;
        }

        if (this.isExperienceEssence(resource)) {
            if (this.canInsertInto(1, resource) && this.canStackInto(1, resource)) {
                return 1;
            }

            return -1;
        }

        if (DynamicRecipeSupport.isEnchanterMaterial(this.tile, resource)
            && this.canInsertInto(0, resource)
            && this.canStackInto(0, resource)) {
            return 0;
        }

        if (this.isEnchantableItem(resource)) {
            if (this.canInsertInto(2, resource) && this.canStackInto(2, resource)) {
                return 2;
            }

            return -1;
        }

        if (this.canInsertInto(0, resource) && this.canStackInto(0, resource)) {
            return 0;
        }

        return -1;
    }

    private int chooseLowestOccupiedCompatibleSlot(ItemResource resource) {
        return this.chooseLowestOccupiedCompatibleSlot(this.policy.inputSlots(), resource);
    }

    private int chooseLowestOccupiedCompatibleSlot(int[] slots, ItemResource resource) {
        var chosenSlot = -1;
        var chosenAmount = Long.MAX_VALUE;

        for (var slot : slots) {
            if (this.isEmptySlot(slot) || !this.canStackInto(slot, resource)) {
                continue;
            }

            var amount = this.inventory.getAmountAsLong(slot);

            if (amount < chosenAmount) {
                chosenSlot = slot;
                chosenAmount = amount;
            }
        }

        return chosenSlot;
    }

    private boolean isEnchanterTarget(ItemResource resource) {
        var stack = resource.toStack();

        return stack.is(Items.BOOK);
    }

    private boolean isEnchantableItem(ItemResource resource) {
        return resource.toStack().isEnchantable();
    }

    private boolean isAwakeningEssence(ItemResource resource) {
        var itemId = BuiltInRegistries.ITEM.getKey(resource.getItem());

        return "mysticalagriculture".equals(itemId.getNamespace()) && itemId.getPath().endsWith("_essence");
    }

    private boolean isExperienceEssence(ItemResource resource) {
        var itemId = BuiltInRegistries.ITEM.getKey(resource.getItem());

        return "mysticalagriculture".equals(itemId.getNamespace()) && "experience_essence".equals(itemId.getPath());
    }

    private int chooseLowestCompatibleSlot(ItemResource resource) {
        var chosenSlot = -1;
        var chosenAmount = Long.MAX_VALUE;

        for (var slot : this.policy.fillOrder()) {
            if (!this.canInsertInto(slot, resource) || !this.canStackInto(slot, resource)) {
                continue;
            }

            var amount = this.inventory.getAmountAsLong(slot);

            if (amount < chosenAmount) {
                chosenSlot = slot;
                chosenAmount = amount;
            }
        }

        return chosenSlot;
    }

    private boolean canInsertInto(int slot, ItemResource resource) {
        if (!this.isValidSlot(slot) || !this.policy.isInputSlot(slot) || resource == null || resource.isEmpty()) {
            return false;
        }

        if (this.policy.bypassRecipeSlotValidation()) {
            return true;
        }

        return this.inventory.checkCanInsert(slot, resource);
    }

    private boolean canStackInto(int slot, ItemResource resource) {
        var current = this.inventory.getResource(slot);

        if (!current.isEmpty() && !current.equals(resource)) {
            return false;
        }

        return this.inventory.getAmountAsLong(slot) < this.inventory.getCapacityAsLong(slot, resource);
    }

    private boolean isEmptySlot(int slot) {
        return this.inventory.getResource(slot).isEmpty() || this.inventory.getAmountAsLong(slot) <= 0;
    }

    private boolean isOutputSlot(int slot) {
        var outputSlots = this.inventory.getOutputSlots();
        return outputSlots != null && Arrays.stream(outputSlots).anyMatch(value -> value == slot);
    }

    private boolean isValidSlot(int slot) {
        return slot >= 0 && slot < this.inventory.size();
    }
}
