package com.kraken.loginbonus;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class MainListener implements Listener {
	
	Main plugin;
	WeakHashMap<String, Boolean> options = new WeakHashMap<>();
	String language;
	
	ArrayList<Player> cooldown = new ArrayList<>();
	
	//Players logins file
	File loginsFile = new File("plugins/LoginBonus", "logins.yml");
    FileConfiguration loginsConfig = YamlConfiguration.loadConfiguration(loginsFile);
	
    public MainListener(Main plugin, String language) {
  	  
  	  plugin.getServer().getPluginManager().registerEvents(this, plugin);
  	  this.plugin = plugin;
  	  this.language = language;
  	  
    }
    
    public void setOption(String option, boolean setting) {
    	options.put(option, setting);
    }
    
    public void setLanguage(String language) {
    	this.language = language;
    }
    
	public void loginBonus(Player player, String date, boolean forceBonus) {
    	
    	//Get the id of the login bonus item
    	String id = plugin.getConfig().getString("bonus");
    	
		ItemStack item;
		
    	//Create & give the item
    	try {
    		
    		String UUIDString = player.getUniqueId().toString();
    		
    		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    	
    		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
    		String last = dateFormat.format( formatter.parse( loginsConfig.getString(UUIDString + ".last") ) );
    		
    		Integer total = loginsConfig.getInt(UUIDString + ".total");
    		String playerName = loginsConfig.getString(UUIDString + ".name");
    		
    		if ( playerName != player.getName() ) {
    			loginsConfig.set( UUIDString + ".name", player.getName() );
    		}
    		
    		if ( !last.equalsIgnoreCase(date) || forceBonus ) {
    			
        		item = new ItemStack( Material.matchMaterial(id) );
            	PlayerInventory inv = player.getInventory();
            	inv.addItem(item);
            	
            	loginsConfig.set( UUIDString + ".total", total++ );
            	loginsConfig.set( UUIDString + ".last", date );
            	
            	//Message player with login bonus items info
            	plugin.messenger.makeMsg(player, "eventLoginBonus");
            	player.sendMessage("Last: " + last);
            	player.sendMessage("Date: " + date);
    			
    		}
    		
        	try {
    			loginsConfig.save(loginsFile);
    		} catch (IOException e) {
    			//No need to fuss...
    		}
    		
    	} catch (NullPointerException e) {
    		
    		plugin.messenger.makeMsg(player, "errorBonusID");
    		
    	} catch (ParseException e1) {

    		plugin.messenger.makeMsg(player, "errorBonusID");
    		
		}
    	
    }
    
    public void createLoginsProfile(Player player, String date) {
    	
    	String UUIDString = player.getUniqueId().toString();
    	
    	loginsConfig.set(UUIDString + ".name", player.getName() );
    	loginsConfig.set(UUIDString + ".total", 1 );
    	loginsConfig.set(UUIDString + ".last", date);
    	
    	try {
			loginsConfig.save(loginsFile);
		} catch (IOException e) {
			//No need to fuss...
		}
    	
    }
    
    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent e) {
    	
    	Player player = e.getPlayer();
    	String UUIDString = player.getUniqueId().toString();
    	
    	if ( cooldown.contains(player) ) {
    		return;
    	}
    	
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date date = new Date();
		String simpleDate = dateFormat.format(date);
    	
    	if ( !loginsConfig.getKeys(false).contains(UUIDString) ) {
    		createLoginsProfile(player, simpleDate);
    		loginBonus(player, simpleDate, true);
    	} else {
    		loginBonus(player, simpleDate, false);
    	}
    	
    	cooldown.add(player);
    	
    	Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
    		public void run() {
    			cooldown.remove(player);
    		}
    	}, 1000 );
    	
    }
    
}
