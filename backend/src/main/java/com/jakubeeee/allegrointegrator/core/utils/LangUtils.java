package com.jakubeeee.allegrointegrator.core.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LangUtils {

    public static <T> T nvl(T nullableObject, T reserveObject) {
        return nullableObject != null ? nullableObject : reserveObject;
    }

}
