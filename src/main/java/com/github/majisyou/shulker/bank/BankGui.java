//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.majisyou.shulker.bank;

import com.github.redshirt53072.api.gui.MoneyGui;
import com.github.redshirt53072.api.item.ItemBuilder;
import com.github.redshirt53072.api.item.ItemLibrary;
import com.github.redshirt53072.api.item.ItemUtil;
import com.github.redshirt53072.api.message.SoundManager;
import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.api.money.MoneyManager;
import com.github.majisyou.shulker.UsefulShulker;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class BankGui extends MoneyGui {
    private int balance = 0;
    private int emerald = 0;
    private int[] inEme = new int[]{1, 1, 1};
    private int[] outEme = new int[]{1, 1, 1};

    public BankGui() {
        super(UsefulShulker.getInstance());
    }

    public void onRegister() {
        this.inv = Bukkit.createInventory((InventoryHolder)null, 36, "Prince銀行");
        this.setEmptyItem(new Integer[]{9, 10, 12, 13, 17, 18, 22, 23, 25, 26});
        this.renderingMoney();
        this.renderingButton(true, 0);
        this.renderingButton(true, 1);
        this.renderingButton(true, 2);
        this.renderingButton(false, 0);
        this.renderingButton(false, 1);
        this.renderingButton(false, 2);
        this.inv.setItem(11, (new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)).setName(TextBuilder.quickBuild(ChatColor.WHITE, new String[]{"全て預け入れる"})).setModelData(3407).build());
        this.inv.setItem(24, (new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)).setName(TextBuilder.quickBuild(ChatColor.WHITE, new String[]{"全て引き出す"})).setModelData(3408).build());
        this.player.playSound(this.player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1.0F, 1.0F);
        this.player.openInventory(this.inv);
    }

    private void renderingButton(boolean in, int type) {
        String v = in ? "預け入れる" : "引き出す";
        int slot = (in ? 19 : 14) + type;
        int amount = in ? this.inEme[type] : this.outEme[type];
        List<String> lore = new ArrayList();
        lore.add((new TextBuilder(ChatColor.WHITE)).addClick("右クリック").addText(new String[]{"で", v, "個数を切り替える"}).build());
        if (type == 0) {
            this.inv.setItem(slot, (new ItemBuilder(Material.EMERALD)).setName((new TextBuilder(ChatColor.WHITE)).addText(new String[]{"エメラルドを"}).addNumText(amount, "個").addText(new String[]{v}).build()).setLore(lore).setAmount(amount).build());
        } else if (type == 1) {
            this.inv.setItem(slot, (new ItemBuilder(Material.EMERALD_BLOCK)).setName((new TextBuilder(ChatColor.WHITE)).addText(new String[]{"エメラルドブロックを"}).addNumText(amount, "個").addText(new String[]{v}).build()).setLore(lore).setAmount(amount).build());
        } else {
            this.inv.setItem(slot, (new ItemBuilder(ItemLibrary.getLiquidEmerald())).setName((new TextBuilder(ChatColor.WHITE)).addText(new String[]{"リキッドエメラルドを"}).addNumText(amount, "個").addText(new String[]{v}).build()).setLore(lore).setAmount(amount).build());
        }

    }

    private void renderingMoney() {
        this.emerald = 0;
        this.emerald += ItemUtil.countItem(this.player, new ItemStack(Material.EMERALD));
        this.emerald += ItemUtil.countItem(this.player, new ItemStack(Material.EMERALD_BLOCK)) * 64;
        this.emerald += ItemUtil.countItem(this.player, ItemLibrary.getLiquidEmerald()) * 4096;
        this.balance = (int)MoneyManager.get(this.player);
        String text1 = (new TextBuilder(ChatColor.WHITE)).addText(new String[]{"口座残高："}).addMoneyText(this.balance).build();
        this.renderingMoney(0, this.balance, (new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)).setName(text1).setModelData(3200).build(), text1);
        String text2 = (new TextBuilder(ChatColor.WHITE)).addText(new String[]{"手持ちエメラルド："}).addNumText(this.emerald, "個").build();
        this.renderingMoney(3, this.emerald, (new ItemBuilder(Material.EMERALD)).setName(text2).build(), text2);
    }

    private void updateButton(boolean in, int type) {
        int amount;
        if (in) {
            amount = this.inEme[type];
            if (amount == 1) {
                this.inEme[type] = 8;
            } else if (amount == 8) {
                this.inEme[type] = 32;
            } else if (amount == 32) {
                this.inEme[type] = 64;
            } else {
                this.inEme[type] = 1;
            }
        } else {
            amount = this.outEme[type];
            if (amount == 1) {
                this.outEme[type] = 8;
            } else if (amount == 8) {
                this.outEme[type] = 32;
            } else if (amount == 32) {
                this.outEme[type] = 64;
            } else {
                this.outEme[type] = 1;
            }
        }

        SoundManager.sendClick(this.player);
        this.renderingButton(in, type);
    }

    public void onClose() {
        this.player.playSound(this.player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0F, 1.0F);
    }

    public boolean onClick(final InventoryClickEvent event) {
        int slot = event.getRawSlot();
        if (slot > 35) {
            final ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null) {
                return true;
            } else if (clickedItem.isSimilar(new ItemStack(Material.EMERALD))) {
                Bukkit.getScheduler().runTask(this.plugin, new Runnable() {
                    public void run() {
                        BankGui.this.player.getInventory().clear(event.getSlot());
                        MoneyManager.add(BankGui.this.player, (double)clickedItem.getAmount());
                        BankGui.this.renderingMoney();
                        SoundManager.sendPickUp(BankGui.this.player);
                    }
                });
                return true;
            } else if (clickedItem.isSimilar(new ItemStack(Material.EMERALD_BLOCK))) {
                Bukkit.getScheduler().runTask(this.plugin, new Runnable() {
                    public void run() {
                        BankGui.this.player.getInventory().clear(event.getSlot());
                        MoneyManager.add(BankGui.this.player, (double)(clickedItem.getAmount() * 64));
                        BankGui.this.renderingMoney();
                        SoundManager.sendPickUp(BankGui.this.player);
                    }
                });
                return true;
            } else if (clickedItem.isSimilar(new ItemStack(ItemLibrary.getLiquidEmerald()))) {
                Bukkit.getScheduler().runTask(this.plugin, new Runnable() {
                    public void run() {
                        BankGui.this.player.getInventory().clear(event.getSlot());
                        MoneyManager.add(BankGui.this.player, (double)(clickedItem.getAmount() * 4096));
                        BankGui.this.renderingMoney();
                        SoundManager.sendPickUp(BankGui.this.player);
                    }
                });
                return true;
            } else {
                SoundManager.sendCancel(this.player);
                return true;
            }
        } else {
            int amount;
            int block;
            int eme;
            if (slot == 11) {
                amount = ItemUtil.removeItem(this.player, new ItemStack(Material.EMERALD));
                block = ItemUtil.removeItem(this.player, new ItemStack(Material.EMERALD_BLOCK));
                eme = ItemUtil.removeItem(this.player, ItemLibrary.getLiquidEmerald());
                int price = amount + block * 64 + eme * 4096;
                if (price == 0) {
                    SoundManager.sendCancel(this.player);
                    return true;
                } else {
                    MoneyManager.add(this.player, (double)price);
                    SoundManager.sendPickUp(this.player);
                    this.renderingMoney();
                    return true;
                }
            } else if (slot == 24) {
                if (this.balance == 0) {
                    SoundManager.sendCancel(this.player);
                    return true;
                } else {
                    amount = this.balance / 4096;
                    block = this.balance % 4096 / 64;
                    eme = this.balance % 4096 % 64;
                    ItemUtil.giveItems(this.player, ItemLibrary.getLiquidEmerald(), amount);
                    ItemUtil.giveItems(this.player, new ItemStack(Material.EMERALD_BLOCK), block);
                    ItemUtil.giveItems(this.player, new ItemStack(Material.EMERALD), eme);
                    MoneyManager.reset(this.player);
                    SoundManager.sendPickUp(this.player);
                    this.renderingMoney();
                    return true;
                }
            } else if (event.getClick().equals(ClickType.LEFT)) {
                if (slot == 14) {
                    amount = this.outEme[0] < this.balance ? this.outEme[0] : this.balance;
                    if (amount == 0) {
                        SoundManager.sendCancel(this.player);
                        return true;
                    } else {
                        SoundManager.sendPickUp(this.player);
                        MoneyManager.remove(this.player, (double)amount);
                        ItemUtil.giveItems(this.player, new ItemStack(Material.EMERALD), amount);
                        this.renderingMoney();
                        return true;
                    }
                } else if (slot == 15) {
                    amount = this.outEme[1] * 64 < this.balance ? this.outEme[1] : this.balance / 64;
                    if (amount == 0) {
                        SoundManager.sendCancel(this.player);
                        return true;
                    } else {
                        SoundManager.sendPickUp(this.player);
                        MoneyManager.remove(this.player, (double)(amount * 64));
                        ItemUtil.giveItems(this.player, new ItemStack(Material.EMERALD_BLOCK), amount);
                        this.renderingMoney();
                        return true;
                    }
                } else if (slot == 16) {
                    amount = this.outEme[2] * 4096 < this.balance ? this.outEme[2] : this.balance / 4096;
                    if (amount == 0) {
                        SoundManager.sendCancel(this.player);
                        return true;
                    } else {
                        SoundManager.sendPickUp(this.player);
                        MoneyManager.remove(this.player, (double)(amount * 4096));
                        ItemUtil.giveItems(this.player, ItemLibrary.getLiquidEmerald(), amount);
                        this.renderingMoney();
                        return true;
                    }
                } else if (slot == 19) {
                    amount = ItemUtil.removeItem(this.player, new ItemStack(Material.EMERALD), this.inEme[0]);
                    if (amount == 0) {
                        SoundManager.sendCancel(this.player);
                        return true;
                    } else {
                        SoundManager.sendPickUp(this.player);
                        MoneyManager.add(this.player, (double)amount);
                        this.renderingMoney();
                        return true;
                    }
                } else if (slot == 20) {
                    amount = ItemUtil.removeItem(this.player, new ItemStack(Material.EMERALD_BLOCK), this.inEme[1]);
                    if (amount == 0) {
                        SoundManager.sendCancel(this.player);
                        return true;
                    } else {
                        SoundManager.sendPickUp(this.player);
                        MoneyManager.add(this.player, (double)(amount * 64));
                        this.renderingMoney();
                        return true;
                    }
                } else if (slot == 21) {
                    amount = ItemUtil.removeItem(this.player, ItemLibrary.getLiquidEmerald(), this.inEme[2]);
                    if (amount == 0) {
                        SoundManager.sendCancel(this.player);
                        return true;
                    } else {
                        SoundManager.sendPickUp(this.player);
                        MoneyManager.add(this.player, (double)(amount * 4096));
                        this.renderingMoney();
                        return true;
                    }
                } else {
                    return true;
                }
            } else if (event.getClick().equals(ClickType.RIGHT)) {
                if (slot == 14) {
                    this.updateButton(false, 0);
                    return true;
                } else if (slot == 15) {
                    this.updateButton(false, 1);
                    return true;
                } else if (slot == 16) {
                    this.updateButton(false, 2);
                    return true;
                } else if (slot == 19) {
                    this.updateButton(true, 0);
                    return true;
                } else if (slot == 20) {
                    this.updateButton(true, 1);
                    return true;
                } else if (slot == 21) {
                    this.updateButton(true, 2);
                    return true;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }
}
