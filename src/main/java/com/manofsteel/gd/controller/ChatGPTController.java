package com.manofsteel.gd.controller;

import com.manofsteel.gd.auth.util.AuthUtil;
import com.manofsteel.gd.exception.NotAllowValueException;
import com.manofsteel.gd.exception.NotFoundException;
import com.manofsteel.gd.exception.PermissionDeniedException;
import com.manofsteel.gd.exception.ThumbnailCreationException;
import com.manofsteel.gd.repository.ChatLogRepository;
import com.manofsteel.gd.repository.DalleImageRepository;
import com.manofsteel.gd.repository.UserRepository;
import com.manofsteel.gd.service.*;
import com.manofsteel.gd.type.dto.*;
import com.manofsteel.gd.type.entity.*;
import com.manofsteel.gd.util.EntityUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gpt")
@io.swagger.v3.oas.annotations.tags.Tag(name = "ChatGPT")
@Slf4j
public class ChatGPTController {

    private final ChatGPTClientService chatGPTClientService;
    private final ChatLogService chatLogService;
    private final ApiKeyService apiKeyService;
    private final PostService postService;
    private final DalleService dalleImageService;
    private final TagService tagService;
    private final ChatLogRepository chatLogRepository;
    private final UserRepository userRepository;

    private final DalleImageRepository dalleImageRepository;


    private boolean generationInterrupted = false;

    // Check if generation should be interrupted
    private boolean shouldInterruptGeneration() {
        return generationInterrupted;
    }

    private ResponseModel createResponseForInterruption() {
        // Create a response model indicating the interruption
        generationInterrupted = false;
        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData("status", "generation_interrupted");
        responseModel.addData("message", "Generation process has been interrupted.");

        return responseModel;
    }

    @Operation(summary = "ChatGPT와 대화", description = "인테리어를 위한 DALL·E-2 프롬프트 생성 - FE-> InputDto 전달"+
            "<br> InputDto: String sentence"+
            "<br>   { \"sentence\" : \"모던한 룸\" }"+
            "<br> itemId(사진번호), URL, chatLogSerialNumber(채팅번호) 반환")
    //@Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PostMapping("/images")
    public ResponseModel processInteriorRequest(@RequestBody InputDto inputDto) {
        String sentence=inputDto.getSentence();

        User user = AuthUtil.getAuthenticationInfo();
        ResponseModel responseModel = ResponseModel.builder().build();
        //User user = EntityUtil.findUser(userRepository,1L);
         try {
             ApiKey apiKey = apiKeyService.findByUser(user);
             try {
                 List<Tag> tags = tagService.getTagsByContent(sentence);


                 try {
                     String prompt = chatGPTClientService.generateResponse(sentence, apiKey.getApiKey());
                     if(prompt.contains("Sorry")||prompt.contains("sorry")){
                         List<DalleItemDto> dalleItemDtoList = new ArrayList<>();

                         dalleItemDtoList.add(DalleItemDto.builder()
                                 .itemId(null)
                                 //대체이미지
                                 .url("https://via.placeholder.com/500x300")
                                 .chatLogSerialNumber(null)
                                 .build());
                         responseModel.addData("response", dalleItemDtoList);
                         responseModel.addData("description", "인테리어에 관련된 요청만 해주세요");
                         responseModel.addData("tag", null);
                         return responseModel;
                     }

                     if (shouldInterruptGeneration()) {
                         // Stop the generation process and return early
                         return createResponseForInterruption();
                     }


                     DalleImageRequest dalleImageRequest = DalleImageRequest.builder()
                             .prompt(prompt)
                             .n(4)
                             .size("1024x1024")
                             .build();
                     ChatLogDto chatLogDto = ChatLogDto.builder()
                             .message(prompt)
                             .contents(sentence)
                             .timestamp(LocalDateTime.now())
                             .build();
                     ChatLog chatLog = chatLogService.save(chatLogDto, user.getUserId());

                     List<DalleImageItem> response = dalleImageService.generateImageUrl(dalleImageRequest,apiKey.getApiKey(), chatLog);
                     List<DalleItemDto> dalleItemDtoList = new ArrayList<>();

                     for (DalleImageItem dalleImageItem : response) {
                         dalleItemDtoList.add(new DalleItemDto(dalleImageItem));
                     }
                     if (shouldInterruptGeneration()) {
                         // Stop the generation process and return early
                         return createResponseForInterruption();
                     }
                     generationInterrupted = false;

                     responseModel.addData("response", dalleItemDtoList);
                     responseModel.addData("description", prompt);
                     responseModel.addData("tag", tags);
                     return responseModel;


                 } catch (Exception e) {//gpt 가 정상작동하지 않은 경우
                     e.printStackTrace();

                     throw new NotAllowValueException("gpt가 정상작동하지 않습니다.");
                 }
             } catch (Exception e) {//tag가 없을 경우

                 e.printStackTrace();
                 //FE 에 해당 오류 전달
                 throw new NotFoundException("태그가 존재하지 않습니다.");
             }
         } catch (Exception e) {//api key가 없을 경우
             e.printStackTrace();
             throw new NotFoundException("api key가 존재하지 않습니다.");

         }
    }

