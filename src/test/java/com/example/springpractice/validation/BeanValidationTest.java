package com.example.springpractice.validation;

import com.example.springpractice.domain.item.Item;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Set;

@Slf4j
public class BeanValidationTest {

    @Test
    void beanValidation(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Item item = new Item();
        item.setItemName(" ");
        item.setPrice(0);
        item.setQuantity(10000);

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        for (ConstraintViolation<Item> violation : violations) {
            log.info("violation ={}", violation);
            log.info("violation.getMessage={}", violation.getMessage());
        }
    }
}
