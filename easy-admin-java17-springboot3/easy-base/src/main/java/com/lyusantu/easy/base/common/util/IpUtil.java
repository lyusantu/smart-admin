package com.lyusantu.easy.base.common.util;

import com.lyusantu.easy.base.common.constant.StringConst;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * IP工具类
 */
@Slf4j
public class IpUtil {

    private static Searcher IP_SEARCHER;

    /**
     * 初始化数据
     *
     * @param filePath
     */
    public static void init(String filePath) {

        try {
            byte[] cBuff = Searcher.loadContentFromFile(filePath);
            IP_SEARCHER = Searcher.newWithBuffer(cBuff);

        } catch (Throwable e) {
            log.error("初始化ip2region.xdb文件失败,报错信息:[{}]", e.getMessage(), e);
            throw new RuntimeException("系统异常!");
        }
    }


    /**
     * 自定义解析ip地址
     *
     * @param ipStr ipStr
     * @return 返回结果例 [河南省, 洛阳市, 洛龙区]
     */
    public static List<String> getRegionList(String ipStr) {
        List<String> regionList = new ArrayList<>();
        try {
            if (StringUtil.isEmpty(ipStr)) {
                return regionList;
            }
            ipStr = ipStr.trim();
            String region = IP_SEARCHER.search(ipStr);
            String[] split = region.split("\\|");
            regionList.addAll(Arrays.asList(split));
        } catch (Exception e) {
            log.error("解析ip地址出错", e);
        }
        return regionList;
    }

    /**
     * 自定义解析ip地址
     *
     * @param ipStr ipStr
     * @return 返回结果例 河南省|洛阳市|洛龙区
     */
    public static String getRegion(String ipStr) {
        try {
            if (StringUtil.isEmpty(ipStr)) {
                return StringConst.EMPTY;
            }
            ipStr = ipStr.trim();
            return IP_SEARCHER.search(ipStr);
        } catch (Exception e) {
            log.error("解析ip地址出错", e);
            return StringConst.EMPTY;
        }
    }

    /**
     * 获取本机第一个ip
     *
     * @return
     */
    public static String getLocalFirstIp() {
        List<String> list = getLocalIp();
        return list.size() > 0 ? list.get(0) : null;
    }

    /**
     * 获取本机ip
     *
     * @return
     */
    public static List<String> getLocalIp() {
        List<String> ipList = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    // 排除回环地址和IPv6地址
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.getHostAddress().contains(StringConst.COLON)) {
                        ipList.add(inetAddress.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipList;
    }
}
