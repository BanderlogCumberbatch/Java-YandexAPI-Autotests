package org.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SuccessResponse {

    /**
     * Ошибка.
     */
    @Builder.Default
    private String method = "string";

    /**
     * Описание.
     */
    @Builder.Default
    private String href = "string";

    /**
     * Сообщение.
     */
    @Builder.Default
    private Boolean templated = true;
}
