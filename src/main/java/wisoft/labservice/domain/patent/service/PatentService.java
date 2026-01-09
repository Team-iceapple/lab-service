package wisoft.labservice.domain.patent.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import wisoft.labservice.domain.common.exception.BusinessException;
import wisoft.labservice.domain.common.exception.ErrorCode;
import wisoft.labservice.domain.file.entity.FileEntity;
import wisoft.labservice.domain.file.service.FileService;
import wisoft.labservice.domain.patent.dto.request.AdminPatentCreateRequest;
import wisoft.labservice.domain.patent.dto.request.AdminPatentUpdateRequest;
import wisoft.labservice.domain.patent.dto.response.AdminPatentListResponse;
import wisoft.labservice.domain.patent.dto.response.AdminPatentResponse;
import wisoft.labservice.domain.patent.dto.response.PatentListResponse;
import wisoft.labservice.domain.patent.dto.response.PatentResponse;
import wisoft.labservice.domain.patent.entity.Patent;
import wisoft.labservice.domain.patent.repository.PatentRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PatentService {

    private final PatentRepository patentRepository;
    private final FileService fileService;

    public PatentListResponse getPatentsForUser() {
        List<Patent> patents = patentRepository.findAllActiveWithFiles();

        List<PatentResponse> responses = patents.stream()
                .map(PatentResponse::from)
                .collect(Collectors.toList());

        return new PatentListResponse(responses);
    }

    public AdminPatentListResponse getPatentsForAdmin() {
        List<Patent> patents = patentRepository.findAllWithFiles();

        List<AdminPatentResponse> responses = patents.stream()
                .map(AdminPatentResponse::from)
                .collect(Collectors.toList());

        return new AdminPatentListResponse(responses);
    }

    public AdminPatentResponse getPatentDetailForAdmin(String patentId) {
        Patent patent = patentRepository.findByIdWithFiles(patentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PATENT_NOT_FOUND));

        return AdminPatentResponse.from(patent);
    }

    @Transactional
    public void createPatent(
            AdminPatentCreateRequest request,
            MultipartFile pdfFile,
            MultipartFile thumbnailFile
    ) {
        FileEntity pdf = fileService.upload(pdfFile, "PATENT");
        FileEntity thumbnail = fileService.upload(thumbnailFile, "PATENT");

        Patent patent = Patent.builder()
                .id("pt_" + UUID.randomUUID())
                .name(request.name())
                .year(request.year())
                .inventor(request.inventor())
                .inventionDate(request.inventionDate())
                .pdfFile(pdf)
                .thumbnailFile(thumbnail)
                .link(request.link())
                .isActive(request.isActive())
                .build();

        patentRepository.save(patent);
    }

    @Transactional
    public void updatePatent(
            final String patentId,
            final AdminPatentUpdateRequest request,
            final MultipartFile pdfFile,
            final MultipartFile thumbnailFile
    ) {
        Patent patent = patentRepository.findByIdWithFiles(patentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PATENT_NOT_FOUND));

        if (request.name() != null) {
            patent.updateName(request.name());
        }
        if (request.year() != null) {
            patent.updateYear(request.year());
        }
        if (request.inventor() != null) {
            patent.updateInventor(request.inventor());
        }
        if (request.inventionDate() != null) {
            patent.updateInventionDate(request.inventionDate());
        }
        if (request.link() != null) {
            patent.updateLink(request.link());
        }
        if (request.isActive() != null) {
            patent.updateIsActive(request.isActive());
        }

        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            if (patent.getThumbnailFile() != null) {
                fileService.delete(patent.getThumbnailFile().getId());
            }
            FileEntity newThumbnail = fileService.upload(thumbnailFile, "PATENT");
            patent.updateThumbnailFile(newThumbnail);
        }

        if (pdfFile != null && !pdfFile.isEmpty()) {
            if (patent.getPdfFile() != null) {
                fileService.delete(patent.getPdfFile().getId());
            }
            FileEntity newPdf = fileService.upload(pdfFile, "PATENT");
            patent.updatePdfFile(newPdf);
        }
    }

    @Transactional
    public void deletePatent(final String patentId) {
        Patent patent = patentRepository.findByIdWithFiles(patentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PATENT_NOT_FOUND));

        String pdfId = null;
        String thumbId = null;

        if (patent.getPdfFile() != null) {
            pdfId = patent.getPdfFile().getId();
            patent.updatePdfFile(null);
        }
        if (patent.getThumbnailFile() != null) {
            thumbId = patent.getThumbnailFile().getId();
            patent.updateThumbnailFile(null);
        }

        patentRepository.delete(patent);

        if (pdfId != null) {
            fileService.delete(pdfId);
        }
        if (thumbId != null) {
            fileService.delete(thumbId);
        }
    }
}

