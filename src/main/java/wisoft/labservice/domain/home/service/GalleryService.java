package wisoft.labservice.domain.home.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import wisoft.labservice.domain.file.service.FileService;
import wisoft.labservice.domain.home.dto.request.SlideOrderUpdateRequest;
import wisoft.labservice.domain.home.dto.response.GalleryImageResponse;
import wisoft.labservice.domain.home.dto.response.GallerySlideResponse;
import wisoft.labservice.domain.home.entity.*;
import wisoft.labservice.domain.home.repository.*;
import wisoft.labservice.domain.common.exception.BusinessException;
import wisoft.labservice.domain.common.exception.ErrorCode;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class GalleryService {

    private final LabImageRepository labImageRepository;
    private final GallerySlideRepository gallerySlideRepository;
    private final FileService fileService;

    // 이미지

    public List<GalleryImageResponse> uploadImages(
            List<MultipartFile> files,
            List<String> titles
    ) {
        for (int i = 0; i < files.size(); i++) {

            // 1. 파일 업로드 (딱 1번)
            var fileEntity = fileService.upload(files.get(i), "home");
            // 2. title 처리 (빈 문자열 → null)
            String title = null;
            if (titles != null && titles.size() > i && !titles.get(i).isBlank()) {
                title = titles.get(i);
            }

            // 3. lab_images 저장
            labImageRepository.save(
                    LabImage.builder()
                            .id("f_" + UUID.randomUUID())
                            .file(fileEntity)
                            .title(title)
                            .build()
            );
        }

        // 4. 등록 순서(createdAt ASC) 그대로 반환
        return labImageRepository.findAllByOrderByCreatedAtAsc()
                .stream()
                .map(GalleryImageResponse::from)
                .toList();
    }

    public List<GalleryImageResponse> getImages() {
        return labImageRepository.findAllByOrderByCreatedAtAsc()
                .stream()
                .map(GalleryImageResponse::from)
                .toList();
    }

    public void deleteImages(List<String> imageIds) {
        imageIds.forEach(labImageRepository::deleteById);
    }

    public GalleryImageResponse updateTitle(String imageId, String title) {
        LabImage image = labImageRepository.findById(imageId)
                .orElseThrow(() -> new BusinessException(ErrorCode.IMAGE_NOT_FOUND));
        image.updateTitle(title);
        return GalleryImageResponse.from(image);
    }

    // 슬라이드

    public List<GallerySlideResponse> getSlides() {
        return gallerySlideRepository.findAllByOrderByDisplayOrderAsc()
                .stream()
                .map(GallerySlideResponse::from)
                .toList();
    }

    public List<GallerySlideResponse> createSlides(List<String> imageIds) {
        int order = gallerySlideRepository.findTopByOrderByDisplayOrderDesc()
                .map(slide -> slide.getDisplayOrder() + 1)
                .orElse(1);

        for (String imageId : imageIds) {
            LabImage image = labImageRepository.findById(imageId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.IMAGE_NOT_FOUND));

            gallerySlideRepository.save(
                    GallerySlide.builder()
                            .id("s_" + UUID.randomUUID())
                            .image(image)
                            .displayOrder(order++)
                            .build()
            );
        }

        return getSlides();
    }

    public void updateSlideOrder(List<SlideOrderUpdateRequest.SlideOrder> slides) {
        slides.forEach(s -> {
            GallerySlide slide = gallerySlideRepository.findById(s.getId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.SLIDE_NOT_FOUND));
            slide.updateOrder(s.getOrder());
        });
    }

    public void deleteSlides(List<String> slideIds) {
        slideIds.forEach(gallerySlideRepository::deleteById);
    }


}