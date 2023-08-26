package com.okworo.cards.repository;

import com.okworo.cards.entities.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Morris.Okworo on 26/08/2023
 */

public interface CardRepository extends JpaRepository<CardEntity, Long> {

    List<CardEntity> findByCreatedBy(Long id);
}
