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
        List<Patent> patents = patentRepository.findAllWithPdfFile();

        List<PatentResponse> patentResponse = patents.stream()
                .map(PatentResponse::from)
                .collect(Collectors.toList());

        return new PatentListResponse(patentResponse);
    }

    public AdminPatentListResponse getPatentsForAdmin() {
        List<Patent> patents = patentRepository.findAllWithPdfFile();

        List<AdminPatentResponse> patentResponse = patents.stream()
                .map(AdminPatentResponse::from)
                .collect(Collectors.toList());

        return new AdminPatentListResponse(patentResponse);
    }

    public AdminPatentResponse getPatentDetailForAdmin(String patentId) {
        Patent patent = patentRepository.findByIdWithPdfFile(patentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PATENT_NOT_FOUND));

        return AdminPatentResponse.from(patent);
    }

    @Transactional
    public void createPatent(AdminPatentCreateRequest request, MultipartFile pdfFile) {
        FileEntity uploadedFile = fileService.upload(pdfFile, "PATENT");

        Patent patent = Patent.builder()
                .id("p_" + UUID.randomUUID())
                .name(request.name())
                .year(request.year())
                .inventionDate(request.inventionDate())
                .pdfFile(uploadedFile)
                .link(request.link())
                .build();

        patentRepository.save(patent);
    }

    @Transactional
    public void updatePatent(final String patentId, final AdminPatentUpdateRequest request,
                            final MultipartFile pdfFile) {

        Patent patent = patentRepository.findByIdWithPdfFile(patentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PATENT_NOT_FOUND));

        if (request.name() != null) {
            patent.updateName(request.name());
        }
        if (request.year() != null) {
            patent.updateYear(request.year());
        }
        if (request.inventionDate() != null) {
            patent.updateInventionDate(request.inventionDate());
        }
        if (pdfFile != null && !pdfFile.isEmpty()) {
            if (patent.getPdfFile() != null) {
                fileService.delete(patent.getPdfFile().getId());
            }
            FileEntity newFile = fileService.upload(pdfFile, "PATENT");
            patent.updatePdfFile(newFile);
        }
        if (request.link() != null) {
            patent.updateLink(request.link());
        }
}

    @Transactional
    public void deletePatent(final String patentId) {
        Patent patent = patentRepository.findByIdWithPdfFile(patentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PATENT_NOT_FOUND));

        String fileId = null;
        if(patent.getPdfFile() != null) {
            fileId = patent.getPdfFile().getId();
            patent.updatePdfFile(null);
        }

        patentRepository.delete(patent);

        if(fileId != null) {
            fileService.delete(fileId);
        }
    }
}
