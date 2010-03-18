package org.seasar.ymir.scaffold.maintenance.dto;

public class EntityLinkDto {
    private String entityName;

    private String path;

    public EntityLinkDto(String entityName, String path) {
        this.entityName = entityName;
        this.path = path;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getPath() {
        return path;
    }
}
