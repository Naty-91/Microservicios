package org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.dtos;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities.Province;
import org.iesalixar.daw2.NataliaFernandez.dwese_ticket_logger_api.entities.Supermarket;

@Getter
@Setter
public class LocationCreateDTO {


    private String address;
    private String city;
    private Long supermarketId;
    private Long provinceId;

    public @NotNull(message = "{msg.location.province.notNull}") Province getProvince() {
        return null;
    }

    public @NotNull(message = "{msg.location.supermarket.notNull}") Supermarket getSupermarket() {
        return null;
    }
}
