package org.spring.dev.company.config;

import lombok.RequiredArgsConstructor;
import org.spring.dev.company.entity.member.MemberEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) //특정 주소로 접근하면 권한 및 인증을 미리 체크 하겠다
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfigClass {


    /*private final AuthenticationFailureHandler   customAuthenticationFailureHandler;*/
    private final CustomAuthenticationHandler customAuthenticationFailureHandler;

    @Bean
    public MemberEntity memberEntity() {
        // Create and return an instance of FreelancerEntity
        return new MemberEntity();
    }


//    @Bean
//    public AuthenticationFailureHandler authenticationFailureHandler() {
//        return new SimpleUrlAuthenticationFailureHandler("/login/loginFail");
//    }

    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        // POST -> 웹페이지 보안 공격
        http.csrf().disable();

        // 사용자 요청에 대한 페이지별 설정
        // 이건 나중에 하겠음
        http.authorizeHttpRequests()
//                 로그인시
                .antMatchers("/member/logout","/chatbot/**").authenticated()
                // STAFF
                .antMatchers("/chat/**").hasAnyRole("STAFF")
                // FREELANCER
                .antMatchers("/schedule/**").hasAnyRole("FREELANCER")
        ;


        // 로그인 설정
        http.formLogin()
                .loginPage("/login/login")// 직접 로그인페이지를 설정, 권한이 없는 페이지 -> 자동이동
                .usernameParameter("email")  // username -> 아이디
                .passwordParameter("password")    // password -> 비밀번호
                .successHandler(
                        new AuthenticationSuccessHandler() {
                            @Override
                            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
                                MemberEntity memberEntity = myUserDetails.getMemberEntity();

                                if (Objects.equals(memberEntity.getPostcode(), "*")) {
                                    System.out.println("여긴가?");
                                    response.sendRedirect("/member/oauth2add");
                                } else {
                                    System.out.println("틀렸네...");
                                    response.sendRedirect("/member/m");
                                }
                            }
                        }
                )
                .loginProcessingUrl("/login/login")
                .defaultSuccessUrl("/home")
//                .failureHandler(customAuthenticationFailureHandler)
                .failureUrl("/")
                .permitAll()
                .and()
                .oauth2Login()
                .loginPage("/login/login")
                .successHandler(
                        new AuthenticationSuccessHandler() {
                            @Override
                            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
                                MemberEntity memberEntity = myUserDetails.getMemberEntity();

                                if (Objects.equals(memberEntity.getPostcode(), "*")) {
                                    System.out.println("여긴가?");
                                    response.sendRedirect("/member/oauth2add");
                                } else {
                                    System.out.println("틀렸네...");
                                    response.sendRedirect("/member/m");
                                }
                            }
                        }
                )
                .userInfoEndpoint()
                .userService(myOAuth2UserService());

        // 로그아웃 설정 -> logout(기본)
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout")) // 직접 로그아웃 URL
                .deleteCookies("JSESSIONID") // 로그아웃 시 JSESSIONID 제거
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessUrl("/")
        ;

        return http.build();
    }


//    public UserDetailsService userDetailsService(){
//        return userDetailsService();
//    }

    // OAuth2.0 SNS로그인 설정
    @Bean       // 요청, User
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> myOAuth2UserService() {
        return new MyOAuth2UserService();
    }


}

