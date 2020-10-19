package dulikkk.livehealthierapi.domain.plan;

import dulikkk.livehealthierapi.domain.plan.dto.DifficultyLevelDto;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static dulikkk.livehealthierapi.domain.plan.dto.DifficultyLevelDto.*;

class PlanLevelCalculator {

    DifficultyLevelDto calculateUserLevelByBmiAndAge(double bmi, int age) {

        if(age >= 35 && age < 45 &&  bmi > 15 && bmi < 25){
            return B1;
        }

        if(age >= 45 && age <= 50 &&  bmi > 16 && bmi < 25){
            return B1;
        }

        if (age > 50 && age <= 60) {
            return C1;
        }
        if (age > 60 || age < 13) {
            return C2;
        }

        BigDecimal bmiBigDecimal = BigDecimal.valueOf(bmi);
        BigDecimal ageBigDecimal = BigDecimal.valueOf(age);
        MathContext mathContext = new MathContext(3, RoundingMode.HALF_UP);

        double level = bmiBigDecimal.divide(BigDecimal.valueOf(2), mathContext )
                .pow(2)
                .multiply(ageBigDecimal.sqrt(mathContext), mathContext)
                .doubleValue();

        if (level > 350 && level < 650) {
            return A1;
        }
        if (level >= 650 && level <= 790) {
            return A2;
        }

        if (level > 790 && level <= 900) {
            return B1;
        }
        if (level > 900 && level <= 1080) {
            return B2;
        }

        if (level > 1080 && level <= 1200) {
            return C1;
        }
        if (level > 1200) {
            return C2;
        }

        return B1;
    }
}
