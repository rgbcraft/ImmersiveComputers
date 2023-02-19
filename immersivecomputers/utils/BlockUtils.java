package immersivecomputers.utils;

import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeDirection;

public class BlockUtils {
    private static final int[] SIDE_LOOKUP = {0, 0, 4, 5, 3, 2};

    // Get the block placement direction based on the player yaw
    public static ForgeDirection getBlockDirection(double yaw) {
        int dir = MathHelper.floor_double((yaw * 4.0F / 360.0F) + 0.5D) & 3;

        switch (dir) {
            case 0:
                return ForgeDirection.getOrientation(2);
            case 1:
                return ForgeDirection.getOrientation(5);
            case 2:
                return ForgeDirection.getOrientation(3);
            case 3:
                return ForgeDirection.getOrientation(4);
            default:
                return ForgeDirection.UNKNOWN;
        }
    }

    public static ForgeDirection getAdjacent(ForgeDirection direction) {
        return ForgeDirection.getOrientation(SIDE_LOOKUP[direction.ordinal()]);
    }
}
