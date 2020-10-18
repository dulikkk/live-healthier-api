package dulikkk.livehealthierapi.domain.user;


import dulikkk.livehealthierapi.domain.user.dto.NewUserCommand;
import dulikkk.livehealthierapi.domain.user.dto.NewUserInfoCommand;
import dulikkk.livehealthierapi.domain.user.dto.exception.UserException;
import dulikkk.livehealthierapi.domain.user.query.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

import java.time.Year;
import java.util.regex.Pattern;

@RequiredArgsConstructor
class UserValidator {

    private final UserQueryRepository userQueryRepository;
    private final EmailValidator emailValidator = EmailValidator.getInstance();
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9]).{10,}$");

    public void validateNewUser(NewUserCommand newUserCommand) {
        validateFields(newUserCommand);

        checkIfThisUserExists(newUserCommand);

        validateUserInfo(newUserCommand.getNewUserInfoCommand());
    }

    public void validateUserInfo(NewUserInfoCommand newUserInfoCommand) {
        if (!isTheUserOlderThan5AndUnder100(newUserInfoCommand.getBirthdate())) {
            throw new UserException("Wiek użytkownika nie może być mniejszy od 5 lat i większy od 100");
        }

        if (newUserInfoCommand.getWeightInKg() <= 10 || newUserInfoCommand.getWeightInKg() >= 300) {
            throw new UserException("Czy to aby napewno twoja waga?");
        }

        if (newUserInfoCommand.getHeightInCm() <= 70 || newUserInfoCommand.getHeightInCm() >= 230) {
            throw new UserException("Czy to aby napewno twój wzrost?");
        }
    }

    private void validateFields(NewUserCommand newUserCommand) {

        if (StringUtils.isEmpty(newUserCommand.getUsername())) {
            throw new UserException("Nazwa użytkownika nie może być pusta");
        }

        if (!emailValidator.isValid(newUserCommand.getEmail())) {
            throw new UserException("Niepoprawny email");
        }

        if (StringUtils.isEmpty(newUserCommand.getPassword())) {
            throw new UserException("Hasło nie może być puste");
        }

        if (!PASSWORD_PATTERN.matcher(newUserCommand.getPassword()).matches()) {
            throw new UserException("Hasło musi zawierać przynajmniej 10 znaków i 1 liczbę");
        }
    }

    private void checkIfThisUserExists(NewUserCommand newUserCommand) {
        userQueryRepository.findByEmail(newUserCommand.getEmail())
                .ifPresent(fetchedUser -> {
                    throw new UserException("Użytkownik z emailem " + newUserCommand.getEmail() + " już istnieje");
                });
        userQueryRepository.findByUsername(newUserCommand.getUsername())
                .ifPresent(fetchedUser -> {
                    throw new UserException(
                            "Nazwa użytkownika " + newUserCommand.getUsername() + " jest już zajęta");
                });
    }

    private boolean isTheUserOlderThan5AndUnder100(int birthdate){
        return Year.now().getValue() - 100 <= birthdate || Year.now().getValue() - 5 >= birthdate;
    }
}
