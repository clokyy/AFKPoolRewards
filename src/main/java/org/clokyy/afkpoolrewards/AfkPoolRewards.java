package org.clokyy.afkpoolrewards;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class AfkPoolRewards extends JavaPlugin {

    private WorldGuardPlugin wg;
    private Essentials ess;
    @Override
    public void onEnable() {
        wg = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
    // Creates a timer for every 30 minutes to run this function
        new BukkitRunnable(){
            public void run(){
                GiveKey();
            }
        }.runTaskTimer(this, 0L, 20L * 60l * 30L); //20 ticks, * 60 == 60 seconds * 30 == 30 minutes
    }

    public boolean isAfk(Player player){
        User essUser = ess.getUser(player);
        return essUser != null && essUser.isAfk();
    }

    public void GiveKey(){
        for (Player player: Bukkit.getOnlinePlayers()){
            if(isInRegion(player)){
                if(isAfk(player)){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "crate give " + player.getName() + "139");
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
}
