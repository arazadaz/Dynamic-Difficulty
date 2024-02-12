package com.arazadaz.dd.api;

import com.arazadaz.dd.api.origins.Origin;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class DDContext {


    public LivingEntity entity;
    public Origin srcOrigin;
    public Modes.DifficultyMode difficultyType;
    public Modes.RadiusMode rMode;
    public Vec3 dstPos;

    public DDContext(@Nullable LivingEntity entity){
        this.entity = entity;
    }

}
