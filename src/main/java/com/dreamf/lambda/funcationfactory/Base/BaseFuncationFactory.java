package com.dreamf.lambda.funcationfactory.Base;

import com.dreamf.lambda.funcationfactory.FuncationFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 函数工厂
 */
public abstract class BaseFuncationFactory implements FuncationFactory<String,Object> {


    protected BaseFuncationFactory(){
        map = new HashMap<>();
    }

    /**
     * 函数容器
     */
    private Map<String,List<Object>> map;

    /**
     * 获取最新函数
     * @param s
     * @return
     */
    @Override
    public Object getFuncation(String s) throws RuntimeException{
        List<Object> list = map.get(s);
        return list.get(list.size()-1);
    }

    /**
     * 获取指定版本的函数
     * @param s
     * @param version
     * @return
     */
    @Override
    public Object getFuncationVersion(String s, int version) {
        List<Object> list = map.get(s);
        if(version-1>list.size()||version<1){
            throw new RuntimeException("没有对应版本号的函数");
        }
        return list.get(version-1);
    }

    /**
     * 存储携带版本信息的函数
     * @param s
     * @param o
     * @param version
     */
    @Override
    public void setFuncation(String s, Object o, int version) {
        List<Object> list = map.get(s);
        if (list == null) {
            list = new ArrayList<>();
            map.put(s, list);
        }
        if(version<=list.size()){
            throw new RuntimeException("版本插入不正确");
        }
        if (list.size()!=version-1 &&list.get(version - 1) != null) {
            throw new RuntimeException("The function name is \"" + s + "\", version=\"" + version + "\" already exists");
        }
        list.add(version - 1, o);
    }

    /**
     * 获取所有函数集列表
     * @return
     */
    @Override
    public List<List<Object>> getFuncations() {
        List<List<Object>> list = new ArrayList<>();
        map.entrySet().forEach(o->list.add(o.getValue()));
        return list;
    }

    /**
     * 获取指定的函数列表
     * @return
     */
    @Override
    public List<Object> getFuncationList(String s) {
        return map.get(s);
    }

    /**
     * 获取函数容器
     * @return
     */
    @Override
    public Map<String, List<Object>> getFuncationsMap() {
        return map;
    }

}
