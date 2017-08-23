package github.allenhsu.navigatorannotation.processor.field;

import com.squareup.javapoet.FieldSpec;

import javax.lang.model.element.Modifier;

import github.allenhsu.navigatorannotation.processor.ProcessorConst;
import github.allenhsu.navigatorannotation.processor.base.IProcessor;

/**
 * Created by Allen on 2017/8/21.
 */

public class FDebuggable implements IProcessor<FieldSpec>{

    @Override
    public FieldSpec gen(){
        FieldSpec ps = FieldSpec.builder(boolean.class, ProcessorConst.F_DEBUGGABLE)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .initializer("false")
                .build();
        return ps;
    }
}
