package dulikkk.livehealthierapi.it

import dulikkk.livehealthierapi.adapter.incoming.api.ApiEndpoint
import dulikkk.livehealthierapi.adapter.incoming.api.ApiResponse
import dulikkk.livehealthierapi.adapter.security.SecurityConstant
import dulikkk.livehealthierapi.adapter.security.authentication.AuthenticationRequest
import dulikkk.livehealthierapi.domain.user.dto.command.NewUserCommand
import dulikkk.livehealthierapi.domain.user.dto.command.NewUserInfoCommand
import dulikkk.livehealthierapi.domain.user.dto.SexDto
import dulikkk.livehealthierapi.domain.user.dto.UserDto
import dulikkk.livehealthierapi.domain.user.dto.UserRoleDto
import dulikkk.livehealthierapi.domain.user.port.outgoing.Encoder
import dulikkk.livehealthierapi.domain.user.port.outgoing.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod



class SecurityIT extends AbstractIT {

    String username = "dulikkk"

    String password = "S3cr3t_P455w0rd"

    @Autowired
    Encoder encoder

    @Autowired
    UserRepository userRepository

    private HttpEntity<AuthenticationRequest> goodAuthenticationRequest = new HttpEntity<>(
            new AuthenticationRequest(username, password), headers)

    private NewUserInfoCommand newUserInfoCommand = NewUserInfoCommand.builder()
            .sex(SexDto.MALE)
            .birthdate(2000)
            .heightInCm(190)
            .weightInKg(58)
            .build()

    def setup() {
        UserDto userDto = UserDto.builder()
                .username(username)
                .email("Kub4k1@gmail.com")
                .password(encoder.encode(password))
                .active(true)
                .roles(Set.of(UserRoleDto.USER))
                .build()

        userRepository.saveUser(userDto)
    }

    def "sign up with good parameters"() {
        given: "new user command and sign up request"
        NewUserCommand goodNewUserCommand = new NewUserCommand("dulikkkkk", "kubek1444@gmail.com", "S3cr3t_P455w0rd", newUserInfoCommand)
        HttpEntity<NewUserCommand> goodSignUpRequest = new HttpEntity<>(goodNewUserCommand, headers)

        when: "trying to register with good parameters"
        def result = testRestTemplate.postForEntity(baseUrl + ApiEndpoint.SIGN_UP, goodSignUpRequest, ApiResponse.class)

        then: "the system should return 201"
        result.getStatusCodeValue() == 201
    }

    def "sign up with bad parameters"() {
        given: "bad new user command and sign up request"
        NewUserCommand badNewUserCommand = new NewUserCommand("ok", "Kub4k1gmail.com", "Secret_Password", newUserInfoCommand)
        HttpEntity<NewUserCommand> badSignUpRequest = new HttpEntity<>(badNewUserCommand, headers)

        when: "trying to register with bad parameters"
        def result = testRestTemplate.postForEntity(baseUrl + ApiEndpoint.SIGN_UP, badSignUpRequest, ApiResponse.class)

        then: "the system should return 400"
        result.getStatusCodeValue() == 400
    }


    def "authenticate user"() {
        when: "trying to authenticate user with good credentials"
        def result = testRestTemplate.postForEntity(baseUrl + ApiEndpoint.SIGN_IN, goodAuthenticationRequest, String.class)

        then: "the system should return 200 with an access token and refresh token"
        result.getStatusCodeValue() == 200

        String cookies = result.getHeaders().get("Set-Cookie")
        cookies.contains(SecurityConstant.REFRESH_TOKEN_NAME.getConstant())
        cookies.contains(SecurityConstant.ACCESS_TOKEN_NAME.getConstant())
    }

    def "authenticate user with bad credentials"() {
        given: "bad authentication request"
        HttpEntity<AuthenticationRequest> badAuthenticationRequest = new HttpEntity<>(
                new AuthenticationRequest("badUsername", "badPassword"), headers)

        when: "trying to authenticate user with bad credentials"
        def result = testRestTemplate.postForEntity(baseUrl + ApiEndpoint.SIGN_IN, badAuthenticationRequest, String.class)

        then: "the system should not authenticate and return 401 status"
        result.getStatusCodeValue() == 401

        result.getHeaders().get("Set-Cookie") == null
    }

    def "refresh access and refresh token"() {
        given: "authenticated user, access token and refresh token"
        testRestTemplate.postForEntity(baseUrl + ApiEndpoint.SIGN_IN, goodAuthenticationRequest, String.class)

        when: "trying to refresh access and refresh token"
        def result = testRestTemplate.exchange(baseUrl + ApiEndpoint.REFRESH_TOKENS,
                HttpMethod.GET, new HttpEntity<Object>(headers), String.class)

        then: "the system should return 200 with new access and refresh token"
        result.getStatusCodeValue() == 200

        String cookies = result.getHeaders().get("Set-Cookie")
        cookies.contains(SecurityConstant.REFRESH_TOKEN_NAME.getConstant())
        cookies.contains(SecurityConstant.ACCESS_TOKEN_NAME.getConstant())
    }

    def "refresh access and refresh token without refresh token"() {
        when: "trying to refresh access and refresh token without refresh token"
        def result = testRestTemplate.exchange(baseUrl + ApiEndpoint.REFRESH_TOKENS,
                HttpMethod.GET, new HttpEntity<Object>(headers), String.class)

        then: "system should return 401 status"
        result.getStatusCodeValue() == 401
    }

    def "logout"() {
        given: "logged in user"
        testRestTemplate.postForEntity(baseUrl + ApiEndpoint.SIGN_IN, goodAuthenticationRequest, String.class)

        when: "trying to log out"
        def result = testRestTemplate.postForEntity(baseUrl + ApiEndpoint.LOGOUT, new HttpEntity<Object>(headers),
                String.class)

        then: "the system should return 200"
        result.getStatusCodeValue() == 200

    }

}
