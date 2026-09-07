import React from 'react'
import { Navigate } from 'react-router-dom'

function ProtectedRoute({ children, requiredRole }) {

    const token = localStorage.getItem("accessToken")
    const role = localStorage.getItem("role")

    if (!token) {
        return <Navigate to="/login" replace />
    }

    if (requiredRole && role !== requiredRole) {
        return <Navigate to="/products" replace />
    }

    return children
}

export default ProtectedRoute