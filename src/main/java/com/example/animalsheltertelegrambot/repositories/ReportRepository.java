package com.example.animalsheltertelegrambot.repositories;

import com.example.animalsheltertelegrambot.models.ProbationPeriod;
import com.example.animalsheltertelegrambot.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    boolean existsByProbationPeriodAndDate(ProbationPeriod probationPeriod,
                                           LocalDate date);

    Optional<Report> findReportByProbationPeriodAndDate(ProbationPeriod probationPeriod,
                                                        LocalDate date);

//    Collection<Report> findReportsByProbationPeriod(ProbationPeriod probationPeriod);
}
