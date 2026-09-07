import React, { useState } from 'react'
import { login } from '../services/authService'
import { useNavigate } from 'react-router-dom'
import '../styles/Login.css'

function Login() {

    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [errorMessage, setErrorMessage] = useState("")

    const navigate = useNavigate()

    async function handleLogin(e) {

        e.preventDefault()

        // Clear any previous error
        setErrorMessage("")

        try {

            const response = await login(email, password)

            console.log(response)

            localStorage.setItem(
                "accessToken",
                response.data.accessToken
            )

            localStorage.setItem(
                "role",
                response.data.role
            )

            navigate("/home")

        }
        catch (error) {

            console.log(error)

            const message =
        typeof error.response?.data === "string"
            ? error.response.data
            : error.response?.data?.message

    setErrorMessage(
        message || "Login failed. Please try again."
    )
        }
    }

    return (
        <div className="login-page">

            <div className="login-glow login-glow-one"></div>
            <div className="login-glow login-glow-two"></div>

            <div className="login-container">

                <div className="login-brand">
                    Vend<span>Era</span>
                </div>

                <div className="login-card">

                    <div className="login-header">

                        <p className="login-eyebrow">
                            WELCOME BACK
                        </p>

                        <h1>
                            Sign <span>In.</span>
                        </h1>

                        <p>
                            Access your VendEra account and continue shopping.
                        </p>

                    </div>


                    <form
                        className="login-form"
                        onSubmit={handleLogin}
                    >

                        <div className="login-form-group">

                            <label>
                                Email
                            </label>

                            <input
                                type="email"
                                placeholder="Enter your email"
                                value={email}
                                onChange={(e) => {
                                    setEmail(e.target.value)
                                    setErrorMessage("")
                                }}
                                required
                            />

                        </div>


                        <div className="login-form-group">

                            <label>
                                Password
                            </label>

                            <input
                                type="password"
                                placeholder="Enter your password"
                                value={password}
                                onChange={(e) => {
                                    setPassword(e.target.value)
                                    setErrorMessage("")
                                }}
                                required
                            />

                        </div>


                        {errorMessage && (
                            <div className="login-error">
                                {errorMessage}
                            </div>
                        )}


                        <button
                            className="login-button"
                            type="submit"
                        >
                            Login
                        </button>

                    </form>


                    <div className="login-register">

                        <span>
                            Don't have an account?
                        </span>

                        <button
                            type="button"
                            onClick={() => navigate("/register")}
                        >
                            Register
                        </button>

                    </div>

                </div>


                <p className="login-footer">
                    Secure access to your VendEra experience.
                </p>

            </div>

        </div>
    )
}

export default Login