    // DALL·E-2가 제공한 이미지가 마음에 든다면 저장
    @Operation(summary = "이미지 저장", description = "산출물이 맘에 든다면 해당 chatLog의 Serialnum을 받아 url을 이미지로 저장하고, post에 path 형태로 경로저장) "+
            "<br> PathVariable: serialNum(chatLog의 serialNum")
    //@Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PostMapping("/store/{serialNum}")
    public ResponseModel saveImageAndCreatePost(@PathVariable Long serialNum) throws IOException, ThumbnailCreationException {
        ChatLog chatLog = chatLogRepository.findBySerialNum(serialNum).orElseThrow(() -> new NotFoundException("채팅로그가 존재하지 않습니다."));
        User user = AuthUtil.getAuthenticationInfo();
        //User user = EntityUtil.findUser(userRepository,1L);

        List<DalleImageItem> dalleImageItems = dalleImageRepository.findAllBySerialNum(chatLog);


        String content = chatLog.getContents();
        PostDto postDto = PostDto.builder()
                .content(content)
                .timestamp(LocalDateTime.now())
                .likesCount(0L)
                .build();

        Post post = postService.save(postDto, user.getUserId());

        dalleImageItems = dalleImageService.urlToImage(chatLog, post);

        String path = dalleImageItems.stream()
                .map(DalleImageItem::getPath)
                .collect(Collectors.joining("|"));

        post.setPath(path);

        //List<Tag> tagList = tagService.getTagsByPost(post.getPostNum());

        ResponseModel responseModel = ResponseModel.builder().build();
        responseModel.addData("post", postService.save(post));


        return responseModel;
    }
    @Operation(summary = "ChatGPT와 대화 중단", description = "API 호출 취소")
    //@Secured({"ROLE_USER", "ROLE_ADMIN"})
    @DeleteMapping("/stop")
    public void interruptGeneration() {
        // Set the generationInterrupted flag to true
        generationInterrupted = true;
    }

    @Operation(summary= "맘에 들지 않는다면 chatLog 삭제" , description = "챗로그 삭제 " +
    "<br> @PathVariable Long serialNumber(챗로그 시리얼번호)")
    @DeleteMapping("/delete/{serialNum}")
    public ResponseModel deleteChatLog(@PathVariable Long serialNum) throws PermissionDeniedException {
        User user = AuthUtil.getAuthenticationInfo();
        //User user = EntityUtil.findUser(userRepository,1L);
        chatLogService.delete(serialNum,user);
        ResponseModel responseModel = ResponseModel.builder().build();
        return responseModel;

    }





}
