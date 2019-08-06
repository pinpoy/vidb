package com.moogos.spacex.util;

public class PayConfig {	
	
	/**
	 * appid		-- 应用appid
	 * privateKey   -- 应用私钥
	 * publicKey	-- 平台公钥(platKey)
	 * notifyurl	-- 商户服务端接收支付结果通知的地址
	 * */
	

	
	/**线上环境*/
	public static final String appid = "3004807420";
	public final static String privateKey = "MIICXQIBAAKBgQCSB1EOMNy/5ojpzM8JFE188pkVPR12qOm44/WdpzPMzV7bYtyiq1lX+UFmqXuteKweTyChfeZWeaNto4z34aYgz5Fsd0aDJuC6785+yVNSCyWgJD5S6ctU/01AFt08d5pRERludP2MI5y/P39PZF/tm+Kq9XwwPxNdnJwqip7c5wIDAQABAoGAV1ltENIFmfyqdT//r+ynFVCAZYXzM+GCPQiPCUjU4XMPhKe0VtGsVcVRa7mBs5h1TIQEftUyjROhwJmOX1Bo8FwhYx+9SZ+4YAxR9zIrYHNzAfEoMHQfPcBLWH28a0tNf66UJd73m7X4k8MckxIkaEi18G4PYUS6QnzIbrXGslECQQDEjPWgL90bL8oT08LZSMXwY+Tt9N7D6tJ92w5QccF3VTklNWxfSiFtxG19mzyg+juJ5QtKjrqpmyUVPiIPb1ObAkEAvjJnL7BIvAWTJS8Leb979M5qxWCm3C2iGifwpef65FCr9l3IJ2a73xNGxoZ0OsLXOGMmJvpeFlQCTr5pCVqOpQJBAJA/65zuw9VKE4LNrXkOgcbVaZSCXGNpGaaoeC7t7dEIyPHX7XtZyoLm4HyIy8xRGhUv9kN30OLdLDAU86ZkS/UCQQC0EkRfgida3GxT6BaVThWt0UCFXtyb2RiAaxAMA3Ymc7pbpq65nyqAKV/41ZN1jsL1P+n/PUuXGDKXGu+XS4WBAkAW/XNw/AoboqfX0h3NaPEH8OyK1YPZG6QJ6MbfQAlvHkI5H/PWXskmdBg4MOboON1BqUsOukjtElCou0cuoUAo";
	public final static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCO56mO8git4THRhfrCSXUj2C0SoBhIAFOXnCbuHYjPww9OO+8vFDpJKEvYQ0qI39nrgHnrrQYIEc9UPrmi+5C1bWOiWvCJyL42j8da1Wal2qVvIxK5jcF/AXHUGNwWNe/LmYMkOb8Artc2bZj78HK+q9MFiMDqkvlylGS1YjPRQQIDAQAB";
	public static final String notifyurl = "http://192.168.0.140:8094/monizhuang/api?type=100";

}
