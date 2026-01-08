package wisoft.labservice.domain.award.service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import wisoft.labservice.domain.award.dto.request.AdminAwardCreateRequest;
import wisoft.labservice.domain.award.dto.request.AdminAwardUpdateRequest;
import wisoft.labservice.domain.award.dto.response.AdminAwardDetailResponse;
import wisoft.labservice.domain.award.dto.response.AdminAwardListResponse;
import wisoft.labservice.domain.award.dto.response.AdminAwardResponse;
import wisoft.labservice.domain.award.dto.response.AwardListResponse;
import wisoft.labservice.domain.award.dto.response.AwardResponse;
import wisoft.labservice.domain.award.entity.Award;
import wisoft.labservice.domain.award.repository.AwardRepository;
import wisoft.labservice.domain.file.entity.FileEntity;
import wisoft.labservice.domain.file.service.FileService;
import wisoft.labservice.domain.common.exception.BusinessException;
import wisoft.labservice.domain.common.exception.ErrorCode;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AwardService {

    private final AwardRepository awardRepository;
    private final FileService fileService;

    public AwardListResponse getAwardsForUser() {
        List<Award> awards = awardRepository.findAllWithImageFile();

        List<AwardResponse> awardResponse = awards.stream()
                .map(AwardResponse::from)
                .collect(Collectors.toList());

        return new AwardListResponse(awardResponse);
    }

    public AdminAwardListResponse getAwardsForAdmin() {
        List<Award> awards = awardRepository.findAllWithImageFile();

        List<AdminAwardResponse> awardResponse = awards.stream()
                .sorted(Comparator.comparing(Award::getAwardDate).reversed())
                .map(AdminAwardResponse::from)
                .collect(Collectors.toList());

        return new AdminAwardListResponse(awardResponse);
    }

    public AdminAwardDetailResponse getAwardDetailForAdmin(final String awardId) {
        Award award = awardRepository.findByIdWithImageFile(awardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.AWARD_NOT_FOUND));

        return AdminAwardDetailResponse.from(award);
    }

    @Transactional
    public void createAward(final AdminAwardCreateRequest request, final MultipartFile imageFile) {
        FileEntity uploadedFile = fileService.upload(imageFile, "AWARD");

        Award award = Award.builder()
                .id("a_" + UUID.randomUUID())
                .title(request.title())
                .awardee(request.awardee())
                .competition(request.competition())
                .summary(request.summary())
                .awardDate(request.date())
                .year(request.year())
                .imageFile(uploadedFile)
                .build();

        awardRepository.save(award);
    }

    @Transactional
    public void updateAward(final String awardId, final AdminAwardUpdateRequest request, final MultipartFile imageFile) {
        Award award = awardRepository.findByIdWithImageFile(awardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.AWARD_NOT_FOUND));

        if (request.title() != null) {
            award.updateTitle(request.title());
        }
        if (request.awardee() != null) {
            award.updateAwardee(request.awardee());
        }
        if (request.competition() != null) {
            award.updateCompetition(request.competition());
        }
        if (request.summary() != null) {
            award.updateSummary(request.summary());
        }
        if (request.date() != null) {
            award.updateAwardDate(request.date());
        }
        if (request.year() != null) {
            award.updateYear(request.year());
        }
        if (imageFile != null && !imageFile.isEmpty()) {
            if (award.getImageFile() != null) {

                fileService.delete(award.getImageFile().getId());
            }
            FileEntity newFile = fileService.upload(imageFile, "AWARD");
            award.updateImageFile(newFile);
        }
    }

    @Transactional
    public void deleteAward(final String awardId) {
        Award award = awardRepository.findByIdWithImageFile(awardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.AWARD_NOT_FOUND));

        String fileId = null;
        if(award.getImageFile() != null) {
            fileId = award.getImageFile().getId();
            award.updateImageFile(null);
        }

        awardRepository.delete(award);

        if(fileId != null) {
            fileService.delete(fileId);
        }
    }
}
