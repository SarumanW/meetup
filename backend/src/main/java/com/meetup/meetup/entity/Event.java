package com.meetup.meetup.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    private int eventId;

    @NotBlank
    @Size(min = 4, max = 50)
    private String name;

    private String eventDate;
    private String pinnedEvent;

    @Size(max = 1023)
    private String description;

    private int periodicityId;
    private EventPeriodicity periodicity;

    @Size(min = 4, max = 100)
    private String place;
    private int eventTypeId;
    private EventType eventType;

    @JsonProperty()
    private boolean isDraft;

    @NotNull
    private int folderId;
    private String imageFilepath;
    private int ownerId;

    private List<User> participants;

    public boolean getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(boolean isDraft) {
        this.isDraft = isDraft;
    }
}
