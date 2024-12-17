package com.telegram.bot.telegram_utils;

import com.telegram.bot.models.Animal;
import com.telegram.bot.models.Shelters;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ModelsHelper {

    public static <T> List<T> getPage(int page, int size, List<T> list) {
        if (page < 1) {
            log.error("Page number is less than 1");
            throw new IllegalArgumentException("page must be greater than 0");
        }

        int i = (page * size) - size;

        if (i > list.size() - 1) {
            return new ArrayList<>();
        }

        int maxPageSize = page * size;

        List<T> newList = new ArrayList<>();

        while (i < maxPageSize && i < list.size()) {
            newList.add(list.get(i));
            i++;
        }

        return newList;
    }
}
