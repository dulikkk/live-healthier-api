package dulikkk.livehealthierapi.domain.user.dto;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class NewUserCommand {

    String username;

    String email;

    String password;

    NewUserInfoCommand newUserInfoCommand;

}
