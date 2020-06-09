package com;

import com.constants.UserConstants;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

public class Test {
    public static void main(String []args){

        String password = "admin";
        String encodedPassword = new SimpleHash("md5",password, UserConstants.salt,UserConstants.HASH_ITERATIONS).toString();

        System.out.printf("原始密码是 %s , 盐是： %s, 运算次数是： %d, 运算出来的密文是：%s ",password,UserConstants.salt,3,encodedPassword);
    }
}
