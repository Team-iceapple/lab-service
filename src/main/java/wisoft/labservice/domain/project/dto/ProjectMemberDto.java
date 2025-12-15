package wisoft.labservice.domain.project.dto;


import wisoft.labservice.domain.project.entity.ProjectMember;

public record ProjectMemberDto(
        String name,

        String extra
) {
    public static ProjectMemberDto from(ProjectMember member) {
        return new ProjectMemberDto(
                member.getName(),
                member.getExtra());
    }

    public ProjectMember toEntity() {
        return ProjectMember.builder()
                .name(name)
                .extra(extra)
                .build();
    }
}