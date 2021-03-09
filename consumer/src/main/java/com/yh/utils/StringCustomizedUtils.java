package com.yh.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

public class StringCustomizedUtils {

    public static final int[] INT_NUMBER_ARRAY = {0,1,2,3,4,5,6,7,8,9};

    public static final char[] NUMBER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * string 字符串去掉左右空格
     * @param str
     * @return
     */
    public static String stringTrim(String str){
        str = str.trim();
        //这里判断是不是全角空格
        while (str.startsWith("　")) {
            str = str.substring(1, str.length()).trim();
        }
        while (str.endsWith("　")) {
            str = str.substring(0, str.length() - 1).trim();
        }
        return str;
    }

    /**
     * 随机生成5-6位数字
     * @return
     */
    public static int autoNumber(){
        int[] array = INT_NUMBER_ARRAY;
        Random rand = new Random();
        for (int i = 10; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = array[index];
            array[index] = array[i - 1];
            array[i - 1] = tmp;
        }
        int result = 0;
        for(int i = 0; i < 6; i++){
            result = result * 10 + array[i];
        }
        return result;
    }
    /**
     *随机生成6位数字
     */
    public static int autosixNumber(){
        //10000 -5位 1000-4位
        return  (int)((Math.random()*9+1)*100000);
    }
    /**
     * 生成随机数字和字母
     * @param length
     * @return
     */
    public static String getStringRandom(int length) {
        String val = "";
        Random random = new Random();
        //length为几位密码
        for(int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
    /**
     * 0-9[6位]数字
     * @param postcode
     * @return
     */
    public static boolean checkValidatecode(String postcode) {
        String regex = "[0-9]\\d{5}";
        return Pattern.matches(regex, postcode);
    }
    /**
     * 产生指定长度的随机字符串，中文环境下是乱码
     * @param length
     * @return
     */
    public static String randomCode(int length){
        return RandomStringUtils.random(length);
    }

    /**
     * 使用指定的字符生成指定长度的随机字符串
     * @param length
     * @return
     */
    public static String randomChar(int length){
        return RandomStringUtils.random(length, NUMBER);
    }

    /**
     * 生成指定长度的随机大小写字母组合的字符串
     * @param length
     * @return
     */
    public static String randomAlphabetic(int length){
        return RandomStringUtils.randomAlphabetic(length);
    }

    /**
     * 生成指定长度的字母数字组合的字符串
     * @param length
     * @return
     */
    public static String randomAlphanumeric(int length){
        return RandomStringUtils.randomAlphanumeric(length);
    }

    /**
     * 生成随机数字字符串
     * @param length
     * @return
     */
    public static String randomNumeric(int length){
        return RandomStringUtils.randomNumeric(length);
    }

    /**
     * 生成从ASCII 32到126组成的随机字符串 （包括符号）
     * @return
     */
    public static String randomAscii(int length){
        return RandomStringUtils.randomAscii(length);
    }

    public static String uniqueCode() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMDDHHmmddSSS");
        String date = sdf.format(new Date());
        return date.concat("_").concat(randomAlphanumeric(9));
    }

    public static boolean isJson(String str) {
        boolean result = false;
        if (StringUtils.isNotBlank(str)) {
            str = str.trim();
            if (str.startsWith("{") && str.endsWith("}")) {
                result = true;
            } else if (str.startsWith("[") && str.endsWith("]")) {
                result = true;
            }
        }
        return result;
    }



}
