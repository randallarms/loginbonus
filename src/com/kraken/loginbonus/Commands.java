package com.kraken.loginbonus;

import java.util.Arrays;
import java.util.WeakHashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Commands {
	
	  Main plugin;
	  String VERSION;
	  
	  String language;
	  Messages messenger;
	  
	  //Options
	  WeakHashMap<String, Boolean> options = new WeakHashMap<>();
	
  //Constructor
	public Commands(Main plugin) {
		
		this.plugin = plugin;
		this.VERSION = Main.VERSION;
		this.options = plugin.options;
		
		this.language = plugin.language;
		this.messenger = new Messages(plugin, language);
	
	}
	
  //Commands
	public boolean execute(boolean isPlayer, Player player, String command, String[] args) {
		
		switch (command) {
		
			//Command: loginbonus
			case "loginbonus":
			case "logins":
				
				switch (args.length) {
						
					case 1:
						
						switch ( args[0].toLowerCase() ) {
						
						  //Command: version     
			    			case "version":
			    			
								if ( !isPlayer ) {
									plugin.consoleMsg("cmdVersion");
								} else {
									plugin.msg(player, "cmdVersion");
								}
								
				                return true;
			                
			                default:
					
								if ( !isPlayer ) {
									plugin.consoleMsg("errorCommandFormat");
								} else {
									plugin.msg(player, "errorIllegalCommand");
								}
								
				                return true;
			                	
						}
						
					case 2:
						
						//Check if sender is a player and if that player has OP perms
						if (isPlayer) {
							
							if ( !player.isOp() ) {
								plugin.msg(player, "errorIllegalCommand");
								return true;
							}
							
						}
						
						//Command handling switch
						switch ( args[0].toLowerCase() ) {
						
							//Command: loginbonus item
							case "item":
							case "bonus":
							
								String item = args[1].toLowerCase();
								
								//Language command handling
								if ( Arrays.asList(Material.values()).contains( item ) ) {
									
									plugin.setBonus(item);
									
									if ( !isPlayer ) {
										plugin.consoleMsg("cmdBonusSet");
									} else {
										plugin.msg(player, "cmdBonus");
									}
									
									return true;
								
								//Language command error handling
								} else {
									
									if ( !isPlayer ) {
										plugin.consoleMsg("errorBonusSet");
									} else {
										plugin.msg(player, "errorBonusNotFound");
									}
									
									return true;
								}
							
							//Command: loginbonus language
							case "language":
							case "lang":
								
								String lang = args[1].toLowerCase();
								
								//Language command handling
								if ( plugin.languages.contains( lang ) ) {
									
									plugin.setLanguage(lang);
									
									if ( !isPlayer ) {
										plugin.consoleMsg("cmdLanguageSet");
									} else {
										plugin.msg(player, "cmdLang");
									}
									
									return true;
								
								//Language command error handling
								} else {
									
									if ( !isPlayer ) {
										plugin.consoleMsg("errorLanguageSet");
									} else {
										plugin.msg(player, "errorLangNotFound");
									}
									
									return true;
								}
								
							//Command: loginbonus silentMode
							case "silentmode":
								
								switch ( args[1].toLowerCase() ) {
								
									case "true":
									case "enable":
									case "enabled":
									case "on":
									case "cierto":
										plugin.setOption("silentMode", true);
										plugin.silencer(true);
										
										if ( !isPlayer ) {
											plugin.consoleMsg("cmdSilentModeOn");
										} else {
											plugin.msg(player, "cmdSilentOn");
										}
										
										return true;
										
									case "false":
									case "disable":
									case "disabled":
									case "off":
									case "falso":
										plugin.setOption("silentMode", false);
										plugin.silencer(false);
										
										if ( !isPlayer ) {
											plugin.consoleMsg("cmdSilentModeOff");
										} else {
											plugin.msg(player, "cmdSilentOff");
										}
										
										return true;
								
								  //Silentmode command error handling
									default: 
										
										if ( !isPlayer ) {
											plugin.consoleMsg("errorCommandFormat");
										} else {
											plugin.msg(player, "errorSilentModeFormat");
										}
										
										return true;
								
								}
							
			        	    //Command: loginbonus perms
			        	    case "perms":
			        	    case "permissions":
			        	    case "perm":
			        	    case "permission":
			        	    		
			        	    		switch ( args[1].toLowerCase() ) {
			        	    		
			        	    			case "on":
			        	    			case "cierto":
			        	    			case "enable":
			        	    			case "enabled":
			        	    			case "true":
			        	    				plugin.setOption("permissions", true);
											
											if ( !isPlayer ) {
												plugin.consoleMsg("cmdPermsEnabled");
											} else {
												plugin.msg(player, "cmdPermsEnabled");
											}
											
			        	    				return true;
			        	    				
			        	    			case "off":
			        	    			case "disable":
			        	    			case "disabled":
			        	    			case "false":
			        	    			case "falso":
			        	    				plugin.setOption("permissions", false);
											
											if ( !isPlayer ) {
												plugin.consoleMsg("cmdPermsDisabled");
											} else {
												plugin.msg(player, "cmdPermsDisabled");
											}
											
			        	    				return true;
			        	    				
			        	    			default:
			        	    				if ( !isPlayer ) {
			        	    					plugin.consoleMsg("errorArgumentFormat");
			        	    				} else {
			        	    					plugin.msg(player, "errorPermsFormat");
			        	    				}
			        	    				
			        	        	    	return true;
			        	        	    	
			        	    		}
			        	    	
						}
								
					default:
						if (isPlayer) {
							plugin.msg(player, "errorIllegalCommand");
						} else {
							plugin.consoleMsg("errorCommandFormat");
						}
						return true;
						
				}
						
			}
		
			return true;
				
		}
	
}
