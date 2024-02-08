package com.arazadaz.dd.core;

import com.arazadaz.dd.api.DifficultyModifier;
import com.arazadaz.dd.api.Modes.DifficultyType;
import com.arazadaz.dd.api.origins.OriginID;

import java.util.ArrayList;
import java.util.HashMap;

//Keeps track of internally defined origins such as user ones or the spawn origin. Mostly used for disabling them if the Origin Manager instructs it.
//Keeps track of difficulty modifiers

//Dynamic Difficulty Vault
public class DDVault {

    private static DDVault singletonInstance;

    public ArrayList<OriginID> userOrigins = new ArrayList<>();
    public OriginID spawnOrigin;

    public HashMap<DifficultyType, ArrayList<DifficultyModifier> > difficultyModifiers = new HashMap<>();

    private DDVault(){
        difficultyModifiers.put(DifficultyType.SURFACE, new ArrayList<>());
        difficultyModifiers.put(DifficultyType.ENVIRONMENTAL, new ArrayList<>());
        difficultyModifiers.put(DifficultyType.SPECIAL, new ArrayList<>());
    }

    public static DDVault createDDVault(){
        if(singletonInstance==null){
            singletonInstance = new DDVault();
            return singletonInstance;
        }
        else return null;
    }


}
