package com.example.springboottesttask.services;

import com.example.springboottesttask.entities.Lord;
import com.example.springboottesttask.entities.Planet;
import com.example.springboottesttask.exception.LordNotFoundException;
import com.example.springboottesttask.exception.PlanetNotFoundException;
import com.example.springboottesttask.repositories.LordRepository;
import com.example.springboottesttask.repositories.PlanetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanetService {

    private final PlanetRepository planetRepository;
    private final LordRepository lordRepository;
    @Autowired
    public PlanetService(PlanetRepository planetRepository, LordRepository lordRepository) {
        this.planetRepository = planetRepository;
        this.lordRepository = lordRepository;
    }
    public List<Planet> getAllPlanets() {
        return (List<Planet>) planetRepository.findAll();
    }

    public void savePlanet(Planet planet) {
        planet.setIsOwned(false);
         planetRepository.save(planet);
    }

    public void setLordToPlanet(long lordId, long planetId) {
        Planet planet = findPlanetById(planetId);
        Lord lord = lordRepository.findById(lordId)
                .orElseThrow(()->new LordNotFoundException("Unable to find lord with id: "+lordId));
        if (!planet.getIsOwned()){
        planet.setIsOwned(true);
        planet.setLord(lord);
        planet.setLordId(lord.getLordId());
        planetRepository.save(planet);
        } else System.out.println("This planet has lord");
    }

    public void deletePlanet(long planetId) {
        planetRepository.deleteById(planetId);
    }

    public Planet findPlanetById(long planetId) {
        return planetRepository.findById(planetId)
        .orElseThrow(()->new PlanetNotFoundException("Unable to find planet with id: "+ planetId));
    }

}
