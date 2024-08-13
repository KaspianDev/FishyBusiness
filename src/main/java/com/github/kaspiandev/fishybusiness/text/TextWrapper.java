package com.github.kaspiandev.fishybusiness.text;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.function.Function;

public class TextWrapper {

    private String text;

    public TextWrapper(String text) {
        this.text = text;
    }

    public TextWrapper replace(String from, String to) {
        this.text = text.replace(from, to);
        return this;
    }

    public TextWrapper replace(String from, Number to) {
        replace(from, String.valueOf(to));
        return this;
    }

    public TextWrapper manipulate(Function<String, String> function) {
        this.text = function.apply(text);
        return this;
    }

    public ColorfulTextWrapper colorize() {
        return new ColorfulTextWrapper(text);
    }

    public BaseComponent[] buildAsComponent() {
        return TextComponent.fromLegacyText(text);
    }

    public String buildAsString() {
        return text;
    }

}
