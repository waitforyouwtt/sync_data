package com.yh.thread;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：List工具类
 * @author songfayuan
 * 2018年7月22日下午2:23:22
 */
@Slf4j
public class ListUtils {

    /**
     * 描述：list集合深拷贝
     * @param src
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * @author songfayuan
     * 2018年7月22日下午2:35:23
     */
    public static <T> List<T> deepCopy(List<T> src) {
        try {
            ByteArrayOutputStream byteout = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteout);
            out.writeObject(src);
            ByteArrayInputStream bytein = new ByteArrayInputStream(byteout.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bytein);
            @SuppressWarnings("unchecked")
            List<T> dest = (List<T>) in.readObject();
            return dest;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 描述：对象深拷贝
     * @param src
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * @author songfayuan
     * 2018年12月14日
     */
    public static <T> T objDeepCopy(T src) {
        try {
            ByteArrayOutputStream byteout = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteout);
            out.writeObject(src);
            ByteArrayInputStream bytein = new ByteArrayInputStream(byteout.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bytein);
            @SuppressWarnings("unchecked")
            T dest = (T) in.readObject();
            return dest;
        } catch (ClassNotFoundException e) {
            log.error("errMsg = {}", e);
            return null;
        } catch (IOException e) {
            log.error("errMsg = {}", e);
            return null;
        }
    }

    /**
     * 将一个list均分成n个list,主要通过偏移量来实现的
     * @author songfayuan
     * 2018年12月14日
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int n) {
        List<List<T>> result = new ArrayList<List<T>>();
        int remaider = source.size() % n;  //(先计算出余数)
        int number = source.size() / n;  //然后是商
        int offset = 0;//偏移量
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remaider > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }

    /**
     * List按指定长度分割
     * @param list the list to return consecutive sublists of （需要分隔的list）
     * @param size the desired size of each sublist (the last may be smaller) （分隔的长度）
     * @author songfayuan
     * @date 2019-07-07 21:37
     */
    public static <T> List<List<T>> partition(List<T> list, int size){
        return  Lists.partition(list, size); // 使用guava
    }

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        List<Integer> bigList = new ArrayList<>();
        for (int i = 0; i < 101; i++){
            bigList.add(i);
        }
        log.info("bigList长度为：{}", bigList.size());
        log.info("bigList为：{}", bigList);
        List<List<Integer>> smallists = partition(bigList, 20);
        log.info("smallists长度为：{}", smallists.size());
        for (List<Integer> smallist : smallists) {
            log.info("拆分结果：{}，长度为：{}", smallist, smallist.size());
        }
    }
}
