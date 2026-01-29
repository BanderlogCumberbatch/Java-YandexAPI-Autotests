package org.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CopyRequest {

    /**
     * Ошибка.
     */
    @Builder.Default
    private String from = "string";

    /**
     * Описание.
     */
    @Builder.Default
    private String path = "string";

    /**
     * Сообщение.
     */
    @Builder.Default
    private Boolean overwrite = false;
}
