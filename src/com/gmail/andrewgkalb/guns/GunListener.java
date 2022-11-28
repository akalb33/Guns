package com.gmail.andrewgkalb.guns;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class GunListener implements Listener
{
    private final CooldownManager cooldownManager = new CooldownManager();


    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteractEvent(PlayerInteractEvent event)
    {
        Player p = event.getPlayer();

        GunList gunlist = new GunList("gunlist.txt");
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        if(!item.getType().isAir())
        {
            for (int i = 0; i < gunlist.mGuns.size(); i++)
            {
                GunInfo gun = gunlist.mGuns.get(i);
                if(meta.getDisplayName().equals(gun.name))
                {
                    if (meta.getLore().get(0).equals(ChatColor.GRAY + "Official Gun"))
                    {
                        if (item.getType().equals(gun.item))
                        {
                            long timeLeft = System.currentTimeMillis() - cooldownManager.getCooldown(p.getUniqueId());
                            //System.out.println("gun can fire every " + 1000 * (1 / gunlist.mGuns.get(i).fireRate));
                            //System.out.println("time passed: " + timeLeft);
                            if(timeLeft >= 1000 * (1 / gunlist.mGuns.get(i).fireRate))
                            {
                                Bullet b = new Bullet(p, gunlist.mGuns.get(i).damage);
                                cooldownManager.setCooldown(p.getUniqueId(), System.currentTimeMillis());
                                break;
                            }
                        }
                    }
                }
            }
        }
        else
        {
            //p.sendMessage(ChatColor.RED + "Wait a bit");
        }

    }

}
