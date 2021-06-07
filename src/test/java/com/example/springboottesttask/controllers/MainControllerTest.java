package com.example.springboottesttask.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.springboottesttask.controllers.dto.LordAndPlanetRequestDTO;
import com.example.springboottesttask.entities.Lord;
import com.example.springboottesttask.entities.Planet;
import com.example.springboottesttask.repositories.LordRepository;
import com.example.springboottesttask.repositories.PlanetRepository;
import com.example.springboottesttask.services.LordService;
import com.example.springboottesttask.services.PlanetService;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ConcurrentModel;

@ContextConfiguration(classes = {MainController.class})
@ExtendWith(SpringExtension.class)
public class MainControllerTest {
    @MockBean
    private LordService lordService;

    @Autowired
    private MainController mainController;

    @MockBean
    private PlanetService planetService;

    @Test
    public void testDeletePlanetById() {
        PlanetRepository planetRepository = mock(PlanetRepository.class);
        doNothing().when(planetRepository).deleteById(any());
        PlanetService planetService = new PlanetService(planetRepository, mock(LordRepository.class));
        assertEquals("redirect:/main",
                (new MainController(planetService, new LordService(mock(LordRepository.class), mock(PlanetRepository.class))))
                        .deletePlanetById(123L));
        verify(planetRepository).deleteById(any());
    }

    @Test
    public void testGetPlanetsForLord() {
        Lord lord = new Lord();
        lord.setPlanetList(new ArrayList<>());
        lord.setLordId(123L);
        lord.setName("Name");
        lord.setAge(1);
        LordRepository lordRepository = mock(LordRepository.class);
        when(lordRepository.findById(any())).thenReturn(Optional.of(lord));
        LordService lordService = new LordService(lordRepository, mock(PlanetRepository.class));
        MainController mainController = new MainController(
                new PlanetService(mock(PlanetRepository.class), mock(LordRepository.class)), lordService);
        assertEquals("showForm", mainController.getPlanetsForLord(new ConcurrentModel(), 123L));
        verify(lordRepository).findById(any());
    }

    @Test
    public void testCreateLord() {
        Lord lord = new Lord();
        lord.setPlanetList(new ArrayList<>());
        lord.setLordId(123L);
        lord.setName("Name");
        lord.setAge(1);
        LordRepository lordRepository = mock(LordRepository.class);
        when(lordRepository.save(any())).thenReturn(lord);
        LordService lordService = new LordService(lordRepository, mock(PlanetRepository.class));
        MainController mainController = new MainController(
                new PlanetService(mock(PlanetRepository.class), mock(LordRepository.class)), lordService);
        assertEquals("redirect:/main", mainController.createLord(new Lord("Name", 1)));
        verify(lordRepository).save(any());
    }

    @Test
    public void testCreateLord2() {
        PlanetService planetService = new PlanetService(mock(PlanetRepository.class), mock(LordRepository.class));
        MainController mainController = new MainController(planetService,
                new LordService(mock(LordRepository.class), mock(PlanetRepository.class)));
        assertEquals("addLord", mainController.createLord(new ConcurrentModel()));
    }

    @Test
    public void testCreateLPlanet() {
        PlanetService planetService = new PlanetService(mock(PlanetRepository.class), mock(LordRepository.class));
        MainController mainController = new MainController(planetService,
                new LordService(mock(LordRepository.class), mock(PlanetRepository.class)));
        assertEquals("addPlanet", mainController.createLPlanet(new ConcurrentModel()));
    }

    @Test
    public void testCreatePlanet() {
        Lord lord = new Lord();
        lord.setPlanetList(new ArrayList<>());
        lord.setLordId(123L);
        lord.setName("Name");
        lord.setAge(1);

        Planet planet = new Planet();
        planet.setLord(lord);
        planet.setPlanetId(123L);
        planet.setName("Name");
        planet.setLordId(123L);
        planet.setIsOwned(true);
        PlanetRepository planetRepository = mock(PlanetRepository.class);
        when(planetRepository.save(any())).thenReturn(planet);
        PlanetService planetService = new PlanetService(planetRepository, mock(LordRepository.class));
        MainController mainController = new MainController(planetService,
                new LordService(mock(LordRepository.class), mock(PlanetRepository.class)));
        Planet planet1 = new Planet("Name");
        assertEquals("redirect:/main", mainController.createPlanet(planet1));
        verify(planetRepository).save(any());
        assertFalse(planet1.getIsOwned());
    }

