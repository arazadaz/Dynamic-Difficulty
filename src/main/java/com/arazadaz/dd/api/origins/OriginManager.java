package com.arazadaz.dd.api.origins;

import com.arazadaz.dd.Main;
import com.arazadaz.dd.core.DDVault;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class OriginManager {

    private static HashMap<String, ArrayList<Origin>> originMap; //Mapping of origins to each world/all;



    public static OriginID registerOrigin(Vec3 pos, String[] tags){ //basic, less control

        double range = 0; //Will get global value from config after it's setup
        String[] formulas = null; //will get global formulas from config after it's setup

        return registerOrigin(pos, formulas, tags, range, false, 1, "all");

    }

    public static OriginID registerOrigin(Vec3 pos, String[] formulas, String[] tags, double range, boolean noCalculationBound, int defaultTag){ //Advanced, more control

        return registerOrigin(pos, formulas, tags, range, noCalculationBound, defaultTag, "all");

    }


    public static OriginID registerOrigin(Vec3 pos, String[] formulas, String tags[], double range, boolean noCalculationBound, int defaultTag, String world){ //Optional assignment of origin to a specific world

        ArrayList<Origin> originsList;

        if(originMap.containsKey(world)){
            originsList = originMap.get(world);
        }else{
            originsList = new ArrayList<Origin>();
        }

        Origin newOrigin = new Origin(pos, formulas, tags, range, noCalculationBound, defaultTag);
        originsList.add(newOrigin);
        originMap.put(world, originsList);
        return new OriginID(world, newOrigin);
    }


    public static OriginID registerOrigin(Origin origin){ //Advanced, more control

        return registerOrigin(origin, "all");

    }

    public static OriginID registerOrigin(Origin origin, String world){ //Advanced, more control

        ArrayList<Origin> originsList;

        if(originMap.containsKey(world)){
            originsList = originMap.get(world);
        }else{
            originsList = new ArrayList<Origin>();
        }

        originsList.add(origin);
        originMap.put(world, originsList);

        return new OriginID(world, origin);

    }



    public static void removeOriginPoint(OriginID id){ //Should really only be done for dynamic origin points, but maybes there's a reason to use it on default types outside of below methods.
        originMap.get(id.world).remove(id.origin);
    }



    public static void disableSpawnOrigin(){ //Should be used if a mod has its own origin definition it wants to use instead or if it's disabled in config
        removeOriginPoint(Main.vault.spawnOrigin);
    }

    public static void disableUserDefinedOrigins(){ //Should be used if a mod has multiple origin definitions it wants to use in place of user-defined ones

        for(int i = 0; i<Main.vault.userOrigins.size(); i++){

            OriginID currentUDO = Main.vault.userOrigins.get(i);

            removeOriginPoint(currentUDO);

        }

    }


    public static Origin getNearestOrigin(String world, String tag, Vec3 pos){

        Iterator<Origin> it = originMap.get("all").iterator();

        Origin nearestOrigin = it.next();

        double nearestDistance = nearestOrigin.pos.distanceTo(pos);

        while(it.hasNext()){
            Origin currentOrigin = it.next();

            boolean containsTag = false;
            for(String currentTag : currentOrigin.tags){
                if(currentTag.equals(tag)) containsTag=true;
            }

            if(!containsTag) continue;

            double currentDistance = currentOrigin.pos.distanceTo(pos);

            if(currentDistance<nearestDistance){
                nearestDistance = currentDistance;
                nearestOrigin = currentOrigin;
            }
        }

        it = originMap.get(world).iterator();

        while(it.hasNext()){
            Origin currentOrigin = it.next();

            boolean containsTag = false;
            for(String currentTag : currentOrigin.tags){
                if(currentTag.equals(tag)) containsTag=true;
            }

            if(!containsTag) continue;

            double currentDistance = currentOrigin.pos.distanceTo(pos);

            if(currentDistance<nearestDistance){
                nearestDistance = currentDistance;
                nearestOrigin = currentOrigin;
            }
        }

        return nearestOrigin;

    }

    public static Direction getNearestOriginDirection(String world, String tag, Vec3 pos){

        Origin target = getNearestOrigin(world, tag, pos);

        Vec3 targetPos = target.pos;

        //Math stuff to return correct direction.

        return Direction.NORTH;
    }


}
