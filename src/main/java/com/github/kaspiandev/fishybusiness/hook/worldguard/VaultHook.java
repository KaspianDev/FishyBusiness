package com.github.kaspiandev.fishybusiness.hook.worldguard;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.hook.Hook;
import com.github.kaspiandev.fishybusiness.reward.RewardType;
import com.github.kaspiandev.fishybusiness.reward.RewardTypeRegistry;
import com.github.kaspiandev.fishybusiness.reward.VaultReward;
import com.github.kaspiandev.fishybusiness.reward.adapter.VaultRewardAdapter;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook extends Hook<Vault> {

    private final Economy economy;

    public VaultHook(FishyBusiness plugin) {
        super(plugin, "Vault");
        RegisteredServiceProvider<Economy> serviceProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (serviceProvider == null) {
            throw new IllegalStateException("Vault hook was enabled, but there is no economy providing plugin");
        }
        this.economy = serviceProvider.getProvider();
    }

    @Override
    protected void load() {
        RewardTypeRegistry.register("vault", new RewardType(VaultReward.class, new VaultRewardAdapter(plugin)));
    }

    public void addMoney(Player player, double amount) {
        economy.depositPlayer(player, amount);
    }

}
