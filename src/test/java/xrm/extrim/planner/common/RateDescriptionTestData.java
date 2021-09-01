package xrm.extrim.planner.common;

import xrm.extrim.planner.domain.RateDescription;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"PMD.ClassNamingConventions"})
public final class RateDescriptionTestData {
    private RateDescriptionTestData() {
    }

    public static List<RateDescription> getRateDescriptions() {
        ArrayList<RateDescription> list = new ArrayList<>();
        for(int i = 0; i < 5 ;i++) {
            list.add(getRateDescription((long) i,i,"description"));
        }
        return list;
    }

    public static RateDescription getRateDescription(Long id, int rate, String description) {
        RateDescription rateDescription = new RateDescription();
        rateDescription.setId(id);
        rateDescription.setRateNumber(rate);
        rateDescription.setDescription(description);
        return rateDescription;
    }
}
