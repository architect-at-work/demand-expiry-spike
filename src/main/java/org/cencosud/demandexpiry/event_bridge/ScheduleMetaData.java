package org.cencosud.demandexpiry.event_bridge;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ScheduleMetaData {

    private String groupName;
    private String schedulerName;
    private String description;

}
