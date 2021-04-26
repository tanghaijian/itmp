package cn.pioneeruniverse.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: session redis 序列化反序列化配置（用于session中对象的存储读取）
 * @Date: Created in 14:30 2019/8/15
 * @Modified By:
 */
@Primary
@Component("springSessionDefaultRedisSerializer")
public class SessionRedisSerializerConfig extends JdkSerializationRedisSerializer {

    private static final Logger log = LoggerFactory.getLogger(SessionRedisSerializerConfig.class);

    @Override
    public byte[] serialize(Object object) {
        try {
            return super.serialize(object);
        } catch (Exception e) {
            log.error("session redis serialize error", e);
            return null;
        }
    }

    @Override
    public Object deserialize(byte[] bytes) {
        try {
            return super.deserialize(bytes);
        } catch (Exception e) {
            log.error("session redis deserialize error", e);
            return null;
        }
    }
}
