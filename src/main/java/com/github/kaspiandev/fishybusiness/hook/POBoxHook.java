package com.github.kaspiandev.fishybusiness.hook;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.config.Message;
import com.github.kaspiandev.fishybusiness.reward.*;
import com.github.kaspiandev.fishybusiness.reward.adapter.ItemB64Adapter;
import com.github.kaspiandev.pobox.POBox;
import com.github.kaspiandev.pobox.event.MailClaimEvent;
import com.github.kaspiandev.pobox.mail.ItemMail;
import com.github.kaspiandev.pobox.mail.Mail;
import com.github.kaspiandev.pobox.mail.MailManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class POBoxHook extends Hook<POBox> implements Listener {

    public POBoxHook(FishyBusiness plugin) {
        super(plugin, "POBox");
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    protected void load() {
        ItemB64Adapter itemAdapter = new ItemB64Adapter();
        RewardTypeRegistry.register("item", new RewardType(ItemReward.class, itemAdapter));
        RewardTypeRegistry.register("pobox", new RewardType(POBoxReward.class));
    }

    public void stashItem(Player player, ItemStack item) {
        MailManager mailManager = hookPlugin.getMailManager();
        mailManager.getBox(player).ifPresent((box) -> {
            ItemMeta meta = item.getItemMeta();
            String name = (plugin.getConf().isPOBoxItemCustomName())
                    ? (meta == null || !meta.hasDisplayName()) ? plugin.getConf().getPOBoxItemName() : meta.getDisplayName()
                    : plugin.getConf().getPOBoxItemName();
            if (plugin.getConf().isPOBoxItemCustomIcon()) {
                mailManager.sendMail(box, new ItemMail(name, item, item.getType()));
            } else {
                mailManager.sendMail(box, new ItemMail(name, item));
            }
        });
    }

    @EventHandler
    public void onClaim(MailClaimEvent event) {
        Player player = event.getPlayer();
        if (plugin.getAreaManager().getPlayerArea(player).isEmpty()) return;

        player.spigot().sendMessage(plugin.getMessages().get(Message.MAIL_CANNOT_CLAIM, player));
        event.setCancelled(true);
    }

    public RewardMail buildRewardMail(String name, Reward reward) {
        return new RewardMail(name, reward);
    }

    public RewardMail buildRewardMail(String name, Reward reward, Material icon) {
        return new RewardMail(name, reward, icon);
    }

    public class RewardMail extends Mail {

        private final String rewardName;

        public RewardMail(String name, Reward reward) {
            super(name);
            this.rewardName = reward.getName();
        }

        public RewardMail(String name, Reward reward, Material icon) {
            super(name, icon);
            this.rewardName = reward.getName();
        }

        @Override
        public void claim(Player player) {
            plugin.getRewardManager().findReward(rewardName).ifPresent((reward) -> reward.reward(plugin, player));
        }

    }

}
