package com.github.majisyou.shulker;

import com.github.redshirt53072.api.BaseAPI;
import com.github.redshirt53072.api.message.LogManager;
import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.api.npc.NpcManager;
import com.github.redshirt53072.api.player.PlayerManager;
import com.github.redshirt53072.api.server.GrowthPlugin;
import com.github.redshirt53072.api.server.PluginManager;
import com.github.redshirt53072.api.server.PluginManager.StopReason;
import com.github.majisyou.shulker.bank.BankCommand;
import com.github.majisyou.shulker.bank.BankNpc;
import com.github.majisyou.shulker.bank.command.ECSubCommand;
import com.github.majisyou.shulker.data.ECLock;
import com.github.majisyou.shulker.data.EnderChest;
import com.github.majisyou.shulker.temp.MoveEnderChest;
import java.util.logging.Level;

public final class UsefulShulker extends GrowthPlugin {
    private static UsefulShulker plugin;

    public UsefulShulker() {
    }

    public void onEnable() {
        this.name = "shulker";
        this.version = "3.0.0";
        plugin = this;
        try {   //try catchで囲んだ
            PluginManager.registerApi(plugin, true);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (!BaseAPI.getInstance().checkVersion("3.0.0")) {
            LogManager.logError("前提プラグイン(GrowthAPI)のバージョンが正しくありません。", this, new Throwable(), Level.SEVERE);
            PluginManager.stopServer("プラグインバージョンの不整合による", StopReason.ERROR);
        }

        (new ECLock()).reload();
        ECSubCommand.register();
        this.getCommand("bank").setExecutor(new BankCommand());
        new PlayerAction();
        PlayerManager.registerInit(new EnderChest());
        PlayerManager.registerInit(new MoveEnderChest());
        NpcManager.registerNpc(new BankNpc());
        LogManager.logInfo(TextBuilder.plus(new String[]{this.getPluginName(), "を読み込みました"}), this, Level.INFO);
    }

    public void onDisable() {
        LogManager.logInfo(TextBuilder.plus(new String[]{this.getPluginName(), "を終了しました"}), this, Level.INFO);
    }

    public static UsefulShulker getInstance() {
        return plugin;
    }
}
