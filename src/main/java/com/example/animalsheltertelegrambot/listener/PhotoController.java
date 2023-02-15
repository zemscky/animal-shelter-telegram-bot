//package com.example.animalsheltertelegrambot.listener;
//
//import com.example.animalsheltertelegrambot.models.PhotoFile;
//import com.example.animalsheltertelegrambot.repositories.PhotoFileRepository;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/photo")
//public class PhotoController {
//
//    private final PhotoFileRepository photoFileRepository;
//
//    public PhotoController(PhotoFileRepository photoFileRepository) {
//        this.photoFileRepository = photoFileRepository;
//    }
//
//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<String> uploadPhoto(
//            @RequestParam MultipartFile photo) throws IOException {
//
////        if (photo.getSize() >= 1024 * 300) {
////            return ResponseEntity.badRequest().body("Слишком большой файл");
////        }
//        PhotoFile photoFile = new PhotoFile();
//        photoFile.setFileSize(photo.getSize());
//        photoFile.setMediaType(photo.getContentType());
//        photoFile.setData(photo.getBytes());
//        photoFileRepository.save(photoFile);
//        return ResponseEntity.ok().body("Фото успешно загружено");
//    }
//
//    @GetMapping()
//    public ResponseEntity<byte[]> getPhoto(@RequestParam Integer id) {
//
//        PhotoFile photoFile = photoFileRepository.findById(id).orElseThrow();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.parseMediaType(photoFile.getMediaType()));
//        headers.setContentLength(photoFile.getData().length);
//        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(photoFile.getData());
//    }
//
////    @ExceptionHandler(NotFoundException.class)
////    public ResponseEntity handleNotFoundException() {
////        return ResponseEntity.badRequest().build();
////    }
//}
