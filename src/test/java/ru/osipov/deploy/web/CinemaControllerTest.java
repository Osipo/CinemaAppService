package ru.osipov.deploy.web;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.osipov.deploy.models.CinemaInfo;
import ru.osipov.deploy.services.CinemaService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static ru.osipov.deploy.TestParams.*;
@ExtendWith(SpringExtension.class)
@WebMvcTest(CinemaController.class)
@AutoConfigureMockMvc
public class CinemaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CinemaService serv;

    private Gson gson = new GsonBuilder().create();

    @Test
    void testGetAll() throws Exception {
        final List<CinemaInfo> cinemas = new ArrayList<>();
        cinemas.add(new CinemaInfo(11l,PARAMS1[0],PARAMS1[1],PARAMS1[2],PARAMS1[3],PARAMS1[4]));
        cinemas.add(new CinemaInfo(12l,PARAMS2[0],PARAMS2[1],PARAMS2[2],PARAMS2[3],PARAMS2[4]));
        when(serv.getAllCinemas()).thenReturn(cinemas);

        mockMvc.perform(get("/v1/cinemas")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(11l))
                .andExpect(jsonPath("$[0].name").value(PARAMS1[0]))
                .andExpect(jsonPath("$[0].country").value(PARAMS1[1]))
                .andExpect(jsonPath("$[0].city").value(PARAMS1[2]))
                .andExpect(jsonPath("$[0].region").value(PARAMS1[3]))
                .andExpect(jsonPath("$[0].street").value(PARAMS1[4]))
                .andExpect(jsonPath("$[1].id").value(12l))
                .andExpect(jsonPath("$[1].name").value(PARAMS2[0]))
                .andExpect(jsonPath("$[1].country").value(PARAMS2[1]))
                .andExpect(jsonPath("$[1].city").value(PARAMS2[2]))
                .andExpect(jsonPath("$[1].region").value(IsNull.nullValue()))
                .andExpect(jsonPath("$[1].street").value(PARAMS2[4]));
        //cinemas.add(new CinemaInfo(13l,PARAMS1[0],PARAMS1[1],PARAMS1[2],PARAMS1[3],PARAMS1[4]));
    }

    @Test
    void getByCity() throws Exception {
        final List<CinemaInfo> cinemas = new ArrayList<>();
        cinemas.add(new CinemaInfo(11l,PARAMS1[0],PARAMS1[1],PARAMS1[2],PARAMS1[3],PARAMS1[4]));
        final List<CinemaInfo> alls = new ArrayList<>();
        alls.add(new CinemaInfo(22l,PARAMS2[0],PARAMS2[1],PARAMS2[2],PARAMS2[3],PARAMS2[4]));

        when(serv.getByCity("Kazan")).thenReturn(cinemas);
        when(serv.getAllCinemas()).thenReturn(alls);

        mockMvc.perform(get("/v1/cinemas/city/Kazan")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(11l))
                .andExpect(jsonPath("$[0].name").value(PARAMS1[0]))
                .andExpect(jsonPath("$[0].country").value(PARAMS1[1]))
                .andExpect(jsonPath("$[0].city").value(PARAMS1[2]))
                .andExpect(jsonPath("$[0].region").value(PARAMS1[3]))
                .andExpect(jsonPath("$[0].street").value(PARAMS1[4]));

        //then parameter is null return all

        mockMvc.perform(get("/v1/cinemas/city/").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(22l))
                .andExpect(jsonPath("$[0].name").value(PARAMS2[0]))
                .andExpect(jsonPath("$[0].country").value(PARAMS2[1]))
                .andExpect(jsonPath("$[0].city").value(PARAMS2[2]))
                .andExpect(jsonPath("$[0].region").value(IsNull.nullValue()))
                .andExpect(jsonPath("$[0].street").value(PARAMS2[4]));
    }

    @Test
    void getByCountry() throws Exception {
        final List<CinemaInfo> cinemas = new ArrayList<>();
        cinemas.add(new CinemaInfo(12l,PARAMS2[0],PARAMS2[1],PARAMS2[2],PARAMS2[3],PARAMS2[4]));

        final List<CinemaInfo> am = new ArrayList<>();
        am.add(new CinemaInfo(99l,PARAMS4[0],PARAMS4[1],PARAMS4[2],PARAMS4[3],PARAMS4[4]));

        final List<CinemaInfo> alls = new ArrayList<>();
        when(serv.getAllCinemas()).thenReturn(alls);
        when(serv.getByCountry("USA")).thenReturn(am);
        when(serv.getByCountry("The Russian Federation")).thenReturn(cinemas);

        mockMvc.perform(get("/v1/cinemas/country/The Russian Federation").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(12l))
                .andExpect(jsonPath("$[0].name").value(PARAMS2[0]))
                .andExpect(jsonPath("$[0].country").value(PARAMS2[1]))
                .andExpect(jsonPath("$[0].city").value(PARAMS2[2]))
                .andExpect(jsonPath("$[0].region").value(IsNull.nullValue()))
                .andExpect(jsonPath("$[0].street").value(PARAMS2[4]));

        mockMvc.perform(get("/v1/cinemas/country/USA").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(99l))
                .andExpect(jsonPath("$[0].name").value(PARAMS4[0]))
                .andExpect(jsonPath("$[0].country").value(PARAMS4[1]))
                .andExpect(jsonPath("$[0].city").value(PARAMS4[2]))
                .andExpect(jsonPath("$[0].region").value(IsNull.nullValue()))
                .andExpect(jsonPath("$[0].street").value(PARAMS4[4]));

        mockMvc.perform(get("/v1/cinemas/country/").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getByRegion() throws Exception {
        final List<CinemaInfo> an = new ArrayList<>();
        an.add(new CinemaInfo(99l,PARAMS4[0],PARAMS4[1],PARAMS4[2],PARAMS4[3],PARAMS4[4]));
        an.add(new CinemaInfo(999l,PARAMS3[0],PARAMS3[1],PARAMS3[2],PARAMS3[3],PARAMS3[4]));
        final List<CinemaInfo> empt = new ArrayList<>();
        final List<CinemaInfo> reg = new ArrayList<>();
        reg.add(new CinemaInfo(1l,PARAMS1[0],PARAMS1[1],PARAMS1[2],PARAMS1[3],PARAMS1[4]));

        when(serv.getByRegion(null)).thenReturn(an);//non null
        when(serv.getAllCinemas()).thenReturn(empt);
        when(serv.getByRegion("Moscovskyi")).thenReturn(reg);
        mockMvc.perform(get("/v1/cinemas/region/").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(99l))
                .andExpect(jsonPath("$[0].name").value(PARAMS4[0]))
                .andExpect(jsonPath("$[0].country").value(PARAMS4[1]))
                .andExpect(jsonPath("$[0].city").value(PARAMS4[2]))
                .andExpect(jsonPath("$[0].region").value(IsNull.nullValue()))
                .andExpect(jsonPath("$[0].street").value(PARAMS4[4]))
                .andExpect(jsonPath("$[1].id").value(999l))
                .andExpect(jsonPath("$[1].name").value(PARAMS3[0]))
                .andExpect(jsonPath("$[1].country").value(PARAMS3[1]))
                .andExpect(jsonPath("$[1].city").value(PARAMS3[2]))
                .andExpect(jsonPath("$[1].region").value(IsNull.nullValue()))
                .andExpect(jsonPath("$[1].street").value(PARAMS3[4]));

        mockMvc.perform(get("/v1/cinemas/region/?all=true").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        mockMvc.perform(get("/v1/cinemas/region/Moscovskyi").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1l))
                .andExpect(jsonPath("$[0].name").value(PARAMS1[0]))
                .andExpect(jsonPath("$[0].country").value(PARAMS1[1]))
                .andExpect(jsonPath("$[0].city").value(PARAMS1[2]))
                .andExpect(jsonPath("$[0].region").value(PARAMS1[3]))
                .andExpect(jsonPath("$[0].street").value(PARAMS1[4]));

    }

    @Test
    void getByStreet() throws Exception {

        final List<CinemaInfo> empt = new ArrayList<>();
        final List<CinemaInfo> str = new ArrayList<>();
        str.add(new CinemaInfo(1l,PARAMS1[0],PARAMS1[1],PARAMS1[2],PARAMS1[3],PARAMS1[4]));
        when(serv.getAllCinemas()).thenReturn(empt);
        when(serv.getByStreet("Korolenko")).thenReturn(str);
        when(serv.getByStreet(any(String.class))).thenReturn(str);
        when(serv.getByStreet("null")).thenReturn(empt);
        mockMvc.perform(get("/v1/cinemas/street/Korolenko").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1l))
                .andExpect(jsonPath("$[0].name").value(PARAMS1[0]))
                .andExpect(jsonPath("$[0].country").value(PARAMS1[1]))
                .andExpect(jsonPath("$[0].city").value(PARAMS1[2]))
                .andExpect(jsonPath("$[0].region").value(PARAMS1[3]))
                .andExpect(jsonPath("$[0].street").value(PARAMS1[4]));

        mockMvc.perform(get("/v1/cinemas/street/").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
        mockMvc.perform(get("/v1/cinemas/street/null").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getByName() throws Exception {
        final List<CinemaInfo> empt = new ArrayList<>();
        CinemaInfo ans = new CinemaInfo(1l,PARAMS1[0],PARAMS1[1],PARAMS1[2],PARAMS1[3],PARAMS1[4]);
        when(serv.getAllCinemas()).thenReturn(empt);
        when(serv.getByName("CMax")).thenReturn(ans);

        mockMvc.perform(get("/v1/cinemas/CMax").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1l))
                .andExpect(jsonPath("$[0].name").value(PARAMS1[0]))
                .andExpect(jsonPath("$[0].country").value(PARAMS1[1]))
                .andExpect(jsonPath("$[0].city").value(PARAMS1[2]))
                .andExpect(jsonPath("$[0].region").value(PARAMS1[3]))
                .andExpect(jsonPath("$[0].street").value(PARAMS1[4]));

        mockMvc.perform(get("/v1/cinemas/").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

    }
}