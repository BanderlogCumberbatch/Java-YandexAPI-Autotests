package org.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@AllArgsConstructor
@Builder
@Jacksonized
@JsonIgnoreProperties({"operation_id"})
public class SuccessResponse {

    /**
     * Метод.
     */
    private String method;

    /**
     * Описание.
     */
    private String href;

    /**
     * Сообщение.
     */
    @Builder.Default
    private Boolean templated = true;
}
