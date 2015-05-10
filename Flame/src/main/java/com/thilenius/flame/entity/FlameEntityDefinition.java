package com.thilenius.flame.entity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FlameEntityDefinition {

    // Required
    String name();
    String blockTextureName();

    // Optional
    float hardness() default 1.0f;
    float resistance() default 5.0f;
    float[] bounds() default { 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f };

}
