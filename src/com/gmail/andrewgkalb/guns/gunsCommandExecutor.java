package com.gmail.andrewgkalb.guns;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

import com.gmail.andrewgkalb.guns.GunList;

public class gunsCommandExecutor implements CommandExecutor
{
    private final guns mGunsPlugin;
    private GunList mGunList;

    public gunsCommandExecutor(guns g)
    {
        mGunsPlugin = g;
        mGunList = new GunList("gunlist.txt");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (cmd.getName().equalsIgnoreCase("guns"))
        {
            Player player = null;
            if (sender instanceof Player)
            {
                player = (Player) sender;
            }

            if (args.length > 0)
            {
                if (args[0].equalsIgnoreCase("help"))
                {
                    displayCommandHelp(player);
                }
                else if (args[0].equalsIgnoreCase("create"))
                {
                    handleCreate(player, args);
                }
                else if (args[0].equalsIgnoreCase("delete"))
                {
                    handleDelete(player, args);
                }
                else if (args[0].equalsIgnoreCase("list"))
                {
                    handleList(player);
                }
                else if (args[0].equalsIgnoreCase("info"))
                {
                    handleInfo(player, args);
                }
                else if (args[0].equalsIgnoreCase("set"))
                {
                    handleSet(player, args);
                }
                else if(args[0].equalsIgnoreCase("give"))
                {
                    handleGive(player, args);
                }
            }
            else
            {
                displayCommandHelp(player);
            }
        }
        return true;
    }

    private void handleCreate(Player p, String[] arguments)
    {
        if (arguments.length > 1)
        {
            if (findGunInList(arguments[1]) != -1)
            {
                p.sendMessage(ChatColor.GRAY + "A gun with that name already exists.");
            }
            else
            {
                try
                {
                    GunInfo g = new GunInfo(arguments[1]);
                    mGunList.mGuns.add(g);

                    FileWriter writer = new FileWriter("gunlist.txt", true);
                    writer.write(g.name + "," + g.fireRate + "," + g.item + "," + g.damage + ",\n");
                    writer.close();
                    System.out.println("Successfully wrote to the file.");

                    p.sendMessage(ChatColor.GRAY + "Gun named \"" + ChatColor.RED + arguments[1] + ChatColor.GRAY + "\" was successfully created.");
                }
                catch (IOException e)
                {
                    System.out.println("An error occurred when writing to the file.");
                    e.printStackTrace();
                }
            }
        }
        else
        {
            p.sendMessage(ChatColor.GRAY + "The correct usage is " + ChatColor.RED + "/guns create <name>");
        }
    }

    private void handleDelete(Player p, String[] arguments)
    {
        if (arguments.length > 1)
        {
            int lineNumber = findGunInList(arguments[1]);
            if (lineNumber == -1)
            {
                p.sendMessage(ChatColor.GRAY + "Couldn't find a gun with that name.");
            }
            else
            {
                /*System.out.println("found at ln " + lineNumber);
                try
                {
                    // Create a copy of the file and store everything besides the line to remove.
                    try
                    {
                        File copy = new File("gunlist copy.txt");
                        if (copy.createNewFile())
                        {
                            System.out.println("File created: " + copy.getName());
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
                    File copy = new File("gunlist copy.txt");


                    FileWriter writer = new FileWriter("gunlist copy.txt", true);
                    File original = new File("gunlist.txt");
                    Scanner scan = new Scanner(original);

                    int lineCount = 0;
                    while (scan.hasNextLine())
                    {
                        if (lineCount != lineNumber)
                        {
                            writer.write(scan.nextLine() + "\n");
                        }
                        else
                        {
                            scan.nextLine();
                        }
                        lineCount++;
                    }
                    scan.close();
                    writer.close();
                    original.delete();
                    copy.renameTo(original);*/

                    mGunList.mGuns.remove(mGunList.mGuns.get(lineNumber));
                    mGunList.updateFile();
                    //System.out.println("Successfully wrote to the file.");
                    p.sendMessage(ChatColor.GRAY + "Gun named \"" + ChatColor.RED + arguments[1] + ChatColor.GRAY + "\" was successfully deleted.");
                /*}
                catch (IOException e)
                {
                    System.out.println("An error occurred when writing to the file.");
                    e.printStackTrace();
                }*/
            }
        }
        else
        {
            p.sendMessage(ChatColor.GRAY + "The correct usage is " + ChatColor.RED + "/guns delete <name>");
        }
    }

