package com.spring.recipes.services;

import com.spring.recipes.domain.UnitOfMeasure;
import com.spring.recipes.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private UnitOfMeasureRepository uomRepository;

    public UnitOfMeasureServiceImpl(UnitOfMeasureRepository uomRepository) {
        this.uomRepository = uomRepository;
        log.debug("Service initialized! Dependencies are injected!");
    }

    @Override
    public Set<UnitOfMeasure> getAll() {
        Set<UnitOfMeasure> uomSet = new HashSet<>();
        uomRepository.findAll().forEach(uomSet::add);
        return uomSet;
    }
}
