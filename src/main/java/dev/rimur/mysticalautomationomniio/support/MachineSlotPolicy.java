package dev.rimur.mysticalautomationomniio.support;

import java.util.Arrays;

public record MachineSlotPolicy(
    MachineKind kind,
    int[] inputSlots,
    int[] fillOrder,
    boolean dynamicRecipe,
    boolean bypassRecipeSlotValidation
) {
    private static final int[] GRID_INPUTS = rangeClosed(0, 8);
    private static final int[] CRAFTING_FILL_ORDER = {0, 1, 3, 4, 2, 5, 6, 7, 8};
    private static final int[] AWAKENING_FILL_ORDER = {0, 2, 4, 6, 8, 1, 3, 5, 7};

    public static MachineSlotPolicy fromClassName(String className) {
        if (className.endsWith(".InfuserTileEntity")) {
            return new MachineSlotPolicy(MachineKind.INFUSER, rangeClosed(0, 6), rangeClosed(0, 6), false, false);
        }

        if (className.endsWith(".CrafterTileEntity")) {
            return new MachineSlotPolicy(MachineKind.CRAFTER, GRID_INPUTS, CRAFTING_FILL_ORDER, true, true);
        }

        if (className.endsWith(".FarmerTileEntity")) {
            return new MachineSlotPolicy(MachineKind.FARMER, rangeClosed(0, 2), rangeClosed(0, 2), false, false);
        }

        if (className.endsWith(".FertilizerTileEntity")) {
            return new MachineSlotPolicy(MachineKind.FERTILIZER, rangeClosed(0, 7), rangeClosed(0, 7), false, false);
        }

        if (className.endsWith(".InfusionAltarnatorTileEntity")) {
            return new MachineSlotPolicy(MachineKind.INFUSION_ALTARNATOR, GRID_INPUTS, GRID_INPUTS, true, true);
        }

        if (className.endsWith(".AwakeningAltarnatorTileEntity")) {
            return new MachineSlotPolicy(MachineKind.AWAKENING_ALTARNATOR, GRID_INPUTS, AWAKENING_FILL_ORDER, true, true);
        }

        if (className.endsWith(".EnchanternatorTileEntity")) {
            return new MachineSlotPolicy(MachineKind.ENCHANTERNATOR, rangeClosed(0, 2), rangeClosed(0, 2), true, true);
        }

        return null;
    }

    public boolean isInputSlot(int slot) {
        return Arrays.stream(this.inputSlots).anyMatch(value -> value == slot);
    }

    private static int[] rangeClosed(int startInclusive, int endInclusive) {
        var values = new int[endInclusive - startInclusive + 1];

        for (int i = 0; i < values.length; i++) {
            values[i] = startInclusive + i;
        }

        return values;
    }
}
