package com.shop.portshop.oauth2;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.ExecutionException;

@Component
public class NaverLoginBO {

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    @Value("${naver.redirect-uri}")
    private String redirectUri;

    @Value("${naver.user-info-uri}")
    private String profileApiUri;

    private String state = "oAuth_state";

    //랜덤 state 생성기
    public String generateState() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    // 네이버 인증 url생성
    public String getAuthorizationUrl(HttpSession session){

        state = generateState();
        session.setAttribute("state", state);

        OAuth20Service oAuth20Service = new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .callback(redirectUri)
                .build(NaverLoginApi.getInstance());

        return oAuth20Service.createAuthorizationUrlBuilder().state(state).build();

    }

    //네이버 콜백, 액세스 토큰 처리
    public OAuth2AccessToken getAccessToken(HttpSession session, String code, String state) throws IOException, ExecutionException, InterruptedException {

        //세션에 저장된 state값과 콜백으로 전달받은 값이 같은지 확인
        String sessionState = (String) session.getAttribute("state");
        if(sessionState.equals(state)){
            OAuth20Service oAuth20Service = new ServiceBuilder(clientId)
                    .apiSecret(clientSecret)
                    .callback(redirectUri)
                    .build(NaverLoginApi.getInstance());

            OAuth2AccessToken accessToken = oAuth20Service.getAccessToken(code);
            return accessToken;
        }
        return null;
    }

    // 네이버 사용자 프로필 API를 호출
    public String getUserProfile(OAuth2AccessToken oAuth2AccessToken) throws IOException, ExecutionException, InterruptedException{

        OAuth20Service oAuth20Service = new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .callback(redirectUri).build(NaverLoginApi.getInstance());
        OAuthRequest request = new OAuthRequest(Verb.GET, profileApiUri);
        oAuth20Service.signRequest(oAuth2AccessToken, request);
        try(Response response = oAuth20Service.execute(request)){
            return response.getBody();

        }
    }
}
