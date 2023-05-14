package hire.service.user.utils;

import com.hire.common.web.utils.TypeUtil;
import hire.service.user.controller.request.*;
import hire.service.user.entity.Forum;
import hire.service.user.entity.House;
import hire.service.user.entity.Report;
import hire.service.user.entity.User;
import hire.service.user.entity.response.ForumResponse;
import hire.service.user.entity.response.HouseResponse;

public class IndexConverter {

    public static HouseResponse convertHouseResponse(House house, User user) {
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
                .userId(user.getId())
                .username(user.getUsername())
                .nick(user.getNick())
                .phone(user.getPhone())
                .sex(user.getSex())
                .build();
    }

    public static ForumResponse convertForumResponse(Forum forum, User user) {
        return ForumResponse.builder()
                .id(forum.getId())
                .title(forum.getTitle())
                .content(forum.getContent())
                .img(TypeUtil.str2List(forum.getImg()))
                .createTime(forum.getCreateTime())
                .userId(user.getId())
                .username(user.getUsername())
                .nick(user.getNick())
                .phone(user.getPhone())
                .sex(user.getSex())
                .build();
    }

    public static House convertHouse(HouseCreateRequest houseCreateRequest) {
        return House.builder()
                .title(houseCreateRequest.getTitle())
                .introduce(houseCreateRequest.getIntroduce())
                .price(houseCreateRequest.getPrice())
                .city(houseCreateRequest.getCity())
                .address(houseCreateRequest.getAddress())
                .image(TypeUtil.list2Str(houseCreateRequest.getImage()))
                .space(houseCreateRequest.getSpace())
                .floor(houseCreateRequest.getFloor())
                .tags(TypeUtil.list2Str(houseCreateRequest.getTags()))
                .term(houseCreateRequest.getTerm())
                .type(houseCreateRequest.getType())
                .state(false)
                .userId(houseCreateRequest.getUserId())
                .build();
    }

    public static House convertUpHouse(HouseUpdateRequest houseUpdateRequest) {
        return House.builder()
                .id(houseUpdateRequest.getId())
                .title(houseUpdateRequest.getTitle())
                .introduce(houseUpdateRequest.getIntroduce())
                .price(houseUpdateRequest.getPrice())
                .city(houseUpdateRequest.getCity())
                .address(houseUpdateRequest.getAddress())
                .image(TypeUtil.list2Str(houseUpdateRequest.getImage()))
                .space(houseUpdateRequest.getSpace())
                .floor(houseUpdateRequest.getFloor())
                .tags(TypeUtil.list2Str(houseUpdateRequest.getTags()))
                .term(houseUpdateRequest.getTerm())
                .type(houseUpdateRequest.getType())
                .build();
    }

    public static Forum convertForum(ForumCreateRequest forumCreateRequest) {
        return Forum.builder()
                .title(forumCreateRequest.getTitle())
                .content(forumCreateRequest.getContent())
                .kind(forumCreateRequest.getKind())
                .img(TypeUtil.list2Str(forumCreateRequest.getImg()))
                .userId(forumCreateRequest.getUserId())
                .build();
    }

    public static Forum convertUpForum(ForumUpdateRequest forumUpdateRequest) {
        return Forum.builder()
                .id(forumUpdateRequest.getId())
                .title(forumUpdateRequest.getTitle())
                .content(forumUpdateRequest.getContent())
                .kind(forumUpdateRequest.getKind())
                .img(TypeUtil.list2Str(forumUpdateRequest.getImg()))
                .build();
    }

    public static Report convertReport(ReportCreateRequest reportCreateRequest) {

        String kind = reportCreateRequest.getUrl().contains("resource") ? "resource" : "forum";
        Long kindId = Long.valueOf(reportCreateRequest.getUrl().replaceAll(".*[^\\d](?=(\\d+))",""));

        return Report.builder()
                .reportType(reportCreateRequest.getReportType())
                .reportTitle(reportCreateRequest.getReportTitle())
                .content(reportCreateRequest.getContent())
                .reportUser(reportCreateRequest.getReportUser())
                .userId(reportCreateRequest.getUserId())
                .url(reportCreateRequest.getUrl())
                .kind(kind)
                .kindId(kindId)
                .reportStatus(0)
                .build();
    }
}
