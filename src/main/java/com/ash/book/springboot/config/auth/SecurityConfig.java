package com.ash.book.springboot.config.auth;

import com.ash.book.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity  //스프링 시큐리티 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    private final Environment env;
//    @Autowired
//    public SecurityConfig(Environment env) {
//        this.env = env;
//        System.out.println("env!!!:" + env.getProperty("spring.profiles.include"));
//        System.out.println("env!!!:" + env.getProperty("spring.security.oauth2.client.registration.google.client-id"));
//
//    }

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("SecurityConfig.configure");
        http
                .csrf().disable()
                .headers().frameOptions().disable() //h2-console 화면을 사용하기 위해 해당 옵션들 disable
                .and()
                    .authorizeRequests() //URL별 권한 관리를 설정하는 옵션의 시작점
                    .antMatchers("/", "/css/**", "/images/**",
                        "/js/**", "/h2-console/**").permitAll()
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                    .anyRequest().authenticated() //위의 설정된 값 이외의 나머지URL.authenticated 사용하여 나머지 URL들은 모두 인증된 사용자들에게만 허용
                .and()
                    .logout()
                    .logoutSuccessUrl("/") //로그아웃 기능에 대한 여러 설정의 진입점. 로그아웃시 "/" 주소로 이동
                .and()
                .oauth2Login() //OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당
                .userInfoEndpoint() //OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당
                .userService(customOAuth2UserService); //소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록
    }
}