    @Test
    public void testAppointLordToRuleThePlanet() {
        Lord lord = new Lord();
        lord.setPlanetList(new ArrayList<>());
        lord.setLordId(123L);
        lord.setName("Name");
        lord.setAge(1);

        Planet planet = new Planet();
        planet.setLord(lord);
        planet.setPlanetId(123L);
        planet.setName("Name");
        planet.setLordId(123L);
        planet.setIsOwned(true);
        PlanetRepository planetRepository = mock(PlanetRepository.class);
        when(planetRepository.findById(any())).thenReturn(Optional.of(planet));

        Lord lord1 = new Lord();
        lord1.setPlanetList(new ArrayList<>());
        lord1.setLordId(123L);
        lord1.setName("Name");
        lord1.setAge(1);
        LordRepository lordRepository = mock(LordRepository.class);
        when(lordRepository.findById(any())).thenReturn(Optional.of(lord1));
        PlanetService planetService = new PlanetService(planetRepository, lordRepository);

        Lord lord2 = new Lord();
        lord2.setPlanetList(new ArrayList<>());
        lord2.setLordId(123L);
        lord2.setName("Name");
        lord2.setAge(1);
        LordRepository lordRepository1 = mock(LordRepository.class);
        when(lordRepository1.findById(any())).thenReturn(Optional.of(lord2));

        Lord lord3 = new Lord();
        lord3.setPlanetList(new ArrayList<>());
        lord3.setLordId(123L);
        lord3.setName("Name");
        lord3.setAge(1);

        Planet planet1 = new Planet();
        planet1.setLord(lord3);
        planet1.setPlanetId(123L);
        planet1.setName("Name");
        planet1.setLordId(123L);
        planet1.setIsOwned(true);
        PlanetRepository planetRepository1 = mock(PlanetRepository.class);
        when(planetRepository1.findById(any())).thenReturn(Optional.of(planet1));
        MainController mainController = new MainController(planetService,
                new LordService(lordRepository1, planetRepository1));

        LordAndPlanetRequestDTO lordAndPlanetRequestDTO = new LordAndPlanetRequestDTO();
        lordAndPlanetRequestDTO.setPlanetId(123L);
        lordAndPlanetRequestDTO.setLordId(0L);
        assertEquals("redirect:/main", mainController.appointLordToRuleThePlanet(lordAndPlanetRequestDTO));
        verify(planetRepository).findById(any());
        verify(lordRepository).findById(any());
        verify(lordRepository1).findById(any());
        verify(planetRepository1).findById(any());
    }

    @Test
    public void testAppointLordToRuleThePlanet2() {
        PlanetService planetService = new PlanetService(mock(PlanetRepository.class), mock(LordRepository.class));
        MainController mainController = new MainController(planetService,
                new LordService(mock(LordRepository.class), mock(PlanetRepository.class)));
        assertEquals("appointForm", mainController.appointLordToRuleThePlanet(new ConcurrentModel()));
    }

    @Test
    public void testShowLordsAndPlanets() throws Exception {
        when(this.planetService.getAllPlanets()).thenReturn(new ArrayList<>());
        when(this.lordService.getLordsByAge()).thenReturn(new ArrayList<>());
        when(this.lordService.getLordsWithoutPlanets()).thenReturn(new ArrayList<>());
        when(this.lordService.getAllLords()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/main");
        MockMvcBuilders.standaloneSetup(this.mainController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4))
                .andExpect(
                        MockMvcResultMatchers.model().attributeExists("lords", "lordsLoungers", "planets", "theYoungestLords"))
                .andExpect(MockMvcResultMatchers.view().name("lordsAndPlanets"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("lordsAndPlanets"));
    }
}

