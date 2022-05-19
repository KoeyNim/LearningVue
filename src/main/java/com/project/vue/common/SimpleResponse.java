package com.project.vue.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleResponse {
	@Builder.Default
    private boolean success = true;
    @Builder.Default
    private int statusCode = 200;
    private String message;
}
