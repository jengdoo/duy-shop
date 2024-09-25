package com.example.shopapp.service.implement;

import com.example.shopapp.Model.Order;
import com.example.shopapp.Model.Payment;
import com.example.shopapp.Model.PaymentStatus;
import com.example.shopapp.config.VNPayConfig;
import com.example.shopapp.dto.PaymentDTO;
import com.example.shopapp.repositories.OrderRepo;
import com.example.shopapp.repositories.PaymentRepo;
import com.example.shopapp.request.PaymentRequest;
import com.example.shopapp.response.OrderResponse;
import com.example.shopapp.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepo paymentRepo;
    private final OrderRepo orderRepo;
    private final VNPayConfig vnPayConfig;

    @Override
    public PaymentDTO createPayment( OrderResponse orderResponse) throws Exception {
        // Kiểm tra đơn hàng
        if (orderResponse.getId() == null) {
            throw new RuntimeException("Order ID must not be null");
        }

        Order orderById = orderRepo.findById(orderResponse.getId())
                .orElseThrow(() -> new RuntimeException("Not found order with id:" + orderResponse.getId()));

        String uniqueId = UUID.randomUUID().toString().substring(0, 8); // Tạo ID giao dịch

        // Tạo URL thanh toán VNPay
        String paymentUrl = createPaymentUrl(uniqueId, orderById.getTotalMoney().longValue());

        // Chỉ tạo Payment mà không lưu vào DB ngay
        Payment payment = new Payment();
        payment.setOrder(orderById);
        payment.setTransactionId(uniqueId);
        payment.setAmount(orderById.getTotalMoney());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(new Date());
        payment.setTxnRef(uniqueId);
        payment = paymentRepo.save(payment);
        // Trả về PaymentDTO với URL thanh toán
        return PaymentDTO.convertToDTO(payment, paymentUrl);
    }

    private String createPaymentUrl(String uniqueId, long amount) throws UnsupportedEncodingException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amounts = amount*100;
        String bankCode = "NCB";

        String vnp_TxnRef = uniqueId;
        String vnp_IpAddr = "127.0.0.1";

        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amounts));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_BankCode", bankCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", "http://localhost:4200/payment/payment-callback");
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        return paymentUrl;
    }



    @Override
    public PaymentDTO getPaymentByTransactionId(String transactionId) {
//        Payment payment = paymentRepo.findByTransactionId(transactionId);
//        if (payment == null) {
//            throw new RuntimeException("Không tìm thấy thanh toán với mã giao dịch: " + transactionId);
//        }
//
//        // Giả sử bạn có cách để tạo paymentUrl từ mã giao dịch
//        String paymentUrl = createPaymentUrl(payment.getTransactionId(), payment.getAmount());

        return  null;
    }

    @Override
    public void updatePaymentStatus(String transactionId, PaymentStatus status) {
        Payment payment = paymentRepo.findByTransactionId(transactionId);
        if (payment != null) {
            payment.setStatus(status);
            paymentRepo.save(payment);
        }
    }

}
