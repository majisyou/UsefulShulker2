//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.majisyou.shulker.gui;

import com.github.redshirt53072.api.gui.Gui;
import com.github.redshirt53072.api.item.ItemBuilder;
import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.api.money.MoneyManager;
import com.github.redshirt53072.api.player.PlayerManager;
import com.github.majisyou.shulker.UsefulShulker;
import com.github.majisyou.shulker.data.ECLock;
import com.github.majisyou.shulker.data.EnderChest;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class EnderGui extends Gui {
    private int openedPage = 1;

    public EnderGui() {
        super(UsefulShulker.getInstance());
    }

    public static int calcCost(int slot, OfflinePlayer player) {
        int unlock = ECLock.getPage(player);
        int totalCost = 0;

        for(int i = 1; unlock + i <= slot; ++i) {
            totalCost += (int)Math.pow(2.0D, (double)(unlock + i)) * 500;
        }

        return totalCost;
    }

    private void load() {
        int unlockedPage = ECLock.getPage(this.player);

        for(int index = 1; index < 10; ++index) {
            int model = 3010 + index;
            String text = TextBuilder.quickBuild(ChatColor.WHITE, new String[]{String.valueOf(index), "ページ"});
            ArrayList<String> lore = new ArrayList();
            if (this.openedPage == index) {
                model -= 10;
                text = (new TextBuilder(text, ChatColor.WHITE)).addColorText(ChatColor.YELLOW, new String[]{"<選択中>"}).build();
            }

            if (unlockedPage < index) {
                model += 10;
                text = (new TextBuilder(text, ChatColor.WHITE)).addColorText(ChatColor.RED, new String[]{"<未開放>"}).build();
                lore.add((new TextBuilder(ChatColor.WHITE)).addText(new String[]{"合計"}).addMoneyText(calcCost(index, this.player)).addText(new String[]{"でこのスロットを開放できます。"}).build());
            }

            this.inv.setItem(index + 26, (new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)).setLore(lore).setModelData(model).setName(text).build());
        }

    }

    public boolean onClick(InventoryClickEvent event) {
        if (PlayerManager.isAsyncLocked(this.player, "ec")) {
            return true;
        } else {
            int slot = event.getRawSlot();
            if (slot <= 35 && slot >= 27) {
                slot -= 26;
                if (this.openedPage == slot) {
                    this.player.playSound(this.player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);
                    return true;
                } else if (ECLock.getPage(this.player) < slot) {
                    int nowEme = (int)MoneyManager.get(this.player);
                    if (nowEme > calcCost(slot, this.player)) {
                        (new ConfirmCheck(slot)).open(this);
                        return true;
                    } else {
                        this.player.playSound(this.player.getLocation(), Sound.BLOCK_CHEST_LOCKED, 1.0F, 1.0F);
                        return true;
                    }
                } else {
                    (new EnderChest()).pageChange(this.player, this.inv, this.openedPage, slot);
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

    public void onReturn() {
        this.load();
        super.onReturn();
    }

    public void onRegister() {
        this.inv = Bukkit.createInventory((InventoryHolder)null, 36, "エンダーチェスト");
        (new EnderChest()).openLoad(this.player, this.inv, this.openedPage);
        this.load();
        this.player.playSound(this.player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1.0F, 1.0F);
        this.player.openInventory(this.inv);
    }

    public void onClose() {
        if (!PlayerManager.isAsyncLocked(this.player, "ec")) {
            (new EnderChest()).closeSave(this.player, this.inv, this.openedPage);
        }

        this.player.playSound(this.player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0F, 1.0F);
    }
}
