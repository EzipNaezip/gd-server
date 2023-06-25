package com.manofsteel.gd.service;
import com.manofsteel.gd.repository.ChatLogRepository;
import com.manofsteel.gd.repository.DalleImageRepository;
import com.manofsteel.gd.type.dto.*;
import com.manofsteel.gd.type.entity.ChatLog;
import com.manofsteel.gd.type.entity.DalleImageItem;
import com.manofsteel.gd.type.entity.Post;
import com.manofsteel.gd.util.Throttler;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import okio.BufferedSink;
import okio.Okio;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.manofsteel.gd.type.entity.QDalleImageItem.dalleImageItem;

@Component
@Service
@RequiredArgsConstructor
public class DalleService {
    private Call currentApiCall;
    private final RestTemplate restTemplate;
    private final DalleImageRepository dalleImageRepository;
    private final ChatLogRepository chatLogRepository;
    private final Throttler throttler = new Throttler(10);
    @Value("${resource.file.path}")
    private String filePath;
    @Value("${resource.file.url}")
    private String fileUrl;

    @Transactional
    public List<DalleImageItem> generateImageUrl(DalleImageRequest request, String apiKey, ChatLog chatLog) {
        try {
            throttler.throttle();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<DalleImageRequest> httpEntity = new HttpEntity<>(request, headers);


            ResponseEntity<DalleImageResponse> response = restTemplate.exchange(
                    "https://api.openai.com/v1/images/generations",
                    HttpMethod.POST,
                    httpEntity,
                    DalleImageResponse.class
            );

            List<DalleImageItem> dalleImageItemList = response.getBody().getData();

            for (DalleImageItem dalleImageItem : dalleImageItemList) {

                dalleImageItem.setSerialNum(chatLog);
                dalleImageRepository.save(dalleImageItem);
            }

            if (response.getStatusCode().is2xxSuccessful()) {
                return dalleImageItemList;
            } else {
                throw new HttpClientErrorException(response.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }
    public List<DalleImageItem> urlToImage(ChatLog chatLog, Post postId) throws IOException {
        List<DalleImageItem> dalleImageItems = dalleImageRepository.findAllBySerialNum(chatLog);

        chatLog.setPostNum(postId);
        chatLogRepository.save(chatLog);

        CountDownLatch latch = new CountDownLatch(dalleImageItems.size());
        ExecutorService executorService = Executors.newFixedThreadPool(dalleImageItems.size());

        OkHttpClient client = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool())
                .build();

        for (DalleImageItem dalleImageItem : dalleImageItems) {
            String imageUrl = dalleImageItem.getUrl();

            String fileExtension = getFileExtension(imageUrl);
            String destinationFile = generateUniqueFileName(fileExtension);
            String filename = destinationFile.substring(0, destinationFile.lastIndexOf(".")) + ".png";

            Request request = new Request.Builder().url(imageUrl).build();

            executorService.submit(() -> {
                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Failed to download file: " + response);
                    }

                    String dirPath = filePath + "/post/" + postId.getPostNum();
                    String filePath = dirPath + "/" + filename;
                    File dir = new File(dirPath);
                    File file = new File(filePath);

                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    try (BufferedSink sink = Okio.buffer(Okio.sink(file))) {
                        sink.writeAll(response.body().source());
                        System.out.println("Image saved to " + filePath);
                        String imagePath = filePath + "/post/" + postId.getPostNum() + "/" + filename;
                        String fakePath = fileUrl + "/post/" + postId.getPostNum() + "/" + filename;
                        System.out.println("imagePath: " + fakePath);
                        dalleImageItem.setPath(fakePath);
                        dalleImageRepository.save(dalleImageItem);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }

        return dalleImageItems;
    }




    private String getFileExtension(String url) {
        int dotIndex = url.lastIndexOf(".");
        if (dotIndex == -1) {
            throw new IllegalArgumentException("Invalid URL: " + url);
        }
        return url.substring(dotIndex + 1);
    }

    private String generateUniqueFileName(String fileExtension) {
        String fileName = System.nanoTime() + "_" + UUID.randomUUID();
        return fileName + "." + fileExtension;
    }

    public void cancelRequest() {
        if (currentApiCall != null) {
            currentApiCall.cancel(); // API 호출 취소

            // 사용자에게 중단되었음을 알리는 메시지를 생성합니다.
            String response = "요청이 중단되었습니다.";

            // 생성된 응답을 사용자에게 제공합니다.
            // 예: 콘솔에 출력하거나 API 응답으로 반환
            System.out.println(response);
        }
    }

    private static BufferedImage readImageFromUrl (String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        InputStream inputStream = url.openStream();
        return ImageIO.read(inputStream);
    }




}