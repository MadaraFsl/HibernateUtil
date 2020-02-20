package net.gvsun;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HibernateUtil {

    /**************************************************************************
     * @Description 通用将Hibernate转换为对应VO类方法
     * 使用时注意：VO类中属性必须和对象sql查询结果集顺序一致，且类型必须对应。
     * @author 付世亮
     * @param objects 需要转换的list
     * @param T 将要转换的vo对象类型(xxxVo.class)
     **************************************************************************/

    public static List transferObjectsToList(List<Object[]> objects, Class T) {
        List list = new ArrayList<>();
        for (Object[] os : objects) {
            Object o = null;
            try {
                o = T.newInstance();
                Field[] fields = T.getDeclaredFields();
                for (int j = 0; j < objects.size(); j++) {
                    if (os[j] != null) {
                        fields[j].setAccessible(true);
                        try {
                            // 区分数据库中datetime类型 如有其他类型特殊处理，可后续添加
                            if (os[j] instanceof Timestamp) {
                                Timestamp timestamp = (Timestamp) os[j];
                                os[j] = timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            }
                            if (os[j] instanceof Boolean) {
                                Boolean aBoolean = (Boolean) os[j];
                                os[j] = aBoolean ? "1" : "0";
                            }
                            fields[j].set(o, os[j]);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            list.add(o);
        }
        return list;
    }
}
