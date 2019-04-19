package com.dreamf.lambda;


import com.dreamf.lambda.annotation.Lambda;
import com.dreamf.lambda.annotation.LambdaInject;
import com.dreamf.lambda.funcationfactory.FuncationFactory;
import com.dreamf.lambda.funcationfactory.impl.FuncationFactoryImpl;
import com.dreamf.lambda.service.User;
import com.dreamf.lambda.springbootfactory.SpringBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;


public class LambdaSpringBootStarterApplication {

    private Logger logger = LoggerFactory.getLogger(LambdaSpringBootStarterApplication.class);


    private List<String> classPath = new ArrayList<>();

    private FuncationFactory<String, Object> funcationFactory = new FuncationFactoryImpl();

    /**
     * 维护一个版本顺序
     */
    private Map<String, List<Integer>> map = new HashMap<>();

    /**
     * 临时存放函数
     */
    private Map<String, Object> funcation = new HashMap<>();


    @Autowired
    private SpringBeanFactory springBeanFactory;


    /**
     * 将需可能会初始化的类加载路径加入List
     *
     * @param file
     * @param resource
     */
    public void initClass(File file, String resource) {
        if (file.isFile()) {
            String classNmae = file.getName();
            if (classNmae.substring(classNmae.lastIndexOf("."), classNmae.length()).equals(".class")) {
                String clas = file.getPath().
                        replace(resource, "").
                        replaceAll("\\\\", ".").
                        replaceAll("/", ".").
                        replace(".class", "");
                clas = clas.substring(clas.indexOf(".") + 1);
                logger.info(clas + "加载类");
                classPath.add(clas);
            }
        } else {
            File[] files = file.listFiles();
            if (files.length > 0) {
                Arrays.stream(files).forEach(o -> {
                    initClass(o, resource);
                });
            }
            return;
        }

    }


    public void start() {
        init();
        Inject();
    }

    public void loaderFuncation() {
        logger.info("调用loaderFuncation方法");
        map.keySet().forEach(key -> {
            List<Integer> list = map.get(key);
            if (list == null) {
                list = new ArrayList<>();
            }
            Collections.sort(list);
            if (funcationFactory.getFuncationList(key) == null) {
                funcationFactory.getFuncationsMap().put(key, new ArrayList<>());
            }
            list.stream().forEach(o -> {
                if (o - 1 > funcationFactory.getFuncationList(key).size()) {
                    int size = funcationFactory.getFuncationList(key).size();
                    for (int i = 0; i < o - 1 - size; i++) {
                        funcationFactory.getFuncationList(key).add(null);
                    }
                }
                funcationFactory.setFuncation(key, funcation.get(key + "." + o), o);
                logger.info("注入：\""+key+"\"版本号为\""+o+"\"函数到容器");
            });
        });
        logger.info("调用loaderFuncation方法完成");
    }


    /**
     * 加载函数实例注入临时容器
     */
    public void init() {
        logger.info("调用 init方法");
        URL url = LambdaSpringBootStarterApplication.class.getClassLoader().getResource("");
        File file = new File(url.getFile());
        initClass(file, file.getPath());
        classPath.forEach(o -> {
            Class cl = classLoader(o);
            Field[] fields = cl.getDeclaredFields();
            Class finalCl = cl;
            Arrays.stream(fields).forEach(field -> {
                if (field.isAnnotationPresent(Lambda.class)) {
                    Object object = null;
                    try {
                        object = finalCl.newInstance();
                        //注入spring的依赖
                        Object finalObject = object;
                        Arrays.stream(finalCl.getDeclaredFields()).forEach(mapper -> {
                            if (mapper.isAnnotationPresent(Autowired.class)) {
                                mapper.setAccessible(true);
                                try {
                                    mapper.set(finalObject, springBeanFactory.getBean(mapper.getType()));
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            } else if (mapper.isAnnotationPresent(Resource.class)) {
                                Resource resource = mapper.getAnnotation(Resource.class);
                                try {
                                    mapper.set(finalObject, springBeanFactory.getBean(resource.name()));
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                            mapper.setAccessible(false);
                        });
                        Lambda lambda = field.getAnnotation(Lambda.class);
                        field.setAccessible(true);
                        if (lambda.name().equals("")) {
                            List<Integer> list = map.get(field.getName());
                            if (list == null) {
                                list = new ArrayList<>();
                                map.put(field.getName(), list);
                            }
                            list.add(lambda.version());
                        } else {
                            List<Integer> list = map.get(lambda.name());
                            if (list == null) {
                                list = new ArrayList<>();
                                map.put(lambda.name(), list);
                            }
                            list.add(lambda.version());
                        }
                        //将函数临时存放到map
                        funcation.put(lambda.name() + "." + lambda.version(), field.get(object));
                        field.setAccessible(false);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
        loaderFuncation();
        logger.info("调用 init方法完成");
    }


    /**
     * 依赖注入的实际调用
     * @param field
     * @param name
     * @param cl
     * @param lambdaInject
     */
    public void beanInject(Field field, String name, Class cl, LambdaInject lambdaInject) {
        field.setAccessible(true);
        Object bean = null;
        if (name.equals("")) {
            bean = springBeanFactory.getBean(cl);
        } else {
            bean = springBeanFactory.getBean(name);
        }
        if (lambdaInject.version() > 0) {
            try {
                field.set(bean, funcationFactory.getFuncationVersion(lambdaInject.name(), lambdaInject.version()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            try {
                field.set(bean, funcationFactory.getFuncation(lambdaInject.name()));
                Class c=bean.getClass();
                logger.info(("注入函数:\""+lambdaInject.name()+"\"到\""+c.getPackage().toString().substring(8)+"."+c.getName()+"\"中"));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        field.setAccessible(false);
    }


    /**
     * 依赖注入
     */
    public void Inject() {
        logger.info("调用 Inject方法");
        classPath.forEach(o -> {
            Class cl = classLoader(o);
            Field[] fields = cl.getDeclaredFields();
            Arrays.stream(fields).forEach(field -> {
                if (field.isAnnotationPresent(LambdaInject.class)) {
                    LambdaInject lambdaInject = field.getAnnotation(LambdaInject.class);
                    if (cl.isAnnotationPresent(Service.class)) {
                        Service component = (Service) cl.getAnnotation(Service.class);
                        beanInject(field, component.value(), cl, lambdaInject);
                    } else if (cl.isAnnotationPresent(Controller.class)) {
                        Controller component = (Controller) cl.getAnnotation(Controller.class);
                        beanInject(field, component.value(), cl, lambdaInject);
                    } else if (cl.isAnnotationPresent(Component.class)) {
                        Component component = (Component) cl.getAnnotation(Component.class);
                        beanInject(field, component.value(), cl, lambdaInject);
                    } else if (cl.isAnnotationPresent(Repository.class)) {
                        Repository component = (Repository) cl.getAnnotation(Repository.class);
                        beanInject(field, component.value(), cl, lambdaInject);
                    } else {
                        throw new RuntimeException(o + " not Inject Spring ioc");
                    }
                }
            });
        });
        logger.info("调用 Inject方法完成");
    }


    public Class classLoader(String o) {
        Class cl = null;
        try {
            cl = Class.forName(o);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cl;
    }


}
