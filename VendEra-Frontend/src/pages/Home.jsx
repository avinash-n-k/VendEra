import React from 'react'
import { Link } from 'react-router-dom'
import '../styles/Home.css'

function Home() {

  const role = localStorage.getItem("role")

  if (role === "ADMIN") {

    return (
      <div className="home-page">

        <div className="home-glow home-glow-one"></div>
        <div className="home-glow home-glow-two"></div>

        <main className="home-content">

          <p className="home-eyebrow">
            VENDERA ADMIN
          </p>

          <h1>
            Manage VendEra,
            <span> all in one place.</span>
          </h1>

          <p className="home-description">
            Manage products, review customer orders, and keep an eye
            on payment activity from your admin dashboard.
          </p>

          <div className="home-admin-actions">

            <Link to="/addproduct" className="home-action-card">
              <div className="home-feature-icon">✦</div>

              <h3>
                Manage Products
              </h3>

              <p>
                Add products and manage the VendEra catalogue.
              </p>
            </Link>


            <Link to="/admin/orders" className="home-action-card">
              <div className="home-feature-icon">✦</div>

              <h3>
                View Orders
              </h3>

              <p>
                Review customer orders and their current status.
              </p>
            </Link>


            <Link to="/admin/payments" className="home-action-card">
              <div className="home-feature-icon">✦</div>

              <h3>
                View Payments
              </h3>

              <p>
                View payment information for customer orders.
              </p>
            </Link>

          </div>

        </main>

      </div>
    )
  }


  return (
    <div className="home-page">

      <div className="home-glow home-glow-one"></div>
      <div className="home-glow home-glow-two"></div>

      <main className="home-content">

        <p className="home-eyebrow">
          WELCOME BACK
        </p>

        <h1>
          Ready to find
          <span> something you love?</span>
        </h1>

        <p className="home-description">
          Explore our products, add your favourites to the cart,
          and enjoy a simple and secure shopping experience.
        </p>

        <Link to="/products" className="home-shop-button">
          Explore Products
        </Link>


        <section className="home-features">

          <div className="home-feature">
            <div className="home-feature-icon">
              ✦
            </div>

            <h3>
              Discover
            </h3>

            <p>
              Browse products and find something that fits your needs.
            </p>
          </div>


          <div className="home-feature">
            <div className="home-feature-icon">
              ✦
            </div>

            <h3>
              Shop Securely
            </h3>

            <p>
              Enjoy a secure checkout experience with trusted payments.
            </p>
          </div>


          <div className="home-feature">
            <div className="home-feature-icon">
              ✦
            </div>

            <h3>
              Track Orders
            </h3>

            <p>
              Keep an eye on your orders from purchase to confirmation.
            </p>
          </div>

        </section>

      </main>

    </div>
  )
}

export default Home