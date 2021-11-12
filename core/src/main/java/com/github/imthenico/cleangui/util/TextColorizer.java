package com.github.imthenico.cleangui.util;

import org.bukkit.ChatColor;

import java.util.List;

public class TextColorizer {

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String[] color(String[] text) {
        for (int i = 0; i < text.length; i++) {
            text[i] = color(text[i]);
        }

        return text;
    }

    public static List<String> color(List<String> text) {
        text.replaceAll(TextColorizer::color);

        return text;
    }
}