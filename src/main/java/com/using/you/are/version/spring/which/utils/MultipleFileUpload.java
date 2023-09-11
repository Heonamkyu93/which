package com.using.you.are.version.spring.which.utils;

import com.using.you.are.version.spring.which.domain.BoardFileEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MultipleFileUpload {

    @Value("${file.dir}")
    private String path;


    public boolean fileDelete(String severName) {

        String fullPath = path + severName;
        File file = new File(fullPath);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("성공");
            } else {
                // 파일 삭제 실패
            }

        }
        return false;
    }
    public ArrayList<BoardFileEntity> fileSave(List<MultipartFile> fileList) {
        int size = 10 * 1024 * 1024;
        ArrayList<BoardFileEntity> fileEntityArrayList = new ArrayList<>();
        BoardFileEntity fileEntity = null;
        if(fileList==null) return fileEntityArrayList;
        for(int i=0;i<fileList.size();i++){
           if(fileList.get(i).getOriginalFilename()==null || fileList.get(i) ==null || fileList.get(i).isEmpty()) continue;
            fileEntity = new BoardFileEntity();
            String uuid = UUID.randomUUID().toString();
            String originalName = fileList.get(i).getOriginalFilename();
            String serverName=uuid+originalName;
            File file = new File(path,serverName);
            fileEntity.setFileOriginalName(originalName);
            fileEntity.setFileServerName(serverName);
            fileEntityArrayList.add(fileEntity);
            // if(!file.isDirectory())file.mkdir();
            try {
                fileList.get(i).transferTo(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
       fileEntityArrayList=getNonEmptyDTOs(fileEntityArrayList);
        return fileEntityArrayList;

    }
   public ArrayList<BoardFileEntity> getNonEmptyDTOs(ArrayList<BoardFileEntity> userImgEntity) {
       return (ArrayList<BoardFileEntity>) userImgEntity.stream()
               .filter(dto -> dto.getFileOriginalName() != null && !dto.getFileOriginalName().isEmpty())
               .collect(Collectors.toList());
   }

}
