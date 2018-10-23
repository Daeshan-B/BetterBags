package com.tscforum.betterbags.listeners;

import com.tscforum.betterbags.Core;
import com.tscforum.betterbags.utils.MessageManager;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.PacketPlayOutBlockAction;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;

public class BagsEventsClass implements Listener {

    private Core plugin;

    public BagsEventsClass(Core plugin) {
        this.plugin = plugin;
    }

    public static String eb_name = ChatColor.DARK_PURPLE + "Ender Bag";
    public static String sb_name = ChatColor.DARK_GREEN + "Small Bag";
    public static String mb_name = ChatColor.GOLD + "Medium Bag";
    public static String lb_name = ChatColor.RED + "Large Bag";

    private boolean bagsAdded = false;

    private Inventory enderBag =  Bukkit.getServer().createInventory(null, 27, eb_name);
    private Inventory smallBag = Bukkit.getServer().createInventory(null, 18, sb_name);
    private Inventory mediumBag = Bukkit.getServer().createInventory(null, 27, mb_name);
    private Inventory largeBag = Bukkit.getServer().createInventory(null, 54, lb_name);

    @EventHandler
    public void openInventory(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        for (Inventory i : plugin.bags) {
            Inventory inventory = event.getInventory();
            if (!player.isOp()) {
                if (inventory.getName().equals(i.getName())) {
                    if (inventory.getName().equals(sb_name)) {
                        if (!player.hasPermission("bags.small")) {
                            player.sendMessage(MessageManager.error + "You do not have permission to open this bag!");
                            event.setCancelled(true);
                        }
                    } else if (inventory.getName().equals(mb_name)) {
                        if (!player.hasPermission("bags.medium")) {
                            player.sendMessage(MessageManager.error + "You do not have permission to open this bag!");
                            event.setCancelled(true);
                        }
                    } else if ((inventory.getName().equals(lb_name)) &&
                            (!player.hasPermission("bags.large"))) {
                        player.sendMessage(MessageManager.error + "You do not have permission to open this bag!");
                        event.setCancelled(true);
                    }
                } else if ((inventory.equals(player.getEnderChest())) &&
                        (!player.hasPermission("bags.ender"))) {
                    player.sendMessage(MessageManager.error + "You do not have permission to open this bag!");
                    event.setCancelled(true);
                }
            } else {
                return;
            }
        }
    }

    @EventHandler
    public void closeInventory(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        final UUID uuid = player.getUniqueId();
        for (final Inventory i : this.plugin.bags) {
            final Inventory inventory = event.getInventory();
            if (inventory.getName().equals(i.getName())) {
                new BukkitRunnable() {
                    public void run() {
                        plugin.getConfig().set(uuid + "." + ChatColor.stripColor(i.getName()) + ".contents",
                                inventory.getContents());
                        BagsEventsClass.this.plugin.saveConfig();
                        BagsEventsClass.this.plugin.reloadConfig();
                    }
                }.runTaskAsynchronously(this.plugin);
                return;
            }
        }
    }

