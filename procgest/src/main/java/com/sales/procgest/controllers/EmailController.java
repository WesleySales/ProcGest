package com.sales.procgest.controllers;

import com.sales.procgest.DTO.EmailDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/procuracoes/")
public class EmailController {

    @PostMapping("/sendEmail")
    public ResponseEntity<?> enviarEmail(@RequestBody EmailDTO data){
        var email = new EmailDTO(data.para(), data.assunto(), data.corpo());
        return ResponseEntity.ok().body("Email enviado com sucesso");
    }

}
