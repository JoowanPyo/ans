package com.gemiso.zodiac.core.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

@Slf4j
public class SearchConvert {

    public static String searchConvert(String text) {

        if (text != null && !"".equals(text)) {
            char[] filters = {'[', ']','^', '"', '\'', '\\', '{', '}', '/', '?', '.', ',', ';', ':', ')', '(', '*', '~', '`', '-', '_', '+', '<', '>', '@', '#', '$', '%', '='};
            StringBuilder search = new StringBuilder("");

            for (char c : text.toCharArray()) {
                if (ArrayUtils.contains(filters, c)) {
                    for (char s : filters) {
                        if (c == s) {
                            search.append("\\" + Character.toString(c));
                        }
                    }
                } else {
                    search.append(Character.toString(c));
                }
            }
            return search.toString();

        } else {

            return "";
        }
    }
}
