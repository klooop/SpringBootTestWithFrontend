package com.example.springboottesttask.services;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.springboottesttask.entities.Lord;
import com.example.springboottesttask.entities.Planet;
import com.example.springboottesttask.exception.LordNotFoundException;
import com.example.springboottesttask.exception.PlanetNotFoundException;
import com.example.springboottesttask.repositories.LordRepository;
import com.example.springboottesttask.repositories.PlanetRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {LordService.class})
@ExtendWith(SpringExtension.class)
public class LordServiceTest {
    @MockBean
    private LordRepository lordRepository;

    @Autowired
    private LordService lordService;

    @MockBean
    private PlanetRepository planetRepository;


    private Lord getLord() {
        Lord lord = new Lord();
        lord.setPlanetList(new ArrayList<>());
        lord.setLordId(123L);
        lord.setName("Name");
        lord.setAge(1);
        return lord;
    }
    private Planet getPlanet(Lord lord) {
        Planet planet = new Planet();
        planet.setLord(lord);
        planet.setPlanetId(123L);
        planet.setName("Name");
        planet.setLordId(123L);
        planet.setIsOwned(false);
        return planet;
    }
    @Test
    public void testFindLordById() {
        Lord lord = getLord();
        Optional<Lord> ofResult = Optional.of(lord);
        when(this.lordRepository.findById(any())).thenReturn(ofResult);
        assertSame(lord, this.lordService.findLordById(123L));
        verify(this.lordRepository).findById(any());
        assertTrue(this.lordService.getAllLords().isEmpty());
    }


    @Test
    public void testFindLordById2() {
        when(this.lordRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(LordNotFoundException.class, () -> this.lordService.findLordById(123L));
        verify(this.lordRepository).findById(any());
    }

    @Test
    public void testGetAllLords() {
        when(this.lordRepository.findAll()).thenThrow(new PlanetNotFoundException("An error occurred"));
        assertThrows(PlanetNotFoundException.class, () -> this.lordService.getAllLords());
        verify(this.lordRepository).findAll();
    }

    @Test
    public void testGetLordsByAge() {
        when(this.lordRepository.getLordsByAge()).thenReturn(new ArrayList<>());
        assertTrue(this.lordService.getLordsByAge().isEmpty());
        verify(this.lordRepository).getLordsByAge();
        assertTrue(this.lordService.getAllLords().isEmpty());
    }

    @Test
    public void testGetLordsWithoutPlanets() {
        ArrayList<Lord> lordList = new ArrayList<>();
        when(this.lordRepository.getLordsByPlanetList()).thenReturn(lordList);
        List<Lord> actualLordsWithoutPlanets = this.lordService.getLordsWithoutPlanets();
        assertSame(lordList, actualLordsWithoutPlanets);
        assertTrue(actualLordsWithoutPlanets.isEmpty());
        verify(this.lordRepository).getLordsByPlanetList();
        assertTrue(this.lordService.getAllLords().isEmpty());
    }

    @Test
    public void testGetPlanetsForLord() {
        Lord lord = new Lord();
        ArrayList<Planet> planetList = new ArrayList<>();
        lord.setPlanetList(planetList);
        lord.setLordId(123L);
        lord.setName("Name");
        lord.setAge(1);
        Optional<Lord> ofResult = Optional.of(lord);
        when(this.lordRepository.findById(any())).thenReturn(ofResult);
        List<Planet> actualPlanetsForLord = this.lordService.getPlanetsForLord(123L);
        assertSame(planetList, actualPlanetsForLord);
        assertTrue(actualPlanetsForLord.isEmpty());
        verify(this.lordRepository).findById(any());
        assertTrue(this.lordService.getAllLords().isEmpty());
    }

    @Test
    public void testGetPlanetsForLord2() {
        when(this.lordRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(LordNotFoundException.class, () -> this.lordService.getPlanetsForLord(123L));
        verify(this.lordRepository).findById(any());
    }

    @Test
    public void testSaveLord() {
        Lord lord = getLord();
        when(this.lordRepository.save(any())).thenReturn(lord);
        this.lordService.saveLord(new Lord("Name", 1));
        verify(this.lordRepository).save(any());
        assertTrue(this.lordService.getAllLords().isEmpty());
    }

    @Test
    public void testSetPlanetToLord() {
        Lord lord = getLord();

        Planet planet = getPlanet(lord);
        Optional<Planet> ofResult = Optional.of(planet);
        when(this.planetRepository.findById(any())).thenReturn(ofResult);

        Lord lord1 = getLord();
        Optional<Lord> ofResult1 = Optional.of(lord1);
        when(this.lordRepository.findById(any())).thenReturn(ofResult1);
        this.lordService.setPlanetToLord(123L, 123L);
        verify(this.planetRepository).findById(any());
        verify(this.lordRepository).findById(any());
        assertTrue(this.lordService.getAllLords().isEmpty());
    }



    @Test
    public void testSetPlanetToLord2() {
        Lord lord = getLord();

        Planet planet = getPlanet(lord);
        Optional<Planet> ofResult = Optional.of(planet);
        when(this.planetRepository.findById(any())).thenReturn(ofResult);

        Lord lord1 = getLord();
        Optional<Lord> ofResult1 = Optional.of(lord1);

        Lord lord2 = getLord();
        when(this.lordRepository.save(any())).thenReturn(lord2);
        when(this.lordRepository.findById(any())).thenReturn(ofResult1);
        this.lordService.setPlanetToLord(123L, 123L);
        verify(this.planetRepository).findById(any());
        verify(this.lordRepository).findById(any());
        verify(this.lordRepository).save(any());
        assertTrue(this.lordService.getAllLords().isEmpty());
    }

    @Test
    public void testSetPlanetToLord3() {
        when(this.planetRepository.findById(any())).thenReturn(Optional.empty());

        Lord lord = getLord();
        Optional<Lord> ofResult = Optional.of(lord);

        Lord lord1 = getLord();
        when(this.lordRepository.save(any())).thenReturn(lord1);
        when(this.lordRepository.findById(any())).thenReturn(ofResult);
        assertThrows(PlanetNotFoundException.class, () -> this.lordService.setPlanetToLord(123L, 123L));
        verify(this.planetRepository).findById(any());
    }

    @Test
    public void testSetPlanetToLord4() {
        Lord lord = getLord();

        Planet planet = getPlanet(lord);
        Optional<Planet> ofResult = Optional.of(planet);
        when(this.planetRepository.findById(any())).thenReturn(ofResult);

        Lord lord1 = getLord();
        when(this.lordRepository.save(any())).thenReturn(lord1);
        when(this.lordRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(LordNotFoundException.class, () -> this.lordService.setPlanetToLord(123L, 123L));
        verify(this.planetRepository).findById(any());
        verify(this.lordRepository).findById(any());
    }
}

