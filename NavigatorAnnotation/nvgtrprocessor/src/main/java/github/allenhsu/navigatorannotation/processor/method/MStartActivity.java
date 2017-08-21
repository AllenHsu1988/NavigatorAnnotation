package github.allenhsu.navigatorannotation.processor.method;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.lang.model.element.Modifier;

import github.allenhsu.navigatorannotation.processor.method.base.MethodBase;
import github.allenhsu.navigatorannotation.processor.ProcessorConst;

/**
 * Created by Allen on 2017/8/18.
 */

public class MStartActivity extends MethodBase {
    private static final String P_CONTEXT = "context";
    private static final String P_CLASS = "activity";

    public MStartActivity(TypeSpec.Builder c_builder){
        super(c_builder);
    }

    @Override
    protected String getMethodName() {
        return ProcessorConst.M_START_ACTIVITY;
    }

    @Override
    protected void init(TypeSpec.Builder c_builder, MethodSpec.Builder m_builder) {

        Set<Modifier> modifiers = new LinkedHashSet<>();
        modifiers.add(Modifier.PUBLIC);
        modifiers.add(Modifier.STATIC);

        Set<ParameterSpec> parameterSpecs = new LinkedHashSet<>();
        parameterSpecs.add(ParameterSpec.builder(ProcessorConst.C_CONTEXT, P_CONTEXT, Modifier.FINAL).build());
        parameterSpecs.add(ParameterSpec.builder(Class.class, P_CLASS, Modifier.FINAL).build());

        m_builder.addModifiers(modifiers)
                .addParameters(parameterSpecs)
                .addCode("$L.Log(\"Tanno\",\"get activity intent: \" + activity);\n", ProcessorConst.T_DEBUG_LOG)
                .beginControlFlow("if(context != null)")
                .addStatement("$T i = new $T($L, activity)", ProcessorConst.C_INTENT, ProcessorConst.C_INTENT, "context")
                .beginControlFlow("if(context instanceof $T)", ProcessorConst.C_ACTIVITY)
                .addStatement("i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)")
                .endControlFlow()
                .beginControlFlow("try")
                .addStatement("context.startActivity(i)")
                .endControlFlow()
                .beginControlFlow("catch(Exception e)")
                .addStatement("e.printStackTrace()")
                .endControlFlow()
                .endControlFlow();
    }
}
