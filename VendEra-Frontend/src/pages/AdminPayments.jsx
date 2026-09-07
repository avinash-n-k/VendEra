import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { getAllPayments } from '../services/paymentService'
import '../styles/AdminPayments.css'

function AdminPayments() {

    const navigate = useNavigate()

    const [payments, setPayments] = useState([])
    const [error, setError] = useState("")

    useEffect(() => {

        async function fetchPayments() {
            try {
                const response = await getAllPayments()
                console.log("All Payments:", response)
                setPayments(response)
            } catch (error) {
                console.log(error)
                setError("Failed to load payments")
            }
        }

        fetchPayments()

    }, [])

    return (
        <div className="admin-payments-page">

            <div className="admin-payments-container">

                <button
                    className="admin-payments-back-button"
                    onClick={() => navigate("/home")}
                >
                    ← Back to Home
                </button>

                <section className="admin-payments-header">

                    <p className="admin-payments-eyebrow">
                        VENDERA ADMIN
                    </p>

                    <h1>
                        All <span>Payments.</span>
                    </h1>

                    <p>
                        Monitor payment transactions and Razorpay details.
                    </p>

                </section>

                {error && (
                    <div className="admin-payments-error">
                        {error}
                    </div>
                )}

                {payments.length === 0 && !error ? (

                    <div className="admin-payments-empty">
                        <h2>No Payments Yet</h2>
                        <p>
                            Payment transactions will appear here once customers complete an order.
                        </p>
                    </div>

                ) : (

                    <section className="admin-payments-list">

                        {payments.map((payment) => (

                            <div
                                className="admin-payment-card"
                                key={payment.id}
                            >

                                <div className="admin-payment-card-header">

                                    <div>
                                        <p className="admin-payment-label">
                                            PAYMENT
                                        </p>

                                        <h2>
                                            #{payment.id}
                                        </h2>
                                    </div>

                                    <span
                                        className={`admin-payment-status ${payment.status?.toLowerCase()}`}
                                    >
                                        {payment.status}
                                    </span>

                                </div>

                                <div className="admin-payment-main-info">

                                    <div className="admin-payment-info-box">
                                        <span>Order ID</span>
                                        <strong>#{payment.orderId}</strong>
                                    </div>

                                    <div className="admin-payment-info-box">
                                        <span>Amount</span>
                                        <strong>
                                            ₹{Number(payment.amount).toLocaleString("en-IN")}
                                        </strong>
                                    </div>

                                </div>

                                <div className="admin-payment-divider"></div>

                                <div className="admin-payment-technical">

                                    <div className="admin-payment-detail">
                                        <span>Razorpay Order ID</span>
                                        <strong>
                                            {payment.razorpayOrderId || "—"}
                                        </strong>
                                    </div>

                                    <div className="admin-payment-detail">
                                        <span>Razorpay Payment ID</span>
                                        <strong>
                                            {payment.razorpayPaymentId || "—"}
                                        </strong>
                                    </div>

                                </div>

                            </div>

                        ))}

                    </section>

                )}

            </div>

        </div>
    )
}

export default AdminPayments