package com.efunds.log.util;

import java.util.UUID;

public class DataUtil {

    public static String randomRequestId(){
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }


}
