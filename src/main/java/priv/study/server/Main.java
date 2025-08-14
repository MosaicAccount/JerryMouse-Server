package priv.study.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import priv.study.server.connector.HttpConnector;

/**
 * 服务启动类
 *
 * @author JLian
 * @version 0.0.1
 * @since 0.0.1
 */
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try (HttpConnector httpConnector = new HttpConnector()) {
           while (true) {
               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
                   log.error("服务运行出错，异常信息为：{}", e.getMessage());
                   break;
               }
           }
        } catch (Exception e) {
            log.error("服务出错，异常信息为：{}", e.getMessage());
        }
    }
}
