package com.rock.passin.services;

import java.text.Normalizer;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rock.passin.domain.attendee.Attendee;
import com.rock.passin.domain.event.Event;
import com.rock.passin.dto.event.EventIdDTO;
import com.rock.passin.dto.event.EventRequestDTO;
import com.rock.passin.dto.event.EventResponseDTO;
import com.rock.passin.repositories.AttendeeRepository;
import com.rock.passin.repositories.EventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final AttendeeRepository attendeeRepository;
    
    public EventResponseDTO getEventDetail(String eventId) {
        Event event = this.eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));
        List<Attendee> attendeeList = this.attendeeRepository.findByEventId(eventId);
        return new EventResponseDTO(event, attendeeList.size());
    }

    public EventIdDTO createEvent(EventRequestDTO eventDTO) {
        Event newEvent = new Event();
        newEvent.setTitle(eventDTO.title());
        newEvent.setDetails(eventDTO.details());
        newEvent.setMaximumAttendees(eventDTO.maximumAttendees());
        newEvent.setSlug(this.createSlug(eventDTO.title()));

        this.eventRepository.save(newEvent);
        return new EventIdDTO(newEvent.getId());
    }

    private String createSlug(String text) {
        String normilazed = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normilazed.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
                .replaceAll("[^\\w\\s]", "")
                .replaceAll("\\s+", "-")
                .toLowerCase();
    }
}
