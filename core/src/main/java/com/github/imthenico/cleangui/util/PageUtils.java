package com.github.imthenico.cleangui.util;

import com.github.imthenico.cleangui.manager.item.GUIItem;

import java.util.Arrays;

public class PageUtils {

    public static int getTotalPages(int objects, int maxItemsPerPage) {
        return Math.max(1, (int) Math.ceil((double) objects / maxItemsPerPage));
    }

    public static GUIItem[] getPageItems(int page, int maxItemsPerPage, GUIItem[] items) {
        int start = (page - 1) * maxItemsPerPage;
        int end = page * maxItemsPerPage;

        return Arrays.copyOfRange(items, start, end);
    }
}