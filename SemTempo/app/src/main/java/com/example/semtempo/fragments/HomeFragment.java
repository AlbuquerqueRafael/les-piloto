package com.example.semtempo.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.semtempo.R;
import com.example.semtempo.adapters.RecentTasksAdapter;
import com.example.semtempo.adapters.SubtitlesAdapter;
import com.example.semtempo.utils.Utils;
import com.txusballesteros.widgets.FitChart;
import com.txusballesteros.widgets.FitChartValue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.semtempo.controllers.AtividadeController;
import com.example.semtempo.model.Atividade;
import com.example.semtempo.model.Horario;
import com.example.semtempo.model.Prioridade;

public class HomeFragment extends Fragment {

    // Lista de cores agradaveis
    private final String[] niceColors = {"#66CCFF", "#667FFF", "#9966FF", "#E666FF", "#FF66CC", "#FF667F", "#FF9966",
            "#FFE666", "#CCFF66", "#7FFF66", "#66FF99", "#66FFE6", "#29B8FF", "#009CEB",
            "#FF7029", "#EB4E00", "#FFE666", "#CCFF66", "#7FFF66", "#66FF99", "#66FFE6",
            "#66CCFF", "#667FFF", "#9966FF", "#E666FF", "#FF66CC", "#FF667F", "#FF7029",
            "#EB4E00", "#29B8FF", "#009CEB"};


    private final int ADD_ICON = R.drawable.ic_add_white_24dp;
    private View rootView;
    private ListView subtitles;
    private ListView recentTasks;
    private TextView seeMore;
    private List<Atividade> atividades;
    private Map<Atividade, Integer> atividadesDaSemana;
    private List<Integer> chartColors;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        seeMore = (TextView) rootView.findViewById(R.id.see_more);

        seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeeMoreFragment fragment = new SeeMoreFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            }
        });

        setUp();
        setFab();
        if (atividadesDaSemana != null) {
            fillChartCollors();
            plotChart();
        }
        loadRecentTasks();

        return rootView;
    }

    private void setFab(){
        FloatingActionButton addFab = (FloatingActionButton) getActivity().findViewById(R.id.add_fab);
        addFab.setImageResource(ADD_ICON);
        addFab.setVisibility(View.VISIBLE);

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddFragment fragment = new AddFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();

            }
        });
    }

    private void loadRecentTasks(){
        recentTasks = (ListView) rootView.findViewById(R.id.recent_tasks);
        List<Atividade> atividades_recentes= new ArrayList<>();

        int i = 0;
        while (i < 5){
            atividades_recentes.add(atividades.get(i));
            i++;
        }

        recentTasks.setAdapter(new RecentTasksAdapter(getActivity(), atividades_recentes, rootView));
        Utils.setListViewHeightBasedOnChildren(recentTasks);

    }

    private void plotChart(){
        subtitles = (ListView) rootView.findViewById(R.id.subtitles);
        TextView perc_text =(TextView) rootView.findViewById(R.id.text_perc);

        List<String> valores = new ArrayList<>();
        List<Float> perc = new ArrayList<>();
        float totalHours = 0;

        for (Map.Entry<Atividade, Integer> entry : atividadesDaSemana.entrySet()) {
            valores.add(entry.getKey().getNome());
            totalHours += entry.getValue();
        }

        for (Map.Entry<Atividade, Integer> entry : atividadesDaSemana.entrySet()) {
            perc.add((entry.getValue()/totalHours)*100f);
        }

        subtitles.setAdapter(new SubtitlesAdapter(getActivity(), valores, chartColors, perc, perc_text, rootView));
        subtitles.setDivider(null);
        Utils.setListViewHeightBasedOnChildren(subtitles);

        final FitChart fitChart = (FitChart) rootView.findViewById(R.id.fitChart);
        fitChart.setMinValue(0f);

        fitChart.setMaxValue(100f);

        Collection<FitChartValue> values = new ArrayList<>();

        for (int i = 0; i < valores.size(); i++) {
            values.add(new FitChartValue(perc.get(i), chartColors.get(i)));
        }

        fitChart.setValues(values);
    }

    private void fillChartCollors(){
        chartColors = new ArrayList<>();

        for (int i = 0; i < atividadesDaSemana.size(); i++) {
            int color = Color.parseColor(niceColors[i]);
            chartColors.add(color);
        }
    }


    private void setUp(){

        atividades = new ArrayList<>();

        Atividade atv1 = new Atividade("Jogar bola na UFCG", Prioridade.ALTA);
        atv1.registrarNovoHorario(new Horario(2, new GregorianCalendar()));

        Atividade atv2 = new Atividade("Fazer cocô", Prioridade.BAIXA);
        atv2.registrarNovoHorario(new Horario(8, new GregorianCalendar()));

        Atividade atv3 = new Atividade("Quebrar o dente", Prioridade.MEDIA);
        atv3.registrarNovoHorario(new Horario(5, new GregorianCalendar()));

        Atividade atv4 = new Atividade("Pular da janela", Prioridade.ALTA);
        atv4.registrarNovoHorario(new Horario(2, new GregorianCalendar()));
        atv4.registrarNovoHorario(new Horario(3, new GregorianCalendar()));

        Atividade atv5 = new Atividade("Quebrar a orelha", Prioridade.MEDIA);
        atv5.registrarNovoHorario(new Horario(1, new GregorianCalendar()));

        Atividade atv6 = new Atividade("Humilhar no LOL", Prioridade.MEDIA);
        atv6.registrarNovoHorario(new Horario(2, new GregorianCalendar()));

        Atividade atv7 = new Atividade("Cagar no DotA", Prioridade.MEDIA);
        atv7.registrarNovoHorario(new Horario(2, new GregorianCalendar()));

        Atividade atv8 = new Atividade("Morrer no CS", Prioridade.MEDIA);
        atv8.registrarNovoHorario(new Horario(2, new GregorianCalendar()));

        atividades.add(atv1);
        atividades.add(atv2);
        atividades.add(atv3);
        atividades.add(atv4);
        atividades.add(atv5);
        atividades.add(atv6);
        atividades.add(atv7);
        atividades.add(atv8);

        setUpWeek();

    }

    private void setUpWeek(){

        atividadesDaSemana = new HashMap<>();

        Calendar cal = Calendar.getInstance();
        GregorianCalendar date = new GregorianCalendar();
        int month = date.get(GregorianCalendar.MONTH);
        int day = date.get(GregorianCalendar.DAY_OF_MONTH);
        int year = date.get(GregorianCalendar.YEAR);

        cal.set(year, month, day);
        int week = cal.get(Calendar.WEEK_OF_YEAR);

        atividadesDaSemana = AtividadeController.filtrarAtvsPorTempoInvestido(atividades, week);
    }

}