package com.learning.mltds.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtUtil {
    // token配置
    private static final long EXIRE_TIME = 15 * 60 * 1000;
    private static final String TOKEN_SECRET = "f26e587c28064d0e855e72c0a6a0618";

    // 密码配置
    private final static String[] hexDigits= {"0", "1", "2", "3", "4",
            "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};


    // 生成token
    public String sign(String username, String userId){
        try {
            Date date = new Date(System.currentTimeMillis() + EXIRE_TIME);

            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            Map<String, Object> header = new HashMap<>(2);
            header.put("typ", "JWT");
            header.put("alg", "HS256");
            return JWT.create().withHeader(header)
                    .withClaim("loginName", username)
                    .withClaim("userId", userId)
                    .withExpiresAt(date)
                    .sign(algorithm);
        }
        catch (Exception e){
            return  null;
        }
    }

    /** * 把inputString加密     */
    public String generatePassword(String inputString){
        return encodeByMD5(inputString);
    }

    /**
     * 验证输入的密码是否正确
     * @param password    加密后的密码
     * @param inputString    输入的字符串
     * @return    验证结果，TRUE:正确 FALSE:错误
     */
    public boolean validatePassword(String password, String inputString) {
        if (password.equals(encodeByMD5(inputString))) {
            return true;
        } else {
            return false;
        }
    }

    //  对字符串进行MD5加密
    private String encodeByMD5(String originString){
        if(originString != null){
            try{
                // 创建具有指定算法名称的信息摘要
                MessageDigest md = MessageDigest.getInstance("MD5");
                // 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                byte [] results = md.digest(originString.getBytes());
                String resultString = byteArrayToHexString(results);
                return resultString.toUpperCase();
            }
            catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }


    /**
     * 转换字节数组为十六进制字符串
     * @return    十六进制字符串
     */
    private String byteArrayToHexString(byte[] b){
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++){
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }


    /** 将一个字节转化成十六进制形式的字符串     */
    private String byteToHexString(byte b){
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
}