    private void handleList(Player p)
    {
        p.sendMessage(ChatColor.GRAY + "Guns:");
        for(int i = 0; i < mGunList.mGuns.size(); i++)
        {
            p.sendMessage(ChatColor.RED + " -" + mGunList.mGuns.get(i).name);
        }
        p.sendMessage(ChatColor.GRAY + "See more information on a gun using the /guns info <gun> command.");

    }

    private void handleInfo(Player p, String[] arguments)
    {
        if (arguments.length > 1)
        {
            boolean done = false;
            for (int i = 0; i < mGunList.mGuns.size() && !done; i++)
            {
                if (mGunList.mGuns.get(i).name.equalsIgnoreCase(arguments[1]))
                {
                    p.sendMessage(ChatColor.RED + mGunList.mGuns.get(i).name + ": ");
                    p.sendMessage(ChatColor.GRAY + "Fire Rate: " + ChatColor.RED + mGunList.mGuns.get(i).fireRate);
                    p.sendMessage(ChatColor.GRAY + "Item: " + ChatColor.RED + mGunList.mGuns.get(i).item.toString());
                    p.sendMessage(ChatColor.GRAY + "Damage: " + ChatColor.RED + mGunList.mGuns.get(i).damage);
                    done = true;
                }
            }
            if (!done)
            {
                p.sendMessage(ChatColor.RED + "\"" + arguments[1] + "\"" + ChatColor.GRAY + " is not a valid gun.");
            }
        }
        else
        {
            p.sendMessage(ChatColor.GRAY + "The correct usage is " + ChatColor.RED + "/guns info <gun>");
        }
    }

    private void handleGive(Player p, String[] arguments)
    {
        if (arguments.length > 2)
        {
            boolean found = false;
            // See if the player is online
            for(Player it : mGunsPlugin.getServer().getOnlinePlayers())
            {
                if(it.getDisplayName().equalsIgnoreCase(arguments[1]))
                {
                    Player recipient = it;
                    int gunIndex = findGunInList(arguments[2]);
                    if(gunIndex != -1)
                    {
                        ItemStack i = new ItemStack(mGunList.mGuns.get(gunIndex).item);
                        ItemMeta meta = i.getItemMeta();
                        meta.setDisplayName(mGunList.mGuns.get(gunIndex).name);
                        List <String> list = new ArrayList<String>();
                        list.add(ChatColor.GRAY + "Official Gun"/* + ChatColor.MAGIC + "#.d"*/);
                        meta.setLore(list);
                        i.setItemMeta(meta);
                        p.getInventory().addItem(i);
                    }
                    else
                    {
                        p.sendMessage(ChatColor.RED + "\"" + arguments[2] + "\"" + ChatColor.GRAY + " is not a valid gun.");
                    }
                    found = true;
                    break;
                }
            }
            if(!found)
            {
                p.sendMessage(ChatColor.GRAY + "Player " + ChatColor.RED + "\"" + arguments[1] + "\"" + ChatColor.GRAY + " not found.");
            }
        }
        else
        {
            p.sendMessage(ChatColor.GRAY + "The correct usage is " + ChatColor.RED + "/guns give [player] <gun>");
        }
    }

