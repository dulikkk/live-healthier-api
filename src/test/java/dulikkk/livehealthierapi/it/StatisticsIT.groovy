package dulikkk.livehealthierapi.it

import dulikkk.livehealthierapi.adapter.incoming.api.ApiEndpoint
import dulikkk.livehealthierapi.adapter.incoming.api.ApiResponse
import dulikkk.livehealthierapi.adapter.security.authentication.AuthenticatedUserInfo
import dulikkk.livehealthierapi.adapter.security.authentication.AuthenticationRequest
import dulikkk.livehealthierapi.domain.statistics.dto.StatisticsDto
import dulikkk.livehealthierapi.domain.statistics.dto.SuperChallengeStatisticsDto
import dulikkk.livehealthierapi.domain.statistics.dto.TrainingStatisticsDto
import dulikkk.livehealthierapi.domain.statistics.query.StatisticsQueryRepository
import dulikkk.livehealthierapi.domain.user.dto.SexDto
import dulikkk.livehealthierapi.domain.user.dto.UserDto
import dulikkk.livehealthierapi.domain.user.dto.command.NewUserCommand
import dulikkk.livehealthierapi.domain.user.dto.command.NewUserInfoCommand
import dulikkk.livehealthierapi.domain.user.port.outgoing.UserRepository
import dulikkk.livehealthierapi.infrastructure.user.mongoDb.UserDocument
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod

import static org.springframework.data.mongodb.core.query.Criteria.where

class StatisticsIT extends AbstractIT {

    String userId

    @Autowired
    UserRepository userRepository

    @Autowired
    StatisticsQueryRepository statisticsQueryRepository

    NewUserInfoCommand newUserInfoCommand = NewUserInfoCommand.builder()
            .sex(SexDto.MALE)
            .birthdate(2000)
            .heightInCm(190)
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

    def "done training"() {
        when: "trying to add done training to statistics"
        def result = testRestTemplate.exchange(baseUrl + ApiEndpoint.DONE_TRAINING + "?id=" + userId, HttpMethod.PATCH,
                null, ApiResponse.class)

        then: "system should update statistics and return 200"
        result.getStatusCodeValue() == 200

        StatisticsDto statisticsDto = statisticsQueryRepository.getStatisticsByUserId(userId).get()
        TrainingStatisticsDto trainingStatisticsDto = statisticsDto.getTrainingStatisticsDto()

        trainingStatisticsDto.getDoneAllTrainings() == 1
        trainingStatisticsDto.getDoneTrainingsThisMonth() == 1
        trainingStatisticsDto.getDoneTrainingsThisWeek() == 1
    }

    def "done super challenge"() {
        when: "trying to add done training to statistics"
        def result = testRestTemplate.exchange(baseUrl + ApiEndpoint.DONE_SUPER_CHALLENGE + "?id=" + userId, HttpMethod.PATCH,
                null, ApiResponse.class)

        then: "system should update statistics and return 200"
        result.getStatusCodeValue() == 200

        StatisticsDto statisticsDto = statisticsQueryRepository.getStatisticsByUserId(userId).get()
        SuperChallengeStatisticsDto superChallengeStatisticsDto = statisticsDto.getSuperChallengeStatisticsDto()

        superChallengeStatisticsDto.getDoneAllSuperChallenges() == 1
        superChallengeStatisticsDto.getDoneSuperChallengesThisMonth() == 1
        superChallengeStatisticsDto.getDoneSuperChallengesThisWeek() == 1
    }
}
