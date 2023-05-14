package hire.service.base.utils;

import com.hire.common.base.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailUtil {

    @Value("${spring.mail.username}")
    private String sender;//获得配置文件中的username

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * 发送文本邮件
     *
     * @param email
     * @return
     */
    public String sendMail(String email) {
        String code = RandomUtil.getSixBitRandom();
        SimpleMailMessage msg = new SimpleMailMessage();
        //发送邮件的邮箱
        msg.setFrom(sender);
        //发送到哪(邮箱)
        msg.setTo(email);
        msg.setSubject("邮箱验证码");
        msg.setText("你的验证码为" + code + "，有效时间为5分钟，请尽快使用并且不要告诉别人。");
        try {
            javaMailSender.send(msg);
        } catch (MailException err) {
            log.error(err.getMessage());
            throw new BizException("邮箱发送异常");
        }
        return code;
    }
}
