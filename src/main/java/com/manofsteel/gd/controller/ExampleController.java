package com.manofsteel.gd.controller;

import com.manofsteel.gd.service.ExampleService;
import com.manofsteel.gd.type.dto.ExampleDto;
import com.manofsteel.gd.type.dto.ResponseModel;
import com.manofsteel.gd.type.entity.Example;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/example")
@RequiredArgsConstructor
@Tag(name = "Example")
@Slf4j
public class ExampleController {

        private final ExampleService exampleService;

        @Operation(summary = "메인페이지 예제 추가", description = "메인페이지 예제 추가")
//        @Secured({"ROLE_ADMIN"})
        @PostMapping
        public ResponseModel addExample(@RequestBody ExampleDto exampleDto) {
                Example example = exampleService.saveExample(exampleDto);
                ResponseModel responseModel = ResponseModel.builder().httpStatus(HttpStatus.CREATED).build();
                return responseModel;
        }

        @Operation(summary = "메인페이지 예제 삭제", description = "메인페이지 예제 삭제")
//        @Secured({"ROLE_ADMIN"})
        @DeleteMapping("/{serialNum}")
        public ResponseModel deleteExample(@PathVariable Long serialNum) {
                exampleService.deleteExample(serialNum);
                ResponseModel responseModel = ResponseModel.builder().build();
                return responseModel;
        }

        @Operation(summary = "모든 예제 조회", description = "모든 예제 조회")
//  @Secured({"ROLE_ADMIN", "ROLE_USER"})
        @GetMapping
        public ResponseModel getAllExamples() {
                List<Example> exampleList = exampleService.getAllExamples();
                ResponseModel responseModel = ResponseModel.builder().build();
                responseModel.addData("example", exampleList);
                return responseModel;
        }

}
