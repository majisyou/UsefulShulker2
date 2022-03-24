//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.majisyou.shulker.bank;

import com.github.redshirt53072.api.message.MessageManager;
import com.github.redshirt53072.api.message.SoundManager;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public final class BankCommand implements TabExecutor {
    public BankCommand() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageManager.sendCommandError("コンソールからは実行できません。", sender);
            return true;
        } else {
            Player p = (Player)sender;
            if (p.getGameMode().equals(GameMode.CREATIVE)) {
                SoundManager.sendCancel(p);
                return true;
            } else {
                MessageManager.sendInfo(p.getName() + "様いらっしゃいませ。Prince銀行ダイレクトでございます。", sender);
                (new BankGui()).open(p);
                return true;
            }
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tab = new ArrayList();
        return tab;
    }
}
