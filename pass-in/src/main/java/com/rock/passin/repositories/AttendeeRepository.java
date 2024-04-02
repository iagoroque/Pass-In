package com.rock.passin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rock.passin.domain.attendee.Attendee;

public interface AttendeeRepository extends JpaRepository<Attendee, String> {
    
}
