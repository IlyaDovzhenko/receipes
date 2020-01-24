package com.spring.recipes.converters;

import com.spring.recipes.command.IngredientCommand;
import com.spring.recipes.command.UnitOfMeasureCommand;
import com.spring.recipes.domain.Ingredient;
import com.spring.recipes.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientToIngredientCommandTest {

    private static final Long INGREDIENT_ID = 1L;
    private static final String INGREDIENT_DESCRIPTION = "ingredient_description";
    private static final BigDecimal AMOUNT = new BigDecimal(1);
    private static final Long UOM_ID = 2L;
    private static final String UOM_DESCRIPTION = "uom_description";
    private UnitOfMeasure uom;
    private UnitOfMeasureCommand uomCommand;

    @Mock
    private UnitOfMeasureToUnitOfMeasureCommand uomConverter;

    @InjectMocks
    private IngredientToIngredientCommand ingredientConverter;

    @BeforeEach
    void setUp() {
        uom = new UnitOfMeasure();
        uom.setId(UOM_ID);
        uom.setDescription(UOM_DESCRIPTION);

        uomCommand = new UnitOfMeasureCommand();
        uomCommand.setId(UOM_ID);
        uomCommand.setDescription(UOM_DESCRIPTION);
    }

    @Test
    void testNullObject() {
        assertNull(ingredientConverter.convert(null));
    }

    @Test
    void testEmptyObject() {
        assertNotNull(ingredientConverter.convert(new Ingredient()));
    }

    @Test
    void convert() {
        //given
        when(uomConverter.convert(uom)).thenReturn(uomCommand);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(INGREDIENT_ID);
        ingredient.setDescription(INGREDIENT_DESCRIPTION);
        ingredient.setAmount(AMOUNT);
        ingredient.setUnitOfMeasure(uom);

        //then
        IngredientCommand ingredientCommand = ingredientConverter.convert(ingredient);

        //when
        assertNotNull(ingredientCommand);
        assertEquals(INGREDIENT_ID, ingredientCommand.getId());
        assertEquals(INGREDIENT_DESCRIPTION, ingredientCommand.getDescription());
        assertEquals(AMOUNT, ingredientCommand.getAmount());
        assertNotNull(ingredientCommand.getUnitOfMeasure());
        assertEquals(UOM_ID, ingredientCommand.getUnitOfMeasure().getId());
        assertEquals(UOM_DESCRIPTION, ingredientCommand.getUnitOfMeasure().getDescription());

        verify(uomConverter, times(1)).convert(uom);
    }

    @Test
    void convertWithNullUom() {
        //given
        Ingredient ingredient = new Ingredient();
        ingredient.setId(INGREDIENT_ID);
        ingredient.setDescription(INGREDIENT_DESCRIPTION);
        ingredient.setAmount(AMOUNT);

        //then
        IngredientCommand ingredientCommand = ingredientConverter.convert(ingredient);

        //when
        assertNotNull(ingredientCommand);
        assertEquals(INGREDIENT_ID, ingredientCommand.getId());
        assertEquals(INGREDIENT_DESCRIPTION, ingredientCommand.getDescription());
        assertEquals(AMOUNT, ingredientCommand.getAmount());
        assertNull(ingredientCommand.getUnitOfMeasure());

        verify(uomConverter, times(1)).convert(any());
    }
}