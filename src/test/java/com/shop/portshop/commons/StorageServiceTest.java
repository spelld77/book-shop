package com.shop.portshop.commons;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StorageServiceTest {


    @Autowired
    StorageService storageService;

    @Test
    public void uploadTest(){
        MockMultipartFile file = new MockMultipartFile("vaddaeraaaeedde", "image.png", "img/png", "some image".getBytes());
//        storageService.uploadFile(file, 1L);
    }

}