    // NOTE: NEED TO EDIT TXT FILE,
    // CONSIDER SWITCHING TO UPDATING,
    // TXT FILE ONLY AT THE END.
    // HOWEVER, THIS WILL DISALLOW
    // DIRECT EDITING OF THE TXT FILE
    // TO FUNCTION PROPERLY.
    private void handleSet(Player p, String[] arguments)
    {
        int length = arguments.length;
        boolean displayUsageMessage = false;
        boolean handleItemSet = false;
        if (length > 2)
        {
            if (arguments[1].equalsIgnoreCase("firerate") || arguments[1].equalsIgnoreCase("damage"))
            {
                try
                {
                    int gunIndex = findGunInList(arguments[2]);
                    if (gunIndex != -1)
                    {
                        if(arguments[1].equalsIgnoreCase("firerate"))
                        {
                            mGunList.mGuns.get(gunIndex).fireRate = Double.parseDouble(arguments[3]);
                        }
                        else
                        {
                            mGunList.mGuns.get(gunIndex).damage = Double.parseDouble(arguments[3]);
                        }
                        p.sendMessage(ChatColor.GRAY + "Gun named \"" + ChatColor.RED + arguments[2] + ChatColor.GRAY + "\" was successfully edited.");
                    }
                    else
                    {
                        p.sendMessage(ChatColor.RED + "\"" + arguments[2] + "\"" + ChatColor.GRAY + " is not a valid gun.");
                    }
                } catch (NumberFormatException e)
                {
                    p.sendMessage(ChatColor.GRAY + arguments[1] + " can not be set to anything other than a number.");
                }
            }
            else if (arguments[1].equalsIgnoreCase("item"))
            {
                handleItemSet = true;
            }
            else
            {
                displayUsageMessage = true;
            }
        }
        else if (length > 1)
        {
            if(arguments[1].equalsIgnoreCase("item"))
            {
                handleItemSet = true;
            }
            else
            {
                displayUsageMessage = true;
            }
        }
        else
        {
            displayUsageMessage = true;
        }

        if(handleItemSet)
        {
            if(length > 2)
            {
                Material m = p.getInventory().getItemInMainHand().getType();

                int gunIndex = findGunInList(arguments[2]);
                if (gunIndex != -1)
                {
                    if (m != null && !m.isAir())
                    {
                        mGunList.mGuns.get(gunIndex).item = m;
                        p.sendMessage(ChatColor.GRAY + "Gun named \"" + ChatColor.RED + arguments[2] + ChatColor.GRAY + "\" was successfully edited.");
                    }
                    else
                    {
                        p.sendMessage(ChatColor.GRAY + "Hold something valid.");
                    }
                }
                else
                {
                    p.sendMessage(ChatColor.RED + "\"" + arguments[1] + "\"" + ChatColor.GRAY + " is not a valid gun.");
                }
            }
            else
            {
                displayUsageMessage = true;
            }
        }

        if(displayUsageMessage)
        {
            p.sendMessage(ChatColor.GRAY + "The correct usage is " + ChatColor.RED + "/guns set [firerate/damage/item] <gun> <value>");
        }
        else
        {
            mGunList.updateFile();
        }
    }

    private void displayCommandHelp(Player p)
    {
        p.sendMessage(ChatColor.GRAY + "-----------------" + ChatColor.RED + "[" + ChatColor.GRAY + "Guns" + ChatColor.RED + "]" + ChatColor.GRAY + "-----------------");
        p.sendMessage(ChatColor.GRAY + "   - " + ChatColor.RED + "help: " + ChatColor.GRAY + "lists this help menu");
        p.sendMessage(ChatColor.GRAY + "   - " + ChatColor.RED + "create <name>: " + ChatColor.GRAY + "creates a gun to be edited");
        p.sendMessage(ChatColor.GRAY + "   - " + ChatColor.RED + "delete <name>: " + ChatColor.GRAY + "deletes a gun from the list");
        p.sendMessage(ChatColor.GRAY + "   - " + ChatColor.RED + "list: " + ChatColor.GRAY + "lists created guns");
        p.sendMessage(ChatColor.GRAY + "   - " + ChatColor.RED + "info <gun>: " + ChatColor.GRAY + "retrieves info on a gun");
        p.sendMessage(ChatColor.GRAY + "   - " + ChatColor.RED + "give [player] <gun>: " + ChatColor.GRAY + "gives a gun to a player");
        p.sendMessage(ChatColor.GRAY + "   - " + ChatColor.RED + "set [firerate/damage] <gun> <value>: " + ChatColor.GRAY + "sets gun values");
        p.sendMessage(ChatColor.GRAY + "   - " + ChatColor.RED + "set [item] <gun> " + ChatColor.GRAY + "sets gun's item appearance to the item in hand");
    }

    // returns line number, beginning at 0
    private int findGunInList(String name)
    {
        for(int i = 0; i < mGunList.mGuns.size(); i++)
        {
            if(mGunList.mGuns.get(i).name.equalsIgnoreCase(name))
            {
                return i;
            }
        }
        return -1;
    }
}
