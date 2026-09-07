import React, { useState } from 'react'
import { register } from '../services/authService'
import { useNavigate } from 'react-router-dom'
import '../styles/Register.css'

function Register() {

    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [confirmPassword, setConfirmPassword] = useState("")
    const [error, setError] = useState("")

    const navigate = useNavigate()

    async function handleRegister(e) {

        e.preventDefault()

        setError("")

        if (password !== confirmPassword) {
            setError("Passwords do not match")
            return
        }

        try {

            await register(
                email,
                password,
                confirmPassword
            )

            navigate("/login")

        }
        catch (error) {

            console.log(error)

            const message =
                typeof error.response?.data === "string"
                    ? error.response.data
                    : error.response?.data?.message

            setError(
                message || "Registration failed"
            )
        }
    }

    return (
        <div className="register-page">

            <div className="register-glow register-glow-one"></div>
            <div className="register-glow register-glow-two"></div>

            <div className="register-container">

                <div className="register-brand">
                    Vend<span>Era</span>
                </div>

                <div className="register-card">

                    <div className="register-header">

                        <p className="register-eyebrow">
                            JOIN VENDERA
                        </p>

                        <h1>
                            Create <span>Account.</span>
                        </h1>

                        <p>
                            Create your VendEra account and start shopping.
                        </p>

                    </div>


                    <form
                        className="register-form"
                        onSubmit={handleRegister}
                    >

                        <div className="register-form-group">

                            <label>
                                Email
                            </label>

                            <input
                                type="email"
                                placeholder="Enter your email"
                                value={email}
                                onChange={(e) => {
                                    setEmail(e.target.value)
                                    setError("")
                                }}
                                required
                            />

                        </div>


                        <div className="register-form-group">

                            <label>
                                Password
                            </label>

                            <input
                                type="password"
                                placeholder="Create a password"
                                value={password}
                                onChange={(e) => {
                                    setPassword(e.target.value)
                                    setError("")
                                }}
                                required
                            />

                        </div>


                        <div className="register-form-group">

                            <label>
                                Confirm Password
                            </label>

                            <input
                                type="password"
                                placeholder="Confirm your password"
                                value={confirmPassword}
                                onChange={(e) => {
                                    setConfirmPassword(e.target.value)
                                    setError("")
                                }}
                                required
                            />

                        </div>


                        {error && (
                            <div className="register-error">
                                {error}
                            </div>
                        )}


                        <button
                            className="register-button"
                            type="submit"
                        >
                            Create Account
                        </button>

                    </form>


                    <div className="register-login">

                        <span>
                            Already have an account?
                        </span>

                        <button
                            type="button"
                            onClick={() => navigate("/login")}
                        >
                            Login
                        </button>

                    </div>

                </div>


                <p className="register-footer">
                    Your VendEra account starts here.
                </p>

            </div>

        </div>
    )
}

export default Register