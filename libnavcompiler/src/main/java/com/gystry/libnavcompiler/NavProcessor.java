package com.gystry.libnavcompiler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.auto.service.AutoService;
import com.gystry.libnavannotation.ActivityDestination;
import com.gystry.libnavannotation.FragmentDestination;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"com.gystry.libnavannotation.ActivityDestination", "com.gystry.libnavannotation.FragmentDestination"})
public class NavProcessor extends AbstractProcessor {

    private Messager messager;
    private Filer filer;
    private static final String OUTPUT_FILE_NAME = "destnation.json";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        //打印日志信息
        messager = processingEnvironment.getMessager();
        //生成文件
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //roundEnvironment.getElementsAnnotatedWith(FragmentDestination.class);返回一个集合
        Set<? extends Element> fragmentElements = roundEnvironment.getElementsAnnotatedWith(FragmentDestination.class);
        Set<? extends Element> activityElements = roundEnvironment.getElementsAnnotatedWith(ActivityDestination.class);
        if (!fragmentElements.isEmpty() || !activityElements.isEmpty()) {
            HashMap<String, JSONObject> destMap = new HashMap<>();
            handleDestination(fragmentElements, FragmentDestination.class, destMap);
            handleDestination(activityElements, ActivityDestination.class, destMap);

            //要把文件创建在 app/src/main/assets目录下，但是又不能直接定位到这个目录下。先能定位到app目录下，然后在app目录下创建文件。
            FileObject filerResource = null;
            try {
                filerResource = filer.createResource(StandardLocation.CLASS_OUTPUT, "", OUTPUT_FILE_NAME);
                String resourcePath = filerResource.toUri().getPath();
                messager.printMessage(Diagnostic.Kind.NOTE, resourcePath + "--ceshishuchu--:" + resourcePath.indexOf("appkt"));
                String appPath = resourcePath.substring(0, resourcePath.indexOf("appkt") + 5);
                messager.printMessage(Diagnostic.Kind.NOTE, appPath + "--ceshishuchu--appPath--:" + resourcePath.indexOf("appkt"));
                String assetsPath = appPath + "/src/main/assets";

                File file = new File(assetsPath);
                if (!file.exists()) {
                    file.mkdirs();
                }

                File outPutFile = new File(file, OUTPUT_FILE_NAME);
                if (outPutFile.exists()) {
                    outPutFile.delete();
                }
                outPutFile.createNewFile();
                try(FileOutputStream fileOutputStream = new FileOutputStream(outPutFile);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");) {
                    String content = JSON.toJSONString(destMap);
                    outputStreamWriter.write(content);
                    outputStreamWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                messager.printMessage(Diagnostic.Kind.NOTE, "ceshishuchu---cuowu:" );
                e.printStackTrace();
            }
        }
        return true;
    }

    private void handleDestination(Set<? extends Element> elements, Class<? extends Annotation> annotationClaz, HashMap<String, JSONObject> destMap) {
        for (Element element : elements) {
            //因为注解是直接标记在类上边的，可以直接转为TypeElement
            TypeElement typeElement = (TypeElement) element;
            String pageUrl = null;
            String claszName = typeElement.getQualifiedName().toString();
            int id = Math.abs(claszName.hashCode());
            boolean needLogin = false;
            boolean asStater = false;
            boolean isFragment = false;

            Annotation annotation = typeElement.getAnnotation(annotationClaz);
            if (annotation instanceof FragmentDestination) {
                FragmentDestination dest = (FragmentDestination) annotation;
                pageUrl = dest.pageUrl();
                asStater = dest.asStarter();
                needLogin = dest.needLogin();
                isFragment = true;
            } else if (annotation instanceof ActivityDestination) {
                ActivityDestination dest = (ActivityDestination) annotation;
                pageUrl = dest.pageUrl();
                asStater = dest.asStarter();
                needLogin = dest.needLogin();
                isFragment = false;
            }

            if (destMap.containsKey(pageUrl)) {
                messager.printMessage(Diagnostic.Kind.ERROR, "不同的页面不允许使用相同的pageUrl");
            } else {
                JSONObject object = new JSONObject();
                object.put("id", id);
                object.put("asStater", asStater);
                object.put("needLogin", needLogin);
                object.put("isFragment", isFragment);
                object.put("claszName", claszName);
                object.put("pageUrl", pageUrl);
                destMap.put(pageUrl, object);
            }
        }
    }
}
