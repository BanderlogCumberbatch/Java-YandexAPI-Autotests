package org.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SuccessResponse {

    /**
     * Метод.
     */
    @Builder.Default
    private String method = null;

    /**
     * Описание.
     */
    @Builder.Default
    private String href = null;

    /**
     * Сообщение.
     */
    @Builder.Default
    private Boolean templated = true;
}
