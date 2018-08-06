package com.hbb.socketdemo.controller;

import com.google.common.collect.Maps;
import com.hbb.socketdemo.dispatcher.WebSocketMessageDispatcher;
import com.hbb.socketdemo.util.QRCodeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 南来
 * @version V1.0
 * @Description //todo
 * @date 2018-08-05 19:20
 */
@Controller
public class QrCodeController {

    @Value("${server.address}")
    private String host;

    @Value("${server.port}")
    private String port;

    @RequestMapping("/qr/{qr}")
    public void getQrCode(@PathVariable String qr, HttpServletResponse response) throws IOException {
        response.setContentType("image/png");
        ImageIO.write(QRCodeUtil.generateQRImage("http://" + host + ":" + port + "/scan/" + qr), "png", response.getOutputStream());
    }

    @RequestMapping("/scan/{qr}")
    public @ResponseBody Map scanQrCode(@PathVariable String qr) throws IOException {
        WebSocketMessageDispatcher.doScan(null, qr);
        HashMap<String, String> map = Maps.newHashMap();
        map.put("msg", "ok");
        return map;
    }

}
