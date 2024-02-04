package com.arazadaz.dd.api;

import com.arazadaz.dd.core.Origin;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;

public class OriginManager {

    private static boolean useSpawnOrigin = true;
    private static boolean useUserOrigins = true;

    private static HashMap<String, ArrayList<Origin>> originMap; //Mapping of origins to each world/all;


    public static void addOrigin(Vec3 pos, String[] types){ //basic, less control

        double range = 0; //Will get global value from config after it's setup
        String[] formulas = null; //will get global formulas from config after it's setup

        addOrigin(pos, formulas, types, range, false, false, "all");

    }
    public static void addOrigin(Vec3 pos, String[] formulas, String[] types, double range, boolean noCalculationBound,  boolean omitDefaultType){ //Advanced, more control

        addOrigin(pos, formulas, types, range, noCalculationBound, omitDefaultType, "all");

    }

    public static void addOrigin(Vec3 pos, String[] formulas, String types[], double range, boolean noCalculationBound,  boolean omitDefaultType, String world){ //Optional assignment of origin to a specific world

        String[] newTypes;

        if(omitDefaultType){ //No modification

            newTypes = types;

        }else{ //Add default type to origin

            newTypes = new String[types.length+1];

            for(int i = 0; i<types.length; i++){
                newTypes[i] = types[i];
            }

            newTypes[types.length] = "default";

        }

        ArrayList<Origin> originsList;

        if(originMap.containsKey(world)){
            originsList = originMap.get(world);
        }else{
            originsList = new ArrayList<Origin>();
        }

        Origin newOrigin = new Origin(pos, formulas, newTypes, range, noCalculationBound);
        originsList.add(newOrigin);
        originMap.put(world, originsList);
    }

    public static void disableSpawnOrigin(){ //Should be used if a mod has its own origin definition it wants to use instead or if it's disabled in config
        useSpawnOrigin = false;
    }

    public static void disableUserDefinedOrigins(){ //Should be used if a mod has multiple origin definitions it wants to use in place of user-defined ones
        useUserOrigins = false;
    }

}
