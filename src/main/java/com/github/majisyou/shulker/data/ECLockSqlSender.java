//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.majisyou.shulker.data;

import com.github.redshirt53072.api.database.SQLSender;
import com.github.majisyou.shulker.UsefulShulker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class ECLockSqlSender extends SQLSender {
    public ECLockSqlSender(Connection connect) {
        super(connect, "pl_ec_unlocked_page", UsefulShulker.getInstance());
    }

    public void insert(UUID player, int page) {
        try {
            Throwable var3 = null;
            Object var4 = null;

            try {
                PreparedStatement statement = this.connectData.prepareStatement("INSERT IGNORE INTO pl_ec_unlocked_page (uuid, page) VALUES (?,?);");

                try {
                    statement.setString(1, player.toString());
                    statement.setInt(2, page);
                    statement.executeUpdate();
                } finally {
                    if (statement != null) {
                        statement.close();
                    }

                }
            } catch (Throwable var13) {
                if (var3 == null) {
                    var3 = var13;
                } else if (var3 != var13) {
                    var3.addSuppressed(var13);
                }

                throw var3;
            }
        } catch (Throwable var14) { //SQLExceptionをThrowableに置換　majisyou
            super.logSevere("INSERT " + player, (Exception) var14); //(Exception) var--に変更 majisyou
        }

    }

    public void delete(UUID player) {
        try {
            Throwable var2 = null;
            Object var3 = null;

            try {
                PreparedStatement statement = this.connectData.prepareStatement("DELETE FROM pl_ec_unlocked_page WHERE uuid = ?;");

                try {
                    statement.setString(1, player.toString());
                    statement.executeUpdate();
                } finally {
                    if (statement != null) {
                        statement.close();
                    }

                }
            } catch (Throwable var12) {
                if (var2 == null) {
                    var2 = var12;
                } else if (var2 != var12) {
                    var2.addSuppressed(var12);
                }

                throw var2;
            }
        } catch (Throwable var13) { //SQLExceptionをThrowableに置換　majisyou
            super.logWarning("DELETE FROM pl_ec_unlocked_page WHERE uuid = " + player.toString(), (Exception) var13); //var--に変更 majisyou
        }

    }

    public Map<UUID, Integer> readAll() {
        try {
            Throwable var1 = null;
            Object var2 = null;

            try {
                PreparedStatement statement = this.connectData.prepareStatement("SELECT * FROM pl_ec_unlocked_page;");

                HashMap var10000;
                try {
                    ResultSet result = statement.executeQuery();
                    HashMap resultMap = new HashMap();

                    while(result.next()) {
                        try {
                            int page = result.getInt("page");
                            String uuid = result.getString("uuid");
                            resultMap.put(UUID.fromString(uuid), page);
                        } catch (Exception var16) {
                            super.logWarning("SELECT * FROM pl_ec_unlocked_page", var16);
                        }
                    }

                    var10000 = resultMap;
                } finally {
                    if (statement != null) {
                        statement.close();
                    }

                }

                return var10000;
            } catch (Throwable var18) {
                if (var1 == null) {
                    var1 = var18;
                } else if (var1 != var18) {
                    var1.addSuppressed(var18);
                }

                throw var1;
            }
        } catch (Throwable var19) { //SQLExceptionをThrowableに置換　majisyou
            super.logWarning("SELECT * FROM pl_ec_unlocked_page", (Exception) var19); //var--に変更 majisyou
            return new HashMap();
        }
    }
}
