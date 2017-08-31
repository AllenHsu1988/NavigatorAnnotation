package github.allenhsu.navigatorannotation.string;

import android.app.Activity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import github.allenhsu.navigaotrannotation.annotation.StringInit;

/**
 * Created by Allen on 2017/8/31.
 */

public class StringInitHelper {

    public static void init(final Activity target) {
        try {
            Class<?> clazz = target.getClass();
            Field[] fields = clazz.getDeclaredFields();
            StringInit byId;
            String name;
            String initStr;

            for (Field field : fields){
                if(field.getType() == String.class){
                    Annotation[] annotations = field.getAnnotations();
                    for(Annotation annotation:annotations) {
                        if (annotation instanceof StringInit) {
                            byId = field.getAnnotation(StringInit.class);
                            field.setAccessible(true);
                            name = field.getName();
                            initStr = byId.value();

                            //if the field is static, give arg1 null
                            field.set(target, initStr);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
