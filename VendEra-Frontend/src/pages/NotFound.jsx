import React from 'react'
import { useNavigate } from 'react-router-dom'
import '../styles/NotFound.css'

function NotFound() {

    const navigate = useNavigate()

    return (
        <div className="not-found-page">

            <div className="not-found-content">

                <p className="not-found-eyebrow">
                    VENDERA
                </p>

                <h1>
                    4<span>0</span>4
                </h1>

                <h2>
                    Page Not Found.
                </h2>

                <p className="not-found-message">
                    Looks like this route doesn't exist.
                </p>

                <button
                    className="not-found-button"
                    onClick={() => navigate("/home")}
                >
                    ← Back Home
                </button>

            </div>

        </div>
    )
}

export default NotFound