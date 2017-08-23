package github.allenhsu.navigatorannotation.processor.method.base;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import github.allenhsu.navigatorannotation.processor.base.IProcessor;

/**
 * Created by Allen on 2017/8/18.
 */

public abstract class MethodBase implements IProcessor<MethodSpec>{

    abstract protected String getMethodName();
    abstract protected void init(TypeSpec.Builder c_builder, MethodSpec.Builder m_builder);

    TypeSpec.Builder cBuilder;

    public MethodBase(TypeSpec.Builder c_builder){
        cBuilder = c_builder;
    }

    @Override
    public MethodSpec gen() {
        return genMethodBuilder().build();
    }

    public MethodSpec.Builder genMethodBuilder(){
        MethodSpec.Builder m_builder = MethodSpec.methodBuilder(getMethodName());
        init(cBuilder, m_builder);
        return m_builder;
    }
}
