package com.order_service.service;


import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.order_service.entity.Order;
import com.order_service.entity.OrderItem;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailService {

    private JavaMailSender mailSender;

    public void sendOrderConfirmation(Order order) {

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            helper.setTo(order.getUserEmail());

            helper.setSubject(
                    "Order Confirmed - Order #" + order.getId()
            );

            StringBuilder products = new StringBuilder();

            double total = 0.0;

            for (OrderItem item : order.getItems()) {

                double subtotal =
                        item.getPrice() * item.getQuantity();

                total = total + subtotal;

                products.append(
                        item.getProductName()
                )
                .append(" | Quantity: ")
                .append(item.getQuantity())
                .append(" | Price: ₹")
                .append(String.format("%.2f", item.getPrice()))
                .append(" | Subtotal: ₹")
                .append(String.format("%.2f", subtotal))
                .append("<br>");
            }

            String html = """
                    <html>
                    <body style="font-family: Arial, sans-serif;">

                        <h2>🎉 Order Confirmed!</h2>

                        <p>
                            <span style="
                                background-color: #155724;
                                color: white;
                                padding: 6px 12px;
                                border-radius: 5px;
                                font-weight: bold;
                            ">
                                SUCCESS
                            </span>
                        </p>

                        <p>Hello,</p>

                        <p>
                            Your order has been successfully placed
                            and confirmed.
                        </p>

                        <p>
                            <strong>Order ID:</strong> %d
                        </p>

                        <p>
                            <strong>Status:</strong> %s
                        </p>

                        <h3>Order Details</h3>

                        <p>
                            %s
                        </p>

                        <p>
                            <strong>Total: ₹%.2f</strong>
                        </p>

                        <hr>

                        <p>
                            Thank you for shopping with us! ❤️
                        </p>

                    </body>
                    </html>
                    """.formatted(
                            order.getId(),
                            order.getStatus(),
                            products.toString(),
                            total
                    );

            helper.setText(html, true);

            mailSender.send(message);

            System.out.println(
                    "Order confirmation email sent to: "
                    + order.getUserEmail()
            );

        } catch (MessagingException e) {

            System.out.println(
                    "Failed to send order confirmation email: "
                    + e.getMessage()
            );
        }
    }


    public void sendOrderFailure(Order order) {

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            helper.setTo(order.getUserEmail());

            helper.setSubject(
                    "Order Failed - Order #" + order.getId()
            );

            StringBuilder products = new StringBuilder();

            double total = 0.0;

            for (OrderItem item : order.getItems()) {

                double subtotal =
                        item.getPrice() * item.getQuantity();

                total = total + subtotal;

                products.append(
                        item.getProductName()
                )
                .append(" | Quantity: ")
                .append(item.getQuantity())
                .append(" | Price: ₹")
                .append(String.format("%.2f", item.getPrice()))
                .append(" | Subtotal: ₹")
                .append(String.format("%.2f", subtotal))
                .append("<br>");
            }

            String html = """
                    <html>
                    <body style="font-family: Arial, sans-serif;">

                        <h2>Order Failed</h2>

                        <p>
                            <span style="
                                background-color: #b02a37;
                                color: white;
                                padding: 6px 12px;
                                border-radius: 5px;
                                font-weight: bold;
                            ">
                                FAILED
                            </span>
                        </p>

                        <p>Hello,</p>

                        <p>
                            Unfortunately, your order could not be completed.
                        </p>

                        <p>
                            <strong>Order ID:</strong> %d
                        </p>

                        <p>
                            <strong>Status:</strong> %s
                        </p>

                        <h3>Order Details</h3>

                        <p>
                            %s
                        </p>

                        <p>
                            <strong>Total: ₹%.2f</strong>
                        </p>

                        <p>
                            Any reserved stock has been restored.
                        </p>

                        <hr>

                        <p>
                            Please try placing the order again.
                        </p>

                    </body>
                    </html>
                    """.formatted(
                            order.getId(),
                            order.getStatus(),
                            products.toString(),
                            total
                    );

            helper.setText(html, true);

            mailSender.send(message);

            System.out.println(
                    "Order failure email sent to: "
                    + order.getUserEmail()
            );

        } catch (MessagingException e) {

            System.out.println(
                    "Failed to send order failure email: "
                    + e.getMessage()
            );
        }
    }
}