package com.gen.vacation.global.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jinwoo.
 * User: jwoh
 * Date: 2020-05-11
 * Time: 오후 3:00
 */
@Configuration
public class JasyptConfig {

    @Bean("encryption")
    public StringEncryptor stringEncryptor() {

        PooledPBEStringEncryptor encrypt = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("joeunins");
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        encrypt.setConfig(config);


        return encrypt;
    }
}
