package github.allenhsu.navigatorannotation.processor.internal;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.lang.model.element.Modifier;

import github.allenhsu.navigatorannotation.processor.ProcessorConst;
import github.allenhsu.navigatorannotation.processor.method.base.MethodBase;

/**
 * Created by Allen on 2017/8/18.
 */

public class MDebugLog extends MethodBase {

    private static final String P_TAG = "tag";
    private static final String P_MESSAGE = "msg";

    public MDebugLog(TypeSpec.Builder c_builder){
        super(c_builder);
    }

    @Override
    protected String getMethodName() {
        return ProcessorConst.M_LOG;
    }

    @Override
    protected void init(TypeSpec.Builder c_builder, MethodSpec.Builder m_builder) {

        Set<Modifier> modifiers = new LinkedHashSet<>();
        modifiers.add(Modifier.PUBLIC);
        modifiers.add(Modifier.STATIC);

        Set<ParameterSpec> parameterSpecs = new LinkedHashSet<>();
        parameterSpecs.add(ParameterSpec.builder(String.class, P_TAG, Modifier.FINAL).build());
        parameterSpecs.add(ParameterSpec.builder(String.class, P_MESSAGE, Modifier.FINAL).build());

        m_builder.addModifiers(modifiers)
                .addParameters(parameterSpecs)
                .beginControlFlow("if(" + ProcessorConst.F_DEBUGGABLE +")")
                .addStatement("$T.d($L, $L)", ProcessorConst.C_LOG, P_TAG, P_MESSAGE)
                .endControlFlow();
    }
}
