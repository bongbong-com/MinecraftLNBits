package com.bongbong.lightning.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import com.bongbong.lightning.DataManager;
import com.bongbong.lightning.DataProfile;
import com.bongbong.lightning.Requests;
import com.bongbong.lightning.ThreadUtil;
import com.bongbong.lightning.responses.PayResponse;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@RequiredArgsConstructor
@CommandAlias("ln|lightning|lncli|btc")
public class PayCommand extends BaseCommand {

    final ThreadUtil threadUtil;
    final DataManager dataManager;
    final Requests requests;

    @Subcommand("pay|send")
    @Description("Pay another player (online or offline)")
    public void onPay(CommandSender sender, OfflinePlayer player, int amount) {

    }

    @Subcommand("lnpay|lightningpay|lnsend|lightningsend")
    @Description("Pay a lightning BOLT11 invoice.")
    public void onLightnningPay(Player player, String bolt11) {
        UUID uuid = player.getUniqueId();

        if (bolt11 == null) {
            player.sendMessage("Usage: /ln lnpay <bolt11>");
            return;
        }

        threadUtil.runTask(true, () -> {
            DataProfile profile;
            profile = dataManager.getProfile(uuid);

            if (profile != null) {
                PayResponse response = requests.payInvoice(bolt11, profile.getAdminKey());

                player.sendMessage("Success! Hash: " + response.getPayment_hash());
            } else {
                player.sendMessage("Error: You do not have a wallet. Try again in 10 seconds or contact an admin.");
            }
        });
    }
}
