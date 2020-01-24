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
class IngredientCommandToIngredientTest {

    private static final Long INGREDIENT_ID = 1L;
    private static final String INGREDIENT_DESCRIPTION = "ingredient_description";
    private static final BigDecimal AMOUNT = new BigDecimal(1);
    private static final Long UOM_ID = 2L;
    private static final String UOM_DESCRIPTION = "uom_description";
    private UnitOfMeasure uom;
    private UnitOfMeasureCommand uomCommand;

    @Mock
    private UnitOfMeasureCommandToUnitOfMeasure uomConverter;

    @InjectMocks
    private IngredientCommandToIngredient ingredientConverter;

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
        assertNotNull(ingredientConverter.convert(new IngredientCommand()));
    }

    @Test
    void convert() {
        //given
        when(uomConverter.convert(any())).thenReturn(uom);

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(INGREDIENT_ID);
        ingredientCommand.setDescription(INGREDIENT_DESCRIPTION);
        ingredientCommand.setAmount(AMOUNT);
        ingredientCommand.setUnitOfMeasure(uomCommand);

        //when
        Ingredient ingredient = ingredientConverter.convert(ingredientCommand);

        //then
        assertNotNull(ingredient);
        assertEquals(INGREDIENT_ID, ingredient.getId());
        assertEquals(INGREDIENT_DESCRIPTION, ingredient.getDescription());
        assertEquals(AMOUNT, ingredient.getAmount());
        assertEquals(UOM_ID, ingredient.getUnitOfMeasure().getId());
        assertEquals(UOM_DESCRIPTION, ingredient.getUnitOfMeasure().getDescription());

        verify(uomConverter, times(1)).convert(any());
    }

    @Test
    void convertWithNullUom() {
        //given
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(INGREDIENT_ID);
        ingredientCommand.setDescription(INGREDIENT_DESCRIPTION);
        ingredientCommand.setAmount(AMOUNT);

        //when
        Ingredient ingredient = ingredientConverter.convert(ingredientCommand);

        //then
        assertNotNull(ingredient);
        assertEquals(INGREDIENT_ID, ingredient.getId());
        assertEquals(INGREDIENT_DESCRIPTION, ingredient.getDescription());
        assertEquals(AMOUNT, ingredient.getAmount());
        assertNull(ingredient.getUnitOfMeasure());

        verify(uomConverter, times(1)).convert(any());
    }
}