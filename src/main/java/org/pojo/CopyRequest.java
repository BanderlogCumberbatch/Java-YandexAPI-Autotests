package org.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@AllArgsConstructor
@Builder
@Jacksonized
public class CopyRequest {

    /**
     * Ошибка.
     */
    private String from;

    /**
     * Описание.
     */
    private String path;

    /**
     * Сообщение.
     */
    @Builder.Default
    private Boolean overwrite = false;
}
