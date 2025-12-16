package wisoft.labservice.domain.home.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wisoft.labservice.domain.home.dto.request.ImageIdsRequest;
import wisoft.labservice.domain.home.dto.request.ImageTitleUpdateRequest;
import wisoft.labservice.domain.home.dto.request.SlideIdsRequest;
import wisoft.labservice.domain.home.dto.request.SlideOrderUpdateRequest;
import wisoft.labservice.domain.home.dto.response.GalleryImageResponse;
import wisoft.labservice.domain.home.dto.response.GallerySlideResponse;
import wisoft.labservice.domain.home.service.GalleryService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gallery")
public class GalleryController {

    private final GalleryService galleryService;

    @PostMapping("/images")
    public Map<String, Object> uploadImages(
            @RequestParam List<MultipartFile> files,
            @RequestParam(required = false) List<String> titles
    ) {
        return Map.of(
                "images",
                galleryService.uploadImages(files, titles)
        );
    }

    @GetMapping("/images")
    public List<GalleryImageResponse> getImages() {
        return galleryService.getImages();
    }

    @PatchMapping("/images/{imageId}")
    public GalleryImageResponse updateTitle(
            @PathVariable String imageId,
            @RequestBody ImageTitleUpdateRequest request
    ) {
        return galleryService.updateTitle(imageId, request.getTitle());
    }

    @DeleteMapping("/images")
    public Map<String, Object> deleteImages(@RequestBody ImageIdsRequest request) {
        galleryService.deleteImages(request.getImageIds());
        return Map.of(
                "deletedCount", request.getImageIds().size(),
                "message", request.getImageIds().size() + "개의 파일이 삭제되었습니다."
        );
    }

    @GetMapping("/slides")
    public List<GallerySlideResponse> getSlides() {
        return galleryService.getSlides();
    }

    @PostMapping("/slides")
    public Map<String, Object> createSlides(
            @RequestBody List<String> imageIds
    ) {
        return Map.of(
                "slides",
                galleryService.createSlides(imageIds)
        );
    }


    @PatchMapping("/slides")
    public Map<String, String> updateOrder(
            @RequestBody SlideOrderUpdateRequest request
    ) {
        galleryService.updateSlideOrder(request.getSlides());
        return Map.of("message", "순서가 업데이트되었습니다.");
    }

    @DeleteMapping("/slides")
    public Map<String, Object> deleteSlides(@RequestBody SlideIdsRequest request) {
        galleryService.deleteSlides(request.getSlideIds());
        return Map.of(
                "deletedCount", request.getSlideIds().size(),
                "message", "슬라이드에서 제거되었습니다."
        );
    }

    @DeleteMapping("/images/{imageId}")
    public Map<String, String> deleteImage(@PathVariable String imageId) {
        galleryService.deleteImages(List.of(imageId));
        return Map.of(
                "id", imageId,
                "message", "파일이 삭제되었습니다."
        );
    }

    @DeleteMapping("/slides/{slideId}")
    public Map<String, String> deleteSlide(@PathVariable String slideId) {
        galleryService.deleteSlides(List.of(slideId));
        return Map.of(
                "id", slideId,
                "message", "슬라이드에서 제거되었습니다."
        );
    }
}
