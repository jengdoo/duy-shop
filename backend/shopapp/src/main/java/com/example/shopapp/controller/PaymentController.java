package com.example.shopapp.controller;

import com.example.shopapp.Model.Order;
import com.example.shopapp.Model.OrderStatus;
import com.example.shopapp.Model.Payment;
import com.example.shopapp.Model.PaymentStatus;
import com.example.shopapp.config.VNPayConfig;
import com.example.shopapp.dto.PaymentDTO;
import com.example.shopapp.repositories.OrderRepo;
import com.example.shopapp.repositories.PaymentRepo;
import com.example.shopapp.request.PaymentRequest;
import com.example.shopapp.response.OrderResponse;
import com.example.shopapp.response.PaymentResponse;
import com.example.shopapp.service.OrderService;
import com.example.shopapp.service.PaymentService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/payment")
public class PaymentController {
    private final OrderService orderService;
    private final OrderRepo orderRepo;
    private final PaymentService paymentService;
    private final PaymentRepo paymentRepo;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            OrderResponse orderResponse = orderService.getOrder(paymentRequest.getOrderId());
            PaymentDTO paymentDTO= paymentService.createPayment(orderResponse);
            return ResponseEntity.ok(paymentDTO);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @GetMapping("/payment-callback")
    public ResponseEntity<Map<String, String>> paymentCallback(@RequestParam Map<String, String> params) {
        String vnp_TxnRef = params.get("vnp_TxnRef");
        System.out.println("Received txnRef: " + vnp_TxnRef);
        String vnp_SecureHash = params.get("vnp_SecureHash");

        String calculatedHash = generateSecureHash(params);

        if (vnp_SecureHash.equals(calculatedHash)) {
            String paymentStatus = params.get("vnp_ResponseCode");

            // Tìm Payment theo txnRef
            Payment payment = paymentRepo.findByTxnRef(vnp_TxnRef);
            if (payment != null) {
                Order order = payment.getOrder(); // Lấy Order từ Payment
                if (order != null) {
                    if ("00".equals(paymentStatus)) {
                        paymentService.updatePaymentStatus(vnp_TxnRef, PaymentStatus.SUCCESS);
                        Map<String, String> response = new HashMap<>();
                        response.put("status", "SUCCESS");
                        response.put("message", "Thanh toán thành công");
                        response.put("orderId", order.getId().toString()); // Trả về orderId
                        return ResponseEntity.ok(response);
                    } else {
                        paymentService.updatePaymentStatus(vnp_TxnRef, PaymentStatus.FAILED);
                        return ResponseEntity.ok(Map.of("status", "FAILED", "message", "Thanh toán thất bại"));
                    }
                } else {
                    return ResponseEntity.ok(Map.of("status", "FAILED", "message", "Đơn hàng không tồn tại"));
                }
            } else {
                return ResponseEntity.ok(Map.of("status", "FAILED", "message", "Thanh toán không tồn tại"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", "INVALID", "message", "Mã bảo mật không hợp lệ"));
        }
    }





    private String generateSecureHash(Map<String, String> params) {
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();

        for (String fieldName : fieldNames) {
            String fieldValue = params.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0 && !fieldName.equals("vnp_SecureHash")) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                hashData.append('&');
            }
        }

        // Xóa ký tự '&' cuối cùng nếu có
        if (hashData.length() > 0) {
            hashData.deleteCharAt(hashData.length() - 1);
        }

        // Thêm secret key vào cuối chuỗi hash
        return VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
    }


//    @GetMapping("/payment-callback")
//    public void paymentCallback(@RequestParam Map<String, String> queryParams, HttpServletResponse response) throws IOException, IOException {
//        String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");
//        String orderId = queryParams.get("orderId");
//        String registerServiceId = queryParams.get("registerServiceId");
//        String billId = queryParams.get("billId");
//        if(orderId!= null && !orderId.equals("")) {
//            if ("00".equals(vnp_ResponseCode)) {
//                // Giao dịch thành công
//                // Thực hiện các xử lý cần thiết, ví dụ: cập nhật CSDL
//                Order orderResponse = orderRepo.findById(Long.parseLong(queryParams.get("orderId"))).orElseThrow(()->new RuntimeException("Khong tim thay id"));
//                orderResponse.setStatus(OrderStatus.PENDING);
//                orderRepo.save(orderResponse);
//                response.sendRedirect("http://localhost:4200/bills");
//            } else {
//                // Giao dịch thất bại
//                // Thực hiện các xử lý cần thiết, ví dụ: không cập nhật CSDL\
//                response.sendRedirect("http://localhost:4200/payment-failed");
//
//            }
//        }
////        if(registerServiceId!= null && !registerServiceId.equals("")) {
////            if ("00".equals(vnp_ResponseCode)) {
////                // Giao dịch thành công
////                // Thực hiện các xử lý cần thiết, ví dụ: cập nhật CSDL
////                RegisterServices registerServices = registerServicesRepository.findById(Integer.parseInt(queryParams.get("registerServiceId")))
////                        .orElseThrow(() -> new NotFoundException("Không tồn tại dịch vụ này của sinh viên"));
////                registerServices.setStatus(1);
////                registerServicesRepository.save(registerServices);
////                response.sendRedirect("http://localhost:4200/info-student");
////            } else {
////                // Giao dịch thất bại
////                // Thực hiện các xử lý cần thiết, ví dụ: không cập nhật CSDL\
////                response.sendRedirect("http://localhost:4200/payment-failed");
////
////            }
////        }
////        if(billId!= null && !billId.equals("")) {
////            if ("00".equals(vnp_ResponseCode)) {
////                // Giao dịch thành công
////                // Thực hiện các xử lý cần thiết, ví dụ: cập nhật CSDL
////                Bill bill = billRepository.findById(Integer.parseInt(queryParams.get("billId")))
////                        .orElseThrow(() -> new NotFoundException("Không tồn tại hóa đơn điện nước này"));
////                bill.setStatus(true);
////                billRepository.save(bill);
////                response.sendRedirect("http://localhost:4200/info-student");
////            } else {
////                // Giao dịch thất bại
////                // Thực hiện các xử lý cần thiết, ví dụ: không cập nhật CSDL\
////                response.sendRedirect("http://localhost:4200/payment-failed");
////
////            }
////        }
//
//
//    }
}
