import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { getAllOrders } from '../services/orderService'
import '../styles/AdminOrders.css'

function AdminOrders() {

    const navigate = useNavigate()

    const [orders, setOrders] = useState([])
    const [error, setError] = useState("")

    useEffect(() => {

        async function fetchOrders() {
            try {
                const response = await getAllOrders()
                console.log("All Orders:", response)
                setOrders(response)
            } catch (error) {
                console.log(error)
                setError("Failed to load orders")
            }
        }

        fetchOrders()

    }, [])

    return (
        <div className="admin-orders-page">

            <div className="admin-orders-container">

                <button
                    className="admin-orders-back-button"
                    onClick={() => navigate("/home")}
                >
                    ← Back to Home  </button>

                <section className="admin-orders-header">

                    <p className="admin-orders-eyebrow">
                        VENDERA ADMIN
                    </p>

                    <h1>
                        All <span>Orders.</span>
                    </h1>

                    <p>
                        Monitor customer orders and their current status.
                    </p>

                </section>

                {error && (
                    <div className="admin-orders-error">
                        {error}
                    </div>
                )}

                {orders.length === 0 && !error ? (

                    <div className="admin-orders-empty">
                        <h2>No Orders Yet</h2>
                        <p>
                            Customer orders will appear here once they are placed.
                        </p>
                    </div>

                ) : (

                    <section className="admin-orders-list">

                        {orders.map((order) => (

                            <div
                                className="admin-order-card"
                                key={order.orderId}
                            >

                                <div className="admin-order-card-header">

                                    <div>
                                        <p className="admin-order-label">
                                            ORDER
                                        </p>

                                        <h2>
                                            #{order.orderId}
                                        </h2>
                                    </div>

                                    <span
                                        className={`admin-order-status ${order.status?.toLowerCase()}`}
                                    >
                                        {order.status}
                                    </span>

                                </div>

                                <div className="admin-order-user">
                                    <span>Customer</span>
                                    <strong>{order.userEmail}</strong>
                                </div>

                                <div className="admin-order-divider"></div>

                                <div className="admin-order-items-header">
                                    <span>Product</span>
                                    <span>Quantity</span>
                                    <span>Price</span>
                                    <span>Subtotal</span>
                                </div>

                                <div className="admin-order-items">

                                    {order.items.map((item) => (

                                        <div
                                            className="admin-order-item"
                                            key={item.orderItemId}
                                        >

                                            <div className="admin-order-product">
                                                <h3>{item.productName}</h3>
                                            </div>

                                            <div className="admin-order-quantity">
                                                {item.quantity}
                                            </div>

                                            <div className="admin-order-price">
                                                ₹{Number(item.price).toLocaleString("en-IN")}
                                            </div>

                                            <div className="admin-order-subtotal">
                                                ₹{Number(item.price * item.quantity).toLocaleString("en-IN")}
                                            </div>

                                        </div>

                                    ))}

                                </div>

                                <div className="admin-order-divider"></div>

                                <div className="admin-order-total">

                                    <span>
                                        Order Total
                                    </span>

                                    <strong>
                                        ₹
                                        {order.items
                                            .reduce(
                                                (sum, item) =>
                                                    sum + item.price * item.quantity,
                                                0
                                            )
                                            .toLocaleString("en-IN")}
                                    </strong>

                                </div>

                            </div>

                        ))}

                    </section>

                )}

            </div>

        </div>
    )
}

export default AdminOrders