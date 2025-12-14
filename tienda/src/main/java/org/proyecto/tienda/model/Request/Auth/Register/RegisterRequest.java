package org.proyecto.tienda.model.Request.Auth.Register;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    String nombres;
    String apellidos;
    String email;
    String password;
}
 