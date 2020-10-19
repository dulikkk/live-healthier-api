package dulikkk.livehealthierapi.domain.user

import dulikkk.livehealthierapi.domain.user.dto.NewUserCommand
import dulikkk.livehealthierapi.domain.user.dto.NewUserInfoCommand
import dulikkk.livehealthierapi.domain.user.dto.SexDto
import dulikkk.livehealthierapi.domain.user.dto.exception.UserException
import dulikkk.livehealthierapi.domain.user.port.outgoing.ActivationTokenCreator
import dulikkk.livehealthierapi.domain.user.port.outgoing.Encoder
import dulikkk.livehealthierapi.domain.user.port.outgoing.TokenSender
import dulikkk.livehealthierapi.infrastructure.user.memory.InMemoryUserRepository
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime

class UserDomainTest extends Specification {


    private InMemoryUserRepository inMemoryUserRepository = new InMemoryUserRepository()

    private Encoder encoder = Mock(Encoder)

    private ActivationTokenCreator activationTokenCreator = Mock(ActivationTokenCreator)

    private TokenSender tokenSender = Mock(TokenSender)

    private UserDomainFacade userFacade = new UserDomainConfigurator().userDomainFacade(inMemoryUserRepository, encoder,
            activationTokenCreator, tokenSender)

    private NewUserInfoCommand badNewUserInfoCommand = NewUserInfoCommand.builder()
            .sex(SexDto.MALE)
            .birthdate(2100)
            .weightInKg(500)
            .heightInCm(2.5)
            .build()

    private NewUserInfoCommand newUserInfoCommand = NewUserInfoCommand.builder()
            .sex(SexDto.MALE)
            .birthdate(2000)
            .weightInKg(70)
            .heightInCm(181)
            .build()

    private NewUserCommand newUserCommand = new NewUserCommand("Kub4k1", "Kub4k1@gmail.com", "S3cr3t_P455w0rd", newUserInfoCommand)

    String activationToken = "abc123"

    def setup() {
        encoder.encode() >> ""
        activationTokenCreator.generateToken() >> activationToken
        activationTokenCreator.getExpirationDateFromToken(activationToken) >> LocalDateTime.now().plusHours(1)
    }

    def "add new user"() {
        when: "add new user"
        String userId = userFacade.addNewUser(newUserCommand)

        then: "the system should create new disabled user"
        inMemoryUserRepository.findById(userId).isPresent()
        !inMemoryUserRepository.findById(userId).get().active
    }

    def "add exists user"() {
        given: "added user"
        userFacade.addNewUser(newUserCommand)

        when: "trying register with the same username and email"
        userFacade.addNewUser(newUserCommand)

        then: "the system should throw exception"
        thrown(UserException)
    }

    @Unroll
    def "add user with bad email"() {
        given: "user with bad email"
        NewUserCommand badEmailNewUserCommand = new NewUserCommand("Kub4k1", email, "S3cr3t_P455w0rd", newUserInfoCommand)

        when: "trying to register with bad email"
        userFacade.addNewUser(badEmailNewUserCommand)


        then: "the system should throw an exception"
        def exception = thrown(expectedException)
        exception.message == expectedMessage

        where:
        email             || expectedException || expectedMessage
        null              || UserException     || "Niepoprawny email"
        ""                || UserException     || "Niepoprawny email"
        "kub4k1gmail.com" || UserException     || "Niepoprawny email"

    }

    @Unroll
    def "add the user with bad password"() {
        given: "user with bad password"
        NewUserCommand badPasswordNewUserCommand = new NewUserCommand("ark21", "Kub4k1@gmail.com", password, newUserInfoCommand)

        when: "trying to register with bad password"
        userFacade.addNewUser(badPasswordNewUserCommand)

        then: "the system should throw an exception"
        def exception = thrown(expectedException)
        exception.message == expectedMessage

        where:
        password        || expectedException || expectedMessage
        null            || UserException     || "Hasło nie może być puste"
        ""              || UserException     || "Hasło nie może być puste"
        "tooshort"      || UserException     || "Hasło musi zawierać przynajmniej 10 znaków i 1 liczbę"
        "withoutnumber" || UserException     || "Hasło musi zawierać przynajmniej 10 znaków i 1 liczbę"

    }

    def "add user with bad infos"() {
        given: "user with bad infos"
        NewUserCommand newUserCommand = new NewUserCommand("Kub4k1", "Kub4k1@gmail.com", "S3cr3t_P455w0rd", badNewUserInfoCommand)

        when: "trying to register with bad user infos"
        userFacade.addNewUser(newUserCommand)

        then: "the system should throw an exception"
        thrown(UserException)
    }

    def "calculate BMI"() {
        given: "user 181cm and 70kg"

        when: "trying to register this user"
        String id = userFacade.addNewUser(newUserCommand)

        then: "calculated BMI should equals 21.37"
        inMemoryUserRepository.findById(id).get().getUserInfoDto().getBmi() == 21.37

    }

    def "update user infos"() {
        given: "registered user and his new weight"
        String userId = userFacade.addNewUser(newUserCommand)
        NewUserInfoCommand newUserInfoCommandUpdate = NewUserInfoCommand.builder()
                .sex(SexDto.MALE)
                .birthdate(2000)
                .weightInKg(73)
                .heightInCm(181)
                .build()

        when: "trying update user information"
        userFacade.updateUserInfo(userId, newUserInfoCommandUpdate)

        then: "the system should update user information"
        inMemoryUserRepository.findById(userId).get().getUserInfoDto().getUserWeightDto().getCurrentWeightInKg() == 73
    }

    def "activate user"() {
        given: "registered user and his token"
        String userId = userFacade.addNewUser(newUserCommand)

        when: "trying to activate account with good token"
        Thread.sleep(500) // sleep because token is sending in second thread
        userFacade.activateUser(activationToken)

        then: "the system should activate this user"
        inMemoryUserRepository.findById(userId).get().isActive()
    }

    def "activate user with bad token"() {
        given: "registered user and bad token"
        userFacade.addNewUser(newUserCommand)
        String badToken = "badtoken"

        when: "trying to activate account with bad token"
        userFacade.activateUser(badToken)

        then: "the system should throw an exception"
        thrown(UserException)
    }
}
