package wisoft.labservice.domain.home.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wisoft.labservice.domain.home.dto.response.HomeStatsResponse;
import wisoft.labservice.domain.project.repository.ProjectRepository;
import wisoft.labservice.domain.paper.repository.PaperRepository;
//import wisoft.labservice.domain.award.repository.AwardRepository;
//import wisoft.labservice.domain.patent.repository.PatentRepository;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final ProjectRepository projectRepository;
    private final PaperRepository paperRepository;
//    private final AwardRepository awardRepository;
//    private final PatentRepository patentRepository;

    public HomeStatsResponse getHomeStats() {
        return new HomeStatsResponse(
                projectRepository.count(),
                paperRepository.count()
//                awardRepository.count(),
//                patentRepository.count()
        );
    }
}