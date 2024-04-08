package com.rock.passin.dto.attendee;

import java.util.List;

public record AttendeesListResponseDTO(
    List<AttendeeDetails> attendees
    ) {
}
