package com.shop.portshop.commons;

import com.shop.portshop.mapper.ArchiveMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class StorageService {
    @Value("${spring.servlet.multipart.location}")
//    @Value("${spring.servlet.multipart.location}")
    private String rootPath;

    private ArchiveMapper archiveMapper;

    @Autowired
    public StorageService(ArchiveMapper archiveMapper){
        this.archiveMapper = archiveMapper;
    }



    public void uploadFiles(MultipartFile[] files, long boardNo){

        ArrayList<String> filesLocation = new ArrayList<>();

        for(MultipartFile file : files){

            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

            String fileName = UUID.randomUUID().toString().replace("-","");
            String fileNameWithExtension = fileName + fileExtension;
            String path = rootPath + "/" + fileNameWithExtension;

            //  file upload
            try {
                file.transferTo(Paths.get(path));
                filesLocation.add(fileNameWithExtension);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // insert file location to DB
        archiveMapper.insertFileLocation(filesLocation, boardNo);



    }
}
