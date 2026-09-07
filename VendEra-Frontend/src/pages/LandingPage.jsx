import React from 'react'
import { Link } from 'react-router-dom'
import '../styles/LandingPage.css'

function LandingPage() {

  return (
    <div className="landing-page">

      <div className="landing-glow landing-glow-one"></div>
      <div className="landing-glow landing-glow-two"></div>

      <div className="landing-content">

        <div className="landing-brand">
          VendEra
        </div>

        <p className="landing-eyebrow">
          SHOP • DISCOVER • ENJOY
        </p>

        <h1>
          Everything you need,
          <span> all in one place.</span>
        </h1>

        <p className="landing-description">
          Discover products, shop with ease, and enjoy a simple
          and secure shopping experience with VendEra.
        </p>

        <div className="landing-buttons">

          <Link to="/login" className="landing-login">
            Login
          </Link>

          <Link to="/register" className="landing-register">
            Create Account
          </Link>

        </div>

        <div className="landing-features">

          <div>
            <span>✦</span>
            Secure Payments
          </div>

          <div>
            <span>✦</span>
            Easy Orders
          </div>

          <div>
            <span>✦</span>
            Simple Shopping
          </div>

        </div>

      </div>

    </div>
  )
}

export default LandingPage