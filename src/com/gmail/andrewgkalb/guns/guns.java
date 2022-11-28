package com.gmail.andrewgkalb.guns;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;

public class guns extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        // Create a file to store guns to, if not already created.
        try
        {
            File f = new File("gunlist.txt");
            if (f.createNewFile())
            {
                System.out.println("File created: " + f.getName());
            }
            else
            {
                System.out.println("File already exists.");
            }
        }
        catch (IOException e)
        {
            System.out.println("An error occurred when creating the file.");
            e.printStackTrace();
        }


        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
        {
            public void run()
            {
                for(int i = 0; i < Bullet.bullets.size(); i++)
                {
                    Bullet b = (Bullet) Bullet.bullets.get(i);
                    b.updateParticlePosition();
                    b.handleCollision();
                }
            }
        }, 2l, 1l);

        getLogger().info("guns has been enabled");
        getServer().getPluginManager().registerEvents(new GunListener(), this);
        this.getCommand("guns").setExecutor(new gunsCommandExecutor(this));
    }

    @Override
    public void onDisable()
    {
        getLogger().info("guns has been disabled");
    }

}
