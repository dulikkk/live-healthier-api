package dulikkk.livehealthierapi.infrastructure.mongoDb.util;

import dulikkk.livehealthierapi.domain.plan.dto.DifficultyLevelDto;
import dulikkk.livehealthierapi.infrastructure.plan.mongoDb.PlanDocument;
import dulikkk.livehealthierapi.infrastructure.statistics.mongoDb.StatisticsDocument;
import dulikkk.livehealthierapi.infrastructure.user.mongoDb.UserDocument;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static dulikkk.livehealthierapi.domain.plan.dto.TrainingTypeDto.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

public class MongoDbQueryAndUpdateUtil {

    // queries

    // user

    public Query userIdQuery(String userId) {
        return new Query(where("id").is(userId));
    }

    public Query emailQuery(String email) {
        return new Query(where("email").is(email));
    }

    public Query usernameQuery(String username) {
        return new Query(where("username").is(username));
    }

    public Query activationTokenQuery(String activationToken) {
        return new Query(where("activationToken").is(activationToken));
    }

    public Query refreshTokenQuery(String refreshToken) {
        return new Query(where("refreshToken").is(refreshToken));
    }

    // trainings

    public Query trainingDifficultyLevel(DifficultyLevelDto difficultyLevelDto) {
        return new Query(where("trainingDifficulty").is(difficultyLevelDto));
    }

    public Query trainingBreakDay() {
        return new Query(where("trainingType").is(BREAK));
    }

    public Query planQueryByUserId(String userId) {
        Query query = new Query(where("id").is(userId));
        query.fields().include("plan").exclude("id");
        return query;
    }

    // statistics

    public Query statisticsQueryByUserId(String userId) {
        Query query = new Query(where("id").is(userId));
//        query.fields().include("statistics").exclude("id");
        return query;
    }

    // updates

    // user

    public Update activateUserUpdate() {
        return new Update().set("active", true);
    }

    public Update deleteActivationTokenUpdate() {
        return new Update().unset("activationToken");
    }

    public Update setActivationTokenUpdate(String activationToken) {
        return new Update().set("activationToken", activationToken);
    }

    public Update setRefreshTokenUpdate(String refreshToken) {
        return new Update().set("refreshToken", refreshToken);
    }

    public Update deleteRefreshTokenUpdate() {
        return new Update().unset("refreshToken");
    }

    public Update updateUser(UserDocument userDocument) {
        return new Update()
                .set("id", userDocument.getId())
                .set("email", userDocument.getEmail())
                .set("username", userDocument.getUsername())
                .set("userInfo", userDocument.getInfo())
                .set("refreshToken", userDocument.getRefreshToken())
                .set("password", userDocument.getPassword())
                .set("roles", userDocument.getRoles())
                .set("active", userDocument.isActive());
    }

    // trainings

    public Update addPlan(PlanDocument planDocument) {
        return new Update().set("plan", planDocument);
    }


    // statistics
    public Update addStatistics(StatisticsDocument statisticsDocument) {
        return new Update().set("statistics", statisticsDocument);
    }


}
