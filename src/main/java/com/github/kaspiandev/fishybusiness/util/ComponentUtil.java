package com.github.kaspiandev.fishybusiness.util;

import net.md_5.bungee.api.chat.BaseComponent;

public class ComponentUtil {

    private ComponentUtil() {}

    public static String toString(BaseComponent[] component) {
        return BaseComponent.toLegacyText(component);
    }

}
