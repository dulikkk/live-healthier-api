package dulikkk.livehealthierapi.it

import dulikkk.livehealthierapi.adapter.incoming.api.ApiEndpoint
import dulikkk.livehealthierapi.adapter.incoming.api.ApiResponse
import dulikkk.livehealthierapi.adapter.security.authentication.AuthenticatedUserInfo
import dulikkk.livehealthierapi.adapter.security.authentication.AuthenticationRequest
import dulikkk.livehealthierapi.domain.plan.dto.PlanDto
import dulikkk.livehealthierapi.domain.plan.dto.TrainingDto
import dulikkk.livehealthierapi.domain.plan.dto.command.ChangeTrainingCommand
import dulikkk.livehealthierapi.domain.plan.query.PlanQueryRepository
import dulikkk.livehealthierapi.domain.user.dto.SexDto
import dulikkk.livehealthierapi.domain.user.dto.UserDto
import dulikkk.livehealthierapi.domain.user.dto.command.NewUserCommand
import dulikkk.livehealthierapi.domain.user.dto.command.NewUserInfoCommand
import dulikkk.livehealthierapi.domain.user.port.outgoing.UserRepository
import dulikkk.livehealthierapi.infrastructure.user.mongoDb.UserDocument
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.HttpEntity

import static java.time.DayOfWeek.*
import static org.springframework.data.mongodb.core.query.Criteria.where

class PlanIT extends AbstractIT {

    String userId

    @Autowired
    UserRepository userRepository

    @Autowired
    PlanQueryRepository planQueryRepository

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

    def "change training"() {
        given: "change training command in tuesday and current tuesday training"
        PlanDto planDto = planQueryRepository.getPlanByUserId(userId).get()
        TrainingDto currentTuesdayTraining = planDto.getTuesday()
        ChangeTrainingCommand changeTrainingCommand = new ChangeTrainingCommand(userId, TUESDAY,
                planDto.getTuesday().getTrainingDifficultyDto())

        when: "trying to change training to different"
        def result = testRestTemplate.patchForObject(baseUrl + ApiEndpoint.CHANGE_TRAINING + "?id=" + userId,
                changeTrainingCommand, ApiResponse.class)

        then: "the system should return new training, update training in repo and 200 status"
        result.getStatus() == 200
        def content = result.getContent()
        content.get("description") != planDto.getTuesday().getDescription()
        currentTuesdayTraining.getDescription() != content.get("description")
    }

    def "get user plan"(){
        when: "trying to retrieve user plan by his id"
        PlanDto planDto = testRestTemplate.getForObject(baseUrl + ApiEndpoint.GET_USER_PLAN + "?id=" + userId, PlanDto)

        then: "the system should return plan"
        planDto != null
    }
}
