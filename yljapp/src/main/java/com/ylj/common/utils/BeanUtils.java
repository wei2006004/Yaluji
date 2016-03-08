package com.ylj.common.utils;

import com.ylj.common.bean.Admin;
import com.ylj.common.bean.Staff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/7 0007.
 */
public class BeanUtils {
    public static List<Map<String,Object>> convertStaffs2Maps(List<Staff> list){
        List<Map<String,Object>> maps =new ArrayList();
        if(list==null)
            return maps;
        for (Staff staff : list) {
            maps.add(staff.convertToMap());
        }
        return maps;
    }

    public static List<Map<String,Object>> convertAdmins2Maps(List<Admin> list){
        List<Map<String,Object>> maps =new ArrayList();
        if(list==null)
            return maps;
        for (Admin admin : list) {
            maps.add(admin.convertToMap());
        }
        return maps;
    }

}
