package com.spring.recipes.services;

import com.spring.recipes.domain.UnitOfMeasure;
import com.spring.recipes.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnitOfMeasureServiceImplTest {

    @Mock
    private UnitOfMeasureRepository uomRepository;

    @InjectMocks
    private UnitOfMeasureServiceImpl uomService;

    private UnitOfMeasure returnedUom;

    @BeforeEach
    void setUp() {
        returnedUom = new UnitOfMeasure();
        returnedUom.setId(1L);
        returnedUom.setDescription("returnedUom");
    }

    @Test
    void getAllTest() {
        //given
        Set<UnitOfMeasure> returnedUomSet = new HashSet<>();
        returnedUomSet.add(returnedUom);

        //when
        when(uomRepository.findAll()).thenReturn(returnedUomSet);

        Set<UnitOfMeasure> uomSet = uomService.getAll();

        //then
        assertNotNull(uomSet);
        assertEquals(1, uomSet.size());
        verify(uomRepository, times(1)).findAll();
    }
}