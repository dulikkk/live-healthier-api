package dulikkk.livehealthierapi.infrastructure.user.mongoDb;

import dulikkk.livehealthierapi.domain.user.dto.UserInfoDto;
import dulikkk.livehealthierapi.domain.user.dto.UserRoleDto;
import dulikkk.livehealthierapi.infrastructure.plan.mongoDb.PlanDocument;
import dulikkk.livehealthierapi.infrastructure.statistics.mongoDb.StatisticsDocument;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Builder
@Value
@Document("user")
public class UserDocument {

    @Id
    String id;

    String username;

    String email;

    String password;

    Set<UserRoleDto> roles;

    boolean active;

    String activationToken;

    String refreshToken;

    UserInfoDto info;

    PlanDocument plan;

    StatisticsDocument statistics;

}
