/**
 *
 */
package com.deloitte.elrr.datasync;

import org.springframework.web.bind.annotation.CrossOrigin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mnelakurti
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@CrossOrigin
public @interface CrossOriginsList {
    /**
     *
     * @return String[]
     */
    String[] crossOrigins() default { "http://localhost:3001",
            "http://localhost:5000" };
}
