package com.github.shortcube.skeet.cape;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import com.github.shortcube.skeet.database.model.User;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import net.minecraft.client.resources.SkinManager.SkinAvailableCallback;

public class Capes {
	
	public static void GetCapes(GameProfile gameProfile,Map skinMap,  SkinAvailableCallback callback){
		try {
			skinMap.put(Type.CAPE, new MinecraftProfileTexture(User.getUser(gameProfile.getId()).getCapeURL(), skinMap));
		} catch (ExecutionException e) {
		}	
	}

}
