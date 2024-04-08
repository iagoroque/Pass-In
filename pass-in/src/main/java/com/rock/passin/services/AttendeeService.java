package com.rock.passin.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.rock.passin.domain.attendee.Attendee;
import com.rock.passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import com.rock.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import com.rock.passin.domain.checkin.CheckIn;
import com.rock.passin.dto.attendee.AttendeeBadgeResponseDTO;
import com.rock.passin.dto.attendee.AttendeeDetails;
import com.rock.passin.dto.attendee.AttendeesListResponseDTO;
import com.rock.passin.dto.attendee.AttendeeBadgeDTO;
import com.rock.passin.repositories.AttendeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;
    private final CheckInService checkInService;

    public List<Attendee> getAllAttendeesFromEvent(String eventId) {
        return this.attendeeRepository.findByEventId(eventId);
    }

    public AttendeesListResponseDTO getEventsAttendee(String eventId) {
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);
        List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
            Optional<CheckIn> checkIn = this.checkInService.getCheckIn(attendee.getId());
            LocalDateTime checkedInAt = checkIn.<LocalDateTime>map(CheckIn::getCreatedAt).orElse(null);
            return new AttendeeDetails(attendee.getId(), attendee.getName(), attendee.getEmail(),
                    attendee.getCreatedAt(), checkedInAt);
        }).toList();
        return new AttendeesListResponseDTO(attendeeDetailsList);
    }

    public void verifyAttendeeSubscription(String email, String eventId) {
        Optional<Attendee> isAttendeeRegistered = this.attendeeRepository.findByEventIdAndEmail(eventId, email);
        if (isAttendeeRegistered.isPresent())
            throw new AttendeeAlreadyExistException("Attendee is already registered");
    }

    public Attendee registerAttendee(Attendee newAttendee) {
        this.attendeeRepository.save(newAttendee);
        return newAttendee;
    }

    public void checkInAttendee(String attendeeId) {
        Attendee attendee = this.getAttendee(attendeeId);
        this.checkInService.registerCheckIn(attendee);
    }

    public Attendee getAttendee(String attendeeId) {
        return this.attendeeRepository.findById(attendeeId)
                .orElseThrow(() -> new AttendeeNotFoundException("Attendee not found with ID: " + attendeeId));
    }

    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
        Attendee attendee = this.getAttendee(attendeeId);
        var uri = uriComponentsBuilder.path("/attendees/{attendeesId}/check-in").buildAndExpand(attendeeId).toUri()
                .toString();
        AttendeeBadgeDTO badgeDTO = new AttendeeBadgeDTO(attendee.getName(), attendee.getEmail(), uri,
                attendee.getEvent().getId());
        return new AttendeeBadgeResponseDTO(badgeDTO);
    }

}
