package com.github.kaspiandev.fishybusiness.text;

import de.themoep.minedown.MineDown;
import net.md_5.bungee.api.chat.BaseComponent;

public class ColorfulTextWrapper {

    private MineDown text;

    public ColorfulTextWrapper(String text) {
        this.text = new MineDown(text);
    }

    public BaseComponent[] buildAsComponent() {
        return text.toComponent();
    }

    public String buildAsString() {
        return BaseComponent.toLegacyText(buildAsComponent());
    }

}
