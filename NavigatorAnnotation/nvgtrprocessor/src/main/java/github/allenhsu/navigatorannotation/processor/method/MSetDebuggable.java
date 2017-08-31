package github.allenhsu.navigatorannotation.processor.method;


import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.lang.model.element.Modifier;

import github.allenhsu.navigatorannotation.processor.navigator.ProcessorConst;
import github.allenhsu.navigatorannotation.processor.method.base.MethodBase;

/**
 * Created by Allen on 2017/8/18.
 */

public class MSetDebuggable extends MethodBase {

    private static final String P_DEBUGGABLE = "is_debuggable";

    public MSetDebuggable(TypeSpec.Builder c_builder){
        super(c_builder);
    }

    @Override
    protected String getMethodName() {
        return ProcessorConst.M_SET_DEBUGGABLE;
    }

    @Override
    protected void init(TypeSpec.Builder c_builder, MethodSpec.Builder m_builder) {

        Set<Modifier> modifiers = new LinkedHashSet<>();
        modifiers.add(Modifier.PUBLIC);
        modifiers.add(Modifier.STATIC);

        Set<ParameterSpec> parameterSpecs = new LinkedHashSet<>();
        parameterSpecs.add(ParameterSpec.builder(boolean.class, P_DEBUGGABLE, Modifier.FINAL).build());

        m_builder.addModifiers(modifiers)
                .addParameters(parameterSpecs)
                .addStatement("DebugLog.$L = $L", ProcessorConst.F_DEBUGGABLE, P_DEBUGGABLE);
    }
}
