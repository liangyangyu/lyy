package cn.itcast.fastdfs;

import org.csource.fastdfs.*;
import org.junit.Test;

public class FastDFSTest {

    @Test
    public void test() throws Exception {
        //配置文件路径
        String conf_filename = ClassLoader.getSystemResource("fastdfs/tracker.conf").getPath();

        //设置初始化参数
        ClientGlobal.init(conf_filename);

        //创建trackerClient
        TrackerClient trackerClient = new TrackerClient();

        //创建追踪服务器
        TrackerServer trackerServer = trackerClient.getConnection();

        //创建storageServer
        StorageServer storageServer = null;

        //创建存储客户端StorageClient
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);

        //上传图片
        /**
         * 返回的数组包含：
         * group1
         * M00/00/00/wKgMqFuLYVCATVr6AABw0se6LsY814.jpg
         */
        String[] uploadFile = storageClient.upload_file("D:\\itcast\\pics\\575968fcN2faf4aa4.jpg", "jpg", null);

        if (uploadFile != null && uploadFile.length > 0) {
            for (String str : uploadFile) {
                System.out.println(str);
            }

            //获取存储服务器信息
            ServerInfo[] serverInfos = trackerClient.getFetchStorages(trackerServer, uploadFile[0], uploadFile[1]);
            for (ServerInfo serverInfo : serverInfos) {
                System.out.println("存储服务器的：ip = " + serverInfo.getIpAddr() + "； port = " + serverInfo.getPort());
            }

            String url = "http://" + serverInfos[0].getIpAddr() + "/" + uploadFile[0] + "/" + uploadFile[1];
            System.out.println("url = "  + url);
        }
    }
}
