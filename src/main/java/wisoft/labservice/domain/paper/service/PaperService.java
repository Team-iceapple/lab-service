package wisoft.labservice.domain.paper.service;

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
import wisoft.labservice.domain.paper.dto.request.AdminPaperCreateRequest;
import wisoft.labservice.domain.paper.dto.request.AdminPaperUpdateRequest;
import wisoft.labservice.domain.paper.dto.response.AdminPaperDetailResponse;
import wisoft.labservice.domain.paper.dto.response.AdminPaperListResponse;
import wisoft.labservice.domain.paper.dto.response.AdminPaperResponse;
import wisoft.labservice.domain.paper.dto.response.PaperListResponse;
import wisoft.labservice.domain.paper.dto.response.PaperResponse;
import wisoft.labservice.domain.paper.entity.Paper;
import wisoft.labservice.domain.paper.repository.PaperRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaperService {

    private final PaperRepository paperRepository;
    private final FileService fileService;


    /**
     * 논문 목록 조회 (사용자용)
     */
    public PaperListResponse getPapersForUser() {
        List<Paper> papers = paperRepository.findAllActiveWithImageFile();

        List<PaperResponse> paperResponse = papers.stream()
                .map(PaperResponse::from)
                .collect(Collectors.toList());

        return new PaperListResponse(paperResponse);
    }

    /**
     * 논문 상세 조회 (사용자용)
     */
    public PaperResponse getPaperDetailForUser(String paperId) {
        Paper paper = paperRepository.findActiveByIdWithImageFile(paperId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PAPER_NOT_FOUND));

        return PaperResponse.from(paper);
    }

    /**
     * 논문 목록 조회 (관리자용)
     */
    public AdminPaperListResponse getPapersForAdmin() {
        List<Paper> papers = paperRepository.findAllWithImageFile();

        List<AdminPaperResponse> paperResponse = papers.stream()
                .map(AdminPaperResponse::from)
                .collect(Collectors.toList());

        return new AdminPaperListResponse(paperResponse);
    }

    /**
     * 논문 상세 조회 (관리자용)
     */
    public AdminPaperDetailResponse getPaperDetailForAdmin(String paperId) {
        Paper paper = paperRepository.findByIdWithImageFile(paperId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PAPER_NOT_FOUND));

        return AdminPaperDetailResponse.from(paper);
    }

    /**
     * 논문 생성 (관리자용)
     */
    @Transactional
    public void createPaper(AdminPaperCreateRequest request, MultipartFile imageFile) {
        FileEntity uploadedFile = fileService.upload(imageFile, "PAPER");

        Paper paper = Paper.builder()
                .id("p_" + UUID.randomUUID())
                .title(request.title())
                .authors(request.authors())
                .paperAbstract(request.paperAbstract())
                .conference(request.conference())
                .journal(request.journal())
                .publicationDate(request.publicationDate())
                .link(request.link())
                .year(request.year())
                .imageFile(uploadedFile)
                .isActive(request.isActive())
                .build();

        paperRepository.save(paper);
    }

    /**
     * 논문 수정 (관리자용)
     */
    @Transactional
    public void updatePaper(final String paperId, final AdminPaperUpdateRequest request,
                            final MultipartFile imageFile) {

        Paper paper = paperRepository.findByIdWithImageFile(paperId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PAPER_NOT_FOUND));

        if (request.title() != null) {
            paper.updateTitle(request.title());
        }
        if (request.authors() != null) {
            paper.updateAuthors(request.authors());
        }
        if (request.paperAbstract() != null) {
            paper.updateAbstract(request.paperAbstract());
        }
        if (request.conference() != null) {
            paper.updateConference(request.conference());
        }
        if (request.journal() != null) {
            paper.updateJournal(request.journal());
        }
        if (request.publicationDate() != null) {
            paper.updatePublicationDate(request.publicationDate());
        }
        if (request.link() != null) {
            paper.updateLink(request.link());
        }
        if (request.year() != null) {
            paper.updateYear(request.year());
        }
        if (request.isActive() != null) {
            paper.updateIsActive(request.isActive());
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            if (paper.getImageFile() != null) {
                fileService.delete(paper.getImageFile().getId());
            }

            FileEntity newFile = fileService.upload(imageFile, "PAPER");
            paper.updateImageFile(newFile);
        }
}

    @Transactional
    public void deletePaper(final String paperId) {
        Paper paper = paperRepository.findByIdWithImageFile(paperId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PAPER_NOT_FOUND));

        String fileId = null;
        if(paper.getImageFile() != null) {
            fileId = paper.getImageFile().getId();
            paper.updateImageFile(null);
        }

        paperRepository.delete(paper);

        if(fileId != null) {
            fileService.delete(fileId);
        }
    }
}
