package com.example.springboottesttask.controllers;

import com.example.springboottesttask.controllers.dto.*;
import com.example.springboottesttask.entities.Lord;
import com.example.springboottesttask.entities.Planet;
import com.example.springboottesttask.services.LordService;
import com.example.springboottesttask.services.PlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/main")
public class MainController {

    private final PlanetService planetService;
    private final LordService lordService;
    @Autowired
    public MainController(PlanetService planetService, LordService lordService) {
        this.planetService = planetService;
        this.lordService = lordService;
    }

    @GetMapping
    public String showLordsAndPlanets(Model model) {
        model.addAttribute("lords",lordService.getAllLords());
        model.addAttribute("planets",planetService.getAllPlanets());
        model.addAttribute("lordsLoungers",lordService.getLordsWithoutPlanets());
        model.addAttribute("theYoungestLords",lordService.getLordsByAge());
        return "lordsAndPlanets";
    }
    @GetMapping("/destroyPlanet/{planetId}")
    public String deletePlanetById(@PathVariable("planetId") Long planetId) {
        planetService.deletePlanet(planetId);
        return "redirect:/main";
    }

    @GetMapping("/getPlanetsForLord/{lordId}")
    public String getPlanetsForLord(Model model, @PathVariable("lordId") Long lordId) {
        model.addAttribute("planets", lordService.getPlanetsForLord(lordId));
        return "showForm";
    }
    @GetMapping("/createLord")
    public String createLord(Model model) {
        Lord lord = new Lord();
        model.addAttribute("lord", lord);
        return "addLord";
    }
    @PostMapping("/createLord")
    public String createLord(@ModelAttribute(value="lord")Lord lord) {
        lordService.saveLord(lord);
        return "redirect:/main";
    }
    @GetMapping("/createPlanet")
    public String createLPlanet(Model model) {
        Planet planet = new Planet();
        model.addAttribute("planet", planet);
        return "addPlanet";
    }
    @PostMapping("/createPlanet")
    public String createPlanet(@ModelAttribute(value="planet") Planet planet) {
        planetService.savePlanet(planet);
        return "redirect:/main";
    }
    @GetMapping("/appoint")
    public String appointLordToRuleThePlanet(Model model) {
        LordAndPlanetRequestDTO lordAndPlanet = new LordAndPlanetRequestDTO();
        model.addAttribute("lordPlanet", lordAndPlanet);
        return "appointForm";
    }
    //thymeleaf плохо себя ведет с PutMapping
    @PostMapping("/appoint")
    public String appointLordToRuleThePlanet(@ModelAttribute (value="lordPlanet") LordAndPlanetRequestDTO lordAndPlanet) {
      System.out.println();
      planetService.setLordToPlanet(lordAndPlanet.getLordId(),lordAndPlanet.getPlanetId());
      lordService.setPlanetToLord(lordAndPlanet.getLordId(), lordAndPlanet.getPlanetId());
      return "redirect:/main";
    }

}
