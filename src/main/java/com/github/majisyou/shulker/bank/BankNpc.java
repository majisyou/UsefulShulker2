//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.github.majisyou.shulker.bank;

import com.github.redshirt53072.api.message.MessageManager;
import com.github.redshirt53072.api.npc.Npc;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

public class BankNpc extends Npc {
    public BankNpc() {
        super("bank");
    }

    public boolean onClick(Player player, Villager vil) {
        (new BankGui()).open(player);
        MessageManager.sendInfo(player.getName() + "様いらっしゃいませ。Prince銀行ロビー本店でございます。", player);
        return true;
    }
}
