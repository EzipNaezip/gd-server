package com.manofsteel.gd.service;

import com.manofsteel.gd.repository.ExampleRepository;
import com.manofsteel.gd.type.dto.ExampleDto;
import com.manofsteel.gd.type.entity.Example;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExampleService {

    private final ExampleRepository exampleRepository;

    public Example saveExample(ExampleDto exampleDto) {
        Example example = Example.builder()
                .imgUrl(exampleDto.getImgUrl())
                .prompt(exampleDto.getPrompt())
                .build();

        return exampleRepository.save(example);
    }

    public void deleteExample(Long serialNum) {
        exampleRepository.deleteById(serialNum);
    }

    public List<Example> getAllExamples() {
        return exampleRepository.findAll();
    }

}
