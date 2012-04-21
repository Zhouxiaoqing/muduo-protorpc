package com.chenshuo.muduo.zurg;

import java.net.InetSocketAddress;

import com.chenshuo.muduo.protorpc.RpcChannel;
import com.chenshuo.muduo.protorpc.RpcClient;
import com.chenshuo.muduo.zurg.proto.SlaveProto;
import com.chenshuo.muduo.zurg.proto.SlaveProto.GetFileContentRequest;
import com.chenshuo.muduo.zurg.proto.SlaveProto.GetFileContentResponse;
import com.chenshuo.muduo.zurg.proto.SlaveProto.RunCommandRequest;
import com.chenshuo.muduo.zurg.proto.SlaveProto.RunCommandResponse;
import com.chenshuo.muduo.zurg.proto.SlaveProto.SlaveService;
import com.google.protobuf.ByteString;

public class SlaveClient {

    private RpcClient client;
    private RpcChannel channel;
    private SlaveService.BlockingInterface slaveService;

    public SlaveClient(InetSocketAddress addr) {
        client = new RpcClient();
        channel = client.blockingConnect(addr);
        slaveService = SlaveProto.SlaveService.newBlockingStub(channel);
    }

    public void close() {
        channel.disconnect();
        client.stop();
    }

    public void getFileContent(String fileName) throws Exception {
        GetFileContentRequest request = GetFileContentRequest.newBuilder().setFileName(fileName).build();
        GetFileContentResponse response = slaveService.getFileContent(null, request);
        System.out.println(response.getErrorCode());
        System.out.println(response.getFileSize());
        ByteString content = response.getContent();
        System.out.println(content.size());
        if (content.size() < 8192) {
            System.out.println(content.toStringUtf8());
        }
    }

    public void runCommand(String cmd, String... args) throws Exception {
        RunCommandRequest request = RunCommandRequest.newBuilder().setCommand(cmd).build();
        RunCommandResponse response = slaveService.runCommand(null, request);
        System.out.println(response);
    }

    public static void main(String[] args) throws Exception {
        InetSocketAddress addr = new InetSocketAddress(args[0], Integer.parseInt(args[1]));
        SlaveClient slaveClient = new SlaveClient(addr);

        slaveClient.getFileContent("/proc/uptime");
        slaveClient.runCommand("/bin/NotExist");
        slaveClient.runCommand("/etc/hosts");
        slaveClient.runCommand("/bin/pwd");
        slaveClient.runCommand("./lsfd.rb");
        slaveClient.runCommand("/bin/false");
        slaveClient.runCommand("./sleep.py");

        slaveClient.close();
    }
}