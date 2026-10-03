package com.untistore.clans.utils;


import net.kyori.adventure.text.Component;

public class ComponentUtil {

    public static Component create(String text, boolean color) {
        if (!color) {
            return Component.text(text);
        } else {
            return Component.text(text.replace("&", "§"));
        }
    }
}
