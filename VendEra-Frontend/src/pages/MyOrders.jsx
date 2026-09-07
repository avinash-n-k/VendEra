import React, { useEffect, useState } from 'react'
import { getMyOrders } from '../services/orderService'
import '../styles/MyOrders.css'

function MyOrders() {

  const [orders, setOrders] = useState([])

  useEffect(() => {

    async function fetchOrders() {

      try {

        const response = await getMyOrders()

        console.log("My Orders:", response)

        setOrders(response)

      } catch (error) {

        console.log("Failed to fetch orders:", error)

      }
    }

    fetchOrders()

  }, [])

  return (
    <div className="my-orders-page">

      <section className="orders-header">

        <p className="orders-eyebrow">
          VENDERA HISTORY
        </p>

        <h1>
          My <span>Orders.</span>
        </h1>

        <p className="orders-subtitle">
          Track your purchases and view your order details.
        </p>

      </section>


      {orders.length === 0 ? (

        <div className="orders-empty">

          <div className="orders-empty-icon">
            ✦
          </div>

          <h2>
            No orders yet
          </h2>

          <p>
            Your completed purchases will appear here.
          </p>

        </div>

      ) : (

        <section className="orders-list">

          {orders.map(order => {

            const orderTotal = order.items.reduce(
              (sum, item) => {
                return sum + (item.price * item.quantity)
              },
              0
            )

            return (

              <div
                className="order-card"
                key={order.orderId}
              >

                <div className="order-card-header">

                  <div>

                    <p className="order-label">
                      ORDER
                    </p>

                    <h2>
                      #{order.orderId}
                    </h2>

                  </div>


                  <span
                    className={`order-status ${order.status?.toLowerCase()}`}
                  >
                    {order.status}
                  </span>

                </div>


                <div className="order-divider"></div>


                <div className="order-items">

                  {order.items.map(item => (

                    <div
                      className="order-item"
                      key={item.orderItemId}
                    >

                      <div className="order-item-info">

                        <h3>
                          {item.productName}
                        </h3>

                        <p>
                          ₹{Number(item.price).toLocaleString("en-IN")}
                          {" × "}
                          {item.quantity}
                        </p>

                      </div>


                      <div className="order-item-subtotal">

                        <span>
                          Subtotal
                        </span>

                        <strong>
                          ₹{(
                            item.price * item.quantity
                          ).toLocaleString("en-IN")}
                        </strong>

                      </div>

                    </div>

                  ))}

                </div>


                <div className="order-divider"></div>


                <div className="order-total">

                  <span>
                    Order Total
                  </span>

                  <strong>
                    ₹{orderTotal.toLocaleString("en-IN")}
                  </strong>

                </div>

              </div>

            )

          })}

        </section>

      )}

    </div>
  )
}

export default MyOrders