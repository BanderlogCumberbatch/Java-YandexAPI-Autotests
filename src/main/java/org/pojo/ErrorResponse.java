package org.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorResponse {

    /**
     * Ошибка.
     */
    @Builder.Default
    private String error = "string";

    /**
     * Описание.
     */
    @Builder.Default
    private String description = "string";

    /**
     * Сообщение.
     */
    @Builder.Default
    private String message = "string";
}
