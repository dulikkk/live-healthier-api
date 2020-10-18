package dulikkk.livehealthierapi.adapter.outgoing.user.encoder;

import dulikkk.livehealthierapi.domain.user.port.outgoing.Encoder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class BCryptEncoder implements Encoder {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encode(String textToEncode) {
        return passwordEncoder.encode(textToEncode);
    }
}
