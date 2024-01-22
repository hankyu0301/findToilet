package com.findToilet.domain.toilet.repository;

import com.findToilet.domain.toilet.entity.Toilet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToiletRepository extends JpaRepository<Toilet, Long>, CustomToiletRepository  {
}
