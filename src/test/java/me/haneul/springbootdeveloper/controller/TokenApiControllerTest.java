//package me.haneul.springbootdeveloper.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import me.haneul.springbootdeveloper.config.jwt.JwtFactory;
//import me.haneul.springbootdeveloper.config.jwt.JwtProperties;
//import me.haneul.springbootdeveloper.domain.RefreshToken;
//import me.haneul.springbootdeveloper.domain.User;
//import me.haneul.springbootdeveloper.dto.CreateAccessTokenRequest;
//import me.haneul.springbootdeveloper.repository.RefreshTokenRepository;
//import me.haneul.springbootdeveloper.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.Map;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class TokenApiControllerTest {
//    @Autowired
//    protected MockMvc mockMvc;
//    @Autowired
//    protected ObjectMapper objectMapper;
//    @Autowired
//    private WebApplicationContext context;
//    @Autowired
//    JwtProperties jwtProperties;
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    RefreshTokenRepository refreshTokenRepository;
//
//    User user;
//
//    @BeforeEach
//    public void mockMvcSetUp() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
//                .build();
//        userRepository.deleteAll();
//    }
//
//    @BeforeEach
//    void setSecurityContext() {
//        userRepository.deleteAll();
//        user = userRepository.save(User.builder()
//                .email("user@gmail.com")
//                .password("test")
//                .build());
//
//        SecurityContext context = SecurityContextHolder.getContext();
//        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
//    }
//
//    @DisplayName("createNewAccessToken: 새로운 액세스 토큰을 발급한다.")
//    @Test
//    public void createNewAccessToken() throws Exception {
//        //given(테스트 유저 생성, 리프래시 토큰 만들어 데이터베이스에 저장, 토큰 생성 API의 요청 본문에 리프레시 토큰 포함 요청 객체 생성)
//        final String url = "/api/token";
//
////        User testUser = userRepository.save(User.builder()
////                .email("user@gmail.com")
////                .password("test")
////                .build());
////
////        String refreshToken = JwtFactory.builder()
////                .claims(Map.of("id", testUser.getId()))
////                .build()
////                .createToken(jwtProperties);
//
//        String refreshToken = createRefreshToken();
//
////        refreshTokenRepository.save(new RefreshToken(testUser.getId(), refreshToken));
//        refreshTokenRepository.save(new RefreshToken(user.getId(), refreshToken));
//
//        CreateAccessTokenRequest request = new CreateAccessTokenRequest();
//        request.setRefreshToken(refreshToken);
//        final String requestBody = objectMapper.writeValueAsString(request);
//
//        //when(토큰 추가 API에 요청 보냄. 요청 타임: JSON, given절에서 만들어둔 객체를 요청 본문으로 보냄)
//        ResultActions resultActions = mockMvc.perform(post(url)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(requestBody));
//
//        //then(응답 코드가 201 Created인지 확인. 응답으로 온 엑세스 토큰이 비어 있지 않은지 확인)
//        resultActions
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.accessToken").isNotEmpty());
//    }
//
//    @DisplayName("deleteRefreshToken: 리프레시 토큰을 삭제한다.")
//    @Test
//    public void deleteRefreshToken() throws Exception {
//        // given
//        final String url = "/api/refresh-token";
//
//        String refreshToken = createRefreshToken();
//
//        refreshTokenRepository.save(new RefreshToken(user.getId(), refreshToken));
//
//        SecurityContext context = SecurityContextHolder.getContext();
//        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, refreshToken, user.getAuthorities()));
//
//        // when
//        ResultActions resultActions = mockMvc.perform(delete(url)
//                .contentType(MediaType.APPLICATION_JSON_VALUE));
//
//        // then
//        resultActions
//                .andExpect(status().isOk());
//
//        assertThat(refreshTokenRepository.findByRefreshToken(refreshToken)).isEmpty();
//    }
//
//
//    private String createRefreshToken() {
//        return JwtFactory.builder()
//                .claims(Map.of("id", user.getId()))
//                .build()
//                .createToken(jwtProperties);
//    }
//
//}
