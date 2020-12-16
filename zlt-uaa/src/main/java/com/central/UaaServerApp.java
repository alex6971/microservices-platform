package com.central;

import com.central.common.constant.SecurityConstants;
import com.central.common.ribbon.annotation.EnableFeignInterceptor;
import com.central.common.utils.RsaUtils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.interfaces.RSAPublicKey;
import java.util.stream.Collectors;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/** 
* @author zlt
*/
@EnableFeignClients
@EnableFeignInterceptor
@EnableDiscoveryClient
@EnableRedisHttpSession
@SpringBootApplication
public class UaaServerApp {
	public static void main(String[] args) {
		SpringApplication.run(UaaServerApp.class, args);

		// RSAPublicKey pubKeyObj = getPubKeyObj();
		// System.out.print(pubKeyObj);
	}
	

	private static final String PUBKEY_START = "-----BEGIN PUBLIC KEY-----";
	private static final String PUBKEY_END = "-----END PUBLIC KEY-----";
	public static RSAPublicKey getPubKeyObj() {
		Resource res = new ClassPathResource(SecurityConstants.RSA_PUBLIC_KEY);
		try (BufferedReader br = new BufferedReader(new InputStreamReader(res.getInputStream()))) {
			String pubKey = br.lines()
					.collect(Collectors.joining("\n"));
			pubKey = pubKey.substring(PUBKEY_START.length(), pubKey.indexOf(PUBKEY_END));
			return RsaUtils.getPublicKey(pubKey);
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
		return null;
	}
}
