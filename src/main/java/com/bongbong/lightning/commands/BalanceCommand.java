package com.bongbong.lightning.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import com.bongbong.lightning.DataManager;
import com.bongbong.lightning.DataProfile;
import com.bongbong.lightning.Requests;
import com.bongbong.lightning.ThreadUtil;
import com.bongbong.lightning.responses.WalletInfoResponse;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.UUID;

@RequiredArgsConstructor
@CommandAlias("ln|lightning|lncli|btc")
public class BalanceCommand extends BaseCommand {

    final ThreadUtil threadUtil;
    final DataManager dataManager;
    final Requests requests;

    @Subcommand("balance|bal")
    @Description("Check your wallet's balance")
    public void onBalance(Player player) {
        UUID uuid = player.getUniqueId();

        threadUtil.runTask(true, () -> {
            DataProfile profile;
            profile = dataManager.getProfile(uuid);

            if (profile != null) {
                WalletInfoResponse response = requests.getWalletBalance(profile.getInvoiceKey());

                player.sendMessage("Your balance: " + response.getBalance()/1000 + " sats");
            } else {
                player.sendMessage("Error: You do not have a wallet. Try again in 10 seconds or contact an admin.");
            }
        });
    }
}