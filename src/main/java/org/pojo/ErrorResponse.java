package org.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@AllArgsConstructor
@Builder
@Jacksonized
public class ErrorResponse {

    /**
     * Ошибка.
     */
    private String error;

    /**
     * Описание.
     */
    private String description;

    /**
     * Сообщение.
     */
    private String message;
}
