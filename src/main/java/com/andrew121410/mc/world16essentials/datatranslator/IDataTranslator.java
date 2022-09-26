package com.andrew121410.mc.world16essentials.datatranslator;

import org.bukkit.entity.Player;

public interface IDataTranslator {

    boolean convertFrom(Player player);

    boolean convertTo(Player player);
}
