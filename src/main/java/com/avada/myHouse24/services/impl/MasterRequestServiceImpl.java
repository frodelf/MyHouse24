package com.avada.myHouse24.services.impl;

import com.avada.myHouse24.entity.MasterRequest;
import com.avada.myHouse24.repo.MasterRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MasterRequestServiceImpl {
    private final MasterRequestRepository masterRequestRepository;
    public List<MasterRequest> getPage(int pageNumber, ModelAndView model){
        --pageNumber;
        double size = 10.0;
        int max = (int)Math.ceil(masterRequestRepository.findAll().size()/size-1) > 0 ? (int)Math.ceil(masterRequestRepository.findAll().size()/size-1) : 0;
        if(pageNumber < 0)pageNumber = 0;
        if(pageNumber > max)pageNumber = max;
        PageRequest pageRequest = PageRequest.of(pageNumber, (int)size);
        model.addObject("max", max);
        model.addObject("current", pageNumber+1);
        return masterRequestRepository.findAll(pageRequest).getContent();
    }
    public void save(MasterRequest masterRequest){
        masterRequestRepository.save(masterRequest);
    }
    public void deleteById(long id){
        masterRequestRepository.deleteById(id);
    }
    public MasterRequest getById(long id){
        return masterRequestRepository.findById(id).get();
    }
}
