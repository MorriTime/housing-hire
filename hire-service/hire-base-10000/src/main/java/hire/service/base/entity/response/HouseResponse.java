package hire.service.base.entity.response;

import com.hire.common.web.utils.TypeUtil;
import com.hire.common.web.utils.convert.ConvertFunction;
import hire.service.base.entity.House;
import hire.service.base.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HouseResponse implements ConvertFunction<House, User, HouseResponse> {

    private Long id;

    private String title;

    private String introduce;

    private double price;

    private String city;

    private String address;

    private List<String> image;

    private double space;

    private int floor;

    private List<String> tags;

    private int term;

    private String type;

    private Boolean state;

    private String createTime;

    private Long usrId;

    private String username;

    private String nick;

    private String phone;

    private Boolean sex;

    @Override
    public HouseResponse apply(House house, User user) {
        return HouseResponse.builder()
                .id(house.getId())
                .title(house.getTitle())
                .introduce(house.getIntroduce())
                .price(house.getPrice())
                .city(house.getCity())
                .address(house.getAddress())
                .image(TypeUtil.str2List(house.getImage()))
                .space(house.getSpace())
                .floor(house.getFloor())
                .tags(TypeUtil.str2List(house.getTags()))
                .term(house.getTerm())
                .type(house.getType())
                .state(house.getState())
                .createTime(house.getCreateTime())
                .usrId(house.getUserId())
                .username(user.getUsername())
                .nick(user.getNick())
                .phone(user.getPhone())
                .sex(user.getSex())
                .build();
    }
}
