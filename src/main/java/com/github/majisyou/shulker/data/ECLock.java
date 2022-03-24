//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.majisyou.shulker.data;

import com.github.redshirt53072.api.BaseAPI;
import com.github.redshirt53072.api.database.SQLInterface;
import com.github.redshirt53072.api.message.LogManager;
import com.github.majisyou.shulker.UsefulShulker;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.OfflinePlayer;

public class ECLock extends SQLInterface {
    private static Map<UUID, Integer> data;

    public ECLock() {
        super(BaseAPI.getInstance());
    }

    private void save(OfflinePlayer player, final int page) {
        final UUID uuid = player.getUniqueId();
        (new Thread() {
            public void run() {
                ECLock.this.connect();
                ECLockSqlSender sender = new ECLockSqlSender(ECLock.this.connectData);
                sender.delete(uuid);
                sender.insert(uuid, page);
                ECLock.this.close();
            }
        }).start();
    }

    public void reload() {
        (new Thread() {
            public void run() {
                ECLock.this.connect();
                ECLockSqlSender sender = new ECLockSqlSender(ECLock.this.connectData);
                ECLock.data = sender.readAll();
                ECLock.this.close();
            }
        }).start();
    }

    public static void setPage(OfflinePlayer player, int page) {
        if (page > 0 && page < 10) {
            (new ECLock()).save(player, page);
            data.replace(player.getUniqueId(), page);
        } else {
            LogManager.logInfo(player.getName() + "のエンダーチェストアンロックページデータベースに不正な値を保存しようとしています。 値：" + page, UsefulShulker.getInstance(), Level.WARNING);
        }
    }

    public static int getPage(OfflinePlayer player) {
        Integer result = (Integer)data.get(player.getUniqueId());
        if (result == null) {
            (new ECLock()).save(player, 1);
            data.put(player.getUniqueId(), 1);
            return 1;
        } else {
            return result;
        }
    }
}
