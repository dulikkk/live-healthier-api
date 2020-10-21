package dulikkk.livehealthierapi.it

import dulikkk.livehealthierapi.adapter.incoming.api.ApiEndpoint
import dulikkk.livehealthierapi.adapter.incoming.api.ApiResponse
import dulikkk.livehealthierapi.adapter.security.authentication.AuthenticatedUserInfo
import dulikkk.livehealthierapi.adapter.security.authentication.AuthenticationRequest
import dulikkk.livehealthierapi.domain.statistics.query.StatisticsQueryRepository
import dulikkk.livehealthierapi.domain.user.dto.SexDto
import dulikkk.livehealthierapi.domain.user.dto.UserDto
import dulikkk.livehealthierapi.domain.user.dto.command.NewUserCommand
import dulikkk.livehealthierapi.domain.user.dto.command.NewUserInfoCommand
import dulikkk.livehealthierapi.domain.user.dto.command.UpdateHeightCommand
import dulikkk.livehealthierapi.domain.user.dto.command.UpdateWeightCommand
import dulikkk.livehealthierapi.domain.user.port.outgoing.UserRepository
import dulikkk.livehealthierapi.domain.user.query.UserQueryRepository
import dulikkk.livehealthierapi.infrastructure.user.mongoDb.UserDocument
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.HttpEntity

import static org.springframework.data.mongodb.core.query.Criteria.where

class UserIT extends AbstractIT {

    String userId

    @Autowired
    UserQueryRepository userQueryRepository

    @Autowired
    UserRepository userRepository

    @Autowired
    StatisticsQueryRepository statisticsQueryRepository

    NewUserInfoCommand newUserInfoCommand = NewUserInfoCommand.builder()
            .sex(SexDto.MALE)
            .birthdate(2000)
            .heightInCm(200)
            .weightInKg(58)
            .build()

    NewUserCommand goodNewUserCommand = new NewUserCommand("dulikkkkk", "kubek1444@gmail.com", "S3cr3t_P455w0rd", newUserInfoCommand)

    def setup() {
        // register user
        HttpEntity<NewUserCommand> goodSignUpRequest = new HttpEntity<>(goodNewUserCommand, headers)
        testRestTemplate.postForEntity(baseUrl + ApiEndpoint.SIGN_UP, goodSignUpRequest, ApiResponse.class)

        // activate
        UserDocument userDocumentToActivate = mongoTemplate.findOne(new Query(where("username")
                .is(goodNewUserCommand.getUsername())), UserDocument.class)
        userId = userDocumentToActivate.getId()
        UserDto userDto = UserDto.builder()
                .id(userDocumentToActivate.getId())
                .username(userDocumentToActivate.getUsername())
                .email(userDocumentToActivate.getEmail())
                .password(userDocumentToActivate.getPassword())
                .active(true)
                .roles(userDocumentToActivate.getRoles())
                .userInfoDto(userDocumentToActivate.getInfo())
                .build()
        userRepository.activateUser(userDto)

        // log in
        HttpEntity<AuthenticationRequest> goodAuthenticationRequest = new HttpEntity<>(
                new AuthenticationRequest(goodNewUserCommand.getUsername(), goodNewUserCommand.getPassword()), headers)
        userId = testRestTemplate.postForEntity(baseUrl + ApiEndpoint.SIGN_IN, goodAuthenticationRequest,
                AuthenticatedUserInfo.class).getBody().getId()
    }

    def cleanup() {
        Query removeQuery = new Query(where("username").is(goodNewUserCommand.getUsername()))
        mongoTemplate.remove(removeQuery, UserDocument.class)
    }

    def "update height"() {
        given: "update height command"
        UpdateHeightCommand updateHeightCommand = new UpdateHeightCommand(userId, 160)

        when: "trying update height"
        def result = testRestTemplate.patchForObject(baseUrl + ApiEndpoint.UPDATE_HEIGHT,
                updateHeightCommand, ApiResponse.class)

        then: "the system should update bmi, statistics and return 200 status"
        userQueryRepository.findById(userId).get().getUserInfoDto().getHeightInCm() == updateHeightCommand.getNewHeightInCm()
        statisticsQueryRepository.getStatisticsByUserId(userId)
                .get()
                .getHeightStatisticsDto()
                .getLastHeightInCm() == updateHeightCommand.getNewHeightInCm()

        result.getStatus() == 200
    }

    def "update weight"() {
        given: "update height command"
        UpdateWeightCommand updateWeightCommand = new UpdateWeightCommand(userId, 80)

        when: "trying update height"
        def result = testRestTemplate.patchForObject(baseUrl + ApiEndpoint.UPDATE_HEIGHT,
                updateWeightCommand, ApiResponse.class)

        then: "the system should update bmi, statistics and return 200 status"
        userQueryRepository.findById(userId).get().getUserInfoDto().getWeightInKg() == updateWeightCommand.getNewWeightInKg()
        statisticsQueryRepository.getStatisticsByUserId(userId)
                .get()
                .getWeightStatisticsDto()
                .getLastWeightInKg() == updateWeightCommand.getNewWeightInKg()

        result.getStatus() == 200
    }
}
