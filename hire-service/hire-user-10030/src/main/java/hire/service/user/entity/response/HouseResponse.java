package hire.service.user.entity.response;

import com.hire.common.web.utils.convert.ConvertFunction;
import com.hire.common.web.utils.convert.MultiConverter;
import hire.service.user.entity.House;
import hire.service.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HouseResponse implements MultiConverter<House, User, HouseResponse> {

    private House house;

    private User user;

    public HouseResponse(House house, User user) {
        this.house = house;
        this.user = user;
    }

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

    private Long userId;

    private String username;

    private String nick;

    private String phone;

    private Boolean sex;

    @Override
    public HouseResponse convert(ConvertFunction<House, User, HouseResponse> f) {
        return f.apply(this.house, this.user);
    }
}
