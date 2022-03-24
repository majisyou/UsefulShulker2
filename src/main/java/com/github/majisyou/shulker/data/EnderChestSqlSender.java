//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.majisyou.shulker.data;

import com.github.redshirt53072.api.database.SQLSender;
import com.github.majisyou.shulker.UsefulShulker;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

class EnderChestSqlSender extends SQLSender {
    public EnderChestSqlSender(Connection connect) {
        super(connect, "player_enderchest", UsefulShulker.getInstance());
    }

    public void init(UUID player) {
        String data = (new Gson()).toJson(new JsonArray());

        try {
            Throwable var3 = null;
            Object var4 = null;

            try {
                PreparedStatement statement = this.connectData.prepareStatement("INSERT IGNORE INTO player_enderchest (uuid, page, data) VALUES (?,1,?),(?,2,?),(?,3,?),(?,4,?),(?,5,?),(?,6,?),(?,7,?),(?,8,?),(?,9,?);");

                try {
                    statement.setString(1, player.toString());
                    statement.setString(3, player.toString());
                    statement.setString(5, player.toString());
                    statement.setString(7, player.toString());
                    statement.setString(9, player.toString());
                    statement.setString(11, player.toString());
                    statement.setString(13, player.toString());
                    statement.setString(15, player.toString());
                    statement.setString(17, player.toString());
                    statement.setString(2, data);
                    statement.setString(4, data);
                    statement.setString(6, data);
                    statement.setString(8, data);
                    statement.setString(10, data);
                    statement.setString(12, data);
                    statement.setString(14, data);
                    statement.setString(16, data);
                    statement.setString(18, data);
                    int result = statement.executeUpdate();
                    if (result > 0) {
                        super.logInfo("INSERTALL " + player + "; result : " + result);
                    }
                } finally {
                    if (statement != null) {
                        statement.close();
                    }

                }
            } catch (Throwable var14) {
                if (var3 == null) {
                    var3 = var14;
                } else if (var3 != var14) {
                    var3.addSuppressed(var14);
                }

                throw var3;
            }
        } catch (SQLException var15) {
            super.logSevere("INSERTALL " + player, var15);
        }

    }

    public void insert(UUID player, int page, String data) {
        try {
            Throwable var4 = null;
            Object var5 = null;

            try {
                PreparedStatement statement = this.connectData.prepareStatement("INSERT INTO player_enderchest (uuid, page, data) VALUES (?,?,?);");

                try {
                    statement.setString(1, player.toString());
                    statement.setInt(2, page);
                    statement.setString(3, data);
                    int result = statement.executeUpdate();
                    if (result != 1) {
                        super.logWarning("INSERT " + player);
                    }
                } finally {
                    if (statement != null) {
                        statement.close();
                    }

                }
            } catch (Throwable var15) {
                if (var4 == null) {
                    var4 = var15;
                } else if (var4 != var15) {
                    var4.addSuppressed(var15);
                }

                throw var4;
            }
        } catch (SQLException var16) {
            super.logSevere("INSERT " + player, var16);
        }

    }

    public void delete(UUID player, int page) {
        try {
            Throwable var3 = null;
            Object var4 = null;

            try {
                PreparedStatement statement = this.connectData.prepareStatement("DELETE FROM player_enderchest WHERE uuid = ? AND page = ?;");

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
        } catch (SQLException var14) {
            super.logWarning("DELETE FROM player_enderchest WHERE uuid = " + player.toString() + " AND page = " + page, var14);
        }

    }

    public String read(UUID player, int page) {
        try {
            Throwable var3 = null;
            Object var4 = null;

            try {
                PreparedStatement statement = this.connectData.prepareStatement("SELECT data FROM player_enderchest WHERE uuid = ? AND page = ?;");

                String var10000;
                try {
                    statement.setString(1, player.toString());
                    statement.setInt(2, page);
                    ResultSet result = statement.executeQuery();
                    if (!result.next()) {
                        super.logWarning("SELECT data FROM player_enderchest WHERE uuid = " + player.toString() + " AND page = " + page, new Exception("結果はnullでした。"));
                        return null;
                    }

                    String resultData = result.getString("data");
                    if (resultData == null) {
                        super.logWarning("SELECT data FROM player_enderchest WHERE uuid = " + player.toString() + " AND page = " + page, new Exception());
                    }

                    var10000 = resultData;
                } finally {
                    if (statement != null) {
                        statement.close();
                    }

                }

                return var10000;
            } catch (Throwable var15) {
                if (var3 == null) {
                    var3 = var15;
                } else if (var3 != var15) {
                    var3.addSuppressed(var15);
                }

                throw var3;
            }
        } catch (SQLException var16) {
            super.logWarning("SELECT data FROM player_enderchest WHERE uuid = " + player.toString() + " AND page = " + page, var16);
            return null;
        }
    }
}
