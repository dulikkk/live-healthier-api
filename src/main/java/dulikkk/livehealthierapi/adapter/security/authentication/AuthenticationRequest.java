package dulikkk.livehealthierapi.adapter.security.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class AuthenticationRequest {

    private String username;

    private String password;
}
