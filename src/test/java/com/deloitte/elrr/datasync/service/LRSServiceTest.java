/**
 *
 */
package com.deloitte.elrr.datasync.service;

import java.sql.Timestamp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

/**
 * @author mnelakurti
 *
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LRSServiceTest {

    /**
    *
    */
   @Mock
   private LRSService mockLRSService;


   @Test
   void test() {
       mockLRSService.process(new Timestamp(System.currentTimeMillis()));
   }
}
