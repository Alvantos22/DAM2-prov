package com.example.demo;

import com.example.demo.domain.Algo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/primer")
public class PrimerMapping {


    @RequestMapping
    public String mostrarPagina(Model model) {
        model.addAttribute("titulo", "Pantalla de index");
        return "Pantalla de index";
    }

    @RequestMapping("/uno")
    public Algo mostrarUno(){
        return new Algo("Paralelepipedo");
    }

    @RequestMapping("/varios")
    public List<Algo> mostrarVarios(){
        return List.of(new Algo("Paralelepipedo"),new Algo("Madagascar"),new Algo("Nenufar"),new Algo("Clerigo"));
    }


}