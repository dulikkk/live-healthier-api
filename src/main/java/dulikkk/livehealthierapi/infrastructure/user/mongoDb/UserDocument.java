package dulikkk.livehealthierapi.infrastructure.user.mongoDb;

import dulikkk.livehealthierapi.domain.user.dto.UserInfoDto;
import dulikkk.livehealthierapi.domain.user.dto.UserRoleDto;
import dulikkk.livehealthierapi.infrastructure.plan.mongoDb.PlanDocument;
import dulikkk.livehealthierapi.infrastructure.statistics.mongoDb.StatisticsDocument;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Getter
@Builder
@Document("user")
public class UserDocument {

    @Id
    private String id;

    private final String username;

    private final String email;

    private final String password;

    private final Set<UserRoleDto> roles;

    private final boolean active;

    private final String activationToken;

    private final String refreshToken;

    private final UserInfoDto info;

    private final PlanDocument planDocument;

    private final StatisticsDocument statisticsDocument;

}
