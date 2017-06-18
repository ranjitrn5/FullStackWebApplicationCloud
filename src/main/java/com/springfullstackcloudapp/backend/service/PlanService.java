package com.springfullstackcloudapp.backend.service;

import com.springfullstackcloudapp.backend.persistence.domains.backend.Plan;
import com.springfullstackcloudapp.backend.persistence.repositories.PlanRepository;
import com.springfullstackcloudapp.enums.PlansEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ranji on 6/18/2017.
 */
@Service
@Transactional
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    public Plan findPlanById(int planId){
        return planRepository.findOne(planId);
    }

    @Transactional
    public Plan createPlan(int planId){
        Plan plan = null;
        if(planId == 1) {
            plan = planRepository.save(new Plan(PlansEnum.BASIC));
        } else if(planId == 2){
            plan = planRepository.save(new Plan(PlansEnum.PRO));
        } else{
            throw new IllegalArgumentException("Plan Id"+planId+"not recognized");
        }
        return plan;
    }
}
