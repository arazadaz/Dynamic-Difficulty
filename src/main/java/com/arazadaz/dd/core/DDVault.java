package com.arazadaz.dd.core;

import com.arazadaz.dd.api.DDContext;
import com.arazadaz.dd.api.Modes.DifficultyType;
import com.arazadaz.dd.api.origins.OriginID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

//Keeps track of internally defined origins such as user ones or the spawn origin. Mostly used for disabling them if the Origin Manager instructs it.
//Keeps track of difficulty modifiers

//Dynamic Difficulty Vault
public class DDVault {

    private static DDVault singletonInstance;

    public ArrayList<OriginID> userOrigins = new ArrayList<>();
    public OriginID spawnOrigin;

    public HashMap<DifficultyType, ArrayList<Predicate<DDContext>> > difficultyModifiers;


    private DDVault(){

    }

    public static DDVault createDDVault(){
        if(singletonInstance!=null){
            singletonInstance = new DDVault();
            return singletonInstance;
        }
        else return null;
    }


}
