package com.lew.jlight.core.util;

import net.sf.cglib.beans.BeanMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 与Spring中的BeanUtils功能等同
 *
 * @author eleven
 */
public abstract class BeanUtil {

    private final static Logger logger = LoggerFactory.getLogger(BeanUtil.class);

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            if (((String) obj).trim().length() == 0) {
                return true;
            }
        } else if (obj instanceof Collection) {
            if (((Collection) obj).isEmpty()) {
                return true;
            }
        } else if (obj.getClass().isArray()) {
            if (((Object[]) obj).length == 0) {
                return true;
            }
        } else if (obj instanceof Map) {
            if (((Map) obj).isEmpty()) {
                return true;
            }
        }else {
            return false;
        }
        return false;
    }

    public static boolean isNumber(Object obj) {

        if (obj == null) {
            return false;
        }
        if (obj instanceof Number) {
            return true;
        }
        if (obj instanceof String) {
            try {
                Double.parseDouble((String) obj);
                return true;
            } catch (NumberFormatException ex) {
                logger.debug(ex.getMessage());
                return false;
            }
        }
        return false;
    }

    public static boolean isInherit(Class currentClass, Class classParent) {
        return classParent.isAssignableFrom(currentClass);
    }

    private static List<Field> getAllDeclareFields(Class<?> cls) {
        List<Field> list = new ArrayList<>();
        Collections.addAll(list, cls.getDeclaredFields());
        return list;
    }


    static List<String> getAllFieldNames(Class<?> cls) {
        List<String> list = new ArrayList<>();
        List<Field> fields = getAllDeclareFields(cls);
        list.addAll(fields.stream().map(Field::getName).collect(Collectors.toList()));
        return list;
    }

    private static Map getBeanMap(Object object) {
        Class<?> beanClass = ClassUtil.getTargetClass(object);
        BeanMap.Generator gen = new BeanMap.Generator();
        gen.setBean(object);
        gen.setBeanClass(beanClass);
        return gen.create();
    }

    public static Map toMap(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Map) {
            return (Map) value;
        }
        if (value instanceof String) {
            String text = (String) value;
            text = text.trim();
            int suffixIndex = text.length() - 1;
            if (suffixIndex > 0 && text.charAt(0) == '{' && text.charAt(suffixIndex) == '}') {
                return JsonUtil.parseMap(text);
            }
        }
        return BeanUtil.getBeanMap(value);
    }
}
