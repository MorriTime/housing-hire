package hire.service.admin.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    @TableId(type = IdType.AUTO)
    private Long id;

    private int reportType;

    private String reportTitle;

    private String content;

    private String reportUser;

    private Long userId;

    private Long reportedId;

    private String url;

    private String kind;

    private Long kindId;

    private int reportStatus;

    private int handleStatus;

    @TableField(fill = FieldFill.INSERT)
    private String createTime;

    @TableField(fill = FieldFill.UPDATE)
    private String updateTime;
}
