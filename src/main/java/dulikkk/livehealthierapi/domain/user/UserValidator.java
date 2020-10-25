package dulikkk.livehealthierapi.domain.user;


import dulikkk.livehealthierapi.domain.user.dto.command.NewUserCommand;
import dulikkk.livehealthierapi.domain.user.dto.command.NewUserInfoCommand;
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
        if (newUserInfoCommand == null) {
            throw new UserException("Proszę podać informację o użytkowniku");
        }
        if(!isTheUserOlderThan5AndUnder100(newUserInfoCommand.getBirthdate())){
            throw new UserException("Czy to aby na pewno twój wiek?");
        }
        validateHeightInCm(newUserInfoCommand.getHeightInCm());
        validateWeightInKg(newUserInfoCommand.getWeightInKg());
    }

    public void validateHeightInCm(double heightInCm) {
        if (heightInCm <= 60 || heightInCm >= 230) {
            throw new UserException("Czy to aby na pewno twój wzrost?");
        }
    }

    public void validateWeightInKg(double weightInKg) {
        if (weightInKg <= 20 || weightInKg >= 250) {
            throw new UserException("Czy to aby na pewno twój wzrost?");
        }
    }

    private void validateFields(NewUserCommand newUserCommand) {

        if (StringUtils.isEmpty(newUserCommand.getUsername())) {
            throw new UserException("Nazwa użytkownika nie może być pusta");
        }

        if(newUserCommand.getUsername().length() < 4 || newUserCommand.getUsername().length() > 15){
            throw new UserException("Nazwa użytkownika musie zawierać od 4 do 15 znaków");
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
                    throw new UserException("Istnieje już konto z takim emailem!");
                });
        userQueryRepository.findByUsername(newUserCommand.getUsername())
                .ifPresent(fetchedUser -> {
                    throw new UserException(
                            "Ta nazwa użytkownika jest zajęta");
                });
    }

    private boolean isTheUserOlderThan5AndUnder100(int birthdate) {
        return Year.now().getValue() - 100 <= birthdate || Year.now().getValue() - 5 >= birthdate;
    }
}
