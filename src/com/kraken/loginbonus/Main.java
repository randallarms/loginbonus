// =========================================================================
// |LOGINBONUS v1.0 | for Minecraft v1.12
// | by Kraken | Link TBA
// | code inspired by various Bukkit & Spigot devs -- thank you.
// |
// | Always free & open-source! If the main plugin is being sold/re-branded,
// | please let me know on the SpigotMC site, or wherever you can. Thanks!
// | Source code: https://github.com/randallarms/loginbonus
// =========================================================================

package com.kraken.loginbonus;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;

public class Main extends JavaPlugin implements Listener {
	
	//Lang vars
	public static String VERSION = "1.0";
	String language;
	ArrayList<String> languages = new ArrayList<String>();
	Messages messenger;
	
	//Class vars
	MainListener listener;
	
	//Options
	WeakHashMap<String, Boolean> options = new WeakHashMap<>();
	
	//Enable
    @Override
    public void onEnable() {
    	
    	getLogger().info("Loading options...");
		
		//Copies the default config.yml from within the .jar if "plugins/LoginBonus/config.yml" does not exist
		getConfig().options().copyDefaults(true);
		
    	//Copies the default logins.yml from within the .jar if "plugins/LoginBonus/logins.yml" does not exist
    	saveResource("logins.yml", false);
		
		//Language/Messages handler class construction
		languages.add("english");
		loadMessageFiles();
		language = getConfig().getString("language");
		messenger = new Messages(this, "english");
		
		//General plugin management
    	PluginManager pm = getServer().getPluginManager();
    	listener = new MainListener(this, language);
		pm.registerEvents(listener, this);
		
		//Language setting
		setLanguage(language);
		
	    //Loading default settings into options
    	setOption( "permissions", getConfig().getBoolean("permissions") );
    	setOption( "silentMode", getConfig().getBoolean("silentMode") );
    	silencer( options.get("silentMode") );
    	
    	getLogger().info("Finished loading!");
			
    }
    
    //Disable
    @Override
    public void onDisable() {
        getLogger().info("Disabling...");
    }
    
    //Messages
    public void msg(Player player, String cmd) {
    	messenger.makeMsg(player, cmd);
    }
    
    public void consoleMsg(String cmd) {
    	messenger.makeConsoleMsg(cmd);
    }
    
    //Setting methods
    //Options setting
    public void setOption(String option, boolean setting) {
    	getConfig().set(option, setting);
    	saveConfig();
    	options.put(option, setting);
    	listener.setOption(option, setting);
    	getLogger().info(option + " setting: " + setting );
    }
    
    //Language setting
    public void setLanguage(String language) {
    	this.language = language;
    	getConfig().set("language", language);
    	saveConfig();
    	listener.setLanguage(language);
    	messenger.setLanguage(language);
    	getLogger().info( "Language: " + language.toUpperCase() );
    }
    
	public void loadMessageFiles() {
		
		for (String lang : languages) {
			
		    File msgFile = new File("plugins/LoginBonus/lang/", lang.toLowerCase() + ".yml");
	        
		    if ( !msgFile.exists() ) {
		    	saveResource("lang/" + lang.toLowerCase() + ".yml", false);
		    }
		    
		}
		
    }
    
    //Silent mode setting
    public void silencer(boolean silentMode) {
    	messenger.silence(silentMode);
    }
    
    //LoginBonus commands
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		//Command handling
		Commands commands = new Commands(this);
		String command = cmd.getName();
		
		//Player handling
		Player player;
		boolean isPlayer = sender instanceof Player;
		
		if (isPlayer) {
			player = (Player) sender;
		} else {
			player = Bukkit.getServer().getPlayerExact("Octopus__");
		}
		
		//Execute command & return
		return commands.execute(isPlayer, player, command, args);
		
	}
	
		
}
