package com.example.springboottesttask.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.springboottesttask.entities.Lord;
import com.example.springboottesttask.entities.Planet;
import com.example.springboottesttask.exception.LordNotFoundException;
import com.example.springboottesttask.exception.PlanetNotFoundException;
import com.example.springboottesttask.repositories.LordRepository;
import com.example.springboottesttask.repositories.PlanetRepository;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {PlanetService.class})
@ExtendWith(SpringExtension.class)
public class PlanetServiceTest {
    @MockBean
    private LordRepository lordRepository;

    @MockBean
    private PlanetRepository planetRepository;

    @Autowired
    private PlanetService planetService;

    private Planet getPlanet(Lord lord, boolean b) {
        Planet planet = new Planet();
        planet.setLord(lord);
        planet.setPlanetId(123L);
        planet.setName("Name");
        planet.setLordId(123L);
        planet.setIsOwned(b);
        return planet;
    }

    private Lord getLord() {
        Lord lord = new Lord();
        lord.setPlanetList(new ArrayList<>());
        lord.setLordId(123L);
        lord.setName("Name");
        lord.setAge(1);
        return lord;
    }
    @Test
    public void testGetAllPlanets() {
        when(this.planetRepository.findAll()).thenThrow(new PlanetNotFoundException("An error occurred"));
        assertThrows(PlanetNotFoundException.class, () -> this.planetService.getAllPlanets());
        verify(this.planetRepository).findAll();
    }

    @Test
    public void testSavePlanet() {
        Lord lord = getLord();

        Planet planet = getPlanet(lord, false);
        when(this.planetRepository.save(any())).thenReturn(planet);
        Planet planet1 = new Planet("Name");
        this.planetService.savePlanet(planet1);
        verify(this.planetRepository).save(any());
        assertFalse(planet1.getIsOwned());
        assertTrue(this.planetService.getAllPlanets().isEmpty());
    }



    @Test
    public void testSavePlanet2() {
        Lord lord = getLord();

        Planet planet = getPlanet(lord, false);
        when(this.planetRepository.save(any())).thenReturn(planet);
        Planet planet1 = mock(Planet.class);
        doNothing().when(planet1).setIsOwned(any());
        this.planetService.savePlanet(planet1);
        verify(this.planetRepository).save(any());
        verify(planet1).setIsOwned(any());
        assertTrue(this.planetService.getAllPlanets().isEmpty());
    }

    @Test
    public void testSetLordToPlanet() {
        Lord lord = getLord();

        Planet planet = getPlanet(lord, false);
        Optional<Planet> ofResult = Optional.of(planet);
        when(this.planetRepository.findById(any())).thenReturn(ofResult);

        Lord lord1 = getLord();
        Optional<Lord> ofResult1 = Optional.of(lord1);
        when(this.lordRepository.findById(any())).thenReturn(ofResult1);
        this.planetService.setLordToPlanet(123L, 123L);
        verify(this.planetRepository).findById(any());
        verify(this.lordRepository).findById(any());
        assertTrue(this.planetService.getAllPlanets().isEmpty());
    }

    @Test
    public void testSetLordToPlanet2() {
        Lord lord = getLord();

        Planet planet = getPlanet(lord, false);
        Optional<Planet> ofResult = Optional.of(planet);

        Lord lord1 = getLord();

        Planet planet1 = getPlanet(lord1, false);
        when(this.planetRepository.save(any())).thenReturn(planet1);
        when(this.planetRepository.findById(any())).thenReturn(ofResult);

        Lord lord2 = getLord();
        Optional<Lord> ofResult1 = Optional.of(lord2);
        when(this.lordRepository.findById(any())).thenReturn(ofResult1);
        this.planetService.setLordToPlanet(123L, 123L);
        verify(this.planetRepository).findById(any());
        verify(this.planetRepository).save(any());
        verify(this.lordRepository).findById(any());
        assertTrue(this.planetService.getAllPlanets().isEmpty());
    }

    @Test
    public void testSetLordToPlanet3() {
        Lord lord = getLord();

        Planet planet = getPlanet(lord, false);
        when(this.planetRepository.save(any())).thenReturn(planet);
        when(this.planetRepository.findById(any())).thenReturn(Optional.empty());

        Lord lord1 = getLord();
        Optional<Lord> ofResult = Optional.of(lord1);
        when(this.lordRepository.findById(any())).thenReturn(ofResult);
        assertThrows(PlanetNotFoundException.class, () -> this.planetService.setLordToPlanet(123L, 123L));
        verify(this.planetRepository).findById(any());
    }

    @Test
    public void testSetLordToPlanet4() {
        Lord lord = getLord();

        Planet planet = getPlanet(lord, false);
        Optional<Planet> ofResult = Optional.of(planet);

        Lord lord1 = getLord();

        Planet planet1 = getPlanet(lord1, true);
        when(this.planetRepository.save(any())).thenReturn(planet1);
        when(this.planetRepository.findById(any())).thenReturn(ofResult);
        when(this.lordRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(LordNotFoundException.class, () -> this.planetService.setLordToPlanet(123L, 123L));
        verify(this.planetRepository).findById(any());
        verify(this.lordRepository).findById(any());
    }

    @Test
    public void testDeletePlanet() {
        doNothing().when(this.planetRepository).deleteById(any());
        this.planetService.deletePlanet(123L);
        verify(this.planetRepository).deleteById(any());
        assertTrue(this.planetService.getAllPlanets().isEmpty());
    }

    @Test
    public void testFindPlanetById() {
        Lord lord = getLord();

        Planet planet = getPlanet(lord, false);
        Optional<Planet> ofResult = Optional.of(planet);
        when(this.planetRepository.findById(any())).thenReturn(ofResult);
        assertSame(planet, this.planetService.findPlanetById(123L));
        verify(this.planetRepository).findById(any());
        assertTrue(this.planetService.getAllPlanets().isEmpty());
    }

    @Test
    public void testFindPlanetById2() {
        when(this.planetRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(PlanetNotFoundException.class, () -> this.planetService.findPlanetById(123L));
        verify(this.planetRepository).findById(any());
    }
}

