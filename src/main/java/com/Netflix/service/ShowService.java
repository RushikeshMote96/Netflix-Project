package com.Netflix.service;

import com.Netflix.acessor.ShowAccessor;
import com.Netflix.acessor.model.ShowDTO;
import com.Netflix.controller.model.ShowOutput;
import com.Netflix.mapper.ShowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ShowService {
    @Autowired
        ShowAccessor showAccessor;
    @Autowired
        ShowMapper showMapper;

    public List<ShowOutput> getShowsByName(final String showName){
        List<ShowDTO> showDTOList = showAccessor.getShowByName(showName);
        List<ShowOutput> showOutput = new ArrayList<>();

        for(ShowDTO showDTO : showDTOList){
            showOutput.add(showMapper.mapShowDtoToOutput(showDTO));
        }
        return showOutput;

    }

}
