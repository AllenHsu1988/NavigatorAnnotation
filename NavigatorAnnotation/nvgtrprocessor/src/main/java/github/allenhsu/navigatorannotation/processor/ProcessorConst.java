package github.allenhsu.navigatorannotation.processor;

import com.squareup.javapoet.ClassName;

/**
 * Created by Allen on 2017/8/18.
 */

public class ProcessorConst {
    public static final ClassName C_INTENT = ClassName.get("android.content", "Intent");
    public static final ClassName C_CONTEXT = ClassName.get("android.content", "Context");
    public static final ClassName C_LOG = ClassName.get("android.util", "Log");
    public static final ClassName C_ACTIVITY = ClassName.get("android.app", "Activity");
}
