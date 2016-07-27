package com.github.shortcube.skeet.database.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import com.github.shortcube.skeet.Skeet;

/**
 * This class represents the User model. This model class can be used thoroughout all
 * layers, the data layer, the controller layer and the view layer.
 *
 * @author Leon Merten
 */
public class User implements Serializable {

    private static final long serialVersionUID = -7564176422209182791L;
    private static Map<UUID, User> users = new HashMap<UUID, User>(  );

    private UUID id;
    private String name;
    private String capeURL;


    @Override
    public String toString() {
    	return "id=" + id + ";" 
    			+ "name=" + name + ";" 
    			+ "capeURL=" + capeURL + ";";
    }

    public static User getUser( UUID id) throws ExecutionException {
        if(users.containsKey( id ))
            return users.get( id );
        else
            return Skeet.getInstance().getUserDAO().find( id.toString() );
    }

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCapeURL() {
		if(capeURL != null && !capeURL.equalsIgnoreCase(""))
			return capeURL;
		else 
			return "http://hydra-media.cursecdn.com/minecraft.gamepedia.com/archive/5/50/20160216090132%21CobaltCape.png";
	}

	public void setCapeURL(String capeURL) {
		this.capeURL = capeURL;
	}
    
}