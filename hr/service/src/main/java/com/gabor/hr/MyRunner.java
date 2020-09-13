package com.gabor.hr;

import com.gabor.hr.model.City;
import com.gabor.hr.repository.CityRepository;
import com.gabor.hr.service.RequestService;
import com.gabor.hr.service.dto.RequestDto;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MyRunner implements CommandLineRunner {

    @Autowired
    private RequestService requestService;

    @Override
    public void run(String... args) {
        if (args.length > 0) {
            if ("mongodb-update".equals(args[0])) {
                //update mongodb based on what is present in the MySQL database
                List<RequestDto> all = requestService.findAll();


                for (RequestDto requestDto : all) {
                    requestService.save(requestDto);
                }
            }
        }
    }
}