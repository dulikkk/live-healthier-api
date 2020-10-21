package dulikkk.livehealthierapi.infrastructure.plan.mongoDb;

import dulikkk.livehealthierapi.domain.plan.dto.PlanDto;

public class PlanConverter {

    public PlanDocument toDocument(PlanDto planDto){
        return PlanDocument.builder()
                .id(planDto.getId())
                .userId(planDto.getUserId())
                .monday(planDto.getMonday())
                .tuesday(planDto.getTuesday())
                .wednesday(planDto.getWednesday())
                .thursday(planDto.getThursday())
                .friday(planDto.getFriday())
                .saturday(planDto.getSaturday())
                .sunday(planDto.getSunday())
                .userLevel(planDto.getUserLevel())
                .build();
    }

    public PlanDto toDto(PlanDocument planDocument){
        return PlanDto.builder()
                .id(planDocument.getId())
                .userId(planDocument.getUserId())
                .monday(planDocument.getMonday())
                .tuesday(planDocument.getTuesday())
                .wednesday(planDocument.getWednesday())
                .thursday(planDocument.getThursday())
                .friday(planDocument.getFriday())
                .saturday(planDocument.getSaturday())
                .sunday(planDocument.getSunday())
                .userLevel(planDocument.getUserLevel())
                .build();
    }
}
