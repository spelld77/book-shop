package com.shop.portshop.oauth2;

//import lombok.Builder;
//import lombok.Getter;
//
//import java.util.Map;
//
//@Getter
//public class OAuthAttributes {
//    private Map<String, Object> attributes; // OAuth2 반환하는 유저 정보 Map
//    private String nameAttributeKey;
//    private String name;
//    private String email;
//    private String id;
//
//    @Builder
//    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String id) {
//        this.attributes = attributes;
//        this.nameAttributeKey = nameAttributeKey;
//        this.name = name;
//        this.email = email;
//        this.id = id;
//    }
//    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
//        // kakao
////        if("kakao".equals(registrationId)){
////            return ofKakao("id", attributes);
////        }
//        // naver
//        if("naver".equals(registrationId)){
//            return ofNaver("id", attributes);
//        }
//        return null;
//        // google
////        return ofGoogle(userNameAttributeName, attributes);
//    }
//
//    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
//        // 유저정보
//        Map<String, Object> response = (Map<String, Object>)attributes.get("response");
//        // kakao_account안에 또 profile이라는 JSON객체가 있다. (nickname, profile_image)
////        Map<String, Object> profile = (Map<String, Object>)response.get("id");
//
//        return OAuthAttributes.builder()
//                .id((String) response.get("id"))
//                .name((String) response.get("name"))
//                .email((String) response.get("email"))
//                .attributes(attributes)
//                .nameAttributeKey(userNameAttributeName)
//                .build();
//    }
//
//}


public class OAuthAttributes{}