package it.alzy.simpleeconomy.plugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import it.alzy.simpleeconomy.plugin.i18n.LanguageManager;
import it.alzy.simpleeconomy.plugin.i18n.enums.LanguageKeys;
import it.alzy.simpleeconomy.plugin.managers.CurrencyManager;
import it.alzy.simpleeconomy.plugin.model.VirtualCurrency;
import it.alzy.simpleeconomy.plugin.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@CommandAlias("wallet")
@Description("Allows you to see how much you have of all loaded currencies")
public class WalletCommand extends BaseCommand {

    private final LanguageManager languageManager;
    private final CurrencyManager currencyManager;

    public WalletCommand(LanguageManager languageManager, CurrencyManager currencyManager) {
        this.languageManager = languageManager;
        this.currencyManager = currencyManager;
    }

    @Default
    public void root(Player player) {
        Collection<VirtualCurrency> loadedCurrencies = this.currencyManager.getAllCurrencies();
        List<Component> lines = loadedCurrencies.stream()
                .map(vc -> ChatUtils.createComponent(
                        languageManager.getMessage(LanguageKeys.WALLET_LINE),
                        "%balance%", vc.getBalance(player.getUniqueId()),
                        "%currency%", vc.getName()
                        )
                ).toList();
        Component header = ChatUtils.parse(languageManager.getMessage(LanguageKeys.WALLET_HEADER));
        Component message = Component.join(
                JoinConfiguration.newlines(),
                Stream.concat(Stream.of(header), lines.stream()).toList()
        );
        player.sendMessage(message);
    }

}
