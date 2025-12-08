package wisoft.labservice.domain.file.entity;

public enum FileCategory {
    HOME,
    PROJECT,
    PAPER,
    AWARD,
    SEMINAR,
    PATENT;

    public static FileCategory from(String value) {
        try {
            return FileCategory.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("INVALID_CATEGORY");
        }
    }
}