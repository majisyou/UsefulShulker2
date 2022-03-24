
package com.github.majisyou.shulker.gui;

import com.github.redshirt53072.api.gui.ChildGui;
import com.github.redshirt53072.api.item.ItemBuilder;
import com.github.redshirt53072.api.message.MessageManager;
import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.api.money.MoneyManager;
import com.github.majisyou.shulker.UsefulShulker;
import com.github.majisyou.shulker.data.ECLock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class ConfirmCheck extends ChildGui {
    private int payPage;
    private int cost;

    public ConfirmCheck(int payPage) {
        super(UsefulShulker.getInstance());
        this.payPage = payPage;
    }

    public boolean onClick(InventoryClickEvent event) {
        int slot = event.getRawSlot();
        event.setCancelled(true);
        if (slot == 11) {
            int nowEme = (int)MoneyManager.get(this.player);
            if (nowEme > this.cost) {
                this.player.playSound(this.player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1.0F, 1.0F);
                ECLock.setPage(this.player, this.payPage);
                MoneyManager.remove(this.player, (double)this.cost);
                MessageManager.sendImportant((new TextBuilder(ChatColor.WHITE)).addMoneyText(this.cost).addText(new String[]{"を使用してエンダーチェストが"}).addNumText(this.payPage, "ページ").addText(new String[]{"まで拡張されました。"}).build(), this.player);
                this.close();
                return true;
            } else {
                this.player.playSound(this.player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 0.5F);
                MessageManager.sendImportant((new TextBuilder(ChatColor.WHITE)).addText(new String[]{"Ɇが足りないため、エンダーチェストの拡張ができません。 所持:"}).addMoneyText(nowEme).addText(new String[]{"/必要:"}).addMoneyText(this.cost).build(), this.player);
                this.close();
                return true;
            }
        } else if (slot == 15) {
            this.player.playSound(this.player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1.0F, 1.0F);
            this.close();
            return true;
        } else {
            return true;
        }
    }

    public void onRegister() {
        this.inv = Bukkit.createInventory((InventoryHolder)null, 27, TextBuilder.plus(new String[]{String.valueOf(this.payPage), "ページまで拡張しますか？"}));
        this.cost = EnderGui.calcCost(this.payPage, this.player);
        this.inv.setItem(11, (new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)).setName((new TextBuilder(ChatColor.WHITE)).addMoneyText(this.cost).addText(new String[]{"支払って確定する"}).build()).setModelData(3400).build());
        this.inv.setItem(15, (new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)).setName(TextBuilder.quickBuild(ChatColor.WHITE, new String[]{"キャンセルする"})).setModelData(3401).build());
        this.player.openInventory(this.inv);
        this.player.playSound(this.player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0F, 1.0F);
    }

    public void onClose() {
        this.player.playSound(this.player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1.0F, 1.0F);
        super.asyncReturn();
    }
}
