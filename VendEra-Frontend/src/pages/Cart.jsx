import React, { useState } from 'react'
import { createOrder } from '../services/orderService'
import {
  waitForPayment,
  verifyPayment,
  failPayment
} from '../services/paymentService'
import '../styles/Cart.css'

function Cart({ cart, setCart }) {

  const [isProcessing, setIsProcessing] = useState(false)
  const [statusMessage, setStatusMessage] = useState("")
  const [statusType, setStatusType] = useState("")

  const total = cart.reduce((sum, item) => {
    return sum + (item.price * item.quantity)
  }, 0)


  function removeFromCart(productId) {

    const updatedCart = cart.filter(
      item => item.productId !== productId
    )

    setCart(updatedCart)
  }


  function increaseQuantity(productId) {

    const updatedCart = cart.map(item => {

      if (item.productId === productId) {

        if (item.quantity >= item.stock) {
          return item
        }

        return {
          ...item,
          quantity: item.quantity + 1
        }
      }

      return item
    })

    setCart(updatedCart)
  }


  function decreaseQuantity(productId) {

    const updatedCart = cart.map(item => {

      if (item.productId === productId) {

        if (item.quantity <= 1) {
          return item
        }

        return {
          ...item,
          quantity: item.quantity - 1
        }
      }

      return item
    })

    setCart(updatedCart)
  }


  async function placeOrder() {

    if (cart.length === 0 || isProcessing) {
      return
    }

    setIsProcessing(true)

    setStatusType("processing")
    setStatusMessage("Placing your order...")

    const orderRequest = {
      items: cart.map(item => {
        return {
          productId: item.productId,
          quantity: item.quantity
        }
      })
    }

    try {

      const response = await createOrder(orderRequest)

      const orderId = response.data.orderId

      console.log("Order ID:", orderId)

      setStatusType("processing")
      setStatusMessage("Preparing payment...")

      const payment = await waitForPayment(orderId)

      console.log("Payment:", payment)

      let paymentFailed = false

      const options = {

        key: import.meta.env.VITE_RAZORPAY_KEY_ID,

        amount: payment.amount * 100,

        currency: "INR",

        name: "VendEra",

        description: "Order Payment",

        order_id: payment.razorpayOrderId,

        handler: async function (response) {

          console.log("Razorpay Response:", response)

          setStatusType("processing")
          setStatusMessage("Verifying payment...")

          try {

            const verificationResponse = await verifyPayment({
              orderId: orderId,
              razorpayPaymentId: response.razorpay_payment_id,
              razorpayOrderId: response.razorpay_order_id,
              razorpaySignature: response.razorpay_signature
            })

            console.log(
              "Payment Verification Response:",
              verificationResponse
            )

            setCart([])

            setStatusType("success")
            setStatusMessage(
              "Payment successful! Your order has been placed successfully."
            )

            setIsProcessing(false)

          } catch (error) {

            console.log("Payment verification failed:", error)

            setStatusType("error")
            setStatusMessage("Payment verification failed.")

            setIsProcessing(false)
          }
        },

        modal: {

          ondismiss: function () {

            console.log("Razorpay checkout closed")

            if (!paymentFailed) {

              setStatusType("cancelled")
              setStatusMessage("Payment cancelled.")
            }

            setIsProcessing(false)
          }
        },

        theme: {
    color: "#e38353"
}
      }

      const razorpay = new window.Razorpay(options)

      razorpay.on("payment.failed", async function (response) {

        console.log(
          "Razorpay Payment Failed:",
          response
        )

        try {

          await failPayment({
            orderId: orderId,
            razorpayOrderId: payment.razorpayOrderId,
            razorpayPaymentId:
              response.error.metadata?.payment_id
          })

          paymentFailed = true

          setStatusType("error")

          setStatusMessage(
            "Payment failed. Your order was cancelled and stock has been restored."
          )

          setTimeout(() => {
            setStatusMessage("")
            setStatusType("")
          }, 3000)

          razorpay.close()

        } catch (error) {

          console.log(
            "Failed to notify Payment-Service:",
            error
          )

          setStatusType("error")

          setStatusMessage(
            "Payment failed, but we could not update the order status. Please contact support."
          )

        } finally {

          setIsProcessing(false)

        }
      })

      razorpay.open()

    } catch (error) {

      console.log(
        "Order creation/payment preparation failed:",
        error
      )

      setStatusType("error")

      setStatusMessage(
        "Unable to prepare your order. Please try again."
      )

      setIsProcessing(false)
    }
  }


  return (
    <div className="cart-page">

      <section className="cart-header">

        <p className="cart-eyebrow">
          VENDERA CHECKOUT
        </p>

        <h1>
          Your <span>Cart.</span>
        </h1>

        <p>
          Review your items and complete your order.
        </p>

      </section>


      {cart.length === 0 ? (

        <div className="empty-cart">

          <div className="empty-cart-icon">
            ✦
          </div>

          {statusMessage ? (

            <>
              <h2>
                Order Successful!
              </h2>

              <p>
                {statusMessage}
              </p>
            </>

          ) : (

            <>
              <h2>
                Your cart is empty
              </h2>

              <p>
                Looks like you haven't added anything yet.
              </p>
            </>

          )}

        </div>

      ) : (

        <div className="cart-layout">

          <div className="cart-items">

            {cart.map((item) => (

              <div
                className="cart-item"
                key={item.productId}
              >

                <div className="cart-item-top">

                  <div>

                    <p className="item-label">
                      PRODUCT
                    </p>

                    <h2>
                      {item.productName}
                    </h2>

                  </div>

                  <p className="item-price">
                    ₹{item.price.toLocaleString("en-IN")}
                  </p>

                </div>


                <div className="cart-item-bottom">

                  <div className="quantity-control">

                    <button
                      onClick={() =>
                        decreaseQuantity(item.productId)
                      }
                    >
                      −
                    </button>

                    <span>
                      {item.quantity}
                    </span>

                    <button
                      onClick={() =>
                        increaseQuantity(item.productId)
                      }
                    >
                      +
                    </button>

                  </div>


                  <div className="item-subtotal">

                    <span>
                      Subtotal
                    </span>

                    <strong>
                      ₹{(
                        item.price * item.quantity
                      ).toLocaleString("en-IN")}
                    </strong>

                  </div>


                  <button
                    className="remove-button"
                    onClick={() =>
                      removeFromCart(item.productId)
                    }
                  >
                    Remove
                  </button>

                </div>

              </div>

            ))}

          </div>


          <aside className="order-summary">

            <p className="summary-label">
              ORDER SUMMARY
            </p>

            <h2>
              Ready to checkout?
            </h2>

            <div className="summary-line">

              <span>
                Items
              </span>

              <span>
                {cart.length}
              </span>

            </div>


            <div className="summary-divider"></div>


            <div className="summary-total">

              <span>
                Total
              </span>

              <strong>
                ₹{total.toLocaleString("en-IN")}
              </strong>

            </div>


            {cart.length > 0 && (

              <button
                onClick={placeOrder}
                disabled={isProcessing}
              >
                {isProcessing
                  ? "Processing..."
                  : "Place Order"}
              </button>

            )}


            {statusMessage && (

              <div
                className={`payment-status ${statusType}`}
              >

                <span>
                  {statusType === "success"
                    ? "✓"
                    : statusType === "error"
                      ? "✕"
                      : statusType === "cancelled"
                        ? "!"
                        : "•"}
                </span>

                <p>
                  {statusMessage}
                </p>

              </div>

            )}


            <p className="payment-note">
              Secure payment powered by Razorpay.
            </p>

          </aside>

        </div>

      )}

    </div>
  )
}

export default Cart