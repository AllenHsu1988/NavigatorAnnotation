package github.allenhsu.navigatorannotation.processor;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import github.allenhsu.navigaotrannotation.annotation.NewIntent;
import github.allenhsu.navigatorannotation.processor.Method.MSetDebuggable;

/**
 * Created by Allen on 2017/8/16.
 */
@AutoService(Processor.class)
public class NewIntentProcessor extends AbstractProcessor {

    private static final String METHOD_PREFIX = "start";

    private Filer filer;
    private Messager messager;
    private Elements elements;
    private Map<String, String> activitiesWithPackage;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
        elements = processingEnvironment.getElementUtils();
        activitiesWithPackage = new HashMap<>();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        try {
            /**
             * 1- Find all annotated element
             */
            for (Element element : roundEnvironment.getElementsAnnotatedWith(NewIntent.class)) {

                if (element.getKind() != ElementKind.CLASS) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "Can be applied to class.");
                    return true;
                }

//                TypeElement typeElement = (TypeElement) element;
                NewIntent ano = element.getAnnotation(NewIntent.class);

                activitiesWithPackage.put(
                        element.getSimpleName().toString(),
                        elements.getPackageOf(element).getQualifiedName().toString());
            }


            /**
             * 2- Generate a class
             */
            TypeSpec.Builder navigatorClass = TypeSpec
                    .classBuilder("Navigator")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

            for (Map.Entry<String, String> element : activitiesWithPackage.entrySet()) {
                String activityName = element.getKey();
                String packageName = element.getValue();
                ClassName activityClass = ClassName.get(packageName, activityName);
                FieldSpec ps = FieldSpec.builder(Class.class, activityName.toUpperCase())
                        .addModifiers(Modifier.PUBLIC)
                        .addModifiers(Modifier.FINAL)
                        .addModifiers(Modifier.STATIC)
                        .initializer(activityClass + ".class")
                        .build();

                navigatorClass.addField(ps);
            }

            /**
             * add setDebuggable method
             */
            navigatorClass.addMethod(new MSetDebuggable(navigatorClass).genMethod());

            MethodSpec intentMethod = MethodSpec
                    .methodBuilder(METHOD_PREFIX)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addParameter(ProcessorConst.C_CONTEXT, "context", Modifier.FINAL)
                    .addParameter(Class.class, "activity", Modifier.FINAL)
                    .addCode("$T.d(\"Tanno\",\"get activity intent: \" + activity);\n", ProcessorConst.C_LOG)
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
                    .endControlFlow()
                    .build();
            navigatorClass.addMethod(intentMethod);

            /**
             * 3- Write generated class to a file
             */
            JavaFile.builder("github.allenhsu.navigatorannotation.processor", navigatorClass.build()).build().writeTo(filer);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return ImmutableSet.of(NewIntent.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
