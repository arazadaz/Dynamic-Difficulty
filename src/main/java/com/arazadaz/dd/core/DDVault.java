package com.arazadaz.dd.core;

import com.arazadaz.dd.api.origins.OriginID;

import java.util.ArrayList;

//Keeps track of internally defined origins such as user ones or the spawn origin. Mostly used for disabling them Origin Manager instructs it.

//Dynamic Difficulty Vault
public class DDVault {

    public static ArrayList<OriginID> userOrigins = new ArrayList<>();
    public static OriginID spawnOrigin;


}
