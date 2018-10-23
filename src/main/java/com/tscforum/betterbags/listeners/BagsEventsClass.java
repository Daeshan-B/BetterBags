package com.tscforum.betterbags.listeners;

import com.tscforum.betterbags.BagMainClass;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.Blocks;
import net.minecraft.server.v1_13_R2.PacketPlayOutBlockAction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
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

public class BagsEventsClass
        implements Listener {
    private BagMainClass plugin = BagMainClass.getPlugin(BagMainClass.class);
    private boolean bagsadded = false;
    public String eb_name = ChatColor.DARK_PURPLE + "Ender Bag";
    public String sb_name = ChatColor.DARK_GREEN + "Small Bag";
    public String mb_name = ChatColor.GOLD + "Medium Bag";
    public String lb_name = ChatColor.RED + "Large Bag";
    private Inventory enderBag = this.plugin.getServer().createInventory(null, 27, this.eb_name);
    private Inventory smallBag = this.plugin.getServer().createInventory(null, 18, this.sb_name);
    private Inventory mediumBag = this.plugin.getServer().createInventory(null, 27, this.mb_name);
    private Inventory largeBag = this.plugin.getServer().createInventory(null, 54, this.lb_name);

    @EventHandler
    public void openInventory(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        for (Inventory i : this.plugin.bags) {
            Inventory inventory = event.getInventory();
            if (!player.isOp()) {
                if (inventory.getName().equals(i.getName())) {
                    if (inventory.getName().equals(this.sb_name)) {
                        if (!player.hasPermission("bags.small")) {
                            player.sendMessage(this.plugin.error + "You do not have permission to open this bag!");
                            event.setCancelled(true);
                        }
                    } else if (inventory.getName().equals(this.mb_name)) {
                        if (!player.hasPermission("bags.medium")) {
                            player.sendMessage(this.plugin.error + "You do not have permission to open this bag!");
                            event.setCancelled(true);
                        }
                    } else if ((inventory.getName().equals(this.lb_name)) &&
                            (!player.hasPermission("bags.large"))) {
                        player.sendMessage(this.plugin.error + "You do not have permission to open this bag!");
                        event.setCancelled(true);
                    }
                } else if ((inventory.equals(player.getEnderChest())) &&
                        (!player.hasPermission("bags.ender"))) {
                    player.sendMessage(this.plugin.error + "You do not have permission to open this bag!");
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
                        BagsEventsClass.this.plugin.getConfig().set(uuid + "." + ChatColor.stripColor(i.getName()) + ".contents",
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
                for (Inventory bag1 : this.plugin.bags) {
                    i = bag1;
                    if ((chest.getCustomName() != null) && (chest.getCustomName().equals(i.getName()))) {
                        block.setType(Material.AIR);
                        Location loc = block.getLocation();
                        loc.setY(loc.getY() + 1.0D);

                        String name = i.getName();
                        ItemStack bag = new ItemStack(Material.CHEST, 1);
                        meta = bag.getItemMeta();
                        ArrayList<String> lore = new ArrayList<>();
                        if (name.equals(this.eb_name)) {
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
            for (Inventory inven : this.plugin.bags) {
                if (click.getType().equals(Material.CHEST)) {
                    if ((click.hasItemMeta()) &&
                            (click.getItemMeta().getDisplayName().equals(inven.getName()))) {
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
                            return;
                        }
                        player.openInventory(inventory);
                    }
                } else if ((click.getType().equals(Material.ENDER_CHEST)) &&
                        (click.hasItemMeta()) &&
                        (click.getItemMeta().getDisplayName().equals(inven.getName()))) {
                    player.openInventory(player.getEnderChest());
                }
            }
        }
        if ((block != null) &&
                (block.getType().equals(Material.CHEST)) && (action.equals(Action.RIGHT_CLICK_BLOCK))) {
            for (Inventory inven : this.plugin.bags) {
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
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            onChestShut(block, player);
        }
    }

    private void onChestShut(final Block block, final Player player) {
        new BukkitRunnable() {
            public void run() {
                if (!player.getOpenInventory().getType().equals(InventoryType.CHEST)) {
                    Location chestLocation = block.getLocation();
                    BlockPosition pos = new BlockPosition(chestLocation.getBlockX(), chestLocation.getBlockY(),
                            chestLocation.getBlockZ());
                    PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(pos, Blocks.CHEST, 1, 0);
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(this.plugin, 0L, 5L);
    }

    @EventHandler
    public void bagPlace(BlockPlaceEvent event) {
        ItemStack iteminhand = event.getItemInHand();
        Block block = event.getBlock();
        if ((block.getType().equals(Material.CHEST)) || (block.getType().equals(Material.ENDER_CHEST))) {
            for (Inventory i : this.plugin.bags) {
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
    public void invenClick(InventoryClickEvent event) {
        ItemStack clicked = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        if ((clicked != null) &&
                (clicked.hasItemMeta())) {
            String clickName = clicked.getItemMeta().getDisplayName();
            if (player.getOpenInventory().getTopInventory().getName().equals(clickName)) {
                player.sendMessage(this.plugin.error + "You can not place the bag in itself!");
                event.setCancelled(true);
                return;
            }
            if (event.getClick().equals(ClickType.RIGHT)) {
                for (Inventory i : this.plugin.bags) {
                    if (clickName.equals(i.getName())) {
                        Inventory inventory = this.plugin.getServer().createInventory(null, i.getSize(), i.getName());
                        String bagname = ChatColor.stripColor(i.getName());
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
                }
            }
        }
    }

    public void createbags() {
        if (!this.bagsadded) {
            this.bagsadded = true;

            this.plugin.bags.add(this.smallBag);
            this.plugin.bags.add(this.mediumBag);
            this.plugin.bags.add(this.largeBag);
            this.plugin.bags.add(this.enderBag);
            for (Inventory i : this.plugin.bags) {
                String name = i.getName();
                ItemStack bag = new ItemStack(Material.CHEST, 1);
                ItemMeta meta = bag.getItemMeta();
                ArrayList<String> lore = new ArrayList<>();
                if (name.equals(this.eb_name)) {
                    bag.setType(Material.ENDER_CHEST);
                    lore.add(ChatColor.WHITE + "Links With your Ender Chest.");
                }
                lore.add(ChatColor.GOLD + "Max Slots: " + ChatColor.WHITE + i.getSize());
                lore.add(ChatColor.GRAY + "RIGHT CLICK TO USE");

                meta.setLore(lore);
                meta.setDisplayName(i.getName());

                bag.setItemMeta(meta);
                this.plugin.bagItems.put(ChatColor.stripColor(bag.getItemMeta().getDisplayName()), bag);
            }
            this.plugin.bagsRecipe.setRecipe();
            this.plugin.getServer().getConsoleSender().sendMessage(this.plugin.info + "Bags added to HashMap");
        } else {
            this.plugin.getServer().getConsoleSender().sendMessage(this.plugin.info + "Bags Have already been added!");
            return;
        }
    }
}
