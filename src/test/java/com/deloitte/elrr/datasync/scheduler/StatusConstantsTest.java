package com.deloitte.elrr.datasync.scheduler;

import static org.assertj.core.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
class StatusConstantsTest {

    @Test
    void test() {

        try {

            Constructor<StatusConstants> constructor = StatusConstants.class
                    .getDeclaredConstructor();
            constructor.setAccessible(true);
            StatusConstants statusConstants = constructor.newInstance();

        } catch (UnsupportedOperationException | InvocationTargetException e) {
            System.out.println(
                    "This is a utility class and cannot be instantiated");
        } catch (NoSuchMethodException | SecurityException
                | InstantiationException | IllegalAccessException
                | IllegalArgumentException e1) {
            fail("Should not have thrown any exception");
        }
    }
}
