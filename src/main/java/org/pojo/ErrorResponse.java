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
    private String error = null;

    /**
     * Описание.
     */
    @Builder.Default
    private String description = null;

    /**
     * Сообщение.
     */
    @Builder.Default
    private String message = null;
}
