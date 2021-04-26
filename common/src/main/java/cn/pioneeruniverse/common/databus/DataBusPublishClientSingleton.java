package cn.pioneeruniverse.common.databus;

import com.ccic.databus.client.publish.DataBusPublishClient;

/**
 * @Author:
 * @Description: dataBus连接池
 * @Date: Created in 2020年9月2日 16:48:56
 * @Modified By:
 */
public class DataBusPublishClientSingleton {
	private static volatile DataBusPublishClient dataBusPublishClient = null;

	private DataBusPublishClientSingleton() {
	}

	public static DataBusPublishClient getInstance(String databussFlag) {
		if (null == dataBusPublishClient) {
			synchronized (DataBusPublishClientSingleton.class) {
				if (null == dataBusPublishClient) {
					dataBusPublishClient = new DataBusPublishClient(databussFlag, null, null);
					dataBusPublishClient.init();
				}
			}
		}
		return dataBusPublishClient;
	}
}
