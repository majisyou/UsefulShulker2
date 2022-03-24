//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.majisyou.shulker.bank.command;

import com.github.redshirt53072.api.command.ManagementCommand;
import com.github.redshirt53072.api.command.SubCommand;
import com.github.redshirt53072.api.gui.Gui;
import com.github.redshirt53072.api.gui.GuiManager;
import com.github.redshirt53072.api.message.MessageManager;
import com.github.redshirt53072.api.message.TextManager;
import com.github.majisyou.shulker.data.ECLock;
import com.github.majisyou.shulker.gui.EnderGui;
import com.github.majisyou.shulker.gui.OpEnderGui;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ECSubCommand implements SubCommand {
    private static ECSubCommand sub;
    private static List<OfflinePlayer> offlinePlayers = new ArrayList();

    public ECSubCommand() {
    }

    public static void register() {
        sub = new ECSubCommand();
        ManagementCommand.register("enderchest", sub);
        OfflinePlayer[] var3;
        int var2 = (var3 = Bukkit.getOfflinePlayers()).length;

        for(int var1 = 0; var1 < var2; ++var1) {
            OfflinePlayer op = var3[var1];
            offlinePlayers.add(op);
        }

    }

    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            MessageManager.sendCommandError("必要な項目が未記入です。", sender);
        } else {
            String name = args[2];
            OfflinePlayer target = null;
            Iterator var8 = offlinePlayers.iterator();

            while(var8.hasNext()) {
                OfflinePlayer op = (OfflinePlayer)var8.next();
                if (op.getName().equals(name)) {
                    target = op;
                    break;
                }
            }

            if (target == null) {
                MessageManager.sendCommandError("無効なプレイヤー名です。", sender);
            } else if (args[1].equals("setunlockpage")) {
                Integer page = TextManager.toNaturalNumber(args[3]);
                if (page == null) {
                    MessageManager.sendCommandError("無効なページ数です。", sender);
                } else if (page >= 1 && page <= 9) {
                    ECLock.setPage(target, page);
                    MessageManager.sendSpecial(target.getName() + "のエンダーチェストの拡張をセットしました。", sender);
                } else {
                    MessageManager.sendCommandError("無効なページ数です。", sender);
                }
            } else {
                Player player;
                if (args[1].equals("view")) {
                    if (!(sender instanceof Player)) {
                        MessageManager.sendCommandError("このサブコマンドはコンソールからは実行できません。", sender);
                    } else {
                        player = (Player)sender;
                        (new OpEnderGui(target, false)).open(player);
                    }
                } else if (args[1].equals("edit")) {
                    if (!(sender instanceof Player)) {
                        MessageManager.sendCommandError("このサブコマンドはコンソールからは実行できません。", sender);
                    } else {
                        player = (Player)sender;
                        if (target.isOnline()) {
                            Gui targetGui = GuiManager.getGui(target.getPlayer());
                            if (targetGui != null && targetGui instanceof EnderGui) {
                                MessageManager.sendCommandError("現在対象プレイヤーがエンダーチェストを開いているため、編集を開始できません", sender);
                                return;
                            }
                        }

                        if (OpEnderGui.isOpenedPlayer(target)) {
                            MessageManager.sendCommandError("現在他の権限持ちプレイヤーがこのプレイヤーのエンダーチェストを編集モードで開いているため、編集を開始できません。", sender);
                        } else {
                            (new OpEnderGui(target, true)).open(player);
                        }
                    }
                } else {
                    MessageManager.sendCommandError("無効なサブコマンドです。", sender);
                }
            }
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tab = new ArrayList();
        if (args.length == 2) {
            if (sender instanceof Player) {
                tab.add("view");
                tab.add("edit");
            }

            tab.add("setunlockpage");
        } else {
            String text1;
            if (args.length == 3) {
                text1 = args[1];
                ArrayList offPlayers;
                OfflinePlayer op;
                int var9;
                int var10;
                OfflinePlayer[] var11;
                if (sender instanceof Player) {
                    if (text1.equals("setunlockpage") || text1.equals("view") || text1.equals("edit")) {
                        offPlayers = new ArrayList();
                        var10 = (var11 = Bukkit.getOfflinePlayers()).length;

                        for(var9 = 0; var9 < var10; ++var9) {
                            op = var11[var9];
                            tab.add(op.getName());
                            offPlayers.add(op);
                        }

                        offlinePlayers = offPlayers;
                    }
                } else if (text1.equals("setunlockpage")) {
                    offPlayers = new ArrayList();
                    var10 = (var11 = Bukkit.getOfflinePlayers()).length;

                    for(var9 = 0; var9 < var10; ++var9) {
                        op = var11[var9];
                        tab.add(op.getName());
                        offPlayers.add(op);
                    }

                    offlinePlayers = offPlayers;
                }
            } else if (args.length == 4) {
                text1 = args[1];
                if (text1.equals("setunlockpage")) {
                    tab.add("開放済みページ数");
                }
            }
        }

        return tab;
    }
}
