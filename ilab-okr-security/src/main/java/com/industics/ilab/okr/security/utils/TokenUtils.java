package com.industics.ilab.okr.security.utils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class TokenUtils {
    //设置过期时间
    private static final long EXPIRE_DATE=24*720*60*1000;
    //token秘钥
    private static final String TOKEN_SECRET = "ZCBOOKBFKSYSTEM2021BQWE";

    public static String generateToken (String username,String password,int identity){
        String token = "";
        try {
            //过期时间
            Date date = new Date(System.currentTimeMillis()+EXPIRE_DATE);
            //秘钥及加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            //设置头部信息
            Map<String,Object> header = new HashMap<>();

            header.put("typ","JWT");
            header.put("alg","HS256");
            //携带username，password信息，生成签名
            token = JWT.create()
                    .withHeader(header)
                    .withClaim("username",username)
                    .withClaim("password",password).withExpiresAt(date)
                    .withClaim("identity",identity)
                    .sign(algorithm);
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
        return token;
    }

    public static boolean verify(String token){
        /**
         * @desc   验证token，通过返回true
         * @create 2021/7/12 10:39
         * @params [token]需要校验的串
         **/
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        }catch (Exception e){
            return  false;
        }
    }

    public static Map<String, Object> parseToken(String token){
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);
        String username=jwt.getClaim("username").asString();
        String password=jwt.getClaim("password").asString();
        int identity=jwt.getClaim("identity").asInt();
        Map<String,Object>map=new HashMap<String,Object>();
        map.put("username",username);
        map.put("password",password);
        map.put("identity",identity);
        return map;
    }

    public static String refresh(String token){
        Map<String,Object>map=parseToken(token);
        token=generateToken(map.get("username").toString(),
                map.get("password").toString(),
                Integer.parseInt(map.get("identity").toString()));
        return token;
    }

    public static boolean isValid(String strLink) {
        URL url;
        try {
            url = new URL(strLink);
            HttpURLConnection connt = (HttpURLConnection)url.openConnection();
            connt.setRequestMethod("HEAD");
            String strMessage = connt.getResponseMessage();
            if (strMessage.compareTo("Not Found") == 0) {
                return false;
            }
            connt.disconnect();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String username ="y384879210@qq.com";
        String password = "123456";
        String token = generateToken(username,password,2);
        System.out.println(token);
        boolean b = verify(token);
        System.out.println(b);
        Random random = new Random();

        System.out.println(isValid("https://img3.doubanio.com/lpic/s4724331.jpg"));
    }
}