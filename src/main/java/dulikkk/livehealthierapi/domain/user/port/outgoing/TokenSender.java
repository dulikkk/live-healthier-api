package dulikkk.livehealthierapi.domain.user.port.outgoing;

public interface TokenSender {

    void sendToken(String token, String email);
}
