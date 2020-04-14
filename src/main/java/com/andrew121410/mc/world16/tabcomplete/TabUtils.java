package com.andrew121410.mc.world16.tabcomplete;

import java.util.ArrayList;
import java.util.List;

public class TabUtils {

    public static List<String> getContainsString(String args, List<String> oldArrayList) {
        List<String> list = new ArrayList<>();

        for (String mat : oldArrayList) {
            if (mat.contains(args.toLowerCase())) {
                list.add(mat);
            }
        }

        return list;
    }
}
