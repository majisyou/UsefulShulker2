//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.majisyou.shulker.data;

import com.github.redshirt53072.api.BaseAPI;
import com.github.redshirt53072.api.database.SQLInterface;
import com.github.redshirt53072.api.gui.GuiManager;
import com.github.redshirt53072.api.player.InitListener;
import com.github.redshirt53072.api.player.PlayerManager;
import com.github.redshirt53072.api.util.Serializer;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class EnderChest extends SQLInterface implements InitListener {
    public EnderChest() {
        super(BaseAPI.getInstance());
    }

    public void closeSave(final OfflinePlayer player, Inventory inv, final int page) {
        PlayerManager.addAsyncLock("ec", player);
        final UUID uuid = player.getUniqueId();
        final String playerData = Serializer.toJson(inv, 0, 26);

        for(int i = 0; i < 27; ++i) {
            inv.clear(i);
        }

        (new Thread() {
            public void run() {
                EnderChest.this.connect();
                EnderChestSqlSender sender = new EnderChestSqlSender(EnderChest.this.connectData);
                try { //try catchで囲んだ majisyou
                    sender.delete(uuid, page);
                    sender.insert(uuid, page, playerData);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                EnderChest.this.close();
                PlayerManager.removeAsyncLock("ec", player);
            }
        }).start();
    }

    public void openLoad(final OfflinePlayer player, final Inventory inv, final int page) {
        final UUID uuid = player.getUniqueId();
        PlayerManager.addAsyncLock("ec", player);
        (new Thread() {
            public void run() {
                EnderChest.this.connect();
                EnderChestSqlSender sender = new EnderChestSqlSender(EnderChest.this.connectData);
                final String data = sender.read(uuid, page);
                EnderChest.this.close();
                Bukkit.getScheduler().runTask(EnderChest.this.plugin, new Runnable() {
                    public void run() {
                        GuiManager.clearItem(inv, 0, 26);
                        Serializer.toGui(data, inv, 0, 26);
                        PlayerManager.removeAsyncLock("ec", player);
                    }
                });
            }
        }).start();
    }

    public void pageChange(final OfflinePlayer player, final Inventory inv, final int oldPage, final int newPage) {
        final UUID uuid = player.getUniqueId();
        final String playerData = Serializer.toJson(inv, 0, 26);
        PlayerManager.addAsyncLock("ec", player);
        (new Thread() {
            public void run() {
                EnderChest.this.connect();
                EnderChestSqlSender sender = new EnderChestSqlSender(EnderChest.this.connectData);
                try { //trycatchで囲んだ　majisyou
                    sender.delete(uuid, oldPage);
                    sender.insert(uuid, oldPage, playerData);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                final String data = sender.read(uuid, newPage);
                EnderChest.this.close();
                Bukkit.getScheduler().runTask(EnderChest.this.plugin, new Runnable() {
                    public void run() {
                        GuiManager.clearItem(inv, 0, 26);
                        Serializer.toGui(data, inv, 0, 26);
                        PlayerManager.removeAsyncLock("ec", player);
                    }
                });
            }
        }).start();
    }

    public void onInit(Player player) {
        final UUID uuid = player.getUniqueId();
        (new Thread() {
            public void run() {
                EnderChest.this.connect();
                EnderChestSqlSender sender = new EnderChestSqlSender(EnderChest.this.connectData);
                sender.init(uuid);
                EnderChest.this.close();
            }
        }).start();
    }
}
