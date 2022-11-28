package com.gmail.andrewgkalb.guns;

import org.bukkit.Material;

public class GunInfo
{
    public String name;
    public double fireRate; // bullets / sec
    public Material item;
    public double damage; // hearts

    public GunInfo(String gunName)
    {
        name = gunName;
        fireRate = 1.0;
        item = Material.GOLDEN_HOE;
        damage = 1.0;
    }

    public GunInfo(String gunName, double rateOfFire, Material itemsName, double dmg)
    {
        name = gunName;
        fireRate = rateOfFire;
        item = itemsName;
        damage = dmg;
    }
}