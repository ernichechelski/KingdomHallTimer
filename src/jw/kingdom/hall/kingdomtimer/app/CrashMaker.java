package jw.kingdom.hall.kingdomtimer.app;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * All rights reserved & copyright ©
 */
public abstract class CrashMaker {
    public static void crashApp(){
        try {
            getUnsafe().allocateInstance(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Unsafe getUnsafe() throws Exception {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        return (Unsafe) f.get(null);
    }

    private CrashMaker(){}
}