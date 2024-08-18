package com.github.kaspiandev.fishybusiness.util;

import de.themoep.minedown.MineDown;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.List;

public class ColorUtil {

    private ColorUtil() {}

    public static List<String> string(List<String> message) {
        return message.stream()
                      .map(ColorUtil::string)
                      .toList();
    }

    public static String string(String message) {
        return BaseComponent.toLegacyText(component(message));
    }

    public static BaseComponent[] component(String message) {
        return new MineDown(message).toComponent();
    }

}
