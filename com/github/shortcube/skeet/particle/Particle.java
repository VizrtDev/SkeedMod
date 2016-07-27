package com.github.shortcube.skeet.particle;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Particle {
	
	public static void spawnParticle( World worldIn, EntityPlayer entityPlayer ) {
		if(!worldIn.isRemote){
			worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, entityPlayer.posX + 0.5D, entityPlayer.posY + 1.0D, entityPlayer.posZ + 0.5D, 0.0D, 0.0D, 0.0D, new int[0]);
		}
	}

}
