package com.github.shortcube.skeet;

import java.util.Date;

import org.lwjgl.opengl.Display;

import com.github.shortcube.skeet.database.DAOFactory;
import com.github.shortcube.skeet.database.UserDAO;
import com.github.shortcube.skeet.database.model.User;

import net.minecraft.client.Minecraft;

public class Skeet {
	private static final String NAME = "SkeedMod";
	private static final String VERSION = "1.5.37";
	private static int messages = 0;
	private static Skeet instance;
	
	private DAOFactory javabase;
	private UserDAO userDAO;
	
	public static void onEnable() {
		instance = new Skeet();
		
		Display.setTitle(NAME + " v" + VERSION);
		
		instance.init();
	}
	
	public void init() {
		this.javabase = DAOFactory.getInstance(
				"javabase.jdbc",
				"jdbc:mysql://localhost:3306/beta",
				"com.mysql.jdbc.Driver",
				"",
				"root"
		);
		this.userDAO = this.javabase.getUserDAO();
	}
	
	public void initPlayer() {
		if(Minecraft.getMinecraft().thePlayer != null ) {
		if(!this.userDAO.exists(Minecraft.getMinecraft().thePlayer.getUniqueID().toString())) {
			User user = new User();
			user.setId(Minecraft.getMinecraft().thePlayer.getUniqueID());
			user.setName(Minecraft.getMinecraft().thePlayer.getName());
			this.userDAO.create(user);
		}
		}
	}
	
	public void updatePlayer(String capeURL) {
		User user = new User();
		user.setId(Minecraft.getMinecraft().thePlayer.getUniqueID());
		user.setName(Minecraft.getMinecraft().thePlayer.getName());
		user.setCapeURL(capeURL);
		this.getUserDAO().update(user);
	}
	
	public static String getName() {
		return NAME;
	}
	
	public static String getVersion() {
		return VERSION;
	}
	
	public static Skeet getInstance() {
		return instance;
	}

	public static int getMessages() {
		return messages;
	}

	public static void setMessages(int messages) {
		Skeet.messages = messages;
	}

	public DAOFactory getJavabase() {
		return javabase;
	}

	public void setJavabase(DAOFactory javabase) {
		this.javabase = javabase;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public static void setInstance(Skeet instance) {
		Skeet.instance = instance;
	}
	
	
}
