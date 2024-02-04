package com.arazadaz.dd.api;

import net.minecraft.world.phys.Vec3;

public class OriginManager {

    private static boolean useSpawnOrigin = true;
    private static boolean useUserOrigins = true;

    public static void addOrigin(Vec3 pos, String[] formulas, String[] types, double range, boolean noCalculationBound){

        addOrigin(pos, formulas, types, range, noCalculationBound, ""); //Empty string means all worlds

    }

    public static void addOrigin(Vec3 pos, String[] formulas, String types[], double range, boolean noCalculationBound, String world){ //Optional assignment of origin to a specific world

    }

    public static void disableSpawnOrigin(){ //Should be used if a mod has its own origin definition it wants to use instead or if it's disabled in config
        useSpawnOrigin = false;
    }

    public static void disableUserDefinedOrigins(){ //Should be used if a mod has multiple origin definitions it wants to use in place of user-defined ones
        useUserOrigins = false;
    }

}
