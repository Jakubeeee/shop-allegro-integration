package com.jakubeeee.allegrointegrator.core.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ThreadUtils {

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
