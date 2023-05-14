package hire.service.user.controller.request;

import com.hire.common.web.utils.convert.Converter;
import hire.service.user.entity.House;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.function.Function;

@Data
public class HouseCreateRequest implements Converter<HouseCreateRequest, House> {

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    @NotNull(message = "标题不能为空")
    private String title;
    /**
     * 简介
     */
    @NotBlank(message = "简介不能为空")
    @NotNull(message = "简介不能为空")
    private String introduce;
    /**
     * 价格
     */
    @NotNull(message = "价格不能为空")
    private double price;
    /**
     * 城市
     */
    @NotBlank(message = "城市不能为空")
    @NotNull(message = "城市不能为空")
    private String city;
    /**
     * 地址
     */
    @NotBlank(message = "地址不能为空")
    @NotNull(message = "地址不能为空")
    private String address;
    /**
     * 图片数组
     */
    @NotNull(message = "地址不能为空")
    private List<String> image;
    /**
     * 面积
     */
    @NotNull(message = "面积不能为空")
    private double space;
    /**
     * 楼层
     */
    @NotNull(message = "楼层不能为空")
    private int floor;
    /**
     * 标签
     */
    @NotNull(message = "标签不能为空")
    private List<String> tags;
    /**
     * 最小租期
     */
    @NotNull(message = "最小租期不能为空")
    private int term;
    /**
     * 户型
     */
    @NotBlank(message = "户型不能为空")
    @NotNull(message = "户型不能为空")
    private String type;

    @NotNull(message = "创建者id不能为空")
    private Long userId;

    @Override
    public House convert(Function<HouseCreateRequest, House> f) {
        return f.apply(this);
    }
}
