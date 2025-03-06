package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProvinceCreateDTO {

    @NotEmpty(message = "{msg.province.code.notEmpty}")
    @Size(max = 2, message = "{msg.province.code.size}")
    private String code;

    @NotEmpty(message = "{msg.province.name.notEmpty}")
    @Size(max = 100, message = "{msg.province.name.size}")
    private String name;

    @NotNull(message = "{msg.province.region.notNull}")
    private Long regionId; // Representa la región mediante su identificador.
}
