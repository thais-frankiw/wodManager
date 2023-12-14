package com.infnet.java.wodManager.service;
import com.infnet.java.wodManager.exception.ResourceNotFoundException;
import com.infnet.java.wodManager.model.Wod;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class WodService {
    @Autowired
    private  ApiServico  apiExterna;
    Logger logger = LoggerFactory.getLogger(Wod.class);
    private static List<Wod> wods = new ArrayList<>();
    @Getter
    private int ultimoId = 7;

    static {
        wods.add(Wod.builder()
                .id(1)
                .nome("Fran")
                .tipo("For Time")
                .duracaoMinutos(10)
                .categoria("Girl Benchmark")
                .descricao(Arrays.asList("21 Thrusters 95/65 lbs", "21 Pull ups", "15 Thrusters 95/65 lbs", "15 Pull ups", "9 Thrusters 95/65 lbs", "9 Pull ups"))
                .score("5min 44seg")
                .data(LocalDate.of(2020, 2, 12))
                .build());

        wods.add(Wod.builder()
                .id(2)
                .nome("DT")
                .tipo("For Time")
                .duracaoMinutos(10)
                .categoria("Hero Wod")
                .descricao(Arrays.asList("5 Rounds:", "12 Deadlifts 155/105 lbs", "9 Hang Power Cleans 155/105 lbs", "6 Push Jerks 155/105 lbs"))
                .score("6min 56seg")
                .data(LocalDate.of(2023, 12, 8))
                .build());


        wods.add(Wod.builder()
                .id(3)
                .nome("Murph")
                .tipo("For Time")
                .duracaoMinutos(180)
                .categoria("Hero Wod")
                .descricao(Arrays.asList("1 Mile Run", "100 Pull-Ups", "200 Push-Ups", "300 Squats", "1 Mile Run"))
                .score("85min 38seg")
                .data(LocalDate.of(2016, 11, 27))
                .build());

        wods.add(Wod.builder()
                .id(4)
                .nome("Cindy")
                .tipo("AMRAP")
                .duracaoMinutos(20)
                .categoria("Girl Benchmark")
                .descricao(Arrays.asList("5 Pull-Ups", "10 Push-Ups", "15 Squats"))
                .score("15 rounds + 25 reps")
                .data(LocalDate.of(2022, 5, 23))
                .build());

        wods.add(Wod.builder()
                .id(5)
                .nome("Nancy")
                .tipo("For Time")
                .duracaoMinutos(20)
                .categoria("Girl Benchmark")
                .descricao(Arrays.asList("5 Rounds: ", "400 Meter Run", "15 Overhead Squats 95/65 lbs"))
                .score("14min 17seg")
                .data(LocalDate.of(2023, 6, 12))
                .build());

        wods.add(Wod.builder()
                .id(6)
                .nome("Kelly")
                .tipo("For Time")
                .duracaoMinutos(30)
                .categoria("Girl Benchmark")
                .descricao(Arrays.asList("5 Rounds:", "400 Meter Run", "30 Box Jumps 24/20 in", "30 Wall Balls 20/14 lbs"))
                .score("28min 23seg")
                .data(LocalDate.of(2023, 9, 19))
                .build());

        wods.add(Wod.builder()
                .id(7)
                .nome("CrossFit Total")
                .tipo("For Max Weight")
                .duracaoMinutos(null) // Duracao não se aplica a este Wod
                .categoria("Levantamento de Peso")
                .descricao(Arrays.asList("Back Squat (1 Rep Max)", "Shoulder Press (1 Rep Max)", "Deadlift (1 Rep Max)"))
                .score("255 lbs, 110 lbs, 275 lbs")
                .data(LocalDate.of(2023, 8, 10))
                .build());
    }



    public List<Wod> getAll() {
        return wods.stream().toList();
    }


    public List<Wod> getWodsByCategoriaETipo(String categoria, String tipo) {
        if ((categoria == null || categoria.isEmpty()) && (tipo == null || tipo.isEmpty())) {
            return getAll();
        }
        Stream<Wod> stream = wods.stream();
        if (categoria != null && !categoria.isEmpty()) {
            stream = stream.filter(w -> w.getCategoria().equalsIgnoreCase(categoria));
        }
        if (tipo != null && !tipo.isEmpty()) {
            stream = stream.filter(w -> w.getTipo().equalsIgnoreCase(tipo));
        }
        return stream.collect(Collectors.toList());
    }


    public Wod getById(int id) {
        return wods.stream()
                .filter(wod -> wod.getId() == id)
                .findFirst().map(wod -> {
                     wod.setFrase(apiExterna.getAdvice());
                     return wod;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Wod com ID " + id + " não encontrado."));
    }

    public Wod deletarById(int id) {
        Wod wodParaRemover = getById(id);
        wods.remove(wodParaRemover);
        return wodParaRemover;
    }

    public int incrementarId(){
        this.ultimoId++;
        return ultimoId;
    }

    public void criarWod(Wod wod) {
        int id = incrementarId();
        wod.setId(id);
        wods.add(wod);
    }

    public Wod alterarWod(int id, Wod wodDetalhes) {
        Wod wodExistente = getById(id);
        if (wodExistente == null) {
            throw new ResourceNotFoundException("Wod com ID " + id + " não encontrado.");
        }
        wodExistente.setNome(wodDetalhes.getNome());
        wodExistente.setTipo(wodDetalhes.getTipo());
        wodExistente.setDuracaoMinutos(wodDetalhes.getDuracaoMinutos());
        wodExistente.setCategoria(wodDetalhes.getCategoria());
        wodExistente.setDescricao(wodDetalhes.getDescricao());
        wodExistente.setScore(wodDetalhes.getScore());
        wodExistente.setData(wodDetalhes.getData());

        return wodExistente;
    }
}


