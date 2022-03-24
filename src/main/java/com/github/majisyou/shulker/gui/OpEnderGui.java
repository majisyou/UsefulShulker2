//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.majisyou.shulker.gui;

import com.github.redshirt53072.api.gui.Gui;
import com.github.redshirt53072.api.item.ItemBuilder;
import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.api.player.PlayerManager;
import com.github.majisyou.shulker.UsefulShulker;
import com.github.majisyou.shulker.data.ECLock;
import com.github.majisyou.shulker.data.EnderChest;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class OpEnderGui extends Gui {
    private static List<OfflinePlayer> editPlayers = new ArrayList();
    private int openedPage = 1;
    private OfflinePlayer target;
    private boolean editMode;

    public OpEnderGui(OfflinePlayer target, boolean editMode) {
        super(UsefulShulker.getInstance());
        this.target = target;
        this.editMode = editMode;
        if (editMode) {
            editPlayers.add(target);
        }

    }

    public static boolean isOpenedPlayer(OfflinePlayer player) {
        return editPlayers.contains(player);
    }

    private void load() {
        int lockedPage = ECLock.getPage(this.target);

        for(int index = 1; index < 10; ++index) {
            int model = 3010 + index;
            String text = index + "ページ";
            ArrayList<String> lore = new ArrayList();
            if (this.openedPage == index) {
                model -= 10;
                text = (new TextBuilder(text, ChatColor.WHITE)).addColorText(ChatColor.YELLOW, new String[]{"<選択中>"}).build();
            }

            if (lockedPage < index) {
                model += 10;
                text = (new TextBuilder(text, ChatColor.WHITE)).addColorText(ChatColor.RED, new String[]{"<未開放>"}).build();
                lore.add((new TextBuilder(ChatColor.WHITE)).addText(new String[]{"合計"}).addMoneyText(EnderGui.calcCost(index, this.target)).addText(new String[]{"でこのスロットを開放できます。"}).build());
            }

            this.inv.setItem(index + 26, (new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)).setLore(lore).setModelData(model).setName(text).build());
        }

    }

    public boolean onClick(InventoryClickEvent event) {
        if (PlayerManager.isAsyncLocked(this.target, "ec")) {
            return true;
        } else {
            int slot = event.getRawSlot();
            if (slot <= 35 && slot >= 27) {
                slot -= 26;
                if (this.openedPage == slot) {
                    this.player.playSound(this.player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);
                    return true;
                } else if (ECLock.getPage(this.target) < slot) {
                    this.player.playSound(this.player.getLocation(), Sound.BLOCK_CHEST_LOCKED, 1.0F, 1.0F);
                    return true;
                } else {
                    if (this.editMode) {
                        (new EnderChest()).pageChange(this.target, this.inv, this.openedPage, slot);
                    } else {
                        (new EnderChest()).openLoad(this.target, this.inv, slot);
                    }

                    this.openedPage = slot;
                    this.load();
                    this.player.playSound(this.player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    public void onRegister() {
        this.inv = Bukkit.createInventory((InventoryHolder)null, 36, TextBuilder.plus(new String[]{this.target.getName(), "-", this.editMode ? "編集モード" : "閲覧モード"}));
        (new EnderChest()).openLoad(this.target, this.inv, this.openedPage);
        this.load();
        this.player.playSound(this.player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1.0F, 1.0F);
        this.player.openInventory(this.inv);
    }

    public void onClose() {
        if (this.editMode) {
            editPlayers.remove(this.target);
            if (!PlayerManager.isAsyncLocked(this.target, "ec")) {
                (new EnderChest()).closeSave(this.target, this.inv, this.openedPage);
            }
        }

        this.player.playSound(this.player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0F, 1.0F);
    }
}
