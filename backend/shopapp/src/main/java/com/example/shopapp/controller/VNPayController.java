package com.example.shopapp.controller;

import com.example.shopapp.config.VNPayConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vnpay_return")
public class VNPayController {
//    private final VNPayConfig vnPayConfig;
//    @GetMapping("")
//    public ResponseEntity<Map<String, Object>> vnPayReturn(@RequestParam Map<String, String> params) {
//        System.out.println("Response from VNPay: " + params);
//
//        // Kiểm tra các tham số cần thiết
//        String transactionId = params.get("vnp_TxnRef");
//        String secureHash = params.get("vnp_SecureHash");
//
//        Map<String, Object> response = new HashMap<>();
//
//        // Thực hiện kiểm tra secure hash để xác thực dữ liệu
////        if (isSecureHashValid(params, secureHash)) {
////            // Cập nhật trạng thái thanh toán trong cơ sở dữ liệu
////            // Có thể thêm logic để lưu trạng thái thanh toán (thành công, thất bại, ...)
////
////            response.put("code", "00");
////            response.put("message", "Thanh toán thành công!");
////            response.put("transactionId", transactionId);
////            return ResponseEntity.ok(response);
////        } else {
//            response.put("code", "01");
//            response.put("message", "Dữ liệu không hợp lệ!");
//            return ResponseEntity.badRequest().body(response);
//        }
//         return ResponseEntity.ok(response);
//    }
//
////    private boolean isSecureHashValid(Map<String, String> params, String secureHash) {
////        // Tính toán secure hash từ các tham số và so sánh với secureHash nhận được
////        String calculatedHash = vnPayConfig.hmacSHA512(vnPayConfig.getSecretKey(), buildHashData(params));
////        return calculatedHash.equals(secureHash);
////    }
//
//    private String buildHashData(Map<String, String> params) {
//        List<String> fieldNames = new ArrayList<>(params.keySet());
//        Collections.sort(fieldNames);
//        StringBuilder hashData = new StringBuilder();
//
//        for (String fieldName : fieldNames) {
//            String fieldValue = params.get(fieldName);
//            if (fieldValue != null && !fieldValue.isEmpty() && !fieldName.equals("vnp_SecureHash")) {
//                hashData.append(fieldName).append("=").append(fieldValue).append("&");
//            }
//        }
//
//        // Xóa dấu '&' cuối cùng
//        if (hashData.length() > 0) {
//            hashData.setLength(hashData.length() - 1);
//        }
//
//        return hashData.toString();
//    }

}
