//package me.haneul.springbootdeveloper.config;
//
//import lombok.RequiredArgsConstructor;
//import me.haneul.springbootdeveloper.service.UserDetailService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class WebSecurityConfig {
//    private final UserDetailService userService;
//
//    //스프링 시큐리티 기능 비활성화
//    @Bean
//    public WebSecurityCustomizer configure() {
//        return (web) -> web.ignoring()
//                .requestMatchers(toH2Console())  //h2 데이터를 확인하는데 사용하는 h2-console 하위 url 대상 인증, 인가 비활성화
//                .requestMatchers(new AntPathRequestMatcher("/img/**"))  // 정적 리소스(img 폴더) 인증, 인가 비활성화
//                .requestMatchers(new AntPathRequestMatcher("/static/**"));  //정적 리소스 인증, 인가 비활성화
//    }
//
//    //특정 HTTP 요청에 대한 웹 기반 보안 구성 - 인증/인가 및 로그인, 로그아웃 관련 설정
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http
//                .authorizeRequests(auth -> auth  //인증, 인가 설정
//                        .requestMatchers(  //특정 요청과 일치하는 url에 대한 액세스 설정
//                                new AntPathRequestMatcher("/login"),
//                                new AntPathRequestMatcher("/signup"),
//                                new AntPathRequestMatcher("/user")
//                        ).permitAll()  //누구나 접근이 가능하게 설정(즉, /login, /signup, /user로 오는 요청은 인증/인가 필요없음)
//                        //anyRequest(): 위에서 설정한 url 이외의 요청에 대해 설정,
//                        //authenticated(): 별도의 인가는 필요하지 않지만 인증이 성공된 상태여야 접근 가능
//                        .anyRequest().authenticated())
//                .formLogin(formLogin -> formLogin  //폼 기반 로그인 설정
//                        .loginPage("/login")  //로그인 페이지 경로 설정
//                        .defaultSuccessUrl("/articles")  //로그인이 완료되었을 때 이동할 경로 설정
//                )
//                .logout(logout -> logout  //로그아웃 설정
//                        .logoutSuccessUrl("/login")  //로그아웃이 완료되었을 때 이동할 경로 설정
//                        .invalidateHttpSession(true)  //로그아웃 이후에 세션을 전체 삭제할지 여부 설정
//                )
//                .csrf(AbstractHttpConfigurer::disable)  //csrf 비활성화
//                .build();
//    }
//
//    //인증 관리자 관련 설정 - 사용자 정보 가져올 서비스 재정의, 인증 방법 등 설정
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http,
//                                                       BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService)
//        throws Exception {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        /*userDetailsService(): 사용자 정보를 가져올 서비스 설정.
//                                이때 설정하는 서비스 클래스는 반드시 UserDetailsService를 상속받은 클래스여야함.*/
//        authProvider.setUserDetailsService(userService);  //사용자 정보 서비스 설정
//        authProvider.setPasswordEncoder(bCryptPasswordEncoder);  //passwordEncoder(): 비밀번호를 암호화하기 위한 인코더 설정
//        return new ProviderManager(authProvider);
//    }
//
//    //패스워드 인코더로 사용할 빈 등록
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
