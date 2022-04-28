package com.project.vue.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleResponse {
    private boolean success;
    private int statusCode;
    private String message;
}
