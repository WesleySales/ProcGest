//package com.sales.procgest.controllers;
//
//import com.sales.procgest.DTO.EmailDTO;
//import com.sales.procgest.services.EmailService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/email")
//public class EmailController {
//
//    @Autowired
//    private EmailService emailService;
//
//    @PostMapping("/enviar")
//    public ResponseEntity enviarEmail(@RequestBody EmailDTO data) {
//        emailService.enviarEmailSimples(data.para(), data.assunto(), data.corpo());
//        return ResponseEntity.ok().build();
//    }
//}
