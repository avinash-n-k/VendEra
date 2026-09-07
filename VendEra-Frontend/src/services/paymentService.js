import api from "./api";

export async function getPaymentByOrderId(orderId) {
    const response = await api.get(`/payments/${orderId}`);
    return response.data;
}

export async function waitForPayment(orderId) {

    const maxAttempts = 10;
    const delay = 1000;

    for (let attempt = 1; attempt <= maxAttempts; attempt++) {

        try {

            const payment = await getPaymentByOrderId(orderId);

            return payment;

        } catch (error) {

            if (error.response?.status !== 404) {
                throw error;
            }

            if (attempt === maxAttempts) {
                throw new Error("Payment was not created in time");
            }

            await new Promise(resolve => setTimeout(resolve, delay));
        }
    }
}

export async function getAllPayments() {
    const response = await api.get("/admin/payments");
    return response.data;
}


export async function verifyPayment(paymentData) {
    const response = await api.post("/payments/verify", paymentData);
    return response.data;
}



export async function failPayment(data) {
    return api.post("/payments/fail", data)
}