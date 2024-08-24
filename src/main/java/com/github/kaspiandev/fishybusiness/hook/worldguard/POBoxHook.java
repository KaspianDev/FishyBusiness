package com.github.kaspiandev.fishybusiness.hook.worldguard;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.hook.Hook;
import com.github.kaspiandev.fishybusiness.reward.ItemReward;
import com.github.kaspiandev.fishybusiness.reward.RewardType;
import com.github.kaspiandev.fishybusiness.reward.RewardTypeRegistry;
import com.github.kaspiandev.fishybusiness.reward.adapter.ItemB64Adapter;
import com.github.kaspiandev.pobox.POBox;
import com.github.kaspiandev.pobox.mail.ItemMail;
import com.github.kaspiandev.pobox.mail.UniqueMail;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class POBoxHook extends Hook<POBox> {

    public POBoxHook(FishyBusiness plugin) {
        super(plugin, "POBox");
    }

    @Override
    protected void load() {
        ItemB64Adapter itemAdapter = new ItemB64Adapter();
        RewardTypeRegistry.register("item", new RewardType(ItemReward.class, itemAdapter));
    }

    public void stashItem(Player player, ItemStack item) {
        hookPlugin.getMailManager().getBox(player).ifPresent((box) -> {
            box.addMail(new UniqueMail(new ItemMail("FishyBusiness Reward", item)));
        });
    }

}
