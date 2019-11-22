package com.cognition.bit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 启动项
 *
 * @author LineInkBook
 */
@EnableTransactionManagement
@MapperScan("com.cognition.bit.*.dao")
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class CognitionBitApplication {
    public static void main(String[] args) {
        SpringApplication.run(CognitionBitApplication.class, args);
        System.out.println(
                        "                                                                   ┏┓　  ┏┓  \n" +
                        "                                                                  ┏┛┻━━━━┛┻━━━━━┓\n" +
                        "                                                                  ┃　　　　　　  ┃\n" +
                        "  ヾ(◍°∇°◍)ﾉﾞ    应用启动成功      ヾ(◍°∇°◍)ﾉﾞ|                   ┃　　 ━　　　  ┃\n" +
                        "  ヾ(◍°∇°◍)ﾉﾞ    应用启动成功      ヾ(◍°∇°◍)ﾉﾞ|                   ┃　┳┛　┗┳　   ┃\n" +
                        "  ヾ(◍°∇°◍)ﾉﾞ    应用启动成功      ヾ(◍°∇°◍)ﾉﾞ|                   ┃　　　　　　　┃\n" +
                        "  ヾ(◍°∇°◍)ﾉﾞ    应用启动成功      ヾ(◍°∇°◍)ﾉﾞ|                   ┃　　 ┻　　　  ┃\n" +
                        "  ヾ(◍°∇°◍)ﾉﾞ    应用启动成功      ヾ(◍°∇°◍)ﾉﾞ|                   ┃　　　　　　　┃\n" +
                        "  ヾ(◍°∇°◍)ﾉﾞ    应用启动成功      ヾ(◍°∇°◍)ﾉﾞ|                   ┗━┓　　　┏━━━━┛\n" +
                        "  ヾ(◍°∇°◍)ﾉﾞ    应用启动成功      ヾ(◍°∇°◍)ﾉﾞ|                     ┃　　  ┃   神兽保佑\n" +
                        "  ヾ(◍°∇°◍)ﾉﾞ    应用启动成功      ヾ(◍°∇°◍)ﾉﾞ|                     ┃　　　┃   代码无BUG！\n" +
                        " ________        ________    ___________       _________            ┃　　　┗━━━━━━┓\n" +
                        "|_   ___  \\     |   _____|  |____   ____|    /   _______|          ┃　　　　　　　┣┓\n" +
                        "  | |__ )  |    |  |_____       |   |       /   /                   ┃　　　　　　　┏┛\n" +
                        "  |  __' //     |   _____|      |   |       |   |    ____           ┗┓┓┏━━━┳┓┏━━━┛\n" +
                        " _| |   \\ |_    |  |_____       |   |       \\   \\ ___|  |            ┃┫┫　 ┃┫┫\n" +
                        "|___|   |__ |   |________|      |___|        \\__________|            ┗┻┛　 ┗┻┛");
    }
}

