package dulikkk.livehealthierapi.domain.user;

import java.math.BigDecimal;
import java.math.MathContext;

class BMICalculator {

    double calculateBMI(double weightInKg, double heightInCm) {
        BigDecimal weightBigDecimal = BigDecimal.valueOf(weightInKg);
        BigDecimal heightInMBigDecimal = BigDecimal.valueOf(heightInCm / 100);

        // weightInKg / (heightInM * heightInM)
        return weightBigDecimal.divide(heightInMBigDecimal.multiply(heightInMBigDecimal), new MathContext(4))
                .doubleValue();
    }
}
