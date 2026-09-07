import React, { useState,useEffect } from 'react'
import Navbar from './components/Navbar'
import { Routes, Route } from 'react-router-dom'
import Home from './pages/Home'
import Products from './pages/Products'
import NotFound from './pages/NotFound'
import Login from './pages/Login'
import Register from './pages/Register'
import Cart from './pages/Cart'
import MyOrders from './pages/MyOrders'
import AddProduct from './pages/AddProduct'
import AdminOrders from './pages/AdminOrders'
import AdminPayments from './pages/AdminPayments'
import ProtectedRoute from './components/ProtectedRoute'
import LandingPage from './pages/LandingPage'

function App() {

  const [cart, setCart] = useState(() => {
    const savedCart = localStorage.getItem("cart")
    return savedCart ? JSON.parse(savedCart) : []
  })

  useEffect(() => {
    localStorage.setItem("cart", JSON.stringify(cart))
  }, [cart])

  return (
    <div>

      <Navbar/>

      <Routes>

        {/* Ivaa Login Register Public path ivaa yalarau hokere idaraga */}

        <Route
          path="/login"
          caseSensitive
          element={<Login />}
        />

        <Route
          path="/register"
          caseSensitive
          element={<Register />}
        />


        <Route
  path="/"
  caseSensitive
  element={<LandingPage />}
/>

 {/* Authenticated User Routes Paaa Ivuuuu */}

<Route
  path="/home"
  caseSensitive
  element={
    <ProtectedRoute>
      <Home />
    </ProtectedRoute>
  }
/>

       

        

        <Route
          path="/products"
          caseSensitive
          element={
            <ProtectedRoute>
              <Products cart={cart} setCart={setCart} />
            </ProtectedRoute>
          }
        />

        <Route
          path="/cart"
          caseSensitive
          element={
            <ProtectedRoute>
              <Cart cart={cart} setCart={setCart} />
            </ProtectedRoute>
          }
        />

        <Route
          path="/my-orders"
          caseSensitive
          element={
            <ProtectedRoute>
              <MyOrders />
            </ProtectedRoute>
          }
        />


        {/* Admin Routes nodd paa Ivuuuu */}

        <Route
          path="/addproduct"
          caseSensitive
          element={
            <ProtectedRoute requiredRole="ADMIN">
              <AddProduct />
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/orders"
          caseSensitive
          element={
            <ProtectedRoute requiredRole="ADMIN">
              <AdminOrders />
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin/payments"
          caseSensitive
          element={
            <ProtectedRoute requiredRole="ADMIN">
              <AdminPayments />
            </ProtectedRoute>
          }
        />


        {/* Palthu Path yenara bandava andra this bhai */}

        <Route
          path="*"
          element={<NotFound />}
        />

      </Routes>

    </div>
  )
}

export default App