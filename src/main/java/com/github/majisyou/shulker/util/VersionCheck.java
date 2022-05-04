package com.github.majisyou.shulker.util;

import com.github.majisyou.shulker.UsefulShulker;

import java.util.regex.Pattern;

public class VersionCheck {
    public static boolean upVersionCheck(String libVersionString, String requireVersionString, UsefulShulker plugin){
        int libVersion;
        int requireVersion;

        try{
            libVersion = Integer.parseInt(String.join("", libVersionString.split(Pattern.quote("."))));
            requireVersion = Integer.parseInt(String.join("", requireVersionString.split(Pattern.quote("."))));
        }catch (NumberFormatException e){
            plugin.getLogger().info("Plugin version cannot parse int.");
            return false;
        }

        return libVersion <= requireVersion;
    }
}
