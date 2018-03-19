package com.wch.e3mall.fast;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import com.wch.e3mall.common.utils.FastDFSClient;

public class FastDFSTest {

//	@Test
	public void test01() throws FileNotFoundException, IOException, MyException {
		ClientGlobal.init("E:/ChuanZhi/mall/e3-manager-web/src/main/resources/conf/client.conf");
		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		StorageServer storageServer = null;
		StorageClient storageClient = new StorageClient(trackerServer,storageServer);
		String[] upload_file = storageClient.upload_file("C:/Users/FANTASY/Desktop/头像1.jpg", "jpg", null);
		for (String str : upload_file) {
			System.out.println(str);
		}
	}
	
//	@Test
	public void test02() throws Exception {
		FastDFSClient fastDFSClient = new FastDFSClient("E:/ChuanZhi/mall/e3-manager-web/src/main/resources/conf/client.conf");
		String uploadFile = fastDFSClient.uploadFile("C:/Users/FANTASY/Desktop/头像1.jpg");
		System.out.println(uploadFile);
	}
	
	
	
	
}
