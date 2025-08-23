package com.fitness.activityservice.controller;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/activities")
@RequiredArgsConstructor
public class ActivityController {
    private static final String X_USER_ID = "X-User-ID";
    private final ActivityService service;

    @PostMapping("/v1/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ActivityResponse trackActivity(@RequestBody ActivityRequest request) {
        return service.trackActivity(request);
    }


    @GetMapping("/v1/user")
    public List<ActivityResponse> getActivities(@RequestHeader(X_USER_ID) String userId) {
        return service.getUserActivities(userId);
    }

    @GetMapping("/v1/{activityId}")
    public ActivityResponse getActivity(@PathVariable("activityId") String activityId) {
        return service.findById(activityId);
    }

}