    @EventHandler
    public void bagClick(PlayerInteractEvent event) {
        ItemStack click = event.getItem();
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Block block = event.getClickedBlock();
        Action action = event.getAction();
        Inventory i;
        ItemMeta meta;
        if (player.isSneaking()) {
            if ((block == null) || (click == null)) {
                return;
            }
            if ((block.getState() instanceof Chest)) {
                Chest chest = (Chest) block.getState();
                for (Inventory bag1 : plugin.bags) {
                    i = bag1;
                    if ((chest.getCustomName() != null) && (chest.getCustomName().equals(i.getName()))) {
                        block.setType(Material.AIR);
                        Location loc = block.getLocation();
                        loc.setY(loc.getY() + 1.0D);

                        String name = i.getName();
                        ItemStack bag = new ItemStack(Material.CHEST, 1);
                        meta = bag.getItemMeta();
                        ArrayList<String> lore = new ArrayList<>();
                        if (name.equals(eb_name)) {
                            bag.setType(Material.ENDER_CHEST);
                            lore.add(ChatColor.WHITE + "Links With your Endßer Chest.");
                        }
                        lore.add(ChatColor.GOLD + "Max Slots: " + ChatColor.WHITE + i.getSize());
                        lore.add(ChatColor.GRAY + "RIGHT CLICK TO USE");

                        meta.setLore(lore);
                        meta.setDisplayName(i.getName());

                        bag.setItemMeta(meta);

                        block.getWorld().dropItemNaturally(loc, bag);
                    }
                }
            } else {
                return;
            }
        }
        if ((click != null) && (event.getAction().equals(Action.RIGHT_CLICK_AIR))) {
            for (Inventory inventory : plugin.bags) {
                if (click.getType().equals(Material.CHEST)) {
                    if ((click.hasItemMeta()) &&
                            (click.getItemMeta().getDisplayName().equals(inventory.getName()))) {
                        Inventory inv = plugin.getServer().createInventory(null,
                                inventory.getSize(), inventory.getName());

                        String bagname = ChatColor.stripColor(inventory.getName());

                        ArrayList contentsList = (ArrayList) this.plugin.getConfig()
                                .getList(uuid + "." + bagname + ".contents");
                        if (contentsList != null) {
                            for (Object object : contentsList) {
                                ItemStack items = (ItemStack) object;
                                if (items != null) {
                                    inventory.addItem(items);
                                }
                            }
                            player.openInventory(inventory);
                            return;
                        }
                        player.openInventory(inventory);
                    }
                } else if ((click.getType().equals(Material.ENDER_CHEST)) &&
                        (click.hasItemMeta()) &&
                        (click.getItemMeta().getDisplayName().equals(inventory.getName()))) {
                    player.openInventory(player.getEnderChest());
                }
            }
        }
        if ((block != null) &&
                (block.getType().equals(Material.CHEST)) && (action.equals(Action.RIGHT_CLICK_BLOCK))) {
            for (Inventory inven : plugin.bags) {
                Chest chest = (Chest) block.getState();
                if (chest.getCustomName() != null) {
                    String cname = chest.getCustomName();
                    if (cname.equals(inven.getName())) {
                        event.setCancelled(true);
                        Inventory inventory = this.plugin.getServer().createInventory(null, inven.getSize(), inven.getName());

                        String bagname = ChatColor.stripColor(inven.getName());

                        ArrayList contentsList = (ArrayList) this.plugin.getConfig()
                                .getList(uuid + "." + bagname + ".contents");
                        if (contentsList != null) {
                            for (Object object : contentsList) {
                                ItemStack items = (ItemStack) object;
                                if (items != null) {
                                    inventory.addItem(items);
                                }
                            }
                            player.openInventory(inventory);
                        }
                    }
                }
            }
            Location chestLocation = block.getLocation();

            BlockPosition pos = new BlockPosition(chestLocation.getBlockX(), chestLocation.getBlockY(),
                    chestLocation.getBlockZ());
            PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(pos, Blocks.CHEST, 1, 1);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet<?>) packet);
            onChestShut(block, player);
        }
    }

    private void onChestShut(Block block, Player player) {
        new BukkitRunnable() {
            public void run() {
                if (!player.getOpenInventory().getType().equals(InventoryType.CHEST)) {
                    Location chestLocation = block.getLocation();
                    BlockPosition pos = new BlockPosition(chestLocation.getBlockX(), chestLocation.getBlockY(),
                            chestLocation.getBlockZ());
                    PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(pos, Blocks.CHEST, 1, 0);
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet<?>) packet);
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 5);
    }

    @EventHandler
    public void bagPlace(BlockPlaceEvent event) {
        ItemStack iteminhand = event.getItemInHand();
        Block block = event.getBlock();
        if ((block.getType().equals(Material.CHEST)) || (block.getType().equals(Material.ENDER_CHEST))) {
            for (Inventory i : plugin.bags) {
                BlockFace[] faces = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
                BlockFace[] arrayOfBlockFace1;
                int j = (arrayOfBlockFace1 = faces).length;
                for (int ii = 0; ii < j; ii++) {
                    BlockFace face = arrayOfBlockFace1[ii];
                    Block other = block.getRelative(face);
                    if (other.getType().equals(Material.CHEST)) {
                        Chest chest = (Chest) other.getState();
                        if ((chest.getCustomName() != null) && (chest.getCustomName().equals(i.getName()))) {
                            event.getPlayer().sendMessage(ChatColor.RED + "You can not connect a chest to a Bag Chest");
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
                if ((iteminhand.hasItemMeta()) &&
                        (iteminhand.getItemMeta().getDisplayName().equals(i.getName()))) {
                    event.getPlayer().sendMessage(ChatColor.GREEN + "You have placed your: " + i.getName());
                    return;
                }
            }
        }
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        ItemStack clicked = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        if ((clicked != null) &&
                (clicked.hasItemMeta())) {
            String clickName = clicked.getItemMeta().getDisplayName();
            if (player.getOpenInventory().getTopInventory().getName().equals(clickName)) {
                player.sendMessage(MessageManager.error + "You can not place the bag in itself!");
                event.setCancelled(true);
                return;
            }
            if (event.getClick().equals(ClickType.RIGHT)) {
                for (Inventory i : plugin.bags) {
                    if (clickName.equals(i.getName())) {
                        Inventory inventory = plugin.getServer().createInventory(null, i.getSize(), i.getName());
                        String bagname = ChatColor.stripColor(i.getName());
                        ArrayList contentsList = (ArrayList) plugin.getConfig()
                                .getList(uuid + "." + bagname + ".contents");
                        if (contentsList != null) {
                            for (Object object : contentsList) {
                                ItemStack items = (ItemStack) object;
                                if (items != null) {
                                    inventory.addItem(items);
                                }
                            }
                            player.openInventory(inventory);
                            return;
                        }
                        player.openInventory(inventory);
                    }
                }
            }
        }
    }

    public void createbags() {
        if (!bagsAdded) {
            bagsAdded = true;

            plugin.bags.add(smallBag);
            plugin.bags.add(mediumBag);
            plugin.bags.add(largeBag);
            plugin.bags.add(enderBag);
            ArrayList<Inventory> bags = plugin.bags;
            for (Inventory i : bags) {
                String name = i.getName();
                ItemStack bag = new ItemStack(Material.CHEST, 1);
                ItemMeta meta = bag.getItemMeta();
                ArrayList<String> lore = new ArrayList<>();
                if (name.equals(eb_name)) {
                    bag.setType(Material.ENDER_CHEST);
                    lore.add(ChatColor.WHITE + "Links With your Ender Chest.");
                }
                lore.add(ChatColor.GOLD + "Max Slots: " + ChatColor.WHITE + i.getSize());
                lore.add(ChatColor.GRAY + "RIGHT CLICK TO USE");

                meta.setLore(lore);
                meta.setDisplayName(i.getName());

                bag.setItemMeta(meta);
                plugin.bagItems.put(ChatColor.stripColor(bag.getItemMeta().getDisplayName()), bag);
            }

            plugin.smallBagRecipe.setSmallRecipe();
            plugin.mediumBagRecipe.setMediumRecipe();
            plugin.enderBagRecipe.setEnderRecipe();
            plugin.largeBagRecipe.setLargeRecipe();

            plugin.getServer().getConsoleSender().sendMessage(MessageManager.info + "Bags added to HashMap");

        } else {
            plugin.getServer().getConsoleSender().sendMessage(MessageManager.info + "Bags Have already been added!");
        }
    }
}
