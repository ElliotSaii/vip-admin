package com.techguy.code;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.symmetric.RC4;

import java.util.Random;

public class OnlyCodeUtils {
    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };
    public static String creatUUID(){
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = IdUtil.simpleUUID();
        System.out.println(uuid);
        uuid=uuid.replace("-", "");
        System.out.println(uuid);
        for (int i = 0; i < 6; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 62]);
        }
        return shortBuffer.toString();
    }

    public static void main(String[] args) {



        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        long id = snowflake.nextId();
        System.out.println(id);
        String code = OnlyCodeUtils.creatUUID();
        System.err.println(code);


    }


}
