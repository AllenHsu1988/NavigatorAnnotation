package github.allenhsu.navigatorannotation.processor.method.base;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

/**
 * Created by Allen on 2017/8/18.
 */

public abstract class MethodBase{

    abstract protected String getMethodName();
    abstract protected void init(TypeSpec.Builder c_builder, MethodSpec.Builder m_builder);

    TypeSpec.Builder cBuilder;

    public MethodBase(TypeSpec.Builder c_builder){
        cBuilder = c_builder;
    }

    public MethodSpec genMethod(){
        return genMethodBuilder().build();
    }

    public MethodSpec.Builder genMethodBuilder(){
        MethodSpec.Builder m_builder = MethodSpec.methodBuilder(getMethodName());
        init(cBuilder, m_builder);
        return m_builder;
    }
}
