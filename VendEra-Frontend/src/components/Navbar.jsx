import React, { useState } from 'react'
import { Link, useLocation } from 'react-router-dom'
import { logout } from '../services/authService'
import { executeLogout } from '../services/authUtils'
import '../styles/NavBar.css'

function Navbar() {

  const location = useLocation()

  const role = localStorage.getItem("role")
  const token = localStorage.getItem("accessToken")

  const [menuOpen, setMenuOpen] = useState(false)

  async function handleLogout() {
    try {
      await logout()
    } catch (error) {
      console.log(error)
    } finally {
      executeLogout()
    }
  }

  function closeMenu() {
    setMenuOpen(false)
  }

  if (
    !token ||
    location.pathname === "/" ||
    location.pathname === "/login" ||
    location.pathname === "/register"
  ) {
    return null
  }

  return (
    <nav className="navbar">

      {/* Brand */}

      <div className="navbar-brand">
        <Link to="/home" onClick={closeMenu}>
          VendEra
        </Link>
      </div>


      {/* Mobile menu button */}

      <button
        className="navbar-menu-button"
        onClick={() => setMenuOpen(!menuOpen)}
        aria-label="Toggle navigation menu"
      >
        {menuOpen ? "✕" : "☰"}
      </button>


      {/* Navigation links */}

      <div className={`navbar-links ${menuOpen ? "navbar-links-open" : ""}`}>

        <Link to="/home" onClick={closeMenu}>
          Home
        </Link>

        <Link to="/products" onClick={closeMenu}>
          Products
        </Link>


        {/* USER */}

        {role === "USER" && (
          <>
            <Link to="/cart" onClick={closeMenu}>
              Cart
            </Link>

            <Link to="/my-orders" onClick={closeMenu}>
              My Orders
            </Link>
          </>
        )}


        {/* ADMIN */}

        {role === "ADMIN" && (
          <>
            <Link to="/addproduct" onClick={closeMenu}>
              Add Product
            </Link>

            <Link to="/admin/orders" onClick={closeMenu}>
              Admin Orders
            </Link>

            <Link to="/admin/payments" onClick={closeMenu}>
              Admin Payments
            </Link>
          </>
        )}


        {/* Mobile Logout */}

        <button
          className="mobile-logout"
          onClick={handleLogout}
        >
          Logout
        </button>

      </div>


      {/* Desktop Logout */}

      <div className="navbar-actions">

        <button onClick={handleLogout}>
          Logout
        </button>

      </div>

    </nav>
  )
}

export default Navbar