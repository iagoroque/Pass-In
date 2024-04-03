package com.rock.passin.dto.event;

import com.rock.passin.domain.event.Event;

public class EventResponseDTO {
    
    EventDetailDTO event;

    public EventResponseDTO(Event event, Integer numberOfAttendees) {
        this.event = new EventDetailDTO(
            event.getId(),
            event.getTitle(),
            event.getDetails(),
            event.getSlug(),
            event.getMaximumAttendees(),
            numberOfAttendees
        );
    }
}
