package com.tjg_project.candy.domain.order.service;

import com.tjg_project.candy.domain.order.dto.KakaoApproveResponse;
import com.tjg_project.candy.domain.order.dto.KakaoReadyResponse;
import com.tjg_project.candy.domain.order.entity.KakaoPay;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class KakaoPayService {

    @Value("${kakao.pay.host}") private String KAKAO_PAY_HOST;
    @Value("${kakao.pay.admin-key}") private String ADMIN_KEY;
    @Value("${kakao.pay.cid}") private String CID;
    @Value("${kakao.pay.ready-path}") private String READY_PATH;
    @Value("${kakao.pay.approve-path}") private String APPROVE_PATH;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ConcurrentHashMap<String, String> tidStore = new ConcurrentHashMap<>();
    String user_id = "test";
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + ADMIN_KEY);
        return headers;
    }

    public KakaoReadyResponse ready(KakaoPay kakaoPay) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", CID);
        params.add("partner_order_id", kakaoPay.getOrderId());
        params.add("partner_user_id", user_id);
        params.add("item_name", kakaoPay.getItemName());
        params.add("quantity", String.valueOf(kakaoPay.getQty()));
        params.add("total_amount", String.valueOf(kakaoPay.getTotalAmount()));
        params.add("tax_free_amount", "0");
        params.add("approval_url",
                "https://candybackend-6skt.onrender.com/payment/qr/success?orderId=" + kakaoPay.getOrderId());

        params.add("cancel_url",
                "https://candybackend-6skt.onrender.com/payment/qr/cancel?orderId=" + kakaoPay.getOrderId());

        params.add("fail_url",
                "https://candybackend-6skt.onrender.com/payment/qr/fail?orderId=" + kakaoPay.getOrderId());

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, getHeaders());

        String url = KAKAO_PAY_HOST + "/v1" + READY_PATH;
        KakaoReadyResponse res = restTemplate.postForObject(url, body, KakaoReadyResponse.class);

        tidStore.put(kakaoPay.getOrderId(), res.getTid());
        return res;
    }

    public KakaoApproveResponse approve(String orderId, String pgToken) {
        String tid = tidStore.get(orderId);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", CID);
        params.add("tid", tid);
        params.add("partner_order_id", orderId);
        params.add("partner_user_id", user_id);
        params.add("pg_token", pgToken);

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, getHeaders());
        String url = KAKAO_PAY_HOST + "/v1" + APPROVE_PATH;

        return restTemplate.postForObject(url, body, KakaoApproveResponse.class);
    }
}
