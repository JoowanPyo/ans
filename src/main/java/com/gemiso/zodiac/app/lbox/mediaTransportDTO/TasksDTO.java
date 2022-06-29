package com.gemiso.zodiac.app.lbox.mediaTransportDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TasksDTO {

    private Long src_media_id;
    private Long trg_media_id;
    private String task_type;
    private String status;
    private Integer priority;
    private String src_path;
    private String trg_path;
    private String src_storage_id;
    private String trg_storage_id;
    private String parameter;
    private ParameterDataDTO parameter_data;
    private String task_data;
    private Long task_preset_id;
    private Long workflow_item_id;
    private Long creator_id;
    private Long root_task_id;
    private String src_content_id;
    private Long workflow_id;
    private String updated_at;
    private String created_at;
    private Long id;
}
