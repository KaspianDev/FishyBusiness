package com.github.kaspiandev.fishybusiness.hook;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.config.Message;
import com.github.kaspiandev.fishybusiness.reward.*;
import com.github.kaspiandev.fishybusiness.reward.adapter.ItemB64Adapter;
import com.github.kaspiandev.fishybusiness.util.ComponentUtil;
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
            String name = ComponentUtil.toString(plugin.getMessages().get(
                    (plugin.getConf().isPOBoxItemCustomName())
                            ? (meta == null || !meta.hasDisplayName()) ? plugin.getConf().getPOBoxItemName() : meta.getDisplayName()
                            : plugin.getConf().getPOBoxItemName()));
            if (plugin.getConf().isPOBoxItemCustomIcon()) {
                mailManager.sendMail(box, new ItemMail(name, item, item.getType()));
            } else {
                mailManager.sendMail(box, new ItemMail(name, item));
            }
        });
    }

    public void sendMail(Player player, Mail mail) {
        hookPlugin.getMailManager().getBox(player).ifPresent((box) -> {
            hookPlugin.getMailManager().sendMail(box, mail);
        });
    }

    @EventHandler
    public void onClaim(MailClaimEvent event) {
        Player player = event.getPlayer();
        if (plugin.getAreaManager().getPlayerArea(player).isEmpty()) return;

        player.spigot().sendMessage(plugin.getMessages().get(Message.MAIL_CANNOT_CLAIM, player));
        event.setCancelled(true);
    }

    public RewardMail buildRewardMail(Player player, String name, Reward reward) {
        String sender = plugin.getConf().getPOBoxItemSender();
        if (sender.isEmpty()) {
            if (plugin.getConf().isPOBoxItemCustomIcon()) {
                return new RewardMail(name, reward.getName(), reward.getDisplay(plugin, player).getType());
            } else {
                return new RewardMail(name, reward.getName());
            }
        } else {
            if (plugin.getConf().isPOBoxItemCustomIcon()) {
                return new RewardMail(name, reward.getName(), reward.getDisplay(plugin, player).getType(), sender);
            } else {
                return new RewardMail(name, sender, reward.getName());
            }
        }
    }

    public RewardMail buildRewardMail(String name, String rewardName) {
        return new RewardMail(name, rewardName);
    }

    public class RewardMail extends Mail {

        private final String rewardName;

        public RewardMail(String name, String rewardName) {
            super(name);
            this.rewardName = rewardName;
        }

        public RewardMail(String name, String rewardName, Material icon) {
            super(name, icon);
            this.rewardName = rewardName;
        }

        public RewardMail(String name, String sender, String rewardName) {
            super(name, sender);
            this.rewardName = rewardName;
        }

        public RewardMail(String name, String rewardName, Material icon, String sender) {
            super(name, icon, sender);
            this.rewardName = rewardName;
        }

        @Override
        public void claim(Player player) {
            plugin.getRewardManager().findReward(rewardName).ifPresent((reward) -> reward.reward(plugin, player));
        }

    }

}
