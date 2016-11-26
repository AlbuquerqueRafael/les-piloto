package com.example.semtempo.controllers;

import java.util.*;
import com.example.semtempo.model.Horario;
import com.example.semtempo.model.Atividade;
import com.example.semtempo.model.Prioridade;

/**
 * Classe que atua sobre uma lista de atividades, filtrando-as sob os mais diferentes critérios.
 */
public class AtividadeController {

    /**
     * Um retorno apropriado para representar o que a função executa diz respeito a um mapa
     * que relaciona as atividades com horas investidas nelas, levando em conta apenas a semana passada como parâmetro.
     */
    public static Map<Atividade, Integer> filtrarAtvsPorTempoInvestido(Collection<Atividade> atvsASeremFiltradas, int semanaEscolhida){

        Map<Atividade, Integer> atvsFiltradas = new HashMap<>();

        for (Atividade atividade: atvsASeremFiltradas){
            for (Horario horario: atividade.getHorarios()){
                if (horario.getDataQueRealizou().get(Calendar.WEEK_OF_YEAR) == semanaEscolhida){
                    if (atvsFiltradas.containsKey(atividade)){
                        int actualTime = atvsFiltradas.get(atividade);
                        atvsFiltradas.put(atividade, actualTime + horario.getTotalHorasInvestidas());
                    } else {
                        atvsFiltradas.put(atividade, horario.getTotalHorasInvestidas());
                    }
                }
            }
        }

        return atvsFiltradas;
    }

    /**
     * Um retorno apropriado para representar o que a função executa diz respeito a uma lista
     * apenas com as atividades realizadas naquela semana.
     */
    public static List<Atividade> filtraAtvsPelaSemana(Collection<Atividade> atvsASeremFiltradas, int semanaEscolhida){
        List<Atividade> atvsFiltradas = new ArrayList<>();

        for (Atividade atividade: atvsASeremFiltradas){
            for (Horario horario: atividade.getHorarios()){
                if (horario.getDataQueRealizou().get(Calendar.WEEK_OF_YEAR) == semanaEscolhida){
                    atvsFiltradas.add(atividade);
                    break;
                }
            }
        }
        return atvsFiltradas;
    }


    /**
     * Um retorno apropriado para representar o que a função executa diz respeito a um mapa
     * que relaciona a prioridade da atividade com as horas investidas nelas.
     */
    public static Map<Prioridade, Integer> agrupaAtvsPorPrioridade(Collection<Atividade> atvsASeremFiltradas){

        Map<Prioridade, Integer> atvsFiltradas = new HashMap<>();

        for (Atividade atividade: atvsASeremFiltradas){
            atvsFiltradas.put(atividade.getPrioridade(), atividade.calcularTotalDeHorasInvestidas());
        }

        return atvsFiltradas;
    }

}