package github.allenhsu.navigatorannotation.processor;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
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
import github.allenhsu.navigatorannotation.processor.field.FDebuggable;
import github.allenhsu.navigatorannotation.processor.internal.MDebugLog;
import github.allenhsu.navigatorannotation.processor.method.MSetDebuggable;
import github.allenhsu.navigatorannotation.processor.method.MStartActivity;

/**
 * Created by Allen on 2017/8/16.
 */
@AutoService(Processor.class)
public class NewIntentProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;
    private Elements elements;
    private Map<String, String> activitiesWithPackage;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();//Returns the filer used to create new source, class, or auxiliary files
        messager = processingEnvironment.getMessager();//Returns the messager used to report errors, warnings, and other notices.
        elements = processingEnvironment.getElementUtils();//Returns an implementation of some utility methods for operating on elements
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
             * Generate DebugLog class
             */
            TypeSpec.Builder debugLogClass = TypeSpec
                    .classBuilder(ProcessorConst.T_DEBUG_LOG)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

            debugLogClass.addMethod(new MDebugLog(debugLogClass).gen());
            debugLogClass.addField(new FDebuggable().gen());

            /**
             * Generate Navigator class
             */
            TypeSpec.Builder navigatorClass = TypeSpec
                    .classBuilder(ProcessorConst.T_NAVIGATOR)
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
            navigatorClass.addMethod(new MSetDebuggable(navigatorClass).gen());
            navigatorClass.addMethod(new MStartActivity(navigatorClass).gen());

            /**
             * 3- Write generated class to a file
             */
            JavaFile.builder(ProcessorConst.PACKAGE_NAME, navigatorClass.build())
                    .addFileComment("This codes are generated automatically. Do not modify!")
                    .build().writeTo(filer);

            JavaFile.builder(ProcessorConst.PACKAGE_NAME, debugLogClass.build())
                    .addFileComment("This codes are generated automatically. Do not modify!")
                    .build().writeTo(filer);


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
