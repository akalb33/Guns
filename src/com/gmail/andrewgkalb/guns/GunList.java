package com.gmail.andrewgkalb.guns;

import org.bukkit.Material;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class GunList
{
    public GunList(String fileName)
    {
        mFile = fileName;
        File f = new File(mFile);
        mGuns = new ArrayList<GunInfo>();
        try
        {
            Scanner scan = new Scanner(f);
            while(scan.hasNextLine())
            {
                String line = scan.nextLine();
                if(line.indexOf(',') == -1)
                {
                    break;
                }
                GunInfo g = new GunInfo(line.substring(0, line.indexOf(',')));
                line = line.substring(line.indexOf(',') + 1);
                g.fireRate = Double.parseDouble(line.substring(0, line.indexOf(',')));
                line = line.substring(line.indexOf(',') + 1);
                g.item = Material.getMaterial(line.substring(0, line.indexOf(',')));
                line = line.substring(line.indexOf(',') + 1);
                g.damage = Double.parseDouble(line.substring(0, line.indexOf(',')));
                mGuns.add(g);
            }
            scan.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public void updateFile()
    {
        try
        {
            File original = new File(mFile);
            File copy = new File(mFile + " copy.txt");
            if (copy.createNewFile())
            {
                System.out.println("File created: " + copy.getName());
            }
            else
            {
                System.out.println("File already exists.");
            }

            FileWriter writer = new FileWriter(mFile + " copy.txt", true);

            for(int i = 0; i < mGuns.size(); i++)
            {
                GunInfo g = mGuns.get(i);
                writer.write(g.name + "," + g.fireRate + "," + g.item + "," + g.damage + ",\n");
                System.out.println("Successfully wrote to the file.");
            }
            writer.close();
            // file somehow not deleted? perhaps?

            if(!original.delete())
            {
                System.out.println("Not deleted.");
            }

            copy.renameTo(original);
        }
        catch (IOException e)
        {
            System.out.println("An error occurred when writing to the file.");
            e.printStackTrace();
        }
    }


    public ArrayList<GunInfo> mGuns;
    private String mFile;
}


