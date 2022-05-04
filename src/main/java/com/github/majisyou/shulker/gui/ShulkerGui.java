package com.github.majisyou.shulker.gui;

import com.github.redshirt53072.api.gui.Gui;
import com.github.majisyou.shulker.UsefulShulker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.ShulkerBox;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BlockStateMeta;

public class ShulkerGui extends Gui {
    public ShulkerGui() {
        super(UsefulShulker.getInstance());
    }

    public void onRegister() {
        this.inv = Bukkit.createInventory((InventoryHolder)null, 27, "シュルカーボックス");
        PlayerInventory nowInv = this.player.getInventory();
        ItemStack handItem = nowInv.getItemInMainHand();
        BlockStateMeta handMeta = (BlockStateMeta)handItem.getItemMeta();
        ShulkerBox box = (ShulkerBox)handMeta.getBlockState();
        Inventory boxInv = box.getInventory();

        for(int index = 0; index < 27; ++index) {
            this.inv.setItem(index, boxInv.getItem(index));
        }
        this.InventoryPosition = this.player.getInventory().getHeldItemSlot();
        this.player.openInventory(this.inv);
        this.player.playSound(this.player.getLocation(), Sound.BLOCK_SHULKER_BOX_OPEN, 1.0F, 1.0F);
    }

    public boolean onClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null) {
            return false;
        } else {
            return item.getType().equals(Material.SHULKER_BOX) || item.getType().equals(Material.BLACK_SHULKER_BOX) || item.getType().equals(Material.BLUE_SHULKER_BOX) || item.getType().equals(Material.BROWN_SHULKER_BOX) || item.getType().equals(Material.CYAN_SHULKER_BOX) || item.getType().equals(Material.GRAY_SHULKER_BOX) || item.getType().equals(Material.GREEN_SHULKER_BOX) || item.getType().equals(Material.LIGHT_BLUE_SHULKER_BOX) || item.getType().equals(Material.LIGHT_GRAY_SHULKER_BOX) || item.getType().equals(Material.LIME_SHULKER_BOX) || item.getType().equals(Material.MAGENTA_SHULKER_BOX) || item.getType().equals(Material.ORANGE_SHULKER_BOX) || item.getType().equals(Material.PINK_SHULKER_BOX) || item.getType().equals(Material.PURPLE_SHULKER_BOX) || item.getType().equals(Material.RED_SHULKER_BOX) || item.getType().equals(Material.WHITE_SHULKER_BOX) || item.getType().equals(Material.YELLOW_SHULKER_BOX);
        }
    }

    public void onClose() {
        this.player.getInventory().setHeldItemSlot(this.InventoryPosition);
        ItemStack handItem = this.player.getInventory().getItemInMainHand();
        BlockStateMeta handMeta = (BlockStateMeta)handItem.getItemMeta();
        ShulkerBox box = (ShulkerBox)handMeta.getBlockState();
        Inventory boxInv = box.getInventory();

        for(int index = 0; index < 27; ++index) {
            boxInv.setItem(index, this.inv.getItem(index));
        }

        handMeta.setBlockState(box);
        handItem.setItemMeta(handMeta);
        this.player.playSound(this.player.getLocation(), Sound.BLOCK_SHULKER_BOX_CLOSE, 1.0F, 1.0F);
    }
}
