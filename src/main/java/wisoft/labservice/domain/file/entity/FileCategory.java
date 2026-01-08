package wisoft.labservice.domain.file.entity;

import wisoft.labservice.domain.common.exception.BusinessException;
import wisoft.labservice.domain.common.exception.ErrorCode;

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
            throw new BusinessException(ErrorCode.INVALID_CATEGORY);
        }
    }
}