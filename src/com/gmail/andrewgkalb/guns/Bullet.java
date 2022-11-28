package com.gmail.andrewgkalb.guns;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Bullet
{
    public static ArrayList bullets = new ArrayList<Bullet>();

    private Player mPlayer;
    private LivingEntity mBat;
    private Particle mParticle;
    private Particle.DustOptions mDustOptions;
    private Vector mVelocity;
    private Location mSpawnLocation;
    private double mDamage;
    private long mSpawnTimeStamp;

    public Bullet(Player shooter, double damage)
    {
        mDamage = damage;
        bullets.add(this);
        mPlayer = shooter;

        mVelocity = mPlayer.getLocation().getDirection().normalize().multiply(2);
        mBat = (LivingEntity) mPlayer.getWorld().spawnEntity(mPlayer.getEyeLocation(), EntityType.BAT);
        mBat.setInvisible(true);
        mBat.setSilent(true);
        mBat.setGliding(true);
        mBat.setVelocity(mVelocity);
        mSpawnTimeStamp = System.currentTimeMillis();
        mDustOptions = new Particle.DustOptions(Color.fromRGB(0, 0, 0), 1.0F);
        mParticle = Particle.REDSTONE;
        mSpawnLocation = mPlayer.getEyeLocation();
        mPlayer.getWorld().spawnParticle(mParticle, mSpawnLocation, 10, mDustOptions);

    }

    public void updateParticlePosition()
    {
        if(mBat.getHealth() > 0.0)
        {
            if (System.currentTimeMillis() - mSpawnTimeStamp > 1000)
            {
                kill();
            }

            mBat.setVelocity(mVelocity);
            mSpawnLocation.setX(mBat.getLocation().getX());
            mSpawnLocation.setY(mBat.getLocation().getY());
            mSpawnLocation.setZ(mBat.getLocation().getZ());

            if(!mBat.getLocation().getBlock().equals(mPlayer.getLocation().getBlock()))
            {
                mPlayer.getWorld().spawnParticle(mParticle, mSpawnLocation, 1, mDustOptions);
            }
        }
    }

    public void handleCollision()
    {
        List<Entity> entities = mBat.getNearbyEntities(0.5, 0.5, 0.5);
        boolean found = false;
        for (int i = 0; i < entities.size() && !found; i++)
        {
            if (!entities.get(i).isDead())
            {
                if(!entities.get(i).equals(mPlayer))
                {
                    // inflict damage
                    LivingEntity entityHit = (LivingEntity) entities.get(i);
                    entityHit.damage(mDamage);
                    kill();
                }
            }
        }
    }

    public void kill()
    {
        bullets.remove(this);
        Location l = mBat.getLocation();
        l.setY(300);
        mBat.teleport(l);
        mBat.setHealth(0.0);
    }
}
