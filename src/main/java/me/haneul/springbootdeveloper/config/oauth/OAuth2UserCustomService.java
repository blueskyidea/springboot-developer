package me.haneul.springbootdeveloper.config.oauth;

import lombok.RequiredArgsConstructor;
import me.haneul.springbootdeveloper.domain.User;
import me.haneul.springbootdeveloper.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {
    private final Logger logger = LoggerFactory.getLogger(OAuth2UserCustomService.class);
    private final UserRepository userRepository;

    /* loadUser(): DefaultOAuth2UserService에서 제공하는 OAuth 서비스에서 제공하는 정보를 기반으로 유저 객체를 만들어주는 메서드
    * loadUser()를 통해 사용자 객체를 불러오는데 여기에는 식별자, 이름, 이메일, 프로필 사진 링크 등의 정보를 담고있음. */
    //리소스 서버에서 보내주는 사용자 정보를 불러오는 메서드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 공급자 ID 123

        // 각 공급자에 따라 다르게 처리
        OAuth2User user = super.loadUser(userRequest);
        if ("kakao".equals(registrationId)) {
            // 카카오 사용자 정보 로그 출력
            logger.info("카카오 OAuth2 User Attributes: {}", user.getAttributes());
            // 카카오 사용자 정보 처리
            saveOrUpdate(user); // 카카오 사용자 저장/업데이트 로직
        } else if ("google".equals(registrationId)) {
            // 구글 사용자 정보 처리
            saveOrUpdate(user); // 구글 사용자 저장/업데이트 로직
        }

        return user;
    }

    //유저가 있으면 업데이트, 없으면 유저 생성
    private User saveOrUpdate(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 카카오의 경우 이메일은 kakao_account 객체 내부에 존재
        String email = null;
        String name = null;

        if (attributes.containsKey("kakao_account")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            email = (String) kakaoAccount.get("email");  //이메일 정보 가져오기

            Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
            if (properties != null) {
                name = (String) properties.get("nickname");  // 닉네임 정보 가져오기
            }
        } else {
            email = (String) attributes.get("email");  // Google, Facebook 등 다른 제공자의 경우
            name = (String) attributes.get("name");
        }

        // 이메일과 이름이 null인지 확인
        if (email == null || email.isEmpty()) {
            logger.error("이메일 정보가 없습니다.");
            throw new IllegalArgumentException("이메일 정보가 없습니다.");
        }

        // 이름이 null일 경우 기본값 설정
        if (name == null || name.isEmpty()) {
            name = "Unknown";  // 기본 이름 설정
        }

        logger.info("카카오 이메일: {}, 닉네임: {}", email, name); // 카카오 이메일과 닉네임 로그 출력

        // name 변수를 final처럼 사용하기 위해 미리 처리
        final String finalName = name;

//        String email = (String) attributes.get("email");
//        String name = (String) attributes.get("name");
        User user = userRepository.findByEmail(email)
                .map(entity -> entity.update(finalName))
                .orElse(User.builder()
                        .email(email)
                        .nickname(finalName)
                        .build());

        return userRepository.save(user);
    }
}
