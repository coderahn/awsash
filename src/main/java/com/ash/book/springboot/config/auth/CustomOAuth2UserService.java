package com.ash.book.springboot.config.auth;

import com.ash.book.springboot.config.auth.dto.OAuthAttributes;
import com.ash.book.springboot.config.auth.dto.SessionUser;
import com.ash.book.springboot.domain.user.User;
import com.ash.book.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("CustomOAuth2UserService.loadUser");
        System.out.println("CustomOAuth2UserService.userRequest:" + userRequest);

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        System.out.println("CustomOAuth2UserService.oAuth2User:" + oAuth2User);


        String registrationId = userRequest.getClientRegistration().getRegistrationId(); //현재 로그인중인 서비스를 구분하는 코드. 지금은 구글만 사용하는 불필요값이지만, 이후 네이버로그인 등 연동시에 구분하기위한 값
        String userNameAttributeName = userRequest.getClientRegistration()
                                        .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); //OAuth2 로그인 진행시 키가 되는 필드값.(PK)

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes()); //OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스.

        User user = saveOrUpdate(attributes);

        httpSession.setAttribute("user", new SessionUser(user)); //세션에 사용자 정보를 저장하기 위한 DTO클래스(USER 클래스 쓰지 않고 새로 만들어쓰는지는..?)

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
