package me.mika.midomikatrashbin.InventoryUntils;

import me.mika.midomikatrashbin.MidoMika_TrashBin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class TrashBinListeners implements Listener {
    private Inventory trashInventory;
    private final Map<UUID, Barrel> lockedBarrels = new HashMap<>();
    private Barrel barrel;

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        trashInventory = e.getInventory();

        if(e.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "Trash Bin")){
            if (lockedBarrels.isEmpty()) {
                ItemStack delete = new ItemStack(Material.RED_WOOL);
                ItemMeta deleteMeta = delete.getItemMeta();
                deleteMeta.setDisplayName(ChatColor.RED + "Delete All Item");
                delete.setItemMeta(deleteMeta);
                trashInventory.setItem(26, delete);
                lockedBarrels.put(e.getPlayer().getUniqueId(), barrel);

            }else {
                e.setCancelled(true);
                sendActionBarMessage((Player) e.getPlayer(), ChatColor.RED + "This Trash Bin is being use");


            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        trashInventory = e.getInventory();

        if(e.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "Trash Bin")){
            trashInventory.setItem(26, null);
            lockedBarrels.remove(e.getPlayer().getUniqueId());

        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(e.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "Trash Bin")){
            if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Delete All Item")) {
                    e.setCancelled(true);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i <= 25; i++){
                                if (trashInventory.getItem(i) != null){
                                    trashInventory.setItem(i, null);

                                }
                            }
                            e.getWhoClicked().getWorld().playSound(e.getWhoClicked().getLocation(), "entity.generic.extinguish_fire", 1, 0);
                        }
                    }.runTaskLater(MidoMika_TrashBin.getInstance(), 2);
                }
            }
        }
    }


    @EventHandler
    public void onTrashBinPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();
        if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Trash Bin")){
            Block block = e.getBlock();
            //如果获取到的方块不为空，就在该方块上生成ArmorStand
            if (block != null) {
                Location location = block.getLocation().add(0.5, -0.7, 0.5);
                ArmorStand trashBinDisplay = (ArmorStand) p.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
                //设置ArmorStand的一些属性
                trashBinDisplay.setVisible(false);
                trashBinDisplay.setInvulnerable(true);
                trashBinDisplay.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.ADDING);
                trashBinDisplay.addEquipmentLock(EquipmentSlot.HAND, ArmorStand.LockType.ADDING);
                trashBinDisplay.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING);
                trashBinDisplay.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.ADDING);
                trashBinDisplay.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.ADDING);
                trashBinDisplay.addEquipmentLock(EquipmentSlot.OFF_HAND, ArmorStand.LockType.ADDING);
                trashBinDisplay.setCustomName(ChatColor.RED + "Trash Bin");
                trashBinDisplay.setCustomNameVisible(true);
                trashBinDisplay.setGravity(false);
            }
        }
    }

    @EventHandler
    public void onTrashBinBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        Block block = e.getBlock();

        new BukkitRunnable() {
            @Override
            public void run() {
                // 获取周围3格内的实体
                Collection<Entity> nearbyEntities = block.getLocation().getWorld().getNearbyEntities(block.getLocation(), 2, 0, 2);

                // 遍历找到的实体，查找是否有 ArmorStand
                for (Entity entity : nearbyEntities) {
                    if (entity instanceof ArmorStand) {
                        ArmorStand trashBinDisplay = (ArmorStand) entity;
                        Location trashBinDisplayCompareLocation = trashBinDisplay.getLocation().add(-0.5, 0.7, -0.5);
                        if(trashBinDisplay.getName().equalsIgnoreCase(ChatColor.RED + "Trash Bin")){
                            if (trashBinDisplayCompareLocation.equals(block.getLocation())) {
                                trashBinDisplay.remove();
//                                break;
                            }
                        }
                    }
                }
            }
        }.runTaskLater(MidoMika_TrashBin.getInstance(), 2);
    }

    @EventHandler
    public void interactWithTrashBin(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.BARREL) {
            barrel = (Barrel) e.getClickedBlock().getState();

        }
    }

    private void sendActionBarMessage(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }
}
