package org.dvare.util;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassUtils {
    private static final int ACCESS_TEST = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE;

    public static Field getDeclaredField(Class<?> cls, String fieldName, boolean forceAccess) {
        try {
            Field field = cls.getDeclaredField(fieldName);
            if (!isAccessible(field)) {
                if (!forceAccess) {
                    return null;
                }
                field.setAccessible(true);
            }
            return field;
        } catch (NoSuchFieldException var4) {
            return null;
        }
    }

    public static Object readField(Field field, Object target, boolean forceAccess) throws IllegalAccessException {
        if (forceAccess && !field.canAccess(target)) {
            field.setAccessible(true);
        } else {
            setAccessibleWorkaround(field, target);
        }

        return field.get(target);
    }

    public static void setAccessibleWorkaround(AccessibleObject o, Object target) {
        if (o != null && !o.canAccess(target)) {
            Member m = (Member) o;
            if (!o.canAccess(target) && Modifier.isPublic(m.getModifiers()) && isPackageAccess(m.getDeclaringClass().getModifiers())) {
                try {
                    o.setAccessible(true);
                } catch (SecurityException ignored) {
                }
            }

        }
    }

    public static void writeField(Field field, Object target, Object value, boolean forceAccess) throws IllegalAccessException {

        if (forceAccess && !field.canAccess(target)) {
            field.setAccessible(true);
        } else {
            setAccessibleWorkaround(field, target);
        }
        field.set(target, value);
    }

    public static Object readStaticField(Class<?> cls, String fieldName, boolean forceAccess) throws IllegalAccessException {
        Field field = getDeclaredField(cls, fieldName, forceAccess);
        return readField(field, null, false);
    }

    public static Field[] getAllFields(final Class<?> cls) {
        final List<Field> allFieldsList = getAllFieldsList(cls);
        return allFieldsList.toArray(new Field[0]);
    }

    public static List<Field> getAllFieldsList(final Class<?> cls) {
        final List<Field> allFields = new ArrayList<>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            Collections.addAll(allFields, declaredFields);
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }

    public static Method getAccessibleMethod(final Class<?> cls, final String methodName, final Class<?>... parameterTypes) {
        try {
            return getAccessibleMethod(cls.getMethod(methodName, parameterTypes));
        } catch (final NoSuchMethodException e) {
            return null;
        }
    }

    public static Method getAccessibleMethod(Method method) {
        if (!isAccessible(method)) {
            return null;
        }

        final Class<?> cls = method.getDeclaringClass();
        if (Modifier.isPublic(cls.getModifiers())) {
            return method;
        }
        return method;
    }

    public static boolean isAccessible(final Member m) {
        return m != null && Modifier.isPublic(m.getModifiers()) && !m.isSynthetic();
    }

    public static boolean isPackageAccess(int modifiers) {
        return (modifiers & ACCESS_TEST) == 0;
    }

}
