package org.clokyy.afkpoolrewards;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.event.Listener;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


public final class AfkPoolRewards extends JavaPlugin implements Listener {

    private WorldGuardPlugin wg;
    private Essentials ess;
    @Override
    public void onEnable() {
        wg = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");

        new BukkitRunnable(){
            public void run(){
                GiveKey();
            }
        }.runTaskTimer(this, 0L, 20L * 30L * 60L);

        getCommand("afktime").setExecutor(this);


        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "-------------------------------------");
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "ENABLING AFK REWARDS");
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Created and Updated by Clokyy (BigScaryMan#2495)");
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Version 1.2 BETA");
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "-------------------------------------");
    }


    public boolean isAfk(Player player){
        User essUser = ess.getUser(player);
        return essUser != null && essUser.isAfk();


    }

    public void GiveKey(){
        for (Player player: Bukkit.getOnlinePlayers()){
            if(isInRegion(player)){
                if(isAfk(player)){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName() + " minecraft:stone");
                }
            }
        }
    }

    public boolean isInRegion(Player player){
        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(player.getWorld()));
        ProtectedRegion afkRegion = regionManager.getRegion("AFK");
        if(afkRegion == null){
            Bukkit.getConsoleSender().sendMessage("Cannot find region labeled AFK, please create one.");
        }

        return afkRegion.contains(BukkitAdapter.asBlockVector(player.getLocation()));
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        Player player = (Player) sender;

        if(cmd.getName().equalsIgnoreCase("afktime")){
//            if(player.hasPermission("afkrewards.afktime")){
                User user = ess.getUser(player);
//
                if(user.isAfk()){
                    long timeAFK = user.getAfkSince();
                    long timeElapsed = System.currentTimeMillis() - timeAFK;
//
                    sender.sendMessage(ChatColor.GREEN + "You have been afk for " + timeElapsed / 1000  + " seconds");
//              }
            }

            return true;
        }

        return false;
    }

}



