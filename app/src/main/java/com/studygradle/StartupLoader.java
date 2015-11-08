package com.studygradle;

import android.content.Context;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import dalvik.system.DexFile;

/**
 * Created by grass on 11/8/15.
 */
public class StartupLoader {

    public static final String SUFFIX = "Startup";

    public static void loadStartupClasses(Context context) {
        try {
            ArrayList e = getPackageClasses(context);
            Iterator i$ = e.iterator();

            while (i$.hasNext()) {
                Enumeration iter = (Enumeration) i$.next();

                while (iter.hasMoreElements()) {
                    String className = (String) iter.nextElement();

                    try {
                        if (className.endsWith(SUFFIX)) {
//                            Log.i("grass", "classname: " + className);
                            Class.forName(className);
                        }
                    } catch (Throwable var6) {
                        ;
                    }
                }
            }
        } catch (Throwable var7) {
            ;
        }

    }

    public static ArrayList<Enumeration<String>> getPackageClasses(Context context) {
        try {
            ArrayList e = getDexFile(context.getClassLoader());
            ArrayList taskLists = new ArrayList();
            Iterator i$ = e.iterator();

            while (i$.hasNext()) {
                DexFile df = (DexFile) i$.next();
                if (df != null) {
                    taskLists.add(df.entries());
                }
            }

            return taskLists;
        } catch (IllegalAccessException var5) {
            var5.printStackTrace();
        } catch (NoSuchFieldException var6) {
            var6.printStackTrace();
        }

        return null;
    }


    private static ArrayList<DexFile> getDexFile(ClassLoader loader) throws NoSuchFieldException, IllegalAccessException {
        ArrayList files = new ArrayList();
        Field pathListField = findField(loader, "pathList");
        Object dexPathList = pathListField.get(loader);
        Field jlrField = findField(dexPathList, "dexElements");
        Object[] original = (Object[]) ((Object[]) jlrField.get(dexPathList));
        for (int i = 0; i < original.length; ++i) {
            Field dexFileField = findField(original[i], "dexFile");
            DexFile dexFile = (DexFile) dexFileField.get(original[i]);
            files.add(dexFile);
        }

        return files;
    }

    private static Field findField(Object instance, String name) throws NoSuchFieldException {
        Class clazz = instance.getClass();

        while (clazz != null) {
            try {
                Field e = clazz.getDeclaredField(name);
                if (!e.isAccessible()) {
                    e.setAccessible(true);
                }

                return e;
            } catch (NoSuchFieldException var4) {
                clazz = clazz.getSuperclass();
            }
        }

        throw new NoSuchFieldException("Field " + name + " not found in " + instance.getClass());
    }
}
