package org.cencosud.demandexpiry.event_bridge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cencosud.demandexpiry.Demand;
import org.cencosud.demandexpiry.Status;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.scheduler.SchedulerClient;
import software.amazon.awssdk.services.scheduler.model.ActionAfterCompletion;
import software.amazon.awssdk.services.scheduler.model.CreateScheduleRequest;
import software.amazon.awssdk.services.scheduler.model.CreateScheduleResponse;
import software.amazon.awssdk.services.scheduler.model.DeleteScheduleRequest;
import software.amazon.awssdk.services.scheduler.model.DeleteScheduleResponse;
import software.amazon.awssdk.services.scheduler.model.FlexibleTimeWindowMode;
import software.amazon.awssdk.services.scheduler.model.Target;
import software.amazon.awssdk.services.scheduler.model.UpdateScheduleResponse;

import java.time.ZonedDateTime;
import java.util.Random;

@Slf4j
@Component
@AllArgsConstructor
public class EventBridgeScheduler {

    private final ObjectMapper objectMapper;
    private static final Random random = new Random();

    public void scheduleEvent() {
        try (var eventBridgeClient = SchedulerClient.builder().region(Region.US_EAST_1).build()) {
            // create schedule
            var demand = new Demand(String.valueOf(random.nextInt()), Status.RESERVED);
            String input = objectMapper.writeValueAsString(demand);
            var target = targetFrom(input);
            var at = new ScheduleAt(ZonedDateTime.now().plusMinutes(10));
            var metaData = ScheduleMetaData.builder().schedulerName(demand.getId()).groupName("demands").description("This is a test event").build();
            var response = scheduleEvent(at, target, metaData, eventBridgeClient);

            // update schedule
            var allocatedDemand = demand.updateStatus(Status.ALLOCATED);
            var updateTarget = targetFrom(objectMapper.writeValueAsString(allocatedDemand));
            var updatedAt = new ScheduleAt(ZonedDateTime.now().plusMinutes(20));
            var updateResponse = update(eventBridgeClient, metaData, updatedAt, updateTarget);

            // delete schedule
            var deleteScheduleResponse = delete(eventBridgeClient, metaData);
        } catch (JsonProcessingException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Target targetFrom(String input) {
        return Target.builder().arn("arn:aws:lambda:us-east-1:767397742130:function:demand-expiry").roleArn("arn:aws:iam::767397742130:role/service-role/Amazon_EventBridge_Scheduler_LAMBDA_a9e18f7a1a").input(input).build();
    }

    private CreateScheduleResponse scheduleEvent(ScheduleAt at, Target scheduleTarget, ScheduleMetaData metaData, SchedulerClient client) {
        CreateScheduleRequest scheduleRequest = CreateScheduleRequest.builder()
                .name(metaData.getSchedulerName())
                .groupName(metaData.getGroupName())
                .description(metaData.getDescription())
                .scheduleExpression(at.expression())
                .actionAfterCompletion(ActionAfterCompletion.DELETE)
                .flexibleTimeWindow(w -> w.mode(FlexibleTimeWindowMode.OFF))
                .scheduleExpressionTimezone(at.timeZone().toString())
                .target(scheduleTarget).build();
        CreateScheduleResponse scheduleResponse = client.createSchedule(scheduleRequest);
        log.info("Created schedule => {}", scheduleResponse);
        return scheduleResponse;
    }

    private DeleteScheduleResponse delete(SchedulerClient client, ScheduleMetaData metaData) throws InterruptedException {
        Thread.sleep(10000);
        var response = client.deleteSchedule(DeleteScheduleRequest.builder().groupName(metaData.getGroupName()).name(metaData.getSchedulerName()).build());
        log.info("Deleted schedule => {}", response);
        return response;
    }


    private UpdateScheduleResponse update(SchedulerClient client, ScheduleMetaData metaData, ScheduleAt at, Target target) throws InterruptedException {
        Thread.sleep(10000);
        var response = client.updateSchedule(builder -> builder
                .groupName(metaData.getGroupName())
                .name(metaData.getSchedulerName())
                .description(metaData.getDescription())
                .scheduleExpression(at.expression())
                .scheduleExpressionTimezone(at.timeZone().toString())
                .actionAfterCompletion(ActionAfterCompletion.DELETE)
                .flexibleTimeWindow(w -> w.mode(FlexibleTimeWindowMode.OFF))
                .target(target));
        log.info("Updated schedule => {}", response);
        return response;
    }
}
