package com.rock.passin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rock.passin.domain.checkin.CheckIn;

public interface CheckinRepository extends JpaRepository<CheckIn, Integer>{
    
}
