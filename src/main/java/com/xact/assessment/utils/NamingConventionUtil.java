/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.utils;

public class NamingConventionUtil {

    public String convertToPascalCase(String text) {
        if (text == null)
            return null;

        final StringBuilder builder = new StringBuilder(text.length());

        for (final String word : text.split(" ")) {
            if (!word.isEmpty()) {
                builder.append(Character.toUpperCase(word.charAt(0)));
                builder.append(word.substring(1).toLowerCase());
            }
            if ((builder.length() != text.length()))
                builder.append(" ");
        }

        return builder.toString();
    }
}
