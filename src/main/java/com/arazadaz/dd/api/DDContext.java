package com.arazadaz.dd.api;

import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public class DDContext {


    public LivingEntity entity;

    public DDContext(@Nullable LivingEntity entity){
        this.entity = entity;
    }

}
