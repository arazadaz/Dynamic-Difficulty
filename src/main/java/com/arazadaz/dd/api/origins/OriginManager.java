package com.arazadaz.dd.api.origins;

import com.arazadaz.dd.api.origins.OriginID;
import com.arazadaz.dd.core.Origin;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;

public class OriginManager {

    private static boolean useSpawnOrigin = true;
    private static boolean useUserOrigins = true;

    private static HashMap<String, ArrayList<Origin>> originMap; //Mapping of origins to each world/all;




    public static OriginID addOrigin(Vec3 pos, String[] tags){ //basic, less control

        double range = 0; //Will get global value from config after it's setup
        String[] formulas = null; //will get global formulas from config after it's setup

        return addOrigin(pos, formulas, tags, range, false, 1, "all");

    }

    public static OriginID addOrigin(Vec3 pos, String[] formulas, String[] tags, double range, boolean noCalculationBound, int defaultTag){ //Advanced, more control

        return addOrigin(pos, formulas, tags, range, noCalculationBound, defaultTag, "all");

    }


    public static OriginID addOrigin(Vec3 pos, String[] formulas, String tags[], double range, boolean noCalculationBound,  int defaultTag, String world){ //Optional assignment of origin to a specific world

        String[] newTags;

        //Add default type to origin or none if desired
        if(defaultTag == 1){ //Default tag

            newTags = new String[tags.length+1];
            for(int i = 1; i<=tags.length; i++){
                newTags[i] = tags[i];
            }

            newTags[0] = "default";

        }else if(defaultTag == 2){ //Dynamic tag

            newTags = new String[tags.length+1];
            for(int i = 1; i<=tags.length; i++){
                newTags[i] = tags[i];
            }

            newTags[0] = "dynamic"; //Dynamic because these are generally going to be used for generated origin points after the world was loaded.

        }else{ //No default tag
            newTags = tags; //Keep original tags, exists to prevent conflicts and in case a mod dev wants to specifically design how their origin point interacts with the game.
        }

        ArrayList<Origin> originsList;

        if(originMap.containsKey(world)){
            originsList = originMap.get(world);
        }else{
            originsList = new ArrayList<Origin>();
        }

        Origin newOrigin = new Origin(pos, formulas, newTags, range, noCalculationBound);
        originsList.add(newOrigin);
        originMap.put(world, originsList);
        return new OriginID(world, newOrigin);
    }



    public static void removeOriginPoint(OriginID id){ //Should really only be done for dynamic origin points, but maybes there's a reason to use it on default types.
        originMap.get(id.world).remove(id.origin);
    }



    public static void disableSpawnOrigin(){ //Should be used if a mod has its own origin definition it wants to use instead or if it's disabled in config
        useSpawnOrigin = false;
    }

    public static void disableUserDefinedOrigins(){ //Should be used if a mod has multiple origin definitions it wants to use in place of user-defined ones
        useUserOrigins = false;
    }


}
