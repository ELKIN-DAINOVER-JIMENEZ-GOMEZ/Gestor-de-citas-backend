package com.GestorDeCitas.Backend.dto.response;

import lombok.Data;

@Data
public class JwtResponse {


    private String token;
    private String type = "Bearer";
    private  String username;
    private  String email;


}
