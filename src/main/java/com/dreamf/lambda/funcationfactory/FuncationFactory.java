package com.dreamf.lambda.funcationfactory;

import java.util.List;
import java.util.Map;

public interface FuncationFactory<T,S> {

    /**
     * 获取最新函数
     * @param t
     * @return
     */
    S getFuncation(T t);

    /**
     * 获取指定版本的函数
     * @param t
     * @return
     */
    S getFuncationVersion(T t,int version);

    /**
     * 存储携带版本信息的函数
     * @param t
     * @param s
     */
    void setFuncation(T t, S s,int version);

    /**
     * 获取所有函数集列表
     * @return
     */
    List<List<Object>> getFuncations();

    /**
     * 获取指定的函数列表
     * @return
     */
    List<Object> getFuncationList(T t);

    /**
     * 获取函数容器
     * @return
     */
    Map<T,List<S>> getFuncationsMap();